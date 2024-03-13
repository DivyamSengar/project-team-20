package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentPendingBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentRecurringBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.ui.dialog.CreateGoalDialogFragment;

public class RecurringFragment extends Fragment {
    private FragmentRecurringBinding view;
    private RecurringFragmentAdapter adapter;

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

        // Initialize the adapter
        this.adapter = new RecurringFragmentAdapter(requireContext(), List.of());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        this.view = FragmentRecurringBinding.inflate(inflater, container, false);

        createSpinner();
        showTopBar();
        addPlusButtonListener();
        addGoalListeners();

        // Inflate the layout for this fragment
        return view.getRoot();
    }

    public void showTopBar(){
        view.topText.setText(R.string.recurring);
    }

    public void addPlusButtonListener(){
        // Show DialogFragment when button is clicked
        view.imageButton.setOnClickListener(v -> {
            var dialogFragment = CreateGoalDialogFragment.newInstance("recurring");
            dialogFragment.show(getParentFragmentManager(), "CreateGoalDialogFragment");
        });
    }

    // TODO: Modify this in long press
    public void addGoalListeners () {
        // Listener for taps/clicks on each list item
        view.listGoals.setOnItemClickListener((parent, view, position, id) -> {
            Goal goal = adapter.getItem(position);
            assert goal != null;
            // If the tapped goal is incomplete, make it complete
            if (!goal.isComplete()) {
                goal.makeComplete();
            }
            // If goal is complete make incomplete
            else {
                goal.makeInComplete();
            }
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