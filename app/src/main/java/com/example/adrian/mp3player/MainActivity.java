package com.example.adrian.mp3player;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    //comment
    //some changes
    //some more changes
    


    private Button stopButton;
    private Button pauseButton;
    ArrayList<String> arrayList;
    ListView listView;
    ArrayAdapter<String> adapter;


    public void doStuff() {

        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                String list = (adapterView.getItemAtPosition(position).toString());
                String location = list.substring(list.indexOf("/"));
                TextView textView = findViewById(R.id.textView);
                textView.setText(location);


                final MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.stop();
                //mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {


                   // mediaPlayer.release();
                    mediaPlayer.setDataSource(location);

                } catch (IOException e) {

                    e.printStackTrace();

                }
                try {

                    mediaPlayer.prepare();

                } catch (IOException e) {

                    e.printStackTrace();
                }
                mediaPlayer.start();

                stopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                    }


                });



               }
            });
    }




    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);


            do {
                String currentTitle = null;
                String currentArtist = null;
                String currentLocation = null;
                currentTitle = songCursor.getString(songTitle);
                currentArtist = songCursor.getString(songArtist);
                currentLocation = songCursor.getString(songLocation);
                arrayList.add("Title: " + currentTitle + "\n" + "Artist:" + currentArtist + "\n" + "Location: " + currentLocation);

            } while (songCursor.moveToNext());

        }
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        stopButton = (Button) findViewById(R.id.stopButton);
        doStuff();




        //  if (ContextCompat.checkSelfPermission(MainActivity.this,
        //  Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        // if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
        //  Manifest.permission.READ_EXTERNAL_STORAGE)) {
        // ActivityCompat.requestPermissions(MainActivity.this,
        //  new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
        // } else {
        // ActivityCompat.requestPermissions(MainActivity.this,
        //       new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
        //     }
        // } else {
        //   doStuff();
        // }
        // }




    }

}




