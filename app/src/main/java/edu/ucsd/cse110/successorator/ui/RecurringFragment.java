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

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentPendingBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentRecurringBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.ui.dialog.CreateGoalDialogFragment;
import edu.ucsd.cse110.successorator.ui.dialog.CreateRecurringDialogFragment;

public class RecurringFragment extends Fragment {
    private FragmentRecurringBinding view;
    private RecurringFragmentAdapter adapter;
    private MainViewModel activityModel;

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

        activityModel.getRecurringGoals().observe(goal -> {
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
        addPlusButtonListener();
        addGoalListeners();

        // Inflate the layout for this fragment
        return view.getRoot();
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
        activityModel.removeGoalIncomplete(goal.id());
        adapter.notifyDataSetChanged();
    }

    public void showTopBar(){
        view.topText.setText(R.string.recurring);
    }

    public void addPlusButtonListener(){
        // Show DialogFragment when button is clicked
        view.imageButton.setOnClickListener(v -> {
            var dialogFragment = CreateRecurringDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateGoalDialogFragment");
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
