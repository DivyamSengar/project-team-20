package edu.ucsd.cse110.successorator.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class MainFragmentAdapter extends ArrayAdapter<Goal> {
    public MainFragmentAdapter(Context context, List<Goal> goals) {
        super(context, 0, new ArrayList<>(goals));
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var goal = getItem(position);
        assert goal != null;

        FragmentMainBinding binding;
        if (convertView != null) {
            binding = FragmentMainBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = FragmentMainBinding.inflate(layoutInflater, parent, false);
        }

//        binding.
        //binding.____ ask in office hours
        return binding.getRoot();
    }

}
