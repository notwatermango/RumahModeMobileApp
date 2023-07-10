package com.example.rumahmode.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rumahmode.R;
import com.example.rumahmode.adapters.DatabaseAdapter;

public class DashboardFragment extends Fragment {

    private TextView welcomeMessage;
    private ImageView discountBanner;
    private EditText addressInput;
    private Button submitButton;

    private DatabaseAdapter dbAdapter;

    private long user_id;
    private String user_name;
    private String user_address;

    private UserViewModel userViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = new DatabaseAdapter(getActivity());

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUser_id().observe(this, new Observer<Long>() {
            @SuppressLint("Range")
            @Override
            public void onChanged(Long aLong) {
                user_id = aLong;

                Cursor cursor = dbAdapter.getUser(user_id);
                if (cursor.moveToFirst()) {
                    user_name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
                    user_address = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ADDRESS));
                }
                cursor.close();

                welcomeMessage.setText("Welcome, " + user_name + "!");
                addressInput.setText(user_address);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        welcomeMessage = (TextView) view.findViewById(R.id.welcome_message);
        discountBanner = (ImageView) view.findViewById(R.id.discount_banner);
        addressInput = (EditText) view.findViewById(R.id.address_input);
        submitButton = (Button) view.findViewById(R.id.submit_button);
        userViewModel.getUser_id().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @SuppressLint("Range")
            @Override
            public void onChanged(Long aLong) {
                user_id = aLong;

                Cursor cursor = dbAdapter.getUser(user_id);
                if (cursor.moveToFirst()) {
                    user_name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
                }
                cursor.close();

                welcomeMessage.setText("Welcome, " + user_name + "!");



            }
        });
        discountBanner.setImageResource(R.drawable.bannerdiscount);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAddress = addressInput.getText().toString();

                if (newAddress.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter your new address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int result = dbAdapter.updateUser(user_id, user_name, newAddress);

                if (result > 0) {
                    Toast.makeText(getActivity(), "Your address has been updated.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to update your address.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
