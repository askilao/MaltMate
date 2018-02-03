package com.example.aao.maltmate;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Askil Olsen
 * @version 1.0
 * @since 2017-12-23
 */

/**
 * Activity that lets user see all the whiskeys and search with particular keywords
 * Also available to use a barcode scanner
 */
public class FindWhiskeyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String KEY_PRIMARY ="primarykey";
    private String searchType;
    private String[] Search_type;
    private Spinner spinnerSearch;
    private EditText search;
    private ListView listView;
    private DatabaseHandler dbhandler;
    private CustomCursorAdapter customAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_whiskey);
        Search_type = getResources().getStringArray(R.array.search_type);
        search = (EditText) findViewById(R.id.search);

        /*
        * Dropdown menu with the different things user can search for
        * equal to column ids in whiskey table*/
        spinnerSearch = (Spinner) findViewById(R.id.spinnerSearch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_type_displayable, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSearch.setAdapter(adapter);
        spinnerSearch.setOnItemSelectedListener(this);

        dbhandler = new DatabaseHandler(this);
        listView = (ListView) findViewById(R.id.list_data);
        listView.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        /*
                        * When clicked the name of the whiskey is found by view
                        * it is then sent to the next activity
                        * so that the singledisplay acitivity knows which whiskey
                        * to display*/
                        String pk = ((TextView) view.findViewById(R.id.whiskey)).getText().toString();
                        Intent intent = new Intent(FindWhiskeyActivity.this, SingleWhiskyDisplayActivity.class);
                        intent.putExtra(KEY_PRIMARY, pk);
                        intent.putExtra("searchtype","primary");
                        startActivity(intent);
                    }
                }
        );
        //Displays all the whiskeys
        cursor = dbhandler.getAll();
        toList(cursor);
    }
    //To select what to search for from the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        searchType = Search_type[position];
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        searchType = "Nothing specified";
    }

    /**
     * Search in the whiskeyTable with a keyword
     * @param v the searchbutton in the activity
     * @see DatabaseHandler
     */
    public void search(View v) {
        String searchWord = search.getText().toString();
        cursor = dbhandler.getSearch(searchType, searchWord);
        toList(cursor);
    }
    /**
     * Prints all the entries to a listview through an adapter
     * @param cursor Cursor with all the whiskeyTable entries
     * @see CustomCursorAdapter
     */
    public void toList(Cursor cursor){
        if(cursor !=null) {
            customAdapter = new CustomCursorAdapter(this, cursor);
            listView.setAdapter(customAdapter);
        }
        else
            Log.d("DEBUGTAG", "cursor failed");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.find_whiskey_menu, menu);
        return true;
    }
    /*
    * Two options in actionbar menu
    * Back to go to previous activity
    * Barcode to scan for a barcode via barcodeactivity*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.back:
                finish();
                Log.d("DEBUGTAG", "BACK BUTTON");
                break;
            case R.id.barcode:
                Intent intent = new Intent(this, BarcodeActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
