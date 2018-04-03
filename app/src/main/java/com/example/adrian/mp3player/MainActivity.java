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
    Handler handler = new Handler();
    TextView locationView;
    private ImageButton backwardButton;
    private ImageButton pausePlayButton;
    private ImageButton forwardButton;
    SeekBar seekBar;
    String current_Location;
    int ArraySize;
    ArrayList<String> arrayList;
    ArrayList<String> locationList;
    ListView listView;
    ArrayAdapter<String> adapter;
    Context context;
    boolean paused;
    int pseudoCursor;
    int progressValue;
    String songLength;
    TextView textView;

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void CreateAdapter_and_getLocation_also_setupOnClickListener() {

        arrayList = new ArrayList<>();
        locationList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                    mediaPlayer.stop();
                    mediaPlayer.reset();


                // getting number of elements in array
                ArraySize = arrayList.size() - 1;

                // setting "Pseudo-cursor"
                pseudoCursor = position;
                // getting location of desired file
                current_Location = locationList.get(pseudoCursor);


                //list2 = (adapterView.getItemAtPosition(position).toString());// it will go back to start of an array
               // onPauseLocation = list4.substring(list4.indexOf("/"));


                // setting data source
                try {
                    //mediaPlayer.setDataSource(current_Location);
                    locationView.setText(current_Location);
                    mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(current_Location));
                }catch (IllegalStateException e){
                    e.printStackTrace();
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

        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                backwardAction();
            }
        });



    }



    public void pausePlayAction() {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            pausePlayButton.setBackgroundResource(R.drawable.play);
        } else {
            mediaPlayer.start();
            pausePlayButton.setBackgroundResource(R.drawable.pause);
            seekBar();
            playCycle();

          //  mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
          //      @Override
          //      public void onCompletion(MediaPlayer mediaPlayer) {
          //          mediaPlayer.release();
           //     }
           // });

        }
    }

    public void forwardAction() {
            mediaPlayer.stop();
            mediaPlayer.reset();

            pseudoCursor++;

            if(pseudoCursor > ArraySize)
            {
                // get back to the first song
                pseudoCursor = 0;
            }

            current_Location = locationList.get(pseudoCursor);
            try {
                mediaPlayer.setDataSource(this, Uri.parse(current_Location));
                locationView.setText(current_Location);
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

    public void backwardAction() {
        mediaPlayer.stop();
        mediaPlayer.reset();

        pseudoCursor--;

        if(pseudoCursor < 0)
        {
            // get back to the first song
            pseudoCursor = 0;
        }

        current_Location = locationList.get(pseudoCursor);
        try {
            mediaPlayer.setDataSource(this, Uri.parse(current_Location));
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

    }
    public void seekBar() {

        seekBar.setMax(mediaPlayer.getDuration());
        textView .setText(seekBar.getProgress() + "/" + seekBar.getMax());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if(input){
                    mediaPlayer.seekTo(progress);
                }
                progressValue = (progress/1000);
                if((seekBar.getMax()/1000)%60 < 10){
                    songLength = (seekBar.getMax()/1000)/60 + ":0" + (seekBar.getMax()/1000)%60;
                }else {
                    songLength = (seekBar.getMax() / 1000) / 60 + ":" + (seekBar.getMax() / 1000) % 60;
                }
                if(progressValue%60 < 10){
                    textView.setText(progressValue/60 + ":0" + progressValue % 60 + "/" + songLength);
                }else{
                textView.setText(progressValue/60 + ":" + progressValue % 60 + "/" + songLength);
            }}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(progressValue/60 + ":" + progressValue % 60 + "/" + songLength);
                if(progressValue%60 < 10){
                    textView.setText(progressValue/60 + ":0" + progressValue % 60 + "/" + songLength);
                }else {
                    textView.setText(progressValue / 60 + ":" + progressValue % 60 + "/" + songLength);
                }
            }
        });
    }

    public void playCycle(){
            seekBar.setProgress(mediaPlayer.getCurrentPosition());

           if(mediaPlayer.isPlaying()){
             Runnable runnable = new Runnable() {
               @Override
               public void run() {
                  playCycle();
                }

           };handler.postDelayed(runnable, 1);

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
                locationList.add(currentLocation);

            } while (songCursor.moveToNext());

        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissionForReadExtertalStorage();

        // find them, find them all!
        listView = (ListView) findViewById(R.id.listView);
        locationView = (TextView) findViewById(R.id.locationView);
        pausePlayButton = (ImageButton) findViewById(R.id.pauseButton);
        forwardButton = (ImageButton) findViewById(R.id.forwardButton);
        backwardButton = (ImageButton) findViewById(R.id.backwardButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.textView);


        //to see pause/play button when app is starting
        pausePlayButton.setBackgroundResource(R.drawable.play);


        CreateAdapter_and_getLocation_also_setupOnClickListener();






    }

}




