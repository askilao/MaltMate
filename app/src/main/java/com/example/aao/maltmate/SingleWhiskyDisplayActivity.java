package com.example.aao.maltmate;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * @author Askil Olsen
 * @since 2017-11-28
 */
/**
 * Activity to show a single whiskey stored in the whiskeytable
 */
public class SingleWhiskyDisplayActivity extends AppCompatActivity {
    public static final String KEY_WHISKYNAME="keyWhiskyName";
    DatabaseHandler dbhandler;
    Cursor cursor;
    String whiskeyname;
    String barcode;
    String searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_whisky_display);


        barcode = getIntent().getStringExtra(BarcodeActivity.KEY_BARCODE);
        searchType = getIntent().getStringExtra("searchtype");
        dbhandler = new DatabaseHandler(this);
        switch (searchType){
            case "primary":
                whiskeyname = getIntent().getStringExtra(FindWhiskeyActivity.KEY_PRIMARY);
                cursor = dbhandler.getSearch("name",whiskeyname);
                break;
            case "barcode":
                cursor = dbhandler.getSearch("barcode",barcode);
                whiskeyname = cursor.getString(1).toString();
                break;
        }
        ImageView imageView = (ImageView) findViewById(R.id.whiskeyimage);
        int resId = getResources().getIdentifier("picture"+cursor.getString(7)+"large" , "drawable", getPackageName());
        imageView.setImageResource(resId);

        TextView whiskeyname = (TextView) findViewById(R.id.whiskey);
        whiskeyname.setText(cursor.getString(1));

        TextView whiskey_type = (TextView) findViewById(R.id.whiskey_type);
        whiskey_type.setText(cursor.getString(2));

        TextView whiskey_class = (TextView) findViewById(R.id.whiskey_class);
        whiskey_class.setText(cursor.getString(3));

        TextView origin = (TextView) findViewById(R.id.origin);
        origin.setText(cursor.getString(4));

        TextView distillery = (TextView) findViewById(R.id.distillery);
        distillery.setText(cursor.getString(5));

        TextView taste = (TextView) findViewById(R.id.taste);
        taste.setText(cursor.getString(9));

        TextView desription = (TextView) findViewById(R.id.description);
        desription.setText(cursor.getString(8));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(checkFavorite()){
            getMenuInflater().inflate(R.menu.added, menu);
        } else {
            getMenuInflater().inflate(R.menu.add, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.barcodeback:
               finish();
                Log.d("DEBUGTAG", "back button clicked");
                break;
            case R.id.add_to_favorites:
                Intent intent = new Intent(this, addToFavoritesActivity.class);
                intent.putExtra("name", whiskeyname);
                Log.d("DEBUGTAG", whiskeyname);
                startActivity(intent);
                finish();
                break;
            case R.id.Added:
                Intent Displayintent = new Intent(this, SingleFavoriteDisplay.class);
                Displayintent.putExtra("name", whiskeyname);
                startActivity(Displayintent);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * To check if current whiskey is in favoritesTable or not
     * because user should not be able to add it twice
     * @return true if in table  false if not
     */
    public boolean checkFavorite(){
        DatabaseHandler db = new DatabaseHandler(this);
        Cursor c = db.oneFavorite(whiskeyname);
        if(c !=null){
            return true;
        } else {
            return false;
        }
    }
}
