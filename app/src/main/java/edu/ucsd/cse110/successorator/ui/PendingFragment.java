package edu.ucsd.cse110.successorator.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentPendingBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.ui.dialog.CreateGoalDialogFragment;
import edu.ucsd.cse110.successorator.ui.dialog.CreatePendingDialogFragment;
import edu.ucsd.cse110.successorator.ui.dialog.FocusModeDialogFragment;

public class PendingFragment extends Fragment implements FocusModeListener{
    private FragmentPendingBinding view;
    private PendingFragmentAdapter adapter;
    private MainViewModel activityModel;

    private int context = 0;


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

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner,modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the adapter
        this.adapter = new PendingFragmentAdapter(requireContext(), List.of());

        activityModel.getContext(activityModel.getPendingGoals(),
                activityModel.getCurrentContextValue()).observe(goal -> {
            if (goal == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(goal));
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        this.view = FragmentPendingBinding.inflate(inflater, container, false);

        view.listGoals.setAdapter(adapter);

        createSpinner();
        showTopBar();
        int[] prevTime = activityModel.getFieldsForLastDate();
        activityModel.rollover(activityModel.getTodayTime(), LocalDateTime.of(prevTime[0],
                prevTime[1], prevTime[2], prevTime[3], prevTime[4]));
        addPlusButtonListener();
        addFocusModeListener();
        addGoalListeners();

        // Inflate the layout for this fragment
        return view.getRoot();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Move to today");
        menu.add("Move to tomorrow");
        menu.add("Finish");
        menu.add("Delete");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Goal goal = adapter.getItem(info.position);

        return true;
    }

    private void showPopupMenu(View view, Goal goal) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.inflate(R.menu.pending_goal_context_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Move to today")) {
                moveToToday(goal);
            } else if (item.getTitle().equals("Move to tomorrow")) {
                moveToTomorrow(goal);
            } else if (item.getTitle().equals("Finish")) {
                finishGoal(goal);
            } else if (item.getTitle().equals("Delete")) {
                deleteGoal(goal);
            }

            return true;
        });
        popupMenu.show();
    }


    // TODO: Modify this in long press
    public void addGoalListeners() {
        view.listGoals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Goal goal = adapter.getItem(position);
                showPopupMenu(view, goal);
                return true;
            }
        });
    }
    @Override
    public void onFocusModeSelected(int context) {
        this.context = context;
        updateGoals();
        if (context == 0) {
            view.hamburgerMenu.setBackgroundColor(Color.parseColor("#D6D7D7"));
        } else if (context == 1){
            view.hamburgerMenu.setBackgroundColor(Color.parseColor("#FFFBB9"));
        }
        else if (context == 2){
            view.hamburgerMenu.setBackgroundColor(Color.parseColor("#92E3FD"));
        }
        else if (context == 3){
            view.hamburgerMenu.setBackgroundColor(Color.parseColor("#EFCAFF"));
        }
        else if (context == 4){
            view.hamburgerMenu.setBackgroundColor(Color.parseColor("#DFEED4"));
        }
    }
    public void updateGoals() {
        LocalDateTime current = activityModel.getTodayTime();
        Instant instant = current.atZone(ZoneId.systemDefault()).toInstant();
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(instant.toEpochMilli());
//        while (!activityModel.getCurrUpdateValue()){}
        System.out.println("curr context in main" + activityModel.getCurrentContextValue()) ;
        activityModel.getContext(activityModel.getPendingGoals(),
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
    public void removeGoalComplete(Goal goal) {
        activityModel.removeGoalIncomplete(goal.id());
        adapter.remove(goal);
        activityModel.appendIncomplete(goal);
        adapter.notifyDataSetChanged();
    }

    public void removeGoalIncomplete(Goal goal) {
        activityModel.removeGoalIncomplete(goal.id());
        adapter.remove(goal);
        activityModel.appendComplete(goal);
        adapter.notifyDataSetChanged();
    }

    private void moveToToday(Goal goal) {
        // Update the goal's date
        LocalDateTime today = activityModel.getTodayTime();
        goal.setDate(today.getMinute(),
                today.getHour(),
                today.getDayOfMonth(),
                today.getMonthValue(),
                today.getYear());

        // Remove the goal from the pending goals

        goal.changePending();
        removeGoalComplete(goal);

        // Get the MainFragment and add the goal
        MainFragment mainFragment = (MainFragment) getParentFragmentManager().findFragmentByTag("android:switcher:" + R.id.fragment_container + ":" + 1);
        if (mainFragment != null) {
            mainFragment.addGoalIncomplete(goal);
        }
    }

    private void moveToTomorrow(Goal goal) {
        LocalDateTime tomorrow = activityModel.getTodayTime().plusDays(1);
        goal.setDate(tomorrow.getMinute(),
                tomorrow.getHour(),
                tomorrow.getDayOfMonth(),
                tomorrow.getMonthValue(),
                tomorrow.getYear());

        goal.changePending();
        removeGoalComplete(goal);

        TomorrowFragment tomorrowFragment = (TomorrowFragment) getParentFragmentManager().findFragmentByTag("android:switcher:" + R.id.fragment_container + ":" + 1);
        if (tomorrowFragment != null){
            tomorrowFragment.addGoalIncomplete(goal);
        }
    }

    private void finishGoal(Goal goal) {
        LocalDateTime today = activityModel.getTodayTime();
        goal.setDate(today.getMinute(),
                today.getHour(),
                today.getDayOfMonth(),
                today.getMonthValue(),
                today.getYear());

        goal.changePending();
        goal.makeComplete();
        removeGoalIncomplete(goal);

        // Get the MainFragment and add the goal
        MainFragment mainFragment = (MainFragment) getParentFragmentManager().findFragmentByTag("android:switcher:" + R.id.fragment_container + ":" + 1);
        if (mainFragment != null) {
            mainFragment.addGoalComplete(goal);
        }
    }

    private void deleteGoal(Goal goal) {
        activityModel.removeGoalIncomplete(goal.id());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
        int[] prevTime = activityModel.getFieldsForLastDate();
        activityModel.rollover(activityModel.getTodayTime(), LocalDateTime.of(prevTime[0],
                prevTime[1], prevTime[2], prevTime[3], prevTime[4]));
        updateGoals();
    }

    public void showTopBar(){
        view.topText.setText(R.string.pending);
    }

    public void addPlusButtonListener(){
        // Show DialogFragment when button is clicked
        view.imageButton.setOnClickListener(v -> {
            var dialogFragment = CreatePendingDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateGoalDialogFragment");
        });
    }


    public void addFocusModeListener(){
        view.hamburgerMenu.setOnClickListener(v -> {
            var dialogFragment = FocusModeDialogFragment.newInstance((FocusModeListener) this);
            dialogFragment.show(getParentFragmentManager(), "FocusModeDialogFragment");
            this.context = dialogFragment.getFocusContext();
        });
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

        //dropdownAdapter.add("");
        dropdownAdapter.add("Pending");
        dropdownAdapter.add("Today");
        dropdownAdapter.add("Tomorrow");

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
                            .replace(R.id.fragment_container, MainFragment.newInstance())
                            .commit();
                }
                else if (position == 2) {
                    dropdownAdapter.clear();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, TomorrowFragment.newInstance())
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
}
