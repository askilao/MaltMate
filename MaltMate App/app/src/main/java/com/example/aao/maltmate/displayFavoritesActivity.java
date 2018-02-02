package com.example.aao.maltmate;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * @author Askil Olsen
 * @version 1.0
 * @since 2017-12-03
 */

/**
 *Activity that displays all the whiskeys in the favorite table
 */
public class displayFavoritesActivity extends AppCompatActivity {
    public final static String PK_KEY="pkKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_favorites);
        //Gets all the favorites and sets them in a listview
        final DatabaseHandler dbhandler = new DatabaseHandler(this);
        Cursor cursor = dbhandler.getAllFavorites();
        toList(cursor);
    }
    /**
     * Puts the cursor in a listview via an adapter
     * @param cursor the cursor with all the favoriteTable entries
     * @see FavoriteAdapter
     */
    public void toList(Cursor cursor){
        if(cursor !=null) {
            ListView listView = (ListView) findViewById(R.id.list_data);
            //Adapter to print out each favorite item in a listview
            FavoriteAdapter favoriteAdapter = new FavoriteAdapter(this, cursor);
            listView.setAdapter(favoriteAdapter);
            //If a whiskey is clicked it is display in SingleFavoriteDisplay
            listView.setOnItemClickListener(

                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Toast.makeText(getBaseContext(),  ((TextView) view.findViewById(R.id.whiskey)).getText().toString() + " clicked", Toast.LENGTH_SHORT).show();
                            String pk = ((TextView) view.findViewById(R.id.whiskey)).getText().toString();
                            Intent intent = new Intent(displayFavoritesActivity.this, SingleFavoriteDisplay.class);
                            intent.putExtra("name", pk);
                            startActivity(intent);
                            finish();

                        }
                    }
            );
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.back:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
