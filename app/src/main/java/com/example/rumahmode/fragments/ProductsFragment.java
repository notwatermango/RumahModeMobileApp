package com.example.rumahmode.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rumahmode.R;
import com.example.rumahmode.adapters.DatabaseAdapter;
import com.example.rumahmode.adapters.OnHistoryChangedListener;
import com.example.rumahmode.adapters.ProductsAdapter;
import com.example.rumahmode.databinding.FragmentProductsBinding;

public class ProductsFragment extends Fragment {

    private FragmentProductsBinding binding;

    // Grid view and adapter objects
    private GridView gridView;
    private ProductsAdapter productsAdapter;

    // Database adapter object
    private DatabaseAdapter dbAdapter;

    // User id
    private long user_id;

    // User view model object
    private UserViewModel userViewModel;
    private TempViewModel tempViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the database adapter
        dbAdapter = new DatabaseAdapter(getActivity());

        // Get the view model instance
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        tempViewModel = new ViewModelProvider(requireActivity()).get(TempViewModel.class);

        // Observe the user id live data
        userViewModel.getUser_id().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                // Update the user id
                user_id = aLong;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        // Initialize the grid view and adapter
        gridView = (GridView) view.findViewById(R.id.grid_view);
        OnHistoryChangedListener onHistoryChangedListener = () -> {};
        productsAdapter = new ProductsAdapter(getActivity(), dbAdapter, user_id, onHistoryChangedListener, tempViewModel);
        gridView.setAdapter(productsAdapter);

        // Return the view
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}