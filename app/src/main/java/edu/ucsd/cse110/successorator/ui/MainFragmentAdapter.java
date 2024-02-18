package edu.ucsd.cse110.successorator.ui;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.databinding.GoalListItemBinding;

public class MainFragmentAdapter extends ArrayAdapter<Goal> {
//    Consumer<Integer> onMarkIncompleteClick;
//    , Consumer<Integer> onMarkIncompleteClick
    private List<Goal> completedGoals;

    private List<Goal> incompletedGoals;
    public MainFragmentAdapter(Context context, List<Goal> completedGoals, List<Goal> incompletedGoals) {
        super(context, 0, new ArrayList<>( Stream.concat(incompletedGoals.stream(),
                completedGoals.stream()).collect(Collectors.toList())));
        this.completedGoals = completedGoals;
        this.incompletedGoals = incompletedGoals;
//        this.onMarkIncompleteClick = onMarkIncompleteClick;
    }
    @Override
    public void add(@Nullable Goal object) {
        super.add(object);
    }

    public void addComplete(Goal object){
        completedGoals.add(object);
        super.clear();
        super.addAll(incompletedGoals);
        super.addAll(completedGoals);
    }

    public void prependIncomplete(Goal object){
        incompletedGoals.add(0, object);
        super.clear();
        super.addAll(incompletedGoals);
        super.addAll(completedGoals);
    }
    public void removeComplete(Goal goal){
        completedGoals.remove(goal);
        super.clear();
        super.addAll(incompletedGoals);
        super.addAll(completedGoals);
    }

    public void removeIncomplete(Goal goal){
        incompletedGoals.remove(goal);
        super.clear();
        super.addAll(incompletedGoals);
        super.addAll(completedGoals);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        var goal = getItem(position);
        assert goal != null;

        GoalListItemBinding binding;
        if (convertView != null) {
            binding = GoalListItemBinding.bind(convertView);
        }
        else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = GoalListItemBinding.inflate(layoutInflater, parent, false);
        }
        binding.goalText.setText(goal.getText());
        if (goal.isComplete()) {
            binding.goalText.setPaintFlags(binding.goalText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            binding.goalText.setPaintFlags(0);
        }


        return binding.getRoot();
    }
}
