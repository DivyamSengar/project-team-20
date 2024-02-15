package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;
import edu.ucsd.cse110.successorator.ui.dialog.CreateGoalDialogFragment;

public class MainFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentMainBinding view;
    private MainFragmentAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        this.view = FragmentMainBinding.inflate(inflater, container, false);

        view.listGoals.setAdapter(adapter);

        // Show DialogFragment
        view.imageButton.setOnClickListener(v -> {
            System.out.println("clicked");
            var dialogFragment = CreateGoalDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateGoalDialogFragment");
        });

        // Inflate the layout for this fragment
        return view.getRoot();
    }

    private void setupMvp(){
        // Observe Model -> call View

        // Observe View -> call Model

    }

    private void showDate(){

    }



//    /*Code got from:
//https://www.geeksforgeeks.org/how-to-get-current-time-and-date-in-android/
//How to Get Current Time and Date in Android?
//Captured at 2/09/2024
//Used for copying code to capture the date, and changed the format to fit our format
//Copied code start-
//SimpleDateFormat sdf = new SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z");
//String currentDateAndTime = sdf.format(new Date());
//-end
//*/
//    SimpleDateFormat date = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
//    String currentDate = date.format(new Date());
//        view.dateText.setText(currentDate);
//
//    //To test the empty goal text
//        view.emptyGoals.setText(R.string.emptyGoalsText);
//
//    var addButton = view.imageButton;
//    // This triggers the popup for keyboard
//        addButton.setOnClickListener(v -> {
//        // Functionality for keyboard and input popup
//    });
            /*
        Temporary object goalList with instance variable List<Goals>
        if (goalList.size() == 0){
            view.emptyGoals.setVisibility(View.VISIBLE);
            view.listGoals.setVisibility(View.INVISIBLE);
        } else {
            view.emptyGoals.setVisibility(View.INVISIBLE);
            view.listGoals.setVisibility(View.VISIBLE);

            // code for displaying goals in ListView
            // use ArrayAdapter

        }
         */

    //current placeholder idea for showing the goal list and empty goal list situation
        /* subject to changes
        ArrayList<String> glist;
        glist = new ArrayList<String>();
        glist.add("Goal 1");
        glist.add("Goal 2");
        if (glist.size() == 0){
            view.emptyGoals.setText(R.string.emptyGoalsText);
        }
        else{
            view.listGoals.setText(glist);
        }
        */
}