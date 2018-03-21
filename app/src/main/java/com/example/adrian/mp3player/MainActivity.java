package com.example.adrian.mp3player;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //declaration section
    MediaPlayer mediaPlayer = new MediaPlayer();
    private ImageButton stopButton;
    private ImageButton pausePlayButton;
    private SeekBar seekBar;
    String location;
    ArrayList<String> arrayList;
    ListView listView;
    ArrayAdapter<String> adapter;

    public void CreateAdapter_and_getLocation_also_setupOnClickListener() {

        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
              }

                //getting location of desired file
                String list = (adapterView.getItemAtPosition(position).toString());
                location = null;
                location = list.substring(list.indexOf("/"));

                //setting data source
                try {
                    mediaPlayer.setDataSource(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //preparing media player
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    pausePlayAction();
                }
            });


            pausePlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pausePlayAction();
                }
            });
       // public void playCycle(){
        //    seekBar.setProgress(mediaPlayer.getCurrentPosition());

         //   if(mediaPlayer.isPlaying()){
           //     runnable = new Runnable() {
             //       @Override
             //       public void run() {
              //          playCycle();
            //        }
             //   };
           //     handler.postDelayed(runnable, 1000);
          //  }
       // }
    }

    public void pausePlayAction(){

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    pausePlayButton.setBackgroundResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    pausePlayButton.setBackgroundResource(R.drawable.pause);
                }
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
        stopButton = (ImageButton) findViewById(R.id.stopButton);
        pausePlayButton = (ImageButton) findViewById(R.id.pauseButton);
        //seekBar = (SeekBar) findViewById(R.id.seekBar);

        //to see pause/play button when app is starting
        pausePlayButton.setBackgroundResource(R.drawable.play);



        CreateAdapter_and_getLocation_also_setupOnClickListener();





    }

}




