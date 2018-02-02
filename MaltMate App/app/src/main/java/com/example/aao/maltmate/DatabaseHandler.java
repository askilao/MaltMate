package com.example.aao.maltmate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.aao.maltmate.data.Favorite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Askil Olsen
 * @version 1.0
 * @since 2017-11-23
 */

/**
 * To handle all things with the database (sql queries, creating tables etc)
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/com.example.aao.maltmate/databases/";
    //file name in assets directory
    private static String DB_NAME = "maltmatedata.sqlite3";

    private SQLiteDatabase myDataBase;

    private Context myContext;

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }
    //creates the database stored in the assets directory
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist) {

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e){
                throw new Error("Error reading database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){
            //database doesn't exist yet.
        }

        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }
    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    /**
     * Adds a favorite object to the database
     * @param favorite new favorite object
     * @see Favorite
     */
    public void addFavorite(Favorite favorite){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("_id", favorite.getName());
        contentValues.put("colRating", favorite.getRating());
        contentValues.put("colLocation", favorite.getLocation());
        contentValues.put("colNotes", favorite.getNotes());

        sqLiteDatabase.insert("favoritesTable", null, contentValues);

        sqLiteDatabase.close();
    }
    /**
     * Search for an entry in the whiskey table
     * @param type column id in the table
     * @param search what to match for in the sql query
     * @return Cursor cursor with the given database entry
     */
    public Cursor getSearch(String type, String search){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM whiskeydatatable WHERE " + type+ " LIKE '%"+search+"%' ORDER BY name", null);
        if(c.moveToFirst()) {
            Log.d("DEBUGTAG", "cursor");
            return c;
        }
        else {
            Log.d("DEBUGTAG", "cursor failed");
            return null;
        }
    }

    /**
     * Retrieve all in the whiskeytable
     * @return Cursor with all the entries in the table
     */
    public Cursor getAll(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM whiskeydatatable ORDER BY name", null);
        if(c.moveToFirst()) {
            Log.d("DEBUGTAG", "cursor");
            return c;
        }
        else {
            Log.d("DEBUGTAG", "cursor failed");
            return null;
        }

    }

    /**
     * Retrieve all entries in the favoriteTable
     * @return Cursor with all entries in table
     */

    public Cursor getAllFavorites(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM favoritesTable ORDER BY colRating", null);

        if(c.moveToFirst()) {
            Log.d("DEBUGTAG", "cursor");
            return c;
        }
        else {
            Log.d("DEBUGTAG", "cursor failed");
            return null;
        }
    }

    /**
     * Retrieve one whiskey in favoriteTable based on the name
     * @param search name of whiskey to search for
     * @return cursor with all data on given whiskey
     */
    public Cursor oneFavorite(String search){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM favoritesTable WHERE _id='"+search+"'", null);
        if(c.moveToFirst()) {
            Log.d("DEBUGTAG", "cursor");
            return c;
        }
        else {
            Log.d("DEBUGTAG", "cursor failed");
            return null;
        }
    }

    /**
     * Delete an entry in a given table where name is the condition
     * @param tablename name of the table you want to delete in
     * @param condition what you want to delete
     */
    public void deleteEntry(String tablename, String condition) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+tablename+" WHERE _id='"+condition+"'");
    }


}
