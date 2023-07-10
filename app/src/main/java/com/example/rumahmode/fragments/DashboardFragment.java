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

    // View objects
    private TextView welcomeMessage;
    private ImageView discountBanner;
    private EditText addressInput;
    private Button submitButton;

    // Database adapter object
    private DatabaseAdapter dbAdapter;

    // User id and name
    private long user_id;
    private String user_name;
    private String user_address;

    // User view model object
    private UserViewModel userViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the database adapter
        dbAdapter = new DatabaseAdapter(getActivity());

        // Get the view model instance
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // Observe the user id live data
        userViewModel.getUser_id().observe(this, new Observer<Long>() {
            @SuppressLint("Range")
            @Override
            public void onChanged(Long aLong) {
                // Update the user id
                user_id = aLong;

                // Get the user name from the database by user id
                Cursor cursor = dbAdapter.getUser(user_id);
                if (cursor.moveToFirst()) {
                    user_name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
                    user_address = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ADDRESS));
                }
                cursor.close();

                // Update the welcome message with the user name
                welcomeMessage.setText("Welcome, " + user_name + "!");
                addressInput.setText(user_address);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize the views from the layout
        welcomeMessage = (TextView) view.findViewById(R.id.welcome_message);
        discountBanner = (ImageView) view.findViewById(R.id.discount_banner);
        addressInput = (EditText) view.findViewById(R.id.address_input);
        submitButton = (Button) view.findViewById(R.id.submit_button);
        // Observe the user id live data
        userViewModel.getUser_id().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @SuppressLint("Range")
            @Override
            public void onChanged(Long aLong) {
                // Update the user id
                user_id = aLong;

                // Get the user name from the database by user id
                Cursor cursor = dbAdapter.getUser(user_id);
                if (cursor.moveToFirst()) {
                    user_name = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_NAME));
                }
                cursor.close();

                // Update the welcome message with the user name
                welcomeMessage.setText("Welcome, " + user_name + "!");



            }
        });
        // Set the discount banner image from a resource or url
        discountBanner.setImageResource(R.drawable.bannerdiscount);
        // Set the submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the new address from the input field
                String newAddress = addressInput.getText().toString();

                // Validate the input (not empty, valid format, etc.)
                if (newAddress.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter your new address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update the user address in the database by user id
                int result = dbAdapter.updateUser(user_id, user_name, newAddress);

                // Show a success or failure message
                if (result > 0) {
                    Toast.makeText(getActivity(), "Your address has been updated.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to update your address.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Return the view
        return view;
    }


}
