package com.example.rumahmode.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rumahmode.R;
import com.example.rumahmode.adapters.DatabaseAdapter;
import com.example.rumahmode.adapters.OnHistoryChangedListener;
import com.example.rumahmode.adapters.ProductsAdapter;
import com.example.rumahmode.databinding.FragmentProductsBinding;

public class ProductsFragment extends Fragment {

    private FragmentProductsBinding binding;

    private GridView gridView;
    private ProductsAdapter productsAdapter;

    private DatabaseAdapter dbAdapter;

    private long user_id;

    private UserViewModel userViewModel;
    private TempViewModel tempViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = new DatabaseAdapter(getActivity());

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        tempViewModel = new ViewModelProvider(requireActivity()).get(TempViewModel.class);

        userViewModel.getUser_id().observe(this, aLong -> user_id = aLong);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        gridView = (GridView) view.findViewById(R.id.grid_view);
        OnHistoryChangedListener onHistoryChangedListener = () -> {};
        productsAdapter = new ProductsAdapter(getActivity(), dbAdapter, user_id, onHistoryChangedListener, tempViewModel);
        gridView.setAdapter(productsAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}