package com.example.adrian.mp3player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Adrian on 20.07.2018.
 */

public class MenuActivity extends AppCompatActivity {

ArrayList<String> menuList;
ListView menuListView;
ArrayAdapter<String> menuAdapter;
Context context;

    private Intent getGoToAboutActivity() {
        Intent goToAboutActivityIntent = new Intent(this, AboutActivity.class);
        goToAboutActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return goToAboutActivityIntent;
    }



    private Intent getGoToMainActivity() {
        Intent goToMainActivityIntent = new Intent(this, MainActivity.class);
        goToMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return goToMainActivityIntent;
    }

public void CreateMenuAdapter_and_setupMenuListener(){

    menuList = new ArrayList<>();
    menuList.add("About");
    menuList.add("Instruction manual");
    menuAdapter = new ArrayAdapter<String>(this, R.layout.list_layout, menuList);
    menuListView.setAdapter(menuAdapter);

    menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i){
                case 0: {
                    //Toast toast = Toast.makeText(context, "0", Toast.LENGTH_LONG);
                    //toast.show();
                    startActivity(getGoToAboutActivity());
                    break;
                }

                case 1: {
                    Toast toast = Toast.makeText(context, "1", Toast.LENGTH_LONG);
                    toast.show();
                    break;
                }

            }

        }
    });

}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        context = this;
        menuListView = (ListView)findViewById(R.id.menu_list_view);
        CreateMenuAdapter_and_setupMenuListener();

    }



    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_to_main_activity, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_to_main_activity_button:
                startActivity(getGoToMainActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
