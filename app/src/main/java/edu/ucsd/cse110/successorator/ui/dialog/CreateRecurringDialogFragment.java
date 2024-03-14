package edu.ucsd.cse110.successorator.ui.dialog;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateGoalBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentRecurringDialogCreateGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class CreateRecurringDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private FragmentRecurringDialogCreateGoalBinding view;

    CreateRecurringDialogFragment(){}

    public static CreateRecurringDialogFragment newInstance() {
        Bundle args = new Bundle();
        CreateRecurringDialogFragment fragment = new CreateRecurringDialogFragment();
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
        this.view = FragmentRecurringDialogCreateGoalBinding.inflate(getLayoutInflater());
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

        view.dailyBtn.setText("Daily...");
        view.weeklyBtn.setText("Weekly...");
        view.monthlyBtn.setText("Monthly...");
        view.yearlyBtn.setText("Yearly...");
        view.weeklyBtn.setChecked(true);

        return dialog;
    }

    private void captureGoalInput(){

        view.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input of the textEdit
                var input = view.goalEditText.getText().toString();
                // Create a new Goal with the text and add it
                String recurring = null;
                int contextOption = 0;

                // check which recurrence option was selected
                if (view.dailyBtn.isChecked()){
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
                
                var dateInput = view.startingDate.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

                try {
                    Date parsed = dateFormat.parse(dateInput);

                    Instant instant = parsed.toInstant();

                    LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

                    int[] date = {localDateTime.getMinute(), localDateTime.getHour(),
                            localDateTime.getDayOfMonth(), localDateTime.getMonthValue(), localDateTime.getYear()};
                    var newGoal = new Goal(null, input, false, -1, false, recurring,
                            date[0], date[1], date[2], date[3], date[4], contextOption);
                    // Create a new Goal with the text and add it
                    activityModel.appendIncomplete(newGoal);
                    if (recurring != null){
                        activityModel.appendToRecurringList(newGoal);
                    }
                    LocalDateTime today = LocalDateTime.now();
                    if ("daily".equals(recurring) && localDateTime.isEqual(today)){
                        LocalDateTime tomorrow = today.plusDays(1);
                        newGoal.setDate(tomorrow.getMinute(), tomorrow.getHour(),
                                tomorrow.getDayOfMonth(), tomorrow.getMonthValue(), tomorrow.getYear());
                        activityModel.appendIncomplete(newGoal);
                    }
                    dismiss();
                } catch (ParseException e){

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
//            // Capture the input
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
//                    // Get the input of the textEdit
//                    var input = view.goalEditText.getText().toString();
//                    // Create a new Goal with the text and add it
//                    String recurring = null;
//
//                    // check which recurrence option was selected
//                    if (view.dailyBtn.isChecked()){
//                        recurring = "daily";
//                    } else if (view.weeklyBtn.isChecked()){
//                        recurring = "weekly";
//                    } else if (view.monthlyBtn.isChecked()){
//                        recurring = "monthly";
//                    } else if (view.yearlyBtn.isChecked()) {
//                        recurring = "yearly";
//                    }
//
//                    var dateInput = view.startingDate.getText().toString();
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
//
//                    try {
//                        Date parsed = dateFormat.parse(dateInput);
//
//                        Instant instant = parsed.toInstant();
//
//                        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
//
////                        Calendar date = Calendar.getInstance();
////                        date.setTime(parsed);
////
////                        var month = date.get(Calendar.MONTH) + 1;
////                        var day = date.get(Calendar.DATE);
////                        var year = date.get(Calendar.YEAR);
//
////                        date.get(Calendar.);
//
//                        int[] date = {localDateTime.getYear(), localDateTime.getMonthValue(),
//                            localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute()};
//                        var newGoal = new Goal(null, input, false, -1, false, recurring,
//                            date[0], date[1], date[2], date[3], date[4]);
//                        // Create a new Goal with the text and add it
//                        activityModel.appendIncomplete(newGoal);
//                        dismiss();
//                    } catch (ParseException e){
//
//                    }
//
//                    //need to set onClick to change date array based on whether or not the calendar was chosen for a future date
////                    int[] date = {currentTime.getYear(), currentTime.getMonthValue(),
////                            currentTime.getDayOfMonth(), currentTime.getHour(), currentTime.getMinute()};
////
////                    var newGoal = new Goal(null, input, false, -1, false, recurring,
////                            date[0], date[1], date[2], date[3], date[4]);
////                    // Create a new Goal with the text and add it
////                    activityModel.appendIncomplete(newGoal);
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
