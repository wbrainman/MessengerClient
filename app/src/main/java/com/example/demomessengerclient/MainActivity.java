package com.example.demomessengerclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DemoMessengerClient";
    private static final int MSG_TO_SERVICE = 100;
    private static final int MSG_FROM_SERVICE = 101;
    private Messenger mMessenger = null;
    private Messenger mService;
    private boolean isConnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMessenger = new Messenger(new IncomingHandler());


        Intent intent = new Intent();
        intent.setAction("com.example.demoMessengerService");
        intent.setPackage("com.example.demomessengerservice");
        bindService(intent, mConnection, BIND_AUTO_CREATE);

        //2nd commit test
        Button button = findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                if (isConnect) {
                    try {
                        mService.send(Message.obtain(null, MSG_TO_SERVICE, 100, 0));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            isConnect = true;
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            isConnect = false;
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_FROM_SERVICE:
                    Log.d(TAG, "handleMessage: receive from service");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}