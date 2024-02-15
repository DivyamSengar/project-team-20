package edu.ucsd.cse110.successorator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentMainBinding;
import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.ui.MainFragmentAdapter;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        // need to move this commented code elsewhere

//        // Temporary
//        var v = FragmentMainBinding.inflate(getLayoutInflater());
//        setContentView(v.getRoot());
//
        // for sure need this but somewhere else
//        SimpleDateFormat date = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
//        String currentDate = date.format(new Date());
//        v.dateText.setText(currentDate);
//
//        //To test the empty goal text
//        v.emptyGoals.setText(R.string.emptyGoalsText);
//
//        var datasource = DataSource.fromDefault();
//        this.model = new MainViewModel(new GoalRepository(datasource));
//        this.view = ActivityMainBinding.inflate(getLayoutInflater());
//
////        var modelOwner = this;
////        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
////        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
////        this.model = modelProvider.get(MainViewModel.class);
//
        // need to check if there are goals but not sure where to put
//        model.isGoalsEmpty().observe(isGoalsEmpty -> {
//            if (Boolean.TRUE.equals(isGoalsEmpty)) {
//                // set viz one way
//                model.getGoals().observe(text -> v.emptyGoals.setText(R.string.emptyGoalsText));
//                v.emptyGoals.setVisibility(View.VISIBLE);
//                v.listGoals.setVisibility(View.INVISIBLE);
//            } else {
//                MainFragmentAdapter adapter = new MainFragmentAdapter(this, datasource.getGoals());
//                model.getGoals().observe(text -> v.listGoals.setAdapter(adapter));
//                v.emptyGoals.setVisibility(View.INVISIBLE);
//                v.listGoals.setVisibility(View.VISIBLE);
//            }
//        });


        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
    }
}
