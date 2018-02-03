package com.example.aao.maltmate;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import java.io.IOException;

/**
 * @author Askil Olsen
 * @version 1.0
 * @since 2017-11-09
 */

/**
 *Main menu for selecting to find whiskey or see favorites
 */
public class MainActivity extends AppCompatActivity {

    ImageButton findwhiskeys;
    ImageButton mywhiskeys;
    public static final int PERMISSION_REQUEST = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        DatabaseHandler myDbHelper = new DatabaseHandler(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }
        try {
            //open database when application is started
            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }
        findwhiskeys = (ImageButton) findViewById(R.id.findwhiskeyButton);
        mywhiskeys = (ImageButton) findViewById(R.id.myWhiskeyButton);

    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.findwhiskeyButton:
                Intent Intent = new Intent(this, FindWhiskeyActivity.class);
                startActivity(Intent);
                break;
            case R.id.myWhiskeyButton:
                Intent myIntent = new Intent(this, displayFavoritesActivity.class);
                startActivity(myIntent);
                break;
        }
    }


}


