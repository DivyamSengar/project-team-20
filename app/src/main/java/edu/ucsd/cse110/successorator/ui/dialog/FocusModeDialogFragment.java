package edu.ucsd.cse110.successorator.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.ucsd.cse110.successorator.databinding.FragmentFocusModeBinding;

public class FocusModeDialogFragment extends DialogFragment {
    private FragmentFocusModeBinding view;
    FocusModeDialogFragment(){}

    public static FocusModeDialogFragment newInstance() {
        Bundle args = new Bundle();
        FocusModeDialogFragment fragment = new FocusModeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentFocusModeBinding.inflate(getLayoutInflater());

        getFocusInput();

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
                // filter the contexts based on home
                System.out.println("Home checked");
                dismiss();
            } else {}
        });

        view.workContextBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                // filter the contexts based on work
                System.out.println("work checked");
                dismiss();
            } else {}
        });

        view.schoolContextBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                // filter the contexts based on school
                System.out.println("school checked");
                dismiss();
            } else {}
        });

        view.errandContextBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                // filter the contexts based on errands
                System.out.println("errand checked");
                dismiss();
            } else {}
        });
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        // Exit out of focus mode
        dialog.dismiss();
    }
}
