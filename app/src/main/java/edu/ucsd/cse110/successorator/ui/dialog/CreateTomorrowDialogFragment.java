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

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class CreateTomorrowDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private FragmentDialogCreateGoalBinding view;

    CreateTomorrowDialogFragment(){
    }

    public static CreateTomorrowDialogFragment newInstance() {
        Bundle args = new Bundle();
        CreateTomorrowDialogFragment fragment = new CreateTomorrowDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreateGoalBinding.inflate(getLayoutInflater());

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

        LocalDateTime todayTime = activityModel.getTodayTime().plusDays(1);
        Calendar c = Calendar.getInstance();
        Instant instant = todayTime.atZone(ZoneId.systemDefault()).toInstant();
        c.setTimeInMillis(instant.toEpochMilli());

        SimpleDateFormat daysOfWeek = new SimpleDateFormat("E", Locale.getDefault());
        SimpleDateFormat dayofMonth = new SimpleDateFormat("F", Locale.getDefault());
        SimpleDateFormat dayofYear = new SimpleDateFormat("MM/dd", Locale.getDefault());

//        c.add(Calendar.DATE, 1);
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
                if (view.workBtn.isChecked() || view.homeBtn.isChecked() ||
                        view.schoolBtn.isChecked() || view.errandBtn.isChecked()){
                    var input = view.goalEditText.getText().toString();
                    LocalDateTime tomorrowTime = activityModel.getTodayTime().plusDays(1);
                    // Create a new Goal with the text and add it
                    int recurring = 0;
                    int contextOption = 0;

                    // check which recurrence option was selected
                    if (view.oneTimeBtn.isChecked()){

                    } else if (view.dailyBtn.isChecked()){
                        recurring = 1;
                    } else if (view.weeklyBtn.isChecked()){
                        recurring = 2;
                    } else if (view.monthlyBtn.isChecked()){
                        recurring = 3;
                    } else if (view.yearlyBtn.isChecked()) {
                        recurring = 4;
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
                    int[] date = {tomorrowTime.getMinute(), tomorrowTime.getHour(),
                            tomorrowTime.getDayOfMonth(), tomorrowTime.getMonthValue(), tomorrowTime.getYear()};
                    int goalPair = activityModel.getMaxGoalPair()+1;
                    var newGoal = new Goal(null, input, false, -1, false, recurring,
                            date[0], date[1], date[2], date[3], date[4], contextOption, goalPair);
                    // Create a new Goal with the text and add it
                    activityModel.appendIncomplete(newGoal);
                    if (recurring != 0){
                        activityModel.appendToRecurringList(newGoal);
                    }
                    if (recurring == 1){
                        newGoal.setDate(date[0], date[1], date[2]+1, date[3], date[4]);
                        activityModel.appendIncomplete(newGoal);
                    }
                    dismiss();
                }
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
//                    LocalDateTime tomorrowTime = LocalDateTime.now().plusDays(1);
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
//                    int[] date = {tomorrowTime.getYear(), tomorrowTime.getMonthValue(),
//                            tomorrowTime.getDayOfMonth(), tomorrowTime.getHour(), tomorrowTime.getMinute()};
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
