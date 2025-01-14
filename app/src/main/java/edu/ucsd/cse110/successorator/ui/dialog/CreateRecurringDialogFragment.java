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
                if (view.workBtn.isChecked() || view.homeBtn.isChecked() ||
                        view.schoolBtn.isChecked() || view.errandBtn.isChecked()) {
                    var input = view.goalEditText.getText().toString();
                    // Create a new Goal with the text and add it
                    int recurring = 0;
                    int contextOption = 0;

                    // check which recurrence option was selected
                    if (view.dailyBtn.isChecked()) {
                        recurring = 1;
                    } else if (view.weeklyBtn.isChecked()) {
                        recurring = 2;
                    } else if (view.monthlyBtn.isChecked()) {
                        recurring = 3;
                    } else if (view.yearlyBtn.isChecked()) {
                        recurring = 4;
                    }

                    if (view.homeBtn.isChecked()) {
                        contextOption = 1;
                    } else if (view.workBtn.isChecked()) {
                        contextOption = 2;
                    } else if (view.schoolBtn.isChecked()) {
                        contextOption = 3;
                    } else if (view.errandBtn.isChecked()) {
                        contextOption = 4;
                    }

                    var dateInput = view.startingDate.getText().toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

                    try {
                        Date parsed = dateFormat.parse(dateInput);

                        Instant instant = parsed.toInstant();

                        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                        LocalDateTime now = activityModel.getTodayTime();

                        int[] date = {now.getMinute(), now.getHour(),
                                localDateTime.getDayOfMonth(), localDateTime.getMonthValue(), localDateTime.getYear()};

                        // if user enters a date that is before today, then just dismiss and do nothing

                        if (LocalDateTime.of(date[4], date[3], date[2], date[1], date[0]).isBefore(now)) {
                            dismiss();
                            return;
                        }
                        int goalPair = activityModel.getMaxGoalPair()+1;
                        var newGoal = new Goal(null, input, false, -1, false, recurring,
                                date[0], date[1], date[2], date[3], date[4], contextOption, goalPair);
                        // Create a new Goal with the text and add it
                        activityModel.appendIncomplete(newGoal);
                        if (recurring != 0) {
                            activityModel.appendToRecurringList(newGoal);
                        }
                        LocalDateTime today = activityModel.getTodayTime();
                        localDateTime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonthValue(),
                                localDateTime.getDayOfMonth(), today.getHour(), today.getMinute());
                        if (1 == recurring && localDateTime.isEqual(today)) {
                            LocalDateTime tomorrow = today.plusDays(1);
                            newGoal.setDate(tomorrow.getMinute(), tomorrow.getHour(),
                                    tomorrow.getDayOfMonth(), tomorrow.getMonthValue(), tomorrow.getYear());
                            activityModel.appendIncomplete(newGoal);
                        }
                        dismiss();
                    } catch (ParseException e) {

                    }
                }
            }
        });
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
