package com.example.adrian.mp3player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Adrian on 17.04.2018.
 */


public class NotificationActivity extends AppCompatActivity {

    ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);

        imageButton = (ImageButton) findViewById(R.id.notificationPausePlayButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("com.example.adrian.mp3player");
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG);
            }
        });

    }
}