package edu.ucsd.cse110.successorator;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.os.Bundle;
import java.text.SimpleDateFormat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);

        /*Code got from:
        https://www.geeksforgeeks.org/how-to-get-current-time-and-date-in-android/
        How to Get Current Time and Date in Android?
        Captured at 2/09/2024
        Used for copying code to capture the date, and changed the format to fit our format
        Copied code start-
        SimpleDateFormat sdf = new SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z");
        String currentDateAndTime = sdf.format(new Date());
        -end
        */
        SimpleDateFormat date = new SimpleDateFormat("EEEE'\n'MM/dd");
        String currentDate = date.format(new Date());
        view.editTextDate.setText(currentDate);
        //To test the empty goal text
        view.emptyGoals.setText(R.string.emptyGoalsText);

        /* //current placeholder idea for showing the goal list and empty goal list situations
        if (goalList == 0){
            view.emptyGoals.setText(R.string.emptyGoalsText);
        }
        else{
            view.listGoals.setText();
        }
         */
        setContentView(view.getRoot());
    }
}
