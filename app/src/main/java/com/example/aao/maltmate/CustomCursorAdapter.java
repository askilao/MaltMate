package com.example.aao.maltmate;

/**
 * Created by aao on 24/11/17.
 */
/**
 * Adapter to print each whiskey to a listview
 * @see FindWhiskeyActivity
 */


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomCursorAdapter extends CursorAdapter {

    public CustomCursorAdapter(Context context, Cursor c) {

        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        return LayoutInflater.from(context).inflate(R.layout.single_row_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views
        ImageView imageView = (ImageView) view.findViewById(R.id.whiskeyimage);
        int resId = context.getResources().getIdentifier("picture"+cursor.getString(7) , "drawable", context.getPackageName());
        imageView.setImageResource(resId);

        TextView whiskeyText = (TextView) view.findViewById(R.id.whiskey);
        whiskeyText.setText(cursor.getString(1));

        TextView whiskey_typeText = (TextView) view.findViewById(R.id.whiskey_type);
        whiskey_typeText.setText(cursor.getString(2));

        TextView whiskey_classText = (TextView) view.findViewById(R.id.whiskey_class);
        whiskey_classText.setText(cursor.getString(3));

        TextView distilleryText = (TextView) view.findViewById(R.id.distillery);
        distilleryText.setText(cursor.getString(5));


    }

}
