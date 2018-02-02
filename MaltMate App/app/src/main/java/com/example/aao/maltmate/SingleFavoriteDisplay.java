package com.example.aao.maltmate;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author Askil Olsen
 * @version 1.0
 * @since 2017-12-04
 */

/**
 * Activity to show a single favorite
 * Shows an image, name, rating, location if added and other notes
 */
public class SingleFavoriteDisplay extends AppCompatActivity {
    private String whiskeyname;
    public static final String KEY_WHISKYNAME="keyWhiskyName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_favorite_display);

        //Gets whiskey name from the activity that started it
        whiskeyname = getIntent().getStringExtra("name");
        DatabaseHandler dbhandler = new DatabaseHandler(this);
        //Gets the rest of the info from the table
        Cursor cursor = dbhandler.oneFavorite(whiskeyname);
        //The image info is stored in whiskeytable so this must also be accessed
        Cursor c = dbhandler.getSearch("name", whiskeyname);
        ImageView imageView = (ImageView) findViewById(R.id.whiskeyimage);
        int resId = getResources().getIdentifier("picture"+c.getString(7)+"large" , "drawable", getPackageName());
        imageView.setImageResource(resId);

        //Sets all the textviews and ratingbar
        TextView whiskeyname = (TextView) findViewById(R.id.whiskey);
        whiskeyname.setText(cursor.getString(0));

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(cursor.getFloat(1));

        TextView location = (TextView) findViewById(R.id.location);
        location.setText(cursor.getString(2));

        TextView note = (TextView) findViewById(R.id.note);
        note.setText(cursor.getString(3));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.fav_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.barcodeback:
                finish();
                break;
            case R.id.edit:
                Intent intent = new Intent(this, addToFavoritesActivity.class);
                intent.putExtra("name", whiskeyname);
                startActivity(intent);
                finish();
                break;
            case R.id.delete:
                AlertDialog diaBox = AskOption();
                diaBox.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called by the AlertDialog
     * Deletes the current whiskeyobject
     * @see DatabaseHandler
     * @see AlertDialog
     */
    public void deleteFavorite(){
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteEntry("favoritesTable", whiskeyname);
    }
    /***************************************************************************************
     *    Title: Android confirmation message for delete [closed]
     *    Author: Ram kiran
     *    Date: 31.07.2012
     *    Code version: N/A
     *    Availability: https://stackoverflow.com/questions/11740311/android-confirmation-message-for-delete
     *
     ***************************************************************************************/
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to delete "+whiskeyname+" from favorites?")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteFavorite();
                        dialog.dismiss();
                        Intent myIntent = new Intent(SingleFavoriteDisplay.this, displayFavoritesActivity.class);
                        startActivity(myIntent);
                        finish();

                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
