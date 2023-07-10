package com.example.rumahmode.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rumahmode.R;
import com.example.rumahmode.adapters.DatabaseAdapter;
import com.example.rumahmode.adapters.HistoryAdapter;
import com.example.rumahmode.adapters.OnHistoryChangedListener;

public class HistoryFragment extends Fragment implements OnHistoryChangedListener {

    private ListView listView;
    private HistoryAdapter historyAdapter;

    private DatabaseAdapter dbAdapter;

    private long user_id;

    private UserViewModel userViewModel;
    private Cursor cursor; // for the adapter

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = new DatabaseAdapter(getActivity());

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUser_id().observe(this, aLong -> {
            user_id = aLong;

            cursor = dbAdapter.getHistoryByUser(user_id);
            historyAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        listView = (ListView) view.findViewById(R.id.list_view);
        historyAdapter = new HistoryAdapter(getActivity(), dbAdapter, user_id);
        listView.setAdapter(historyAdapter);

        return view;
    }

    public HistoryAdapter getHistoryAdapter() {
        return historyAdapter;
    }

    @Override
    public void onHistoryChanged() {
        cursor = dbAdapter.getHistoryByUser(user_id);
        historyAdapter.setCursor(cursor);
        historyAdapter.notifyDataSetChanged();
    }
}
