package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.ui.dialog.CreateGoalDialogFragment;

/**
 * MainFragment is the main fragment for the application
 */
public class MainFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentMainBinding view;
    private MainFragmentAdapter adapter;

    private TomorrowFragmentAdapter adapter2;

    private PendingFragmentAdapter adapter3;

    private RecurringFragmentAdapter adapter4;
    /**
     * Required empty public constructor
     */
    public MainFragment() {
    }

    /**
     * Creates a new instance of MainFragment
     *
     * @return Fragment - returns a new fragment instance for MainFragment
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Method that runs when MainFragment is created
     *
     * @param savedInstanceState - state of the application
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner,modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the adapter
        this.adapter = new MainFragmentAdapter(requireContext(), List.of());
        this.adapter2 = new TomorrowFragmentAdapter(requireContext(), List.of());
        this.adapter3 = new PendingFragmentAdapter(requireContext(), List.of());
        this.adapter4 = new RecurringFragmentAdapter(requireContext(), List.of());

        // Observe goals, adapter fills the ListView
        activityModel.getGoals().observe(goal -> {
            if (goal == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(goal));
            adapter.notifyDataSetChanged();
        });

    }

    /**
     * Method that runs when the View is created
     *
     * @param inflater - instantiates XML into View objects
     * @param container - container that holds View objects
     * @param savedInstanceState - state of the application
     * @return The root of the view layout
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        this.view = FragmentMainBinding.inflate(inflater, container, false);

        view.listGoals.setAdapter(adapter);

        // Show the current date at the top
        SimpleDateFormat date = new SimpleDateFormat("E MM/dd", Locale.getDefault());
        String currentDate = date.format(new Date());



        /*
        https://developer.android.com/develop/ui/views/components/spinner#java
        Source Title: Add spinners to your app
        Date Captured: 3/5/2024 12:33 am
        Used as a reference to have a drop down menu to switch between views via spinner
        Handle: smhitle
         */
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item);
        dropdownAdapter.setDropDownViewResource(R.layout.dropdown_item);

        Calendar t = Calendar.getInstance();
        t.add(Calendar.DATE, 1);
        String tomorrow = date.format(t.getTime());

        dropdownAdapter.add("Today, " + currentDate);
        dropdownAdapter.add("Tomorrow, " + tomorrow);
        dropdownAdapter.add("Pending");
        dropdownAdapter.add("Recurring");

        view.dropdown.setAdapter(dropdownAdapter);

        // Button for developer testing, changes the date by a day
        view.imageButton2.setOnClickListener(new View.OnClickListener(){
            Calendar c = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            @Override
            public void onClick(View v){
                dropdownAdapter.clear();
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
                if (c.equals(c2)){
                    c2.add(Calendar.DATE, 1);
                }
                String currentDate = date.format(c.getTime());
                String nextDate = date.format(c2.getTime());
                dropdownAdapter.insert("Today "+currentDate,0);
                dropdownAdapter.insert("Tomorrow "+nextDate,1);
                dropdownAdapter.add("Pending");
                dropdownAdapter.add("Recurring");
                activityModel.deleteCompleted();
            }

        });

        view.dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                    if (id == dropdownAdapter.getItemId(0)) {
                        view.listGoals.setAdapter(adapter);
                        view.getRoot();
                    }
                    else if (id == dropdownAdapter.getItemId(1)) {
                        view.listGoals.setAdapter(adapter2);
                        view.getRoot();
                    }
                    else if (id == dropdownAdapter.getItemId(2)) {
                        view.listGoals.setAdapter(adapter3);
                        view.getRoot();
                    }
                    else if (id == dropdownAdapter.getItemId(3)) {
                        view.listGoals.setAdapter(adapter4);
                        view.getRoot();
                    }
                    else{}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });




        // Show DialogFragment when button is clicked
        view.imageButton.setOnClickListener(v -> {
            var dialogFragment = CreateGoalDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateGoalDialogFragment");
        });

        // Observer to check whether or not goals is empty to display the ListView or TextView
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

        // Listener for taps/clicks on each list item
        view.listGoals.setOnItemClickListener((parent, view, position, id) -> {
            Goal goal = adapter.getItem(position);
            assert goal != null;
            // If the tapped goal is incomplete, make it complete
            if (!goal.isComplete()){
                goal.makeComplete();
                activityModel.removeGoalIncomplete(goal.id());
                activityModel.appendComplete(goal);
            }
            // If goal is complete make incomplete
            else{
                goal.makeInComplete();
                activityModel.removeGoalComplete(goal.id());
                activityModel.prependIncomplete(goal);
            }
        });

        // Inflate the layout for this fragment
        return view.getRoot();
    }
    @Override
    public void onResume(){
        super.onResume();

        // Show the current date at the top
        SimpleDateFormat date = new SimpleDateFormat("E MM/dd", Locale.getDefault());
        String currentDate = date.format(new Date());



        /*
        https://developer.android.com/develop/ui/views/components/spinner#java
        Source Title: Add spinners to your app
        Date Captured: 3/5/2024 12:33 am
        Used as a reference to have a drop down menu to switch between views via spinner
        Handle: smhitle
         */
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item);
        dropdownAdapter.setDropDownViewResource(R.layout.dropdown_item);

        Calendar t = Calendar.getInstance();
        t.add(Calendar.DATE, 1);
        String tomorrow = date.format(t.getTime());

        dropdownAdapter.add("Today, " + currentDate);
        dropdownAdapter.add("Tomorrow, " + tomorrow);
        dropdownAdapter.add("Pending");
        dropdownAdapter.add("Recurring");

        view.dropdown.setAdapter(dropdownAdapter);
    }


}




/*

        // Current time and time that the app was last opened
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

        // If current time is at least 24 hours ahead, perform completed goals deletion
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

 */
