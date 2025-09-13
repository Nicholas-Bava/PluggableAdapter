package hybrid.model.impl;

import hybrid.model.IDDefinedEntity;

public class Site extends IDDefinedEntity {

    public Site(){
        super();
    };

    @Override
    public String toString() {
        return String.format("Site[ID=%d, Name=%s, ContestID=%s]",
                id, name, parentContestId);
    }
}
