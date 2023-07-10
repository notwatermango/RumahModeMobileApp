package com.example.rumahmode.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rumahmode.R;

public class HistoryAdapter extends BaseAdapter {

    private Context context;

    private DatabaseAdapter dbAdapter;

    private Cursor cursor;

    private long user_id;

    public HistoryAdapter(Context context, DatabaseAdapter dbAdapter, long user_id) {
        this.context = context;
        this.dbAdapter = dbAdapter;
        this.cursor = dbAdapter.getHistoryByUser(user_id);
        this.user_id = user_id;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        cursor.moveToPosition(position);
        return cursor;
    }

    @SuppressLint("Range")
    @Override
    public long getItemId(int position) {
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndex(DatabaseAdapter.KEY_ID));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_item, parent, false);
        }

        TextView productTitle = (TextView) convertView.findViewById(R.id.product_title);
        TextView productPrice = (TextView) convertView.findViewById(R.id.product_price);
        TextView purchaseDate = (TextView) convertView.findViewById(R.id.purchase_date);
        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.delete_button);

        cursor.moveToPosition(position);

        @SuppressLint("Range") long history_id = cursor.getLong(cursor.getColumnIndex(DatabaseAdapter.KEY_ID));
        @SuppressLint("Range") long product_id = cursor.getLong(cursor.getColumnIndex(DatabaseAdapter.KEY_PRODUCT_ID));
        @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_DATE));

        Cursor productCursor = dbAdapter.getProduct(product_id);
        if (productCursor.moveToFirst()) {
            @SuppressLint("Range") String title = productCursor.getString(productCursor.getColumnIndex(DatabaseAdapter.KEY_TITLE));
            @SuppressLint("Range") double price = productCursor.getDouble(productCursor.getColumnIndex(DatabaseAdapter.KEY_PRICE));

            productTitle.setText(title);
            productPrice.setText("IDR " + price);
            purchaseDate.setText(date);
        }
        productCursor.close();

        deleteButton.setOnClickListener(v -> {
            new DeleteHistoryTask(history_id).execute();
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    private class DeleteHistoryTask extends AsyncTask<Long, Void, Void> {

        private long history_id;

        public DeleteHistoryTask(long history_id) {
            this.history_id = history_id;
        }

        @Override
        protected Void doInBackground(Long... params) {
            dbAdapter.deleteHistory(history_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            cursor = dbAdapter.getHistoryByUser(user_id);
            notifyDataSetChanged();
        }
    }
}
