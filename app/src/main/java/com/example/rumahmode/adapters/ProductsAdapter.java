package com.example.rumahmode.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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


import com.example.rumahmode.R;
import com.example.rumahmode.fragments.TempViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductsAdapter extends BaseAdapter {

    private Context context;

    private DatabaseAdapter dbAdapter;

    private Cursor cursor;

    private long user_id;

    private OnHistoryChangedListener listener;
    private TempViewModel tempViewModel;
    public ProductsAdapter(Context context, DatabaseAdapter dbAdapter, long user_id, OnHistoryChangedListener listener, TempViewModel tempViewModel) {
        this.context = context;
        this.dbAdapter = dbAdapter;
        this.cursor = dbAdapter.getAllProducts();
        this.user_id = user_id;
        this.listener = listener;
        this.tempViewModel = tempViewModel;
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
            convertView = inflater.inflate(R.layout.product_item, parent, false);
        }

        ImageView productImage = (ImageView) convertView.findViewById(R.id.product_image);
        TextView productTitle = (TextView) convertView.findViewById(R.id.product_title);
        TextView productPrice = (TextView) convertView.findViewById(R.id.product_price);
        Button buyButton = (Button) convertView.findViewById(R.id.buy_button);

        cursor.moveToPosition(position);

        @SuppressLint("Range") long product_id = cursor.getLong(cursor.getColumnIndex(DatabaseAdapter.KEY_ID));
        @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_TITLE));
        @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(DatabaseAdapter.KEY_PRICE));
        @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex(DatabaseAdapter.KEY_IMAGE));

        productTitle.setText(title);
        productPrice.setText("IDR " + price);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        productImage.setImageBitmap(bitmap);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm purchase");
                builder.setMessage("Are you sure you want to buy " + title + " for IDR " + price + "?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = dateFormat.format(new Date());
                    long ret = dbAdapter.insertHistory(user_id, product_id, date);

                    if (listener != null) {
                        listener.onHistoryChanged();
                    }
                    tempViewModel.setTempVal(tempViewModel.getTempVal().getValue() + 1);

                    if (ret != 0) {
                        Toast.makeText(context, "You have bought " + title + ".", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", (dialog, which) -> {});
                builder.create().show();
            }
        });

        return convertView;
    }
}
