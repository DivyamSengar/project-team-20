package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
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
import edu.ucsd.cse110.successorator.ui.dialog.FocusModeDialogFragment;

/**
 * MainFragment is the main fragment for the application
 */
public class MainFragment extends Fragment implements FocusModeListener {
    private MainViewModel activityModel;
    private FragmentMainBinding view;
    private MainFragmentAdapter adapter;



    public int getThisContext() {
        return context;
    }

    private int context = 0;

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


        // Observe goals, adapter fills the ListView

        /* UDPATE! Need to fix this! We want each fragment to have its own view, so for example,
        today view should call getGoalsLessThanOrEqualToDay() with today's date as argument,
        tomorrow view should call getGoalsbyDay on tomorrow's date, and recurring and pending should call
        get recurring/pending goals respectively. Remember, we want the different views to have the correct goals displayed
        */
        LocalDateTime current = activityModel.getTodayTime();
        Instant instant = current.atZone(ZoneId.systemDefault()).toInstant();
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(instant.toEpochMilli());
        System.out.println("curr context in main" + activityModel.getCurrentContextValue());
        activityModel.removeContext();
        activityModel.setContext(0);
        activityModel.getContext(activityModel.getGoalsLessThanOrEqualToDay(today.get(Calendar.YEAR),
                        (today.get(Calendar.MONTH)+1), today.get(Calendar.DAY_OF_MONTH)),
                        0)
                .observe(goal -> {
                    if (goal == null) return;
                    System.out.println("My size is " + goal.size());
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

        // Set the list's adapter
        view.listGoals.setAdapter(adapter);

        showTopBar();
        activityModel.removeContext();
        activityModel.setContext(0);
        activityModel.rollover();

        checkGoalsIsEmpty();
        addPlusButtonListener();
        addFocusModeListener();
        addGoalListeners();
        createSpinner();
        createDeveloperButton();
        onFocusModeSelected(0);

        // Inflate the layout for this fragment
        return view.getRoot();
    }

    @Override
    public void onResume(){
        System.out.println("resumed??");
        super.onResume();
        // Show the current date at the top
        LocalDateTime todayTime = activityModel.getTodayTime();
        System.out.println("so now what is the time?" + todayTime);
        activityModel.updateTodayTime(todayTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd");
        String formattedDate = formatter.format(todayTime);
//        LocalDateTime update = LocalDateTime.from((TemporalAccessor) date);
        String currentDate = "Today, " + formattedDate;

        view.topText.setText(currentDate);
        activityModel.rollover();
    }

    public void showTopBar(){
        // Show the current date at the top
        LocalDateTime todayTime = activityModel.getTodayTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd");
        String formattedDate = formatter.format(todayTime);

        String currentDate = "Today, " + formattedDate;

        view.topText.setText(currentDate);
    }

    public void addPlusButtonListener(){
        // Show DialogFragment when button is clicked
        view.imageButton.setOnClickListener(v -> {
            var dialogFragment = CreateGoalDialogFragment.newInstance("today");
            dialogFragment.show(getParentFragmentManager(), "CreateGoalDialogFragment");
        });
    }

    public void addFocusModeListener(){
        view.hamburgerMenu.setOnClickListener(v -> {
            var dialogFragment = FocusModeDialogFragment.newInstance(this);
            System.out.println(dialogFragment.getFocusContext() + "printed here");
            dialogFragment.show(getParentFragmentManager(), "FocusModeDialogFragment");
        });

    }

    @Override
    public void onFocusModeSelected(int context) {
        this.context = context;
        updateGoals();
    }

    public void updateGoals() {
        LocalDateTime current = activityModel.getTodayTime();
        Instant instant = current.atZone(ZoneId.systemDefault()).toInstant();
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(instant.toEpochMilli());
//        while (!activityModel.getCurrUpdateValue()){}
        System.out.println("curr context in main" + activityModel.getCurrentContextValue()) ;
        activityModel.getContext(activityModel.getGoalsLessThanOrEqualToDay(today.get(Calendar.YEAR),
                                (today.get(Calendar.MONTH)+1), today.get(Calendar.DAY_OF_MONTH)),
                        activityModel.getCurrentContextValue())
                .observe(goal -> {
                    if (goal == null) {
                        System.out.println("way too early?");
                        return;
                    }
                    System.out.println("My size is " + goal.size());
                    adapter.clear();
                    adapter.addAll(new ArrayList<>(goal));
                    adapter.notifyDataSetChanged();
                });
    }


//        view.hamburgerMenu.onScreenStateChanged(v -> {
//            LocalDateTime current = activityModel.getTodayTime();
//            Instant instant = current.atZone(ZoneId.systemDefault()).toInstant();
//            Calendar today = Calendar.getInstance();
//            today.setTimeInMillis(instant.toEpochMilli());
//            System.out.println("curr context in main" + activityModel.getCurrentContextValue()) ;
//            activityModel.getContext(activityModel.getGoalsLessThanOrEqualToDay(today.get(Calendar.YEAR),
//                                    (today.get(Calendar.MONTH)+1), today.get(Calendar.DAY_OF_MONTH)),
//                            activityModel.getCurrentContextValue())
//                    .observe(goal -> {
//                        if (goal == null) {
//                            System.out.println("way too early?");
//                            return;
//                        }
//                        System.out.println("My size is " + goal.size());
//                        adapter.clear();
//                        adapter.addAll(new ArrayList<>(goal));
//                        adapter.notifyDataSetChanged();
//                    });
//            view.hamburgerMenu.onWindowFocusChanged();
//        });




    public void checkGoalsIsEmpty(){
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
    }

    public void addGoalListeners (){
        // Listener for taps/clicks on each list item
        view.listGoals.setOnItemClickListener((parent, view, position, id) -> {
            Goal goal = adapter.getItem(position);
            assert goal != null;
            // If the tapped goal is incomplete, make it complete
            if (!goal.isComplete()){
                goal.makeComplete();
                activityModel.removeGoalIncomplete(goal.id());
                activityModel.appendComplete(goal);
                updateGoals();
            }
            // If goal is complete make incomplete
            else{
                goal.makeInComplete();
                activityModel.removeGoalComplete(goal.id());
                activityModel.prependIncomplete(goal);
                updateGoals();
            }
        });
    }

    public void addGoalIncomplete(Goal goal) {
        activityModel.appendIncomplete(goal);
        adapter.addAll(goal);
        adapter.notifyDataSetChanged();
    }

    public void addGoalComplete(Goal goal) {
        activityModel.appendComplete(goal);
        adapter.addAll(goal);
        adapter.notifyDataSetChanged();
    }

    public void createSpinner(){
        /*
        https://developer.android.com/develop/ui/views/components/spinner#java
        Source Title: Add spinners to your app
        Date Captured: 3/5/2024 12:33 am
        Used as a reference to have a drop down menu to switch between views via spinner
        Handle: smhitle
         */
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        dropdownAdapter.add("");
        dropdownAdapter.add("Today");
        dropdownAdapter.add("Tomorrow");
        dropdownAdapter.add("Pending");
        dropdownAdapter.add("Recurring");

        view.dropdown.setAdapter(dropdownAdapter);

        view.dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                }
                else if (position == 1) {
                    dropdownAdapter.clear();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, TomorrowFragment.newInstance())
                            .commit();
                }
                else if (position == 2) {
                    dropdownAdapter.clear();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, PendingFragment.newInstance())
                            .commit();
                }
                else if (position == 3) {
                    dropdownAdapter.clear();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, RecurringFragment.newInstance())
                            .commit();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    public void createDeveloperButton(){
        // Show the current date at the top
//        SimpleDateFormat date = new SimpleDateFormat("E MM/dd", Locale.getDefault());
        // Button for developer testing, changes the date by a day
        view.imageButton2.setOnClickListener(new View.OnClickListener(){
            // Create Calendar objects from Instant
//            Calendar c = Calendar.getInstance();
//            Calendar c2 = Calendar.getInstance();
//            LocalDateTime todayTime = activityModel.getTodayTime();

            @Override
            public void onClick(View v){
//                LocalDateTime todayTime = activityModel.getTodayTime();
//                Instant instant = todayTime.atZone(ZoneId.systemDefault()).toInstant();
//                c.setTimeInMillis(instant.toEpochMilli());
//                c2.setTimeInMillis(instant.toEpochMilli());
//                String formattedDate = formatter.format(c);

//                c.add(Calendar.DATE, 1);
//                c2.add(Calendar.DATE, 1);
//                if (c.equals(c2)){
//                    c2.add(Calendar.DATE, 1);
//                }
                activityModel.updateTodayTime(activityModel.getTodayTime().plusDays(1));
                LocalDateTime current = activityModel.getTodayTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd");
                String formattedDate = formatter.format(current);



                // CALL ROLLOVER IN ALL ONRESUME METHODS AND IN ALL OTHER APPLICABLE METHODS (ON CREATE?)
                String currentDate =  "Today, " + formattedDate;
//                String nextDate = date.format(c2.getTime());

                view.topText.setText(currentDate);

                activityModel.rollover();
            }
        });
    }
}

