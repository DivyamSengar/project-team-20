package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentTomorrowBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.ui.dialog.CreateGoalDialogFragment;

public class TomorrowFragment extends Fragment {
    private FragmentTomorrowBinding view;
    private TomorrowFragmentAdapter adapter;

    /**
     * Required empty public constructor
     */
    public TomorrowFragment(){
    }

    public static TomorrowFragment newInstance(){
        TomorrowFragment fragment = new TomorrowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the adapter
        this.adapter = new TomorrowFragmentAdapter(requireContext(), List.of());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        this.view = FragmentTomorrowBinding.inflate(inflater, container, false);

        createSpinner();
        showTopBar();
        addPlusButtonListener();
        addGoalListeners();
        createDeveloperButton();

        // Inflate the layout for this fragment
        return view.getRoot();
    }

    @Override
    public void onResume(){
        super.onResume();
        // Show the current date at the top
        SimpleDateFormat date = new SimpleDateFormat("E MM/dd", Locale.getDefault());

        Calendar t = Calendar.getInstance();
        t.add(Calendar.DATE, 1);
        String tomorrow = "Tomorrow, " + date.format(t.getTime());

        view.topText.setText(tomorrow);
    }

    public void showTopBar(){
        // Show the current date at the top
        SimpleDateFormat date = new SimpleDateFormat("E MM/dd", Locale.getDefault());

        Calendar t = Calendar.getInstance();
        t.add(Calendar.DATE, 1);
        String tomorrow = "Tomorrow, " + date.format(t.getTime());

        view.topText.setText(tomorrow);
    }

    // DO NOT USE THIS WE DO NOT HAVE THE CORRECT DIALOG FRAGMENT SETUP
    public void addPlusButtonListener(){
        // Show DialogFragment when button is clicked
        view.imageButton.setOnClickListener(v -> {
            var dialogFragment = CreateGoalDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateGoalDialogFragment");
        });
    }

    // TODO: Modify this so that it actually adds to the correct list
    public void addGoalListeners() {
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

//        dropdownAdapter.add("");
        dropdownAdapter.add("Tomorrow");
        dropdownAdapter.add("Today");

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
                            .replace(R.id.fragment_container, MainFragment.newInstance())
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
        SimpleDateFormat date = new SimpleDateFormat("E MM/dd", Locale.getDefault());
        // Button for developer testing, changes the date by a day
        view.imageButton2.setOnClickListener(new View.OnClickListener(){
            Calendar c = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            @Override
            public void onClick(View v){
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
                if (c.equals(c2)){
                    c2.add(Calendar.DATE, 1);
                }
                String currentDate =  "Today, " + date.format(c.getTime());
                String nextDate = "Tomorrow, " +date.format(c2.getTime());

                view.topText.setText(nextDate);

            }
        });
    }

}
