package edu.ucsd.cse110.successorator.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentFocusModeBinding;
import edu.ucsd.cse110.successorator.ui.MainFragment;

public class FocusModeDialogFragment extends DialogFragment {
    private FragmentFocusModeBinding view;

    private MainViewModel activityModel;


//    private int whichView;




    private int focusContext;
    FocusModeDialogFragment(){
    }

    public static FocusModeDialogFragment newInstance() {
        Bundle args = new Bundle();
        FocusModeDialogFragment fragment = new FocusModeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner,modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentFocusModeBinding.inflate(getLayoutInflater());

        getFocusInput();
        System.out.println(focusContext + "did it happen ???");

//        radioGroup = view.getRoot().findViewById(R.id.main_radio_group);
//        radioGroup.setOnCheckedChangeListener(this);


        return new AlertDialog.Builder(getActivity())
                .setTitle("Focus Mode")
                .setMessage("Choose one")
                .setView(view.getRoot())
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void getFocusInput(){
        view.homeContextBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                this.focusContext = 1;
                // filter the contexts based on home
                System.out.println("Home checked");
                System.out.println("in dialog" + focusContext);
                activityModel.removeContext();
                activityModel.setContextWithBoolean(1, true);
                dismiss();

//                return 1;
            } else {}
        });

        view.workContextBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                this.focusContext = 2;
                // filter the contexts based on work
                System.out.println("work checked");
                System.out.println("in dialog" + focusContext);
                activityModel.removeContext();
                activityModel.setContextWithBoolean(2, true);
                dismiss();
            } else {}
        });

        view.schoolContextBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                this.focusContext = 3;
                // filter the contexts based on school
                System.out.println("school checked");
                System.out.println("in dialog" + focusContext);
                activityModel.removeContext();
                activityModel.setContextWithBoolean(3, true);
                dismiss();
            } else {}
        });

        view.errandContextBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                this.focusContext = 4;
                // filter the contexts based on errands
                System.out.println("errand checked");
                System.out.println("in dialog" + focusContext);
                activityModel.removeContext();
                activityModel.setContextWithBoolean(4, true);
                dismiss();
            } else {}
        });

        System.out.println("did it happen");

    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        // Exit out of focus mode
        this.focusContext = 0;
        System.out.println("in dialog" + focusContext);
        activityModel.removeContext();
        activityModel.setContextWithBoolean(0, true);
        dialog.dismiss();
    }

    public int getFocusContext() {
        return focusContext;
    }

//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//            case R.id.homeContextBtn:
//                focusContext = 1;
//                // filter the contexts based on home
//                System.out.println("Home checked");
//                System.out.println("in dialog" + focusContext);
//                activityModel.removeContext();
//                activityModel.setContext(1);
//                updateView(focusContext);
//                break;
//            case R.id.workContextBtn:
//                focusContext = 2;
//                // filter the contexts based on work
//                System.out.println("work checked");
//                System.out.println("in dialog" + focusContext);
//                activityModel.removeContext();
//                activityModel.setContext(2);
//                updateView(focusContext);
//                break;
//            case R.id.schoolContextBtn:
//                focusContext = 3;
//                // filter the contexts based on school
//                System.out.println("school checked");
//                System.out.println("in dialog" + focusContext);
//                activityModel.removeContext();
//                activityModel.setContext(3);
//                updateView(focusContext);
//                break;
//            case R.id.errandContextBtn:
//                focusContext = 4;
//                // filter the contexts based on errands
//                System.out.println("errand checked");
//                System.out.println("in dialog" + focusContext);
//                activityModel.removeContext();
//                activityModel.setContext(4);
//                updateView(focusContext);
//                break;
//        }
//    }

}
