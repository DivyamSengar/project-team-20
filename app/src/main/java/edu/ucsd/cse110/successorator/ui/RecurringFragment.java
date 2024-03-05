package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import edu.ucsd.cse110.successorator.databinding.FragmentPendingBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentRecurringBinding;

public class RecurringFragment extends Fragment {
    private FragmentRecurringBinding view;
    private RecurringFragmentAdapter adapter;

    public RecurringFragment(){
    }

    public static RecurringFragment newInstance() {
        Bundle args = new Bundle();
        RecurringFragment fragment = new RecurringFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the adapter
        this.adapter = new RecurringFragmentAdapter(requireContext(), List.of());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        this.view = FragmentRecurringBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return view.getRoot();
    }
}
