package com.example.adrian.mp3player;

import android.Manifest;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;




public class MainActivity extends AppCompatActivity {

    // declaration section
    final MediaPlayer mediaPlayer = new MediaPlayer();
    Handler handler = new Handler();
    TextView locationView;
    TextView title;
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
    int pseudoCursor;
    int progressValue;
    String songLength;
    TextView textView;
    private RemoteViews remoteViews;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    public PendingIntent pendingPlayPauseIntent;
    public PendingIntent pendingForwardIntent;
    public PendingIntent pendingBackwardIntent;
    public static final String PAUSE = "com.example.adrian.mp3player.PAUSE";
    public static final String FORWARD = "com.example.adrian.mp3player.FORWARD";
    public static final String BACKWARD = "com.example.adrian.mp3player.BACKWARD";
    ImageButton repeatButton;
    boolean loop = false;
    boolean isThereLocationSet = false;
    boolean isThereNotificationSet = false;
    //int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private int notification_id;
    public static final int PERMISSION_GRANTED = 0;
    public static final int PERMISSION_DENIED = -1;
    boolean permission;
    boolean pause = false;



    public void looping()
    {
        if(!loop){
            mediaPlayer.setLooping(true);
            repeatButton.setBackgroundResource(R.drawable.ic_repeat_one_song);
            loop = true;

        }else {
                mediaPlayer.setLooping(false);
                repeatButton.setBackgroundResource(R.drawable.repeat);
                loop = false;

        }
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

                if(!isThereLocationSet){
                    // getting number of elements in array
                    ArraySize = titleList.size() - 1;
                    // setting "Pseudo-cursor"
                    pseudoCursor = position;
                    // getting location of desired file
                    current_Location = locationList.get(pseudoCursor);
                    // encode location
                    // current_Location = Uri.encode(current_Location);
                    // isThereLocationSet
                    isThereLocationSet=true;
                    // set title
                    current_Title = titleList.get(pseudoCursor);
                    // set title text view
                    title.setText(current_Title);
                    remoteViews.setTextViewText(R.id.nTitle, current_Title);
                    // there will be error if current_Location have # sign inside
                    //  String finalLocation = Uri.parse(current_Location).toString();
                    // mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(current_Location));
                    try {
                        mediaPlayer.setDataSource(current_Location);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.yellow_pause);
                            pausePlayButton.setBackgroundResource(R.drawable.yellow_pause);
                            mediaPlayer.start();
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if(mediaPlayer.isLooping()){
                                remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.yellow_pause);
                                pausePlayButton.setBackgroundResource(R.drawable.yellow_pause);
                                mediaPlayer.start();
                                seekBar();
                                playCycle();
                            }else{
                                forwardAction();
                            }
                        }
                    });



                }else if(mediaPlayer.isPlaying() || pause ){

                   // mediaPlayer.stop();
                    mediaPlayer.reset();
                    ArraySize = titleList.size() - 1;
                    // setting "Pseudo-cursor"
                    pseudoCursor = position;
                    // getting location of desired file
                    current_Location = locationList.get(pseudoCursor);
                    // encode location
                    // current_Location = Uri.encode(current_Location);
                    // isThereLocationSet
                    isThereLocationSet=true;
                    // set title
                    current_Title = titleList.get(pseudoCursor);
                    // set title text view
                    title.setText(current_Title);
                    // there will be error if current_Location have # sign inside
                    //  String finalLocation = Uri.parse(current_Location).toString();
                    // mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(current_Location));
                    try {
                        mediaPlayer.setDataSource(current_Location);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.yellow_pause);
                            pausePlayButton.setBackgroundResource(R.drawable.yellow_pause);
                            mediaPlayer.start();
                            seekBar();
                            playCycle();

                        }
                });


            }
        }
        });



        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(!isThereLocationSet){
                    return;
                }*/
                looping();

            }
        });





        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isThereLocationSet){
                    return;
                }
                forwardAction();
            }
        });

        pausePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isThereLocationSet){
                    return;
                }pausePlayAction();
            }
        });

        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isThereLocationSet){
                    return;
                }
                backwardAction();
            }
        });


    }

/*
    public void pausePlayAction() {
        if(!isThereLocationSet){
            return;
        }
        looping();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            notificationPlayer();
            pausePlayButton.setBackgroundResource(R.drawable.play);
            //remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.play);
        } else {
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();


            notificationPlayer();
            pausePlayButton.setBackgroundResource(R.drawable.yellow_pause);
          //  remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.yellow_pause);
          //  seekBar();
            seekBar();
            playCycle();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    pausePlayButton.setBackgroundResource(R.drawable.play);
                   // remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.play);
                    if(loop){
                        pausePlayAction();
                    }else{
                        pausePlayButton.setBackgroundResource(R.drawable.play);
                       // remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.play);
                        forwardAction();
                    }
                }
            });


        }
    }
    */

    public void pausePlayAction(){
        if(!isThereLocationSet){
            return;
        }
        if(mediaPlayer.isPlaying()){
            remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.play);
            pausePlayButton.setBackgroundResource(R.drawable.play);
            pause = true;
            mediaPlayer.pause();
        } else {
            remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.yellow_pause);
            pausePlayButton.setBackgroundResource(R.drawable.yellow_pause);
            mediaPlayer.start();
            seekBar();
            playCycle();
        }
    }

    public void forwardAction() {

        remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.yellow_pause);
        pausePlayButton.setBackgroundResource(R.drawable.yellow_pause);
        //mediaPlayer.stop();
        mediaPlayer.reset();

        pseudoCursor++;

        if (pseudoCursor > ArraySize) {
            // get back to the first song
            pseudoCursor = 0;
        }
        // get location
        current_Location = locationList.get(pseudoCursor);
        // encode location
        //current_Location = Uri.encode(current_Location);
        // set title
        current_Title = titleList.get(pseudoCursor);
        remoteViews.setTextViewText(R.id.nTitle, current_Title);
        //notificationPlayer();
       // remoteViews.setTextViewText(R.id.nTitle, current_Title);

        try {
            mediaPlayer.setDataSource(current_Location);
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
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                seekBar();
                playCycle();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(mediaPlayer.isLooping()){
                    mediaPlayer.start();
                    seekBar();
                    playCycle();
                }else{
                    forwardAction();
                }
            }
        });



    }

    public void backwardAction() {

        remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.yellow_pause);
        pausePlayButton.setBackgroundResource(R.drawable.yellow_pause);
        //mediaPlayer.stop();
        mediaPlayer.reset();

        pseudoCursor--;

        if (pseudoCursor < 0) {
            // get back to the first song
            pseudoCursor = 0;
        }
        // get location
        current_Location = locationList.get(pseudoCursor);
        // encode location
        //current_Location = Uri.encode(current_Location);
        // set title
        current_Title = titleList.get(pseudoCursor);
        remoteViews.setTextViewText(R.id.nTitle, current_Title);
        //notificationPlayer();
        //remoteViews.setTextViewText(R.id.nTitle, current_Title);
        // set data source
        try {
            mediaPlayer.setDataSource(current_Location);
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
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                seekBar();
                playCycle();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(mediaPlayer.isLooping()){
                    mediaPlayer.start();
                    seekBar();
                    playCycle();
                }else{
                    forwardAction();
                }
            }
        });

    }

    public void seekBar() {

        seekBar.setMax(mediaPlayer.getDuration());
        //textView.setText(seekBar.getProgress() + "/" + seekBar.getMax());
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
                textView.setText(progressValue / 60 + ":" + progressValue % 60 + "/" + songLength);
                if (progressValue % 60 < 10) {
                    textView.setText(progressValue / 60 + ":0" + progressValue % 60 + "/" + songLength);
                } else {
                    textView.setText(progressValue / 60 + ":" + progressValue % 60 + "/" + songLength);
                }

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
        //looping();
        if (mediaPlayer.isPlaying()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (progressValue % 60 < 10) {
                        remoteViews.setTextViewText(R.id.notificationTimer, progressValue / 60 + ":0" + progressValue % 60 + "/" + songLength);
                        notificationManager.notify(notification_id, builder.build());
                    } else {
                        remoteViews.setTextViewText(R.id.notificationTimer, progressValue / 60 + ":" + progressValue % 60 + "/" + songLength);
                        notificationManager.notify(notification_id, builder.build());
                    }
                    //notificationPlayer();
                    playCycle();
                }

            };
            handler.postDelayed(runnable, 1000 );

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
        if(songCursor != null) {
            songCursor.close();
        }
    }

   private Intent getNotificationIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        return intent;
        //Intent intent = new Intent(this, MainActivity.class);
      //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_SINGLE_TOP   );
     //   return intent;


    }

    public void expandStatusBar(){


    }

    public void keyLockChecker(){
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode()) {
            //it is locked
        } else {
            //it is not locked
        }
    }


    public void notificationPlayer() {

        Intent pauseIntent = new Intent();
        pauseIntent.setAction(PAUSE);
        pauseIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // pauseIntent.putExtra(PAUSE,"value!");


        Intent forwardIntent = new Intent();
        forwardIntent.setAction(FORWARD);
        forwardIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Intent backwardIntent = new Intent();
        backwardIntent.setAction(BACKWARD);
        backwardIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        if (isThereNotificationSet == false) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            pendingPlayPauseIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingForwardIntent = PendingIntent.getBroadcast(this, 0, forwardIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingBackwardIntent = PendingIntent.getBroadcast(this, 0, backwardIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
            if (mediaPlayer.isPlaying()) {
                remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.yellow_pause);
            } else {
                remoteViews.setImageViewResource(R.id.notificationPausePlayButton, R.drawable.play);
            }
            remoteViews.setImageViewResource(R.id.imageView, R.drawable.ic_note);
            remoteViews.setTextViewText(R.id.nTitle, current_Title);

            if (progressValue % 60 < 10) {
                remoteViews.setTextViewText(R.id.notificationTimer, progressValue / 60 + ":0" + progressValue % 60 + "/" + songLength);
            } else {
                remoteViews.setTextViewText(R.id.notificationTimer, progressValue / 60 + ":" + progressValue % 60 + "/" + songLength);
            }


                remoteViews.setOnClickPendingIntent(R.id.notificationPausePlayButton, pendingPlayPauseIntent);
                remoteViews.setOnClickPendingIntent(R.id.notificationForwardButton, pendingForwardIntent);
                remoteViews.setOnClickPendingIntent(R.id.notificationBackwardButton, pendingBackwardIntent);




                // to manage clicking on notification  Intent NotifIntent = new Intent(this, MainActivity.class);
                // to manage clicking on notification NotifIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_SINGLE_TOP );


                builder = new NotificationCompat.Builder(getApplicationContext(), "channel_1");
                // to manage clicking on notification builder.setContentIntent(PendingIntent.getActivity(this, 0, NotifIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                builder.setContent(remoteViews);
                builder.setSmallIcon(R.drawable.ic_notifiaction_status_bar);
                builder.setTicker(current_Title);



                notificationManager.notify(notification_id, builder.build());
            }
        }

public void findFeaturesAndSetButtons(){

        // find them, find them all!
        title = (TextView) findViewById(R.id.title_view);
        title.setSelected(true);
        listView = (ListView) findViewById(R.id.listView);
        // locationView = (TextView) findViewById(R.id.locationView);
        pausePlayButton = (ImageButton) findViewById(R.id.pauseButton);
        forwardButton = (ImageButton) findViewById(R.id.forwardButton);
        backwardButton = (ImageButton) findViewById(R.id.backwardButton);
        repeatButton = (ImageButton) findViewById(R.id.repeatButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.timer);


        // to see pause/play button and repeat button when app is starting
        pausePlayButton.setBackgroundResource(R.drawable.play);
        repeatButton.setBackgroundResource(R.drawable.repeat);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        loop = false;

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
            findFeaturesAndSetButtons();
            CreateAdapter_and_getLocation_also_setupOnClickListener();
            setUpNotif();
        } else {
            requestStoragePermission();
        }

// after requestStoragePermission there can't be any more instructions in onCreate because program will continue without permission which will crush in result

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(context,"Closing an App!",Toast.LENGTH_LONG).show();
                            closeApplication();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }



    private Intent goToMenuActivity(){

        Intent goToMenuActivityIntent = new Intent(this, MenuActivity.class);
        goToMenuActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return goToMenuActivityIntent;
    }

    public void setUpNotif(){
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationPlayer();
        // processIntentAction(getIntent());

        MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(PAUSE);
        filter.addAction(FORWARD);
        filter.addAction(BACKWARD);
        this.registerReceiver(broadcastReceiver, filter);
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                startActivity(goToMenuActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String string = intent.getAction();

            switch (string){
                case PAUSE:
                    pausePlayAction();
                    break;


                case FORWARD:
                    forwardAction();
                    break;

                case BACKWARD:
                    backwardAction();
                    break;

            }

        }
    }


    public void closeApplication() {
        finish();
        System.exit(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted!", Toast.LENGTH_LONG).show();
                findFeaturesAndSetButtons();
                CreateAdapter_and_getLocation_also_setupOnClickListener();
                setUpNotif();
            } else {
                Toast.makeText(this,"Permission denied!", Toast.LENGTH_LONG).show();
                closeApplication();

            }
        }
    }
}






