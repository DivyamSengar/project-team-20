package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;

public class TomorrowFragment extends Fragment {
    private FragmentMainBinding view;
    private TomorrowFragmentAdapter adapter;

    /**
     * Required empty public constructor
     */
    public TomorrowFragment(){
    }

    public static TomorrowFragment newInstance(){
        TomorrowFragment fragment = new TomorrowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the adapter
        this.adapter = new TomorrowFragmentAdapter(requireContext(), List.of());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        this.view = FragmentMainBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return view.getRoot();
    }
}
