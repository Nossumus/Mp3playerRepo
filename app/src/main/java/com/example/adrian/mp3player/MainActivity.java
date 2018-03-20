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


    private ImageButton stopButton;
    private ImageButton pauseButton;
    private SeekBar seekBar;
    String location;
    ArrayList<String> arrayList;
    ListView listView;
    ArrayAdapter<String> adapter;
    int status = 0; //zero means pause view

    public void doStuff() {

        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                String list = (adapterView.getItemAtPosition(position).toString());
                location = list.substring(list.indexOf("/"));

                final MediaPlayer mediaPlayer = new MediaPlayer();

                



                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {

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
                pauseButton.setBackgroundResource(R.drawable.pause);
                if(status==0)
                {
                    pauseButton.setBackgroundResource(R.drawable.pause);
                    status = 1;
                }



                stopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mediaPlayer.stop();
                        pauseButton.setBackgroundResource(R.drawable.play);
                        mediaPlayer.reset();


                    }


                });


                pauseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                if(mediaPlayer.isPlaying()==true)
                                {
                                    mediaPlayer.pause();

                                    if(status==1)
                                    {
                                        pauseButton.setBackgroundResource(R.drawable.play);
                                        status = 0;
                                    }


                                }else {
                                    mediaPlayer.start();

                                    if(status==0){
                                        pauseButton.setBackgroundResource(R.drawable.pause);
                                        status = 1;
                                    }
                                }

                            }
                        });

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
        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        //seekBar = (SeekBar) findViewById(R.id.seekBar);
        if(status==0)
        {
            pauseButton.setBackgroundResource(R.drawable.play);
        }

        doStuff();





    }

}




