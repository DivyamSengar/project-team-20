package edu.ucsd.cse110.successorator.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.GoalListItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class TomorrowFragmentAdapter extends ArrayAdapter<Goal> {
    public TomorrowFragmentAdapter(Context context, List<Goal> goals){
        super(context, 0, new ArrayList<>(goals));
    }

    @Override
    public void add(@Nullable Goal object) {super.add(object);}

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

        if (goal.getContext() == 1){
            binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.home_context_icon));
            binding.goalContextIcon.setText("H");
        } else if (goal.getContext() == 2){
            binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.work_context_icon));
            binding.goalContextIcon.setText("W");
        } else if (goal.getContext() == 3){
            binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.school_context_icon));
            binding.goalContextIcon.setText("S");
        } else if (goal.getContext() == 4){
            binding.goalContextIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.errand_context_icon));
            binding.goalContextIcon.setText("E");
        }

        return binding.getRoot();
    }
}
