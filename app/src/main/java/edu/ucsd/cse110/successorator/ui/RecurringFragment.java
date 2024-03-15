package edu.ucsd.cse110.successorator.ui;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentPendingBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentRecurringBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.ui.dialog.CreateGoalDialogFragment;
import edu.ucsd.cse110.successorator.ui.dialog.CreateRecurringDialogFragment;
import edu.ucsd.cse110.successorator.ui.dialog.FocusModeDialogFragment;
public class RecurringFragment extends Fragment implements FocusModeListener {
    private FragmentRecurringBinding view;
    private RecurringFragmentAdapter adapter;
    private MainViewModel activityModel;
    private int context = 0;
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
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner,modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
        // Initialize the adapter
        this.adapter = new RecurringFragmentAdapter(requireContext(), List.of());
        activityModel.getContext(activityModel.getGoalsFromRecurringList(),
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
        this.view = FragmentRecurringBinding.inflate(inflater, container, false);
        view.listGoals.setAdapter(adapter);
        createSpinner();
        showTopBar();
        activityModel.rollover();
        addPlusButtonListener();
        addFocusModeListener();
        addGoalListeners();
        // Inflate the layout for this fragment
        return view.getRoot();
    }
    public void showTopBar(){
        view.topText.setText(R.string.recurring);
    }
    @Override
    public void onResume(){
        super.onResume();
        activityModel.rollover();
        updateGoals();
    }
    public void addPlusButtonListener(){
        // Show DialogFragment when button is clicked
        view.imageButton.setOnClickListener(v -> {
            var dialogFragment = CreateRecurringDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateGoalDialogFragment");
        });
    }
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
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
        popupMenu.inflate(R.menu.recurring_goal_context_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Delete")) {
                deleteGoal(goal);
            }
            return true;
        });
        popupMenu.show();
    }
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
    private void deleteGoal(Goal goal) {
        activityModel.removeGoalFromRecurringList(goal.id());
        adapter.notifyDataSetChanged();
    }
    public void addFocusModeListener(){
        view.hamburgerMenu.setOnClickListener(v -> {
            var dialogFragment = FocusModeDialogFragment.newInstance(this);
            dialogFragment.show(getParentFragmentManager(), "FocusModeDialogFragment");
            this.context = dialogFragment.getFocusContext();
        });
    }
    @Override
    public void onFocusModeSelected(int context) {
        this.context = context;
        updateGoals();
    }
    public void updateGoals() {
//        LocalDateTime current = activityModel.getTodayTime();
//        Instant instant = current.atZone(ZoneId.systemDefault()).toInstant();
//        Calendar today = Calendar.getInstance();
//        today.setTimeInMillis(instant.toEpochMilli());
////        while (!activityModel.getCurrUpdateValue()){}
//        System.out.println("curr context in main" + activityModel.getCurrentContextValue()) ;
        activityModel.getContext(activityModel.getGoalsFromRecurringList(),
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
        dropdownAdapter.add("Recurring");
        dropdownAdapter.add("Today");
        dropdownAdapter.add("Tomorrow");
        dropdownAdapter.add("Pending");
        view.dropdown.setAdapter(dropdownAdapter);
        view.dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                } else if (position == 1) {
                    dropdownAdapter.clear();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, MainFragment.newInstance())
                            .commit();
                } else if (position == 2) {
                    dropdownAdapter.clear();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, TomorrowFragment.newInstance())
                            .commit();
                } else if (position == 3) {
                    dropdownAdapter.clear();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, PendingFragment.newInstance())
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