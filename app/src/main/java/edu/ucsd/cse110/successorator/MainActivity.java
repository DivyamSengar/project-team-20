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
import edu.ucsd.cse110.successorator.ui.MainFragment;
import edu.ucsd.cse110.successorator.ui.MainFragmentAdapter;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        System.out.println(view);
        System.out.println(view.getRoot());
        setContentView(view.getRoot());
    }
}
