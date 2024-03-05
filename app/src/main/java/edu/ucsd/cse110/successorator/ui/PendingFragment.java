package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import edu.ucsd.cse110.successorator.databinding.FragmentPendingBinding;

public class PendingFragment extends Fragment {
    private FragmentPendingBinding view;
    private PendingFragmentAdapter adapter;

    public PendingFragment(){
    }

    public static PendingFragment newInstance() {
        Bundle args = new Bundle();
        PendingFragment fragment = new PendingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the adapter
        this.adapter = new PendingFragmentAdapter(requireContext(), List.of());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        this.view = FragmentPendingBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return view.getRoot();
    }
}
