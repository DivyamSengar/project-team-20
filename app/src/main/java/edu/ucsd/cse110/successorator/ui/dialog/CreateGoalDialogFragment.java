package edu.ucsd.cse110.successorator.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class CreateGoalDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private FragmentDialogCreateGoalBinding view;
    CreateGoalDialogFragment(){
    }

    public static CreateGoalDialogFragment newInstance(){
        var fragment = new CreateGoalDialogFragment();
        Bundle args = new Bundle();
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
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        this.view = FragmentDialogCreateGoalBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Enter Most Important Thing")
                .setView(view.getRoot())
                .setPositiveButton("", this::onPositiveButtonClick)
                .setPositiveButtonIcon(ContextCompat.getDrawable(getActivity(), R.drawable.done))
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();

    }

    private void onPositiveButtonClick(DialogInterface dialog, int which){
        // Functionality for adding
        var input = view.cardFrontEditText.getText().toString();

        // should be null instead
        var newGoal = new Goal(5, input, false);
        activityModel.addGoal(newGoal);

//        var front = view.cardFrontEditText.getText().toString();
//        var back = view.cardBackEditText.getText().toString();
//
//        // Sort order is an invalid value here, because append/prepend will replace it
//        var card = new Flashcard(null, front, back, -1);
//
//        if (view.appendRadioBtn.isChecked()){
//            activityModel.append(card);
//        } else if (view.prependRadioBtn.isChecked()) {
//            activityModel.prepend(card);
//        } else {
//            throw new IllegalStateException("No radio button is checked");
//        }

        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }
}

//            return new AlertDialog.Builder(getActivity())
//            .setTitle("Enter Most Important Thing")
//                .setView(view.getRoot())
//            .setPositiveButton("", this::onPositiveButtonClick)
//                .setPositiveButtonIcon(ContextCompat.getDrawable(getActivity(), R.drawable.done))
//            .setNegativeButton("Cancel", this::onNegativeButtonClick)
//                .create();
