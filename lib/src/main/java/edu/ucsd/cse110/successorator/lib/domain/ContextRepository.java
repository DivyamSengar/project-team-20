package edu.ucsd.cse110.successorator.lib.domain;

public interface ContextRepository {

     void setContext(int context);

     void removeContext();

     int getContext();
     void setContextWithBoolean(int context, boolean update);
     boolean getCurrentUpdateValue();

}
