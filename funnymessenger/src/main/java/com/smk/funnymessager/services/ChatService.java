package com.smk.funnymessager.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.codebutler.android_websockets.WebSocketClient;
import com.google.gson.Gson;
import com.smk.funnymessager.ConversationActivity;
import com.smk.funnymessager.R;
import com.smk.funnymessager.database.DatabaseManager;
import com.smk.funnymessager.database.controller.MessageController;
import com.smk.funnymessager.database.controller.UserController;
import com.smk.funnymessager.models.Message;
import com.smk.funnymessager.models.User;
import com.smk.funnymessager.utils.StoreUtil;

import org.greenrobot.eventbus.EventBus;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SMK on 5/31/2016.
 */
public class ChatService extends Service {
    public static WebSocketClient client;
    private String URL_WEBSOCKET = "ws://192.168.0.100:8080/SocketNotification/chat";
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();

        connectToServer();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if(client != null){
                    client.connect();
                }

                // Try to reobject client;
                if(client == null){

                    connectToServer();
                }
            }
        }, 3000, 3000);
    }

    private void connectToServer(){
        user = StoreUtil.getInstance().selectFrom("fnUser");

        if(user != null){
            /**
             * Creating web socket client. This will have callback methods
             * */
            client = new WebSocketClient(URI.create(URL_WEBSOCKET +"?id="+ URLEncoder.encode(user.getId().toString())), listener, null);

            client.connect();
        }
    }

    private WebSocketClient.Listener listener = new WebSocketClient.Listener() {
        @Override
        public void onConnect() {

        }

        @Override
        public void onMessage(String message) {
            Log.i("Socket Message", message);
            Message msg = new Gson().fromJson(message, Message.class);
            if(msg.getType().equals("new") || msg.getType().equals("exit")){
                joinedFriend(msg);
            }else if(msg.getType().equals("message")){
                doManageMessage(msg);
            }else if(msg.getType().equals("seen")){
                doSeenMessage(msg);
            }else if(msg.getType().equals("ommit") || msg.getType().equals("unommit")){
                doOmmit(msg);
            }
        }

        @Override
        public void onMessage(byte[] data) {

        }

        @Override
        public void onDisconnect(int code, String reason) {
            String message = String.format(Locale.US,URL_WEBSOCKET+"is disconnected! Code: %d Reason: %s", code, reason);
            Log.e("Socket Disconnected", message);
        }

        @Override
        public void onError(Exception error) {
            Log.e("Socket Error", error.toString());
            if(client != null){
                client.disconnect();
            }
        }
    };
    private  void joinedFriend(Message message){
        if(message != null){
            DatabaseManager<User> databaseManager = new UserController(getApplicationContext());
            User user = databaseManager.select(message.getFromUserId());
            if(user != null){
                if(message.getType().equals("new"))
                    user.setOnSession(true);
                else
                    user.setOnSession(false);
                databaseManager.update(user);
                EventBus.getDefault().post(message);
            }
        }
    }
    private void doManageMessage(Message message){

        if(message != null){
            if(message.getFromUserId().equals(user.getId())){
                message.setSelf(true);
            }else {
                message.setSelf(false);
            }

            DatabaseManager<Message> databaseManager = new MessageController(getApplicationContext());
            databaseManager.save(message);

            DatabaseManager<User> userDatabaseManager = new UserController(getApplicationContext());
            User sender = userDatabaseManager.select(message.getFromUserId());
            if(sender != null){
                sender.setMessage(message.getMessage());
                sender.setUpdatedAt(message.getCreatedAt());
                userDatabaseManager.update(sender);
                if(!ConversationActivity.isActive){
                    notifyMsg(sender, message.getMessage(), 1);
                }
            }else{
                //getNewUser(message);
            }
            User user = userDatabaseManager.select(message.getToUserId());
            if(user != null){
                user.setMessage(message.getMessage());
                user.setUpdatedAt(message.getCreatedAt());
                userDatabaseManager.update(user);
            }
            EventBus.getDefault().post(message);
        }
    }

    /*protected void getNewUser(final Message msg){
        NetworkEngine.getInstance().getUser(msg.getFromUserId(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                if(!ConversationActivity.isActive){
                    DatabaseManager<User> userDatabaseManager = new UserController(getApplicationContext());
                    userDatabaseManager.save(user);
                    notifyMsg(user, msg.getMessage(), 1);
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }*/

    private void doSeenMessage(Message message){
        if(message != null) {
            if (message.getFromUserId().equals(user.getId())) {
                message.setSelf(true);
            } else {
                message.setSelf(false);
            }
            DatabaseManager<Message> databaseManager = new MessageController(getApplicationContext());
            databaseManager.update(message);
            EventBus.getDefault().post(message);
        }
    }

    private void doOmmit(Message message){
        EventBus.getDefault().post(message);
    }

    public void notifyMsg(User user, String newNessage, int msgType){
        long when = System.currentTimeMillis();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Sets an ID for the notification, so it can be updated
        Random random = new Random();
        int notifyID = random.nextInt(9999 - 1000) + 1000;

        Intent notificationIntent = new Intent(this, ConversationActivity.class);
        notificationIntent.putExtra("friend", new Gson().toJson(user));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(msgType == 1){
            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(user.getName()+ "'s New Message")
                    .setSmallIcon(getNotificationIcon())
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(newNessage))
                    .setContentText(newNessage)
                    .setSound(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.water_droplet))
                    .setAutoCancel(true)
                    .setWhen(when)
                    .setContentIntent(intent);

            mNotificationManager.notify(
                    notifyID,
                    mNotifyBuilder.build());
        }
        if(msgType == 2){
            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(user.getName()+ "'s New Photo")
                    .setSmallIcon(getNotificationIcon())
                    .setSound(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.water_droplet))
                    .setAutoCancel(true)
                    .setWhen(when)
                    .setContentIntent(intent);

            mNotificationManager.notify(
                    notifyID,
                    mNotifyBuilder.build());
        }

        if(msgType == 3){
            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(user.getName()+ "'s Audio")
                    .setSmallIcon(getNotificationIcon())
                    .setSound(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.water_droplet))
                    .setAutoCancel(true)
                    .setWhen(when)
                    .setContentIntent(intent);

            mNotificationManager.notify(
                    notifyID,
                    mNotifyBuilder.build());


        }

    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        //return useWhiteIcon ? R.drawable.ic_stat_food : R.mipmap.ic_launcher;
        return 0;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(client != null){
            client.disconnect();
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if(client != null){
            client.disconnect();
        }
        super.onDestroy();
    }
}
