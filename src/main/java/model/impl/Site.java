package model.impl;

import model.IDDefinedEntity;

public class Site extends IDDefinedEntity {

    public Site(){
        super();
    };

    @Override
    public String getEntityType() { return "Site"; }

    @Override
    public String toString() {
        return String.format("Site[ID=%d, Name=%s, ContestID=%s]",
                id, name, parentContestId);
    }
}
