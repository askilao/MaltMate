package com.example.aao.maltmate;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author Askil Olsen
 * @version 1.0
 * @since 2017-12-03
 */

/**
 * Adapter for displaying a favorite object in a listview
 */

public class FavoriteAdapter extends CursorAdapter {
    DatabaseHandler dbhandler;
    /*
    * Two cursors are used
    * one for displaying info from favoritestable
    * and one for displaying info from whiskeytable*/
    public FavoriteAdapter(Context context, Cursor favoritecursor) {

        super(context, favoritecursor,0);
        dbhandler = new DatabaseHandler(context);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        return LayoutInflater.from(context).inflate(R.layout.single_favorite_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // Setting the data from the two tables to the view
        Cursor c = dbhandler.getSearch("name", cursor.getString(0));

        ImageView imageView = (ImageView) view.findViewById(R.id.whiskeyimage);
        int resId = context.getResources().getIdentifier("picture"+c.getString(7) , "drawable", context.getPackageName());
        imageView.setImageResource(resId);

        TextView whiskeyText = (TextView) view.findViewById(R.id.whiskey);
        whiskeyText.setText(cursor.getString(0));

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingBar.setRating(cursor.getFloat(1));
        TextView location = (TextView) view.findViewById(R.id.location);
        location.setText(cursor.getString(2));
    }
}
