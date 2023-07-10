package com.example.rumahmode.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rumahmode.R;
import com.example.rumahmode.fragments.HistoryFragment;
import com.example.rumahmode.fragments.TempViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductsAdapter extends BaseAdapter {

    // Context object
    private Context context;

    // Database adapter object
    private DatabaseAdapter dbAdapter;

    // Cursor object for products records
    private Cursor cursor;

    // User id
    private long user_id;

    private OnHistoryChangedListener listener;
    private TempViewModel tempViewModel;
    // Constructor
    public ProductsAdapter(Context context, DatabaseAdapter dbAdapter, long user_id, OnHistoryChangedListener listener, TempViewModel tempViewModel) {
        this.context = context;
        this.dbAdapter = dbAdapter;
        this.cursor = dbAdapter.getAllProducts();
        this.user_id = user_id;
        this.listener = listener;
        this.tempViewModel = tempViewModel;
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
            convertView = inflater.inflate(R.layout.product_item, parent, false);
        }

        // Get the views from the layout
        ImageView productImage = (ImageView) convertView.findViewById(R.id.product_image);
        TextView productTitle = (TextView) convertView.findViewById(R.id.product_title);
        TextView productPrice = (TextView) convertView.findViewById(R.id.product_price);
        Button buyButton = (Button) convertView.findViewById(R.id.buy_button);

        // Move the cursor to the current position
        cursor.moveToPosition(position);

        // Get the product data from the cursor
        @SuppressLint("Range") long product_id = cursor.getLong(cursor.getColumnIndex(DatabaseAdapter.KEY_ID));
        @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TITLE));
        @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(DatabaseAdapter.KEY_PRICE));
        @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex(DatabaseAdapter.KEY_IMAGE));

        // Set the views with the product data
        productTitle.setText(title);
        productPrice.setText("IDR " + price);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        productImage.setImageBitmap(bitmap);

        // Set the buy button click listener
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation popup
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm purchase");
                builder.setMessage("Are you sure you want to buy " + title + " for IDR " + price + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Insert a history record into the database with the user id, product id and current date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = dateFormat.format(new Date());
                        long ret = dbAdapter.insertHistory(user_id, product_id, date);

                        // Notify data change
                        if (listener != null) {
                            listener.onHistoryChanged();
                        }
                        tempViewModel.setTempVal(tempViewModel.getTempVal().getValue() + 1);

                        // Show a success message
                        if (ret != 0) {
                            Toast.makeText(context, "You have bought " + title + ".", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
                builder.create().show();
            }
        });

        // Return the view
        return convertView;
    }
}
