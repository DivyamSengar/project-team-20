package edu.ucsd.cse110.successorator.ui;

import android.graphics.Paint;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainActivity;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.ui.dialog.CreateGoalDialogFragment;
import edu.ucsd.cse110.successorator.data.db.RoomGoalRepository;

public class MainFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentMainBinding view;
    private MainFragmentAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        System.out.println("hello from newInstance");
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("hello from onCreate");

        // Initialize the model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner,modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);



        this.adapter = new MainFragmentAdapter(requireContext(), List.of());
        activityModel.getGoals().observe(goal -> {
            if (goal == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(goal));
            adapter.notifyDataSetChanged();
        });

        System.out.print(adapter);
    }

    ImageButton imageButton2;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        this.view = FragmentMainBinding.inflate(inflater, container, false);

        view.listGoals.setAdapter(adapter);

        SimpleDateFormat date = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
        String currentDate = date.format(new Date());
        view.dateText.setText(currentDate);

        imageButton2 = view.imageButton2;
        imageButton2.setOnClickListener(new View.OnClickListener(){
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v){
                c.add(Calendar.DATE, 1);
                String currentDate = date.format(c.getTime());
                view.dateText.setText(currentDate);
                activityModel.deleteCompleted();
            }

        });


        LocalDateTime currentTime = LocalDateTime.now();
        int lastOpenedHour = activityModel.getFields()[3];
        int lastOpenedMinute = activityModel.getFields()[4];
        int lastDay = activityModel.getFields()[2];
        int lastMonth =activityModel.getFields()[1];
        int lastYear = activityModel.getFields()[0];

        LocalDateTime previous = LocalDateTime.of(lastYear, lastMonth,
                lastDay, lastOpenedHour, lastOpenedMinute);

        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int currDay = currentTime.getDayOfMonth();
        int currMonth = currentTime.getMonthValue();
        int currYear = currentTime.getYear();

        var minus24 = currentTime.minusHours(24);
        if(minus24.isAfter(previous)){
            activityModel.deleteCompleted();
        }
        else if (minus24.isEqual(previous)){
            activityModel.deleteCompleted();
        }
        else if (currentTime.isBefore(previous));
        else if (currDay > lastDay) {
            if ((lastDay + 1) < currDay) {
                activityModel.deleteCompleted();
            } else {
                if (hour >= 2) {
                    activityModel.deleteCompleted();
                }
            }
        }
        else {
            if ((hour >= 2)
                && (lastOpenedHour <= 2)) {
                activityModel.deleteCompleted();
            }
        }
        activityModel.deleteTime();
        activityModel.appendTime(currentTime);


        // Show DialogFragment
        view.imageButton.setOnClickListener(v -> {
            System.out.println("clicked");
            var dialogFragment = CreateGoalDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateGoalDialogFragment");
        });

        activityModel.isGoalsEmpty().observe(isGoalsEmpty -> {
            if (Boolean.TRUE.equals(isGoalsEmpty)) {
                view.emptyGoals.setText(R.string.emptyGoalsText);
                view.emptyGoals.setVisibility(View.VISIBLE);
                view.listGoals.setVisibility(View.INVISIBLE);
            } else {
                view.emptyGoals.setVisibility(View.INVISIBLE);
                view.listGoals.setVisibility(View.VISIBLE);
            }

        });

        view.listGoals.setOnItemClickListener((parent, view, position, id) -> {
            Goal goal = adapter.getItem(position);
            assert goal != null;
            if (!goal.isComplete()){
                goal.makeComplete();
                activityModel.removeGoalIncomplete(goal.id());
                activityModel.appendComplete(goal);
            }
            else{
                goal.makeInComplete();
                activityModel.removeGoalComplete(goal.id());
                activityModel.prependIncomplete(goal);
            }
        });

        // Inflate the layout for this fragment
        return view.getRoot();
    }

}