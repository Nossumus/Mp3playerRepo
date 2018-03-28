package com.example.adrian.mp3player;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
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
    Handler handler;

    TextView locationView;
    private ImageButton pausePlayButton;
    private ImageButton forwardButton;
    SeekBar seekBar;
    String current_Location;
    String next_Location;
    String previous_Location;
    String onPauseLocation;
    String list;
    String list2;
    String list3;
    String list4;
    int ArraySize;
    ArrayList<String> arrayList;
    ListView listView;
    ArrayAdapter<String> adapter;
    Context context;
    boolean paused;

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void CreateAdapter_and_getLocation_also_setupOnClickListener() {

        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();

                }

                ArraySize = arrayList.size() - 1;

                //getting location of desired file
                list = (adapterView.getItemAtPosition(position).toString());


                if (position == ArraySize) {
                    list2 = (adapterView.getItemAtPosition(position).toString());// it will go back to start of an array

                    list4 = (adapterView.getItemAtPosition(position).toString());
                } if (paused){
                    list2 = (adapterView.getItemAtPosition(position + 2).toString());
                    list4 = (adapterView.getItemAtPosition(position + 2).toString());
                }
                else {
                    list2 = (adapterView.getItemAtPosition(position + 1).toString());

                    list4 = (adapterView.getItemAtPosition(position + 2).toString());
                }
                if (position < 0) {

                    list3 = (adapterView.getItemAtPosition(position + 1).toString()); //it will play first song from array instead of getting crush the app

                } else {

                    list3 = (adapterView.getItemAtPosition(position).toString());
                }


                current_Location = null;
                next_Location = null;
                previous_Location = null;

                current_Location = list.substring(list.indexOf("/"));
                next_Location = list2.substring(list2.indexOf("/"));
                previous_Location = list3.substring(list3.indexOf("/"));
                onPauseLocation = list4.substring(list4.indexOf("/"));


                if (!mediaPlayer.isPlaying() && paused) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }

                //setting data source
                try {
                    //mediaPlayer.setDataSource(current_Location);
                    mediaPlayer.setDataSource(getApplicationContext(),Uri.parse(current_Location));
                    locationView.setText(current_Location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //preparing media player
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //run play/pause action
                pausePlayAction();
                // seekBar.setMax(mediaPlayer.getDuration());

            }
        });


        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forwardAction();
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


    public void pausePlayAction() {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            pausePlayButton.setBackgroundResource(R.drawable.play);
            paused = true;
        } else {
            mediaPlayer.start();
            pausePlayButton.setBackgroundResource(R.drawable.pause);
            paused = false;

        }
    }

    public void forwardAction() {
       // if (mediaPlayer.isPlaying() || paused)
            mediaPlayer.stop();
            mediaPlayer.reset();

            try {
                mediaPlayer.setDataSource(this, Uri.parse(next_Location));
                locationView.setText(next_Location);
                // mediaPlayer.setDataSource(next_Location);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //preparing media player
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //run play/pause action
            pausePlayAction();

    }
       // if(paused){
        //    mediaPlayer.stop();
         //   mediaPlayer.reset();
         //   try {
                //mediaPlayer.setDataSource(onPauseLocation);
           //     mediaPlayer.setDataSource(this,Uri.parse(onPauseLocation));
         //   } catch (IOException e) {
         //       e.printStackTrace();
         //   }
            //preparing media player
         //   try {
          //      mediaPlayer.prepare();
          //  } catch (IOException e) {
          //      e.printStackTrace();
          //  }
            //run play/pause action
          //  pausePlayAction();

     //   }



    public void playCycle(){
            seekBar.setProgress(mediaPlayer.getCurrentPosition());

           if(mediaPlayer.isPlaying()){
             Runnable runnable = new Runnable() {
               @Override
               public void run() {
                  playCycle();
                }
           };
             handler.postDelayed(runnable, 1000);
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

        checkPermissionForReadExtertalStorage();

        listView = (ListView) findViewById(R.id.listView);

        locationView = (TextView) findViewById(R.id.locationView);
        pausePlayButton = (ImageButton) findViewById(R.id.pauseButton);
        forwardButton = (ImageButton) findViewById(R.id.forwardButton);
       // seekBar = (SeekBar) findViewById(R.id.seekBar);

        //to see pause/play button when app is starting
        pausePlayButton.setBackgroundResource(R.drawable.play);

        CreateAdapter_and_getLocation_also_setupOnClickListener();





    }

}




