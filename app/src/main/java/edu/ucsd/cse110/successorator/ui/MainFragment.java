package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {
    // private MainViewModel activityModel
    private FragmentMainBinding view;

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
        // var modelOwner = requireActivity()
        // var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer)
        // var modelProvider = new ViewModelProvider.get(MainViewModel.class)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = FragmentMainBinding.inflate(inflater, container, false);

        setupMvp();
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