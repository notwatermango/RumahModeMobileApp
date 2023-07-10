package com.example.rumahmode.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rumahmode.R;
import com.example.rumahmode.adapters.DatabaseAdapter;
import com.example.rumahmode.adapters.HistoryAdapter;
import com.example.rumahmode.adapters.OnHistoryChangedListener;

public class HistoryFragment extends Fragment implements OnHistoryChangedListener {

    // List view and adapter objects
    private ListView listView;
    private HistoryAdapter historyAdapter;

    // Database adapter object
    private DatabaseAdapter dbAdapter;

    // User id
    private long user_id;

    // User view model object
    private UserViewModel userViewModel;
    private Cursor cursor; // for the adapter

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the database adapter
        dbAdapter = new DatabaseAdapter(getActivity());

        // Get the view model instance
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // Observe the user id live data
        userViewModel.getUser_id().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                // Update the user id
                user_id = aLong;

                // Update the cursor and adapter
                cursor = dbAdapter.getHistoryByUser(user_id);
                historyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Initialize the list view and adapter
        listView = (ListView) view.findViewById(R.id.list_view);
        historyAdapter = new HistoryAdapter(getActivity(), dbAdapter, user_id);
        listView.setAdapter(historyAdapter);

        // Return the view
        return view;
    }

    // Get the history adapter
    public HistoryAdapter getHistoryAdapter() {
        return historyAdapter;
    }

    @Override
    public void onHistoryChanged() {
        // Update the cursor and notify the adapter
        cursor = dbAdapter.getHistoryByUser(user_id);
        historyAdapter.setCursor(cursor);
        historyAdapter.notifyDataSetChanged();
    }
}
