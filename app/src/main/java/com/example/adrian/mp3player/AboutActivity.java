package com.example.adrian.mp3player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Adrian on 23.07.2018.
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle("About");
        //setTheme(android.R.style.Theme_Black_NoTitleBar);
        setContentView(R.layout.about_layout);
    }

    private Intent getGoToMenuActivity() {
        Intent goToMenuActivityIntent = new Intent(this, MenuActivity.class);
        goToMenuActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return goToMenuActivityIntent;

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_to_menu_activity, menu);
        return true;

    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_to_menu_activity_button:
                startActivity(getGoToMenuActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}