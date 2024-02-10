package edu.ucsd.cse110.successorator.lib.domain;

import java.util.ArrayList;
import java.util.List;


import edu.ucsd.cse110.successorator.lib.data.DataSource;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class GoalList {

    private DataSource dataSource;

    public GoalList(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public Integer count() {
        return dataSource.getGoals().size();
    }

    public SimpleSubject<GoalEntry> find(int id){
        return dataSource.getGoalEntrySubject(id);
    }

    public SimpleSubject<List<GoalEntry>> findAll() {
        return dataSource.getAllGoalEntrySubject();
    }

    public void save(GoalEntry goalEntry) {
        dataSource.putGoalEntry(goalEntry);
    }
}
