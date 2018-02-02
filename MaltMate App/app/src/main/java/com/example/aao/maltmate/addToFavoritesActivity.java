package com.example.aao.maltmate;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aao.maltmate.data.Favorite;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * @author Askil Olsen
 * @version 1.0
 * @since 2017-11-30
 *
 */

/**
 * Activity that lets user add a given whiskey to favoritedatabase
 */
public class addToFavoritesActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST = 100;
    public static final int PLACE_PICKER_REQUEST = 1;
    private DatabaseHandler dbhandler;
    private Cursor cursor;
    private String whiskeyname;
    private TextView location;
    private EditText note;
    private RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorites);


        dbhandler = new DatabaseHandler(this);
        //Whiskeyname was passed from previous activity
        whiskeyname = getIntent().getStringExtra("name");
        location = (TextView) findViewById(R.id.location);
        //Checks if the whiskey is already in the database
        Log.d("DEBUGTAG", whiskeyname);
        //Returns the info stored about the whiskey in the whiskey table
        cursor = dbhandler.getSearch("name",whiskeyname);

        note = (EditText) findViewById(R.id.note);
        rating = (RatingBar) findViewById(R.id.ratingBar);

        /*
        sets the image as the value of the whiskey tables "picture" row plus the word picture
        All pictures are have picture + an int as the name
        */
        ImageView imageView = (ImageView) findViewById(R.id.whiskeyimage);
        int resId = getResources().getIdentifier("picture"+cursor.getString(7) , "drawable", getPackageName());
        imageView.setImageResource(resId);

        TextView whiskey = (TextView) findViewById(R.id.whiskey);
        whiskey.setText(cursor.getString(1));

        //Asks the user for permission to user the GPS sensor
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Google created class that pops up and allows user to select location
                based on nearby places
                */
                try{
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(addToFavoritesActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    Log.e("DEBUGTAG","Placepicker failed");
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e("DEBUGTAG","Placepicker ");
                }

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                //Returns the place selected by user
            Place place = PlacePicker.getPlace(data, this);
              location.setText(String.format(place.getName().toString()));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorites_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.back:
                finish();
                Log.d("DEBUGTAG", "back button clicked");
                break;
            case R.id.save:
                checkForDuplicates();
                String aloc;
                if(location.getText().toString().equals("Add Location")){
                    aloc = "No location added";
                } else {
                    aloc = location.getText().toString();
                }
                String aNote = note.getText().toString();
                float aRating = rating.getRating();
                dbhandler.addFavorite(new Favorite(whiskeyname, aRating, aloc, aNote));
                Toast.makeText(getBaseContext(), "Favorite saved", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * Function that asks for a certain whiskey in the favoritesTable
     * If it is there then it deletes it and lets user create a new one
     * @see DatabaseHandler
     */
    void checkForDuplicates(){
        DatabaseHandler db = new DatabaseHandler(this);
        Cursor c = db.oneFavorite(whiskeyname);
        if(c !=null){
            db.deleteEntry("favoritesTable", whiskeyname);
        }
    }


}
