package com.smk.funnymessager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.smk.funnymessager.adapters.ConversationListAdapter;
import com.smk.funnymessager.database.DatabaseManager;
import com.smk.funnymessager.database.controller.MessageController;
import com.smk.funnymessager.models.Message;
import com.smk.funnymessager.models.User;
import com.smk.funnymessager.services.ChatService;
import com.smk.funnymessager.utils.StoreUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ConversationActivity extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener{

    public static boolean isActive = false;
    private ListView lst_message;
    private List<Message> msg_list;
    private ConversationListAdapter conversationAdapter;
    private User friend;
    private EmojiconEditText edt_message;
    private IconicsImageView btn_send;
    private User user;
    private int notSeenCount = 0;
    private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<Integer, Integer>();
    private int minDataScroll = 1;
    private boolean always_scroll = true;
    private View ommit;
    private boolean sendOmmit = false;
    private LinearLayout btn_emojicon;
    private LinearLayout btn_choose_image;
    private LinearLayout btn_take_image;
    private LinearLayout btn_audio;
    private LinearLayout btn_location;
    private RelativeLayout layout_emojicon;
    private boolean toggleEmojiconClick = false;
    private View indicator_emojicon;
    private View indicator_choose_image;
    private View indicator_take_image;
    private View indicator_audio;
    private View indicator_location;
    private SipAudioCall call;
    private SipManager manager;
    private String sipAddress = "959000002";
    private SipProfile me;
    private AlertDialog m_AlertDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        lst_message = (ListView) findViewById(R.id.lst_conversation);
        edt_message = (EmojiconEditText) findViewById(R.id.edt_msg);
        btn_send    = (IconicsImageView) findViewById(R.id.btn_send);
        btn_emojicon    = (LinearLayout) findViewById(R.id.btn_emojicon);
        btn_choose_image    = (LinearLayout) findViewById(R.id.btn_choose_image);
        btn_take_image    = (LinearLayout) findViewById(R.id.btn_take_image);
        btn_audio    = (LinearLayout) findViewById(R.id.btn_audio);
        btn_location    = (LinearLayout) findViewById(R.id.btn_location);
        layout_emojicon = (RelativeLayout) findViewById(R.id.layout_emojicon);
        indicator_emojicon = (View) findViewById(R.id.indicator_emojicon);
        indicator_choose_image = (View) findViewById(R.id.indicator_choose_image);
        indicator_take_image = (View) findViewById(R.id.indicator_take_image);
        indicator_audio = (View) findViewById(R.id.indicator_audio);
        indicator_location = (View) findViewById(R.id.indicator_location);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            friend = new Gson().fromJson(bundle.getString("friend"), User.class);
        }

        user = StoreUtil.getInstance().selectFrom("fnUser");
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle(friend.getName());
        if(friend.getOnSession() != null && friend.getOnSession())
            toolbar.setSubtitle("Online");
        else
            toolbar.setSubtitle("Offline");
        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_send.setOnClickListener(onClickListener);
        btn_emojicon.setOnClickListener(onClickListener);
        btn_choose_image.setOnClickListener(onClickListener);
        btn_take_image.setOnClickListener(onClickListener);
        btn_audio.setOnClickListener(onClickListener);
        btn_location.setOnClickListener(onClickListener);

        msg_list = new ArrayList<>();
        conversationAdapter = new ConversationListAdapter(this, msg_list, user, friend);
        lst_message.setAdapter(conversationAdapter);

        conversationAdapter.notifyDataSetChanged();

        edt_message.setOnClickListener(onClickListener);
        edt_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    lst_message.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    btn_send.setColor(getResources().getColor(R.color.fnMessagerPrimaryColor));
                    btn_send.setEnabled(true);
                    if(ChatService.client != null && !sendOmmit){
                        sendOmmit = true;
                        Message message = new Message(user.getId(),friend.getId(),"Typing...", "ommit");
                        ChatService.client.send(new Gson().toJson(message));
                    }
                }else{
                    if(ChatService.client != null && sendOmmit){
                        sendOmmit = false;
                        Message message = new Message(user.getId(),friend.getId(),"Not Typing...", "unommit");
                        ChatService.client.send(new Gson().toJson(message));
                    }
                    btn_send.setColor(getResources().getColor(R.color.fnMessagerGrey));
                    btn_send.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lst_message.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount - (visibleItemCount + firstVisibleItem) <= minDataScroll && getScroll() > 0 ) {
                    notSeenCount = 0;
                    setNotificationCount(notSeenCount);
                    always_scroll = true;
                    lst_message.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                } else {
                    always_scroll = false;
                    lst_message.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
                }

            }
        });

        ommit = View.inflate(this, R.layout.ommit_layout, null);
        EventBus.getDefault().register(this);
        getNewMessage();
        edt_message.setUseSystemDefault(true);
        //setEmojiconFragment(true);

    }

    @Override
    public void onStart() {
        isActive = true;
        super.onStart();
    }

    @Override
    protected void onResume() {
        isActive = true;
        super.onResume();
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    private void tryToSend(){
        if(lst_message.getFooterViewsCount() == 0)
            lst_message.addFooterView(ommit);
    }

    private void tryToNotSend(){
        if(lst_message.getFooterViewsCount() > 0){
            lst_message.removeFooterView(ommit);
        }
    }

    private int getScroll() {
        View c = lst_message.getChildAt(0); //this is the first visible row
        if(c != null){
            int scrollY = -c.getTop();
            listViewItemHeights.put(lst_message.getFirstVisiblePosition(), c.getHeight());
            for (int i = 0; i < lst_message.getFirstVisiblePosition(); ++i) {
                if (listViewItemHeights.get(i) != null) //(this is a sanity check)
                    scrollY += listViewItemHeights.get(i); //add all heights of the views that are gone
            }
            return scrollY;
        }
        return 0;
    }

    @Subscribe
    public void onEvent(final Message message){
        if((message.getFromUserId().equals(friend.getId())) || message.getFromUserId().equals(user.getId()) || (message.getToUserId().equals(friend.getId()))){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(message.getType().equals("message") && message.getDelivered() && !message.getFromUserId().equals(user.getId())){
                        if(always_scroll){
                            message.setSeen(true);
                            message.setType("seen");
                            ChatService.client.send(new Gson().toJson(message));
                            DatabaseManager<Message> databaseManager = new MessageController(getApplicationContext());
                            databaseManager.update(message);
                        }else {
                            notSeenCount +=1;
                            setNotificationCount(notSeenCount);
                        }
                        getNewMessage();
                    }else if(message.getType().equals("new") && message.getFromUserId().equals(friend.getId())){
                        getSupportActionBar().setSubtitle("Online");
                    }else if(message.getType().equals("exit") && message.getFromUserId().equals(friend.getId())){
                        getSupportActionBar().setSubtitle("Offline");
                    }else if(message.getType().equals("ommit") && message.getFromUserId().equals(friend.getId())){
                        tryToSend();
                    }else if(message.getType().equals("unommit") && message.getFromUserId().equals(friend.getId())){
                        tryToNotSend();
                    }else{
                        getNewMessage();
                    }

                }
            });

        }
    }

    private void getNewMessage(){
        msg_list.clear();
        DatabaseManager<Message> databaseManager = new MessageController(getApplicationContext());
        msg_list.addAll(databaseManager.select(friend.getId().toString()));
        conversationAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == btn_send){
                Message message = new Message(user.getId(),friend.getId(),edt_message.getText().toString(), "message");
                if(ChatService.client != null) {
                    ChatService.client.send(new Gson().toJson(message));
                    edt_message.setText("");
                }else{
                    //TODO connecting status
                    ChatService.client.connect();
                }
            }
            if(v == edt_message){
                if(toggleEmojiconClick){
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
            if(v == btn_emojicon){
                toggleEmojiconView();
            }
            if(v == btn_choose_image){
                //TODO image message
            }
            if(v == btn_take_image){
                //TODO image message
            }
            if(v == btn_location){
                //TODO location message
            }
            if(v == btn_audio){
                //TODO audio message
            }

        }
    };

    private void toggleEmojiconView(){
        if(toggleEmojiconClick){
            toggleEmojiconClick = false;
            indicator_emojicon.setVisibility(View.INVISIBLE);
            layout_emojicon.setVisibility(View.GONE);
        }else{
            toggleEmojiconClick = true;
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            Handler mHandler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    indicator_emojicon.setVisibility(View.VISIBLE);
                    layout_emojicon.setVisibility(View.VISIBLE);
                }
            };
            mHandler.postDelayed(runnable, 300);


        }
    }

    public IconicsDrawable getIconicDrawable(String icon, int color, int size){
        return new IconicsDrawable(this)
                .icon(icon)
                .color(getResources().getColor(color))
                .sizeDp(size);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conversation_menu_items, menu);
        MenuItem item_audio = menu.findItem(R.id.action_audio);
        item_audio.setIcon(getIconicDrawable(GoogleMaterial.Icon.gmd_phone.toString(), android.R.color.white, 20));
        item_audio.setVisible(false);
        MenuItem item_video = menu.findItem(R.id.action_video);
        item_video.setIcon(getIconicDrawable(GoogleMaterial.Icon.gmd_videocam.toString(), android.R.color.white, 22));
        item_video.setVisible(false);
        MenuItem item = menu.findItem(R.id.action_friends);
        MenuItemCompat.setActionView(item, R.layout.notification_layout);
        View view = MenuItemCompat.getActionView(item);
        TextView noti = (TextView) view.findViewById(R.id.txt_noti_count);
        IconicsImageView icon = (IconicsImageView) view.findViewById(R.id.icon_noti);
        icon.setIcon(GoogleMaterial.Icon.gmd_notifications);
        noti.setText(String.valueOf(notSeenCount));
        if(notSeenCount > 0){
            noti.setVisibility(View.VISIBLE);
        }else{
            noti.setVisibility(View.GONE);
        }
        item.getActionView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notSeenCount = 0;
                setNotificationCount(notSeenCount);
                lst_message.smoothScrollByOffset(msg_list.size());
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_audio) {
            //TODO audio message
            return true;
        }
        if (id == R.id.action_video) {
            //TODO video message
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNotificationCount(int count){
        notSeenCount = count;
        this.supportInvalidateOptionsMenu();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        isActive = false;
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(edt_message);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(edt_message, emojicon);
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        onBackPressed();
        return super.getSupportParentActivityIntent();
    }

    @Override
    public void onBackPressed() {
        if(toggleEmojiconClick){
            toggleEmojiconView();
            return;
        }
        super.onBackPressed();
    }
}
