package com.example.adrian.mp3player;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RemoteViews;
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
    TextView title;
    private Button notifButton;
    private ImageButton backwardButton;
    private ImageButton pausePlayButton;
    private ImageButton forwardButton;
    SeekBar seekBar;
    String current_Location;
    String current_Title;
    int ArraySize;
    ArrayList<String> titleList;
    ArrayList<String> locationList;
    ListView listView;
    ArrayAdapter<String> adapter;
    Context context;
    boolean paused;
    int pseudoCursor;
    int progressValue;
    String songLength;
    TextView textView;
    TextView notificationTitle;
    private RemoteViews remoteViews;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    private int notification_id;
    private RemoteAction remoteAction;
    boolean condition = true;
    public PendingIntent pendingIntent;
    public static final String PAUSE = "com.example.adrian.mp3player.pausePlayAction";


    public boolean checkPermissionForReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void CreateAdapter_and_getLocation_also_setupOnClickListener() {

        titleList = new ArrayList<>();
        locationList = new ArrayList<>();
        getMusic();

        adapter = new ArrayAdapter<String>(this, R.layout.list_layout, titleList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                mediaPlayer.stop();
                mediaPlayer.reset();
                // getting number of elements in array
                ArraySize = titleList.size() - 1;
                // setting "Pseudo-cursor"
                pseudoCursor = position;
                // getting location of desired file
                current_Location = locationList.get(pseudoCursor);
                // encode location
                current_Location = Uri.encode(current_Location);
                // set title
                current_Title = titleList.get(pseudoCursor);
                notificationPlayer();
                // set title text view
                title.setText(current_Title);
                // there will be error if current_Location have # sign inside
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(current_Location));
                pausePlayAction();
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

        if (pseudoCursor > ArraySize) {
            // get back to the first song
            pseudoCursor = 0;
        }
        // get location
        current_Location = locationList.get(pseudoCursor);
        // encode location
        current_Location = Uri.encode(current_Location);
        // set title
        current_Title = titleList.get(pseudoCursor);
        notificationPlayer();

        try {
            mediaPlayer.setDataSource(this, Uri.parse(current_Location));
            // set title text view
            title.setText(current_Title);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // preparing media player
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // run play/pause action
        pausePlayAction();

    }

    public void backwardAction() {
        mediaPlayer.stop();
        mediaPlayer.reset();

        pseudoCursor--;

        if (pseudoCursor < 0) {
            // get back to the first song
            pseudoCursor = 0;
        }
        // get location
        current_Location = locationList.get(pseudoCursor);
        // encode location
        current_Location = Uri.encode(current_Location);
        // set title
        current_Title = titleList.get(pseudoCursor);
        // set data source
        notificationPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(current_Location));
            // set title text view
            title.setText(current_Title);
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
        textView.setText(seekBar.getProgress() + "/" + seekBar.getMax());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {

                if (input) {
                    mediaPlayer.seekTo(progress);
                }
                progressValue = (progress / 1000);
                if ((seekBar.getMax() / 1000) % 60 < 10) {
                    songLength = (seekBar.getMax() / 1000) / 60 + ":0" + (seekBar.getMax() / 1000) % 60;
                } else {
                    songLength = (seekBar.getMax() / 1000) / 60 + ":" + (seekBar.getMax() / 1000) % 60;
                }
                if (progressValue % 60 < 10) {
                    textView.setText(progressValue / 60 + ":0" + progressValue % 60 + "/" + songLength);
                } else {
                    textView.setText(progressValue / 60 + ":" + progressValue % 60 + "/" + songLength);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(progressValue / 60 + ":" + progressValue % 60 + "/" + songLength);
                if (progressValue % 60 < 10) {
                    textView.setText(progressValue / 60 + ":0" + progressValue % 60 + "/" + songLength);
                } else {
                    textView.setText(progressValue / 60 + ":" + progressValue % 60 + "/" + songLength);
                }
            }
        });
    }

    public void playCycle() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }

            };
            handler.postDelayed(runnable, 1);

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
                titleList.add(currentTitle);
                locationList.add(currentLocation);


            } while (songCursor.moveToNext());

        }
    }

    public void notificationPlayer() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.play);
        //remoteViews.setImageViewResource(R.id.pauseButton,R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.nTitle, current_Title);

        builder = new NotificationCompat.Builder(getApplicationContext(), "channel_1");
        //builder.setCustomBigContentView(remoteViews);
        builder.setCustomContentView(remoteViews);
        builder.setSmallIcon(R.drawable.ic_action_name);
        //builder.addAction(R.drawable.pause,current_Title,pendingIntent);

        remoteViews.setOnClickPendingIntent(R.id.nTitle,pendingIntent);



        notificationManager.notify(0, builder.build());


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissionForReadExternalStorage();

        // find them, find them all!
        title = (TextView) findViewById(R.id.title_view);
        title.setSelected(true);
        listView = (ListView) findViewById(R.id.listView);
        // locationView = (TextView) findViewById(R.id.locationView);
        pausePlayButton = (ImageButton) findViewById(R.id.pauseButton);
        forwardButton = (ImageButton) findViewById(R.id.forwardButton);
        backwardButton = (ImageButton) findViewById(R.id.backwardButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.timer);


        // to see pause/play button when app is starting
        pausePlayButton.setBackgroundResource(R.drawable.play);


        CreateAdapter_and_getLocation_also_setupOnClickListener();


    }
}





