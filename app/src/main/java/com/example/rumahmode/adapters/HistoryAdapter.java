package com.example.rumahmode.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rumahmode.R;

public class HistoryAdapter extends BaseAdapter {

    // Context object
    private Context context;

    // Database adapter object
    private DatabaseAdapter dbAdapter;

    // Cursor object for history records
    private Cursor cursor;

    // User id
    private long user_id;

    // Constructor
    public HistoryAdapter(Context context, DatabaseAdapter dbAdapter, long user_id) {
        this.context = context;
        this.dbAdapter = dbAdapter;
        this.cursor = dbAdapter.getHistoryByUser(user_id); // user_id is the id of the logged in user
        this.user_id = user_id;
    }

    // Get the number of items in the adapter
    @Override
    public int getCount() {
        return cursor.getCount();
    }

    // Get the item at a given position in the adapter
    @Override
    public Object getItem(int position) {
        cursor.moveToPosition(position);
        return cursor;
    }

    // Get the item id at a given position in the adapter
    @SuppressLint("Range")
    @Override
    public long getItemId(int position) {
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndex(DatabaseAdapter.KEY_ID));
    }

    // Get the view for a given position in the adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout if convertView is null
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_item, parent, false);
        }

        // Get the views from the layout
        TextView productTitle = (TextView) convertView.findViewById(R.id.product_title);
        TextView productPrice = (TextView) convertView.findViewById(R.id.product_price);
        TextView purchaseDate = (TextView) convertView.findViewById(R.id.purchase_date);
        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.delete_button);

        // Move the cursor to the current position
        cursor.moveToPosition(position);

        // Get the history record data from the cursor
        @SuppressLint("Range") long history_id = cursor.getLong(cursor.getColumnIndex(DatabaseAdapter.KEY_ID));
        @SuppressLint("Range") long product_id = cursor.getLong(cursor.getColumnIndex(DatabaseAdapter.KEY_PRODUCT_ID));
        @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_DATE));

        // Get the product data from the database by product id
        Cursor productCursor = dbAdapter.getProduct(product_id);
        if (productCursor.moveToFirst()) {
            @SuppressLint("Range") String title = productCursor.getString(productCursor.getColumnIndex(DatabaseAdapter.KEY_TITLE));
            @SuppressLint("Range") double price = productCursor.getDouble(productCursor.getColumnIndex(DatabaseAdapter.KEY_PRICE));

            // Set the views with the product data
            productTitle.setText(title);
            productPrice.setText("IDR " + price);
            purchaseDate.setText(date);
        }
        productCursor.close();

        // Set the delete button click listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the history record from the database by history id
                new DeleteHistoryTask(history_id).execute();
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        // Return the view
        return convertView;
    }

    // Set the cursor
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    private class DeleteHistoryTask extends AsyncTask<Long, Void, Void> {

        // The history id to delete
        private long history_id;

        // The constructor
        public DeleteHistoryTask(long history_id) {
            this.history_id = history_id;
        }

        // The background method
        @Override
        protected Void doInBackground(Long... params) {
            // Delete the history record from the database by history id
            dbAdapter.deleteHistory(history_id);
            return null;
        }

        // The post-execute method
        @Override
        protected void onPostExecute(Void result) {
            // Update the cursor and notify the adapter
            cursor = dbAdapter.getHistoryByUser(user_id);
            notifyDataSetChanged();
        }
    }
}
