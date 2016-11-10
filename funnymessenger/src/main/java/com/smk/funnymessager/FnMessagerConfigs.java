package com.smk.funnymessager;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;
import com.smk.funnymessager.clients.NetworkEngine;
import com.smk.funnymessager.models.User;
import com.smk.funnymessager.services.ChatService;
import com.smk.funnymessager.utils.StoreUtil;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SMK on 11/10/2016.
 */

public class FnMessagerConfigs {
    private static FnMessagerConfigs ourInstance;
    public static String PACKAGE_NAME;

    private String name;
    private String email;
    private String phone;
    private String image;
    private String deviceId;
    private Integer clientId;
    private FnRegisterCallback mFnRegisterCallback;
    private FnUserCallback mFnUserCallback;

    public static FnMessagerConfigs getInstance() {

        if(ourInstance != null)
            return ourInstance;
        else
            ourInstance = new FnMessagerConfigs();
        return ourInstance;
    }


    private FnMessagerConfigs() {

    }

    public static String getPackageName() {
        return PACKAGE_NAME;
    }

    public static void setPackageName(String packageName) {
        PACKAGE_NAME = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public void register(final Activity activity, FnRegisterCallback callback){
        mFnRegisterCallback = callback;
        NetworkEngine.getInstance().postUser(this.name, this.email, this.phone, this.image, this.deviceId, this.clientId, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                StoreUtil.getInstance().saveTo("fnUser", user);
                activity.startService(new Intent(activity.getApplicationContext(), ChatService.class));
                if(mFnRegisterCallback != null){
                    mFnRegisterCallback.onSuccess(user);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public interface FnRegisterCallback{
        void onSuccess(User user);
    }

    public boolean isRegister(){
        User user = StoreUtil.getInstance().selectFrom("fnUser");
        if(user != null)
            return true;
        return false;
    }

    public void openChat(final Activity activity, final Integer fnUserID){
        NetworkEngine.getInstance().getUser(fnUserID, clientId, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                activity.startActivity(new Intent(activity.getApplicationContext(), ConversationActivity.class).putExtra("friend", new Gson().toJson(user)));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    public void getFnUserCallbackListener(FnUserCallback callback){
        mFnUserCallback = callback;
        getFnUsers();
    }

    public interface FnUserCallback{
        void results(List<User> users);
    }
    private List<User> getFnUsers(){
        NetworkEngine.getInstance().getUsers(clientId, new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                if(mFnUserCallback != null){
                    mFnUserCallback.results(users);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return null;
    }
}
