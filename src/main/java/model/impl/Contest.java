package model.impl;

import model.IDDefinedEntity;

public class Contest extends IDDefinedEntity {


    public Contest(){
        super();
    };

    @Override
    public String getEntityType() { return "Contest"; }

    @Override
    public String toString() {
        return String.format("Contest[ID=%d, Name=%s, ContestID=%s]",
                id, name, parentContestId);
    }
}
