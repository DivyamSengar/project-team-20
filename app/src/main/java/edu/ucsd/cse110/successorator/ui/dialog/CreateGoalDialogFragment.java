package edu.ucsd.cse110.successorator.ui.dialog;

import static java.lang.Integer.parseInt;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

/**
 * DialogFragment that captures the input of the user when inputting a goal
 *
 */
public class CreateGoalDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private FragmentDialogCreateGoalBinding view;
    private static String viewType;

    /**
     * Required empty public constructor
     */
    CreateGoalDialogFragment(){
    }

    /**
     * Creates a new instance of MainFragment
     *
     * @return Fragment - returns a new fragment instance for MainFragment
     */
    public static CreateGoalDialogFragment newInstance(String vType){
        viewType = vType;
        var fragment = new CreateGoalDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initializes the model when the DialogFragment is created
     *
     * @param savedInstanceState - state of the application
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    /**
     * Creates a dialog for capturing input from the user when entering a goal
     *
     * @param savedInstanceState - state of the application
     * @return The dialog fragment for capturing text input
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreateGoalBinding.inflate(getLayoutInflater());
//        /*
//        https://stackoverflow.com/questions/12937731/android-enter-key-listener
//        Source Title:
//        Date Captured: 2/17/2024 4:51 pm
//        Used as a reference to have the done/check button on the keyboard to "mark as done"
//        and capture the input to add as a goal. When typing .setOnEditorActionListener()
//        the code automatically generated, the rest was reused from previously written code
//        Handle: smhitle
//         */
//        view.goalEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
//                    // Get the input of the textEdit
//                    var input = view.goalEditText.getText().toString();
//                    // get whether it's pending/get the view
//                    // get whether it's recurring and if so which type
//                    // get current system time
//
//                    LocalDateTime currentTime = LocalDateTime.now();
//                    // Create a new Goal with the text and add it
//                    // need to get the current date, decide whether it's pending, and decide whether it's recurring
//                    boolean pending = false;
//                    String recurring = null;
//                    if(viewType.equals("tomorrow")){
//                        currentTime = currentTime.plusDays(1);
//                    } else if(viewType.equals("pending")){
//                        pending = true;
//                    } else if(viewType.equals("recurring")){
//                        //need to instantiate the recurring field based on the OnClick which will determine whether the goal is daily, weekly, monthly, yearly
//                    }
//
//                    //need to set onClick to change date array based on whether or not the calendar was chosen for a future date
//                    int[] date = {currentTime.getYear(), currentTime.getMonthValue(),
//                            currentTime.getDayOfMonth(), currentTime.getHour(), currentTime.getMinute()};
//
//                    var newGoal = new Goal(null, input, false, -1, pending, recurring,
//                            date[0], date[1], date[2], date[3], date[4]);
//                    activityModel.appendIncomplete(newGoal);
//                    dismiss();
//                }
//                return false;
//            }
//        });
        captureGoalInput();
        return makeDialog();
    }

    private Dialog makeDialog(){
        /*
        https://stackoverflow.com/questions/17237952/dialogfragment-and-force-to-show-keyboard
        Source Title: DialogFragment and force to show keyboard
        Date Captured: 2/17/2024 4:48 pm
        Used as a reference to get the keyboard to show up on screen and have the editText focus,
        built upon previous code written just in a different way
        Handle: smhitle
         */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder
                .setView(view.getRoot())
                .setNegativeButton("Cancel", this::onNegativeButtonClick);
        Dialog dialog = dialogBuilder.create();
        var edit = view.goalEditText;
        edit.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        SimpleDateFormat daysOfWeek = new SimpleDateFormat("E", Locale.getDefault());
        SimpleDateFormat dayofMonth = new SimpleDateFormat("F", Locale.getDefault());
        SimpleDateFormat dayofYear = new SimpleDateFormat("MM/dd", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        String[] postfix = {"st", "nd", "rd", "th","th","th"};
        String weeklyText = "Weekly on " + daysOfWeek.format(c.getTime());
        // not entirely sure if this is correct or not
        String monthlyText = "Monthly on " + (dayofMonth.format(c.getTime())) + postfix[parseInt((dayofMonth.format(c.getTime()))) -1] + " " + daysOfWeek.format(c.getTime());;
        String yearlyText = "Yearly on " + (dayofYear.format(c.getTime()));

        view.oneTimeBtn.setText("One-time");
        view.dailyBtn.setText("Daily");
        view.weeklyBtn.setText(weeklyText);
        view.monthlyBtn.setText(monthlyText);
        view.yearlyBtn.setText(yearlyText);
        view.oneTimeBtn.setChecked(true);
        return dialog;
    }

    private void captureGoalInput(){
        view.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input of the textEdit
                var input = view.goalEditText.getText().toString();
                LocalDateTime currentTime = LocalDateTime.now();
                // Create a new Goal with the text and add it
                String recurring = null;
                int contextOption = 0;

                // check which recurrence option was selected
                if (view.oneTimeBtn.isChecked()){

                } else if (view.dailyBtn.isChecked()){
                    recurring = "daily";
                } else if (view.weeklyBtn.isChecked()){
                    recurring = "weekly";
                } else if (view.monthlyBtn.isChecked()){
                    recurring = "monthly";
                } else if (view.yearlyBtn.isChecked()) {
                    recurring = "yearly";
                }

                if (view.homeBtn.isChecked()){
                    contextOption = 1;
                } else if (view.workBtn.isChecked()){
                    contextOption = 2;
                } else if (view.schoolBtn.isChecked()){
                    contextOption = 3;
                } else if (view.errandBtn.isChecked()){
                    contextOption = 4;
                }

                //need to set onClick to change date array based on whether or not the calendar was chosen for a future date
                int[] date = {currentTime.getMinute(), currentTime.getHour(),
                        currentTime.getDayOfMonth(), currentTime.getMonthValue(), currentTime.getYear()};

                var newGoal = new Goal(null, input, false, -1, false, recurring,
                        date[0], date[1], date[2], date[3], date[4], contextOption);
                // Create a new Goal with the text and add it
                activityModel.appendIncomplete(newGoal);
                dismiss();
            }
        });

        /*
        https://stackoverflow.com/questions/12937731/android-enter-key-listener
        Source Title:
        Date Captured: 2/17/2024 4:51 pm
        Used as a reference to have the done/check button on the keyboard to "mark as done"
        and capture the input to add as a goal. When typing .setOnEditorActionListener()
        the code automatically generated, the rest was reused from previously written code
        Handle: smhitle
         */
//        view.goalEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            // Capture the input
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
//                    // Get the input of the textEdit
//                    var input = view.goalEditText.getText().toString();
//                    LocalDateTime currentTime = LocalDateTime.now();
//                    // Create a new Goal with the text and add it
//                    String recurring = null;
//
//                    // check which recurrence option was selected
//                    if (view.oneTimeBtn.isChecked()){
//
//                    } else if (view.dailyBtn.isChecked()){
//                        recurring = "daily";
//                    } else if (view.weeklyBtn.isChecked()){
//                        recurring = "weekly";
//                    } else if (view.monthlyBtn.isChecked()){
//                        recurring = "monthly";
//                    } else if (view.yearlyBtn.isChecked()) {
//                        recurring = "yearly";
//                    }
//
//                    //need to set onClick to change date array based on whether or not the calendar was chosen for a future date
//                    int[] date = {currentTime.getYear(), currentTime.getMonthValue(),
//                            currentTime.getDayOfMonth(), currentTime.getHour(), currentTime.getMinute()};
//
//                    var newGoal = new Goal(null, input, false, -1, false, recurring,
//                            date[0], date[1], date[2], date[3], date[4]);
//                    // Create a new Goal with the text and add it
//                    activityModel.appendIncomplete(newGoal);
//                    dismiss();
//                }
//                return false;
//            }
//        });
    }

    /**
     * Behavior of the negative button of the DialogFragment
     *
     * @param dialog - instance of the dialog the negative button belongs to
     * @param which - the identifier of the button that was clicked
     */
    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }
}
