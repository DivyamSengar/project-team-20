package edu.ucsd.cse110.successorator.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.databinding.GoalListItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class PendingFragmentAdapter extends ArrayAdapter<Goal> {
    public PendingFragmentAdapter(Context context, List<Goal> goals){
        super(context, 0, new ArrayList(goals));
    }

    @Override
    public void add(@Nullable Goal object) {super.add(object);}


    public int getPosition(Goal goal) {
        return (Integer) super.getPosition(goal);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        var goal = getItem(position);
        assert goal != null;

        // Binds the data to the ListView
        GoalListItemBinding binding;
        if (convertView != null) {
            binding = GoalListItemBinding.bind(convertView);
        }
        else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = GoalListItemBinding.inflate(layoutInflater, parent, false);
        }

        binding.goalText.setText(goal.getText());

        return binding.getRoot();
    }
}
