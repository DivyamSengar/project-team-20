package edu.ucsd.cse110.successorator.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentFocusModeBinding;
public class FocusModeDialogFragment extends DialogFragment {
    private FragmentFocusModeBinding view;

//    private MainViewModel activityModel;

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
//        var modelOwner = requireActivity();
//        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
//        var modelProvider = new ViewModelProvider(modelOwner,modelFactory);
//        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentFocusModeBinding.inflate(getLayoutInflater());

        focusContext = getFocusInput();
        System.out.println(focusContext + "did it happen ???");

        return new AlertDialog.Builder(getActivity())
                .setTitle("Focus Mode")
                .setMessage("Choose one")
                .setView(view.getRoot())
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private int getFocusInput(){
        AtomicInteger context = new AtomicInteger();
        view.homeContextBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                this.focusContext = 1;
                context.set(1);
                // filter the contexts based on home
                System.out.println("Home checked");
                System.out.println("in dialog" + focusContext);
                dismiss();

//                return 1;
            } else {}
        });

        view.workContextBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                this.focusContext = 2;
                context.set(2);
                // filter the contexts based on work
                System.out.println("work checked");
                System.out.println("in dialog" + focusContext);
                dismiss();
            } else {}
        });

        view.schoolContextBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                this.focusContext = 3;
                context.set(3);
                // filter the contexts based on school
                System.out.println("school checked");
                System.out.println("in dialog" + focusContext);
                dismiss();
            } else {}
        });

        view.errandContextBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                this.focusContext = 4;
                context.set(4);
                // filter the contexts based on errands
                System.out.println("errand checked");
                System.out.println("in dialog" + focusContext);
                dismiss();
            } else {}
        });

        System.out.println("did it happen");
        return context.get();
    }

    private int onNegativeButtonClick(DialogInterface dialog, int which){
        // Exit out of focus mode
        this.focusContext = 0;
        System.out.println("in dialog" + focusContext);
        dialog.dismiss();
        return 0;
    }

    public int getFocusContext() {
        return focusContext;
    }

}
