package edu.ucsd.cse110.successorator.context.db;

import edu.ucsd.cse110.successorator.lib.domain.ContextRepository;

public class RoomContextRepository implements ContextRepository {
    private final ContextDao contextDao;

    public RoomContextRepository(ContextDao contextDao){
        this.contextDao = contextDao;
    }

    public void setContext(int context){
        contextDao.insertContext(new ContextEntity(context));
    }

    public void removeContext(){
        contextDao.deleteContext();
    }

    public int getContext(){
        return contextDao.getContext().getContextValue();
    }

}
