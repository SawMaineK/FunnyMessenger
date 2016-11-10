package com.funny.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smk.funnymessager.FnMessagerConfigs;
import com.smk.funnymessager.models.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn_open_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FnMessagerConfigs fnMessagerConfigs = FnMessagerConfigs.getInstance();
        fnMessagerConfigs.setName("Saw Maine K");
        fnMessagerConfigs.setEmail("sawmainek90@gmail.com");
        fnMessagerConfigs.setImage("test.png");
        fnMessagerConfigs.setPhone("09324234232");
        fnMessagerConfigs.setDeviceId(DeviceUtil.getInstance(this).getID());
        fnMessagerConfigs.setClientId(123456789);
        fnMessagerConfigs.setPackageName(getPackageName());
        fnMessagerConfigs.register(this, new FnMessagerConfigs.FnRegisterCallback() {
            @Override
            public void onSuccess(User user) {
                btn_open_chat = (Button) findViewById(R.id.btn_open_chat);
                btn_open_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(fnMessagerConfigs.isRegister()){
                            fnMessagerConfigs.getFnUserCallbackListener(new FnMessagerConfigs.FnUserCallback() {
                                @Override
                                public void results(List<User> users) {
                                    fnMessagerConfigs.openChat(MainActivity.this, users.get(0).getId());
                                }
                            });
                        }
                    }
                });
            }
        });



    }
}
