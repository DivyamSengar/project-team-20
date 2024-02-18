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
import edu.ucsd.cse110.successorator.lib.data.DataSource;
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
//        activityModel.getIncompletedGoals().observe(goal -> {
//            System.out.println("This is" + goal);
//            if (goal == null) return;
//            adapter.clear();
//            adapter.addAll(new ArrayList<>(goal));
//            adapter.notifyDataSetChanged();
//        });
//        activityModel.getCompletedGoals().observe(goal -> {
//            if (goal == null) return;
//            adapter.clear();
//            adapter.addAll(new ArrayList<>(goal));
//            adapter.notifyDataSetChanged();
//        });
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


        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        if (hour == 2 && minute == 0) {
            activityModel.deleteCompleted();
        }


        // Show DialogFragment
        view.imageButton.setOnClickListener(v -> {
            System.out.println("clicked");
            var dialogFragment = CreateGoalDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateGoalDialogFragment");
        });

        activityModel.isGoalsEmpty().observe(isGoalsEmpty -> {
            if (Boolean.TRUE.equals(isGoalsEmpty)) {
//                activityModel.getGoals().observe(text -> view.emptyGoals.setText(R.string.emptyGoalsText));
                view.emptyGoals.setText(R.string.emptyGoalsText);
                view.emptyGoals.setVisibility(View.VISIBLE);
                view.listGoals.setVisibility(View.INVISIBLE);
            } else {
                view.emptyGoals.setVisibility(View.INVISIBLE);
                view.listGoals.setVisibility(View.VISIBLE);
                view.listGoals.setBackgroundColor(60);
            }

        });

        view.listGoals.setOnItemClickListener((parent, view, position, id) -> {
            Goal goal = adapter.getItem(position);
            assert goal != null;
            if (!goal.isComplete()){
                goal.makeComplete();
                activityModel.removeGoalIncomplete(goal.id());
                activityModel.appendComplete(goal);
//                adapter.removeComplete(goal);
//                adapter.remove(goal);
//                adapter.add(goal);
//                adapter.addComplete(goal);
//                adapter.notifyDataSetChanged();
            }
            else{
                goal.makeInComplete();
                activityModel.removeGoalComplete(goal.id());
                activityModel.prependIncomplete(goal);
//                adapter.removeComplete(goal);
//                adapter.remove(goal);
//                adapter.insert(goal, 0);
//                adapter.prependIncomplete(goal);
//                adapter.notifyDataSetChanged();
            }
        });

        // Inflate the layout for this fragment
        return view.getRoot();
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