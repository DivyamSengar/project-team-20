package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.DataSource;

public interface GoalRepository {
    // DataSource getter used for testing
    DataSource getDataSource();

    // Potentially useful for UI Implementation
    Integer count();

    // getting a SimpleSubject to a goal based on its id
    Subject<Goal> find(int id);

    // get all Goals, Incomplete and Complete
    Subject<List<Goal>> findAll();

    // add a goal to the list
    void save(Goal goal);

    // adds a set of goals to the list
    void save(List<Goal> goals);

    // markAsIncomplete
    void remove(int id);

    /*
        WILL BE USEFUL FOR SHIFTING GOALS, but not exactly these methods
         */
    void append(Goal goal);

    void prepend(Goal goal);
}
