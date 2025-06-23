package model.builder;

import model.IDDefinedEntity;
import model.impl.Contest;
import model.impl.Site;

import java.util.List;

public class ContestBuilder implements IDDefinedEntity.Builder {

    protected int id;
    protected String name;
    protected Integer parentContestId;
    @Override
    public IDDefinedEntity build() {
        Contest contest = new Contest();
        contest.setId(id);
        contest.setName(name);
        contest.setParentContestId(parentContestId);
        System.out.println("Contest built");
        return contest;
    }

    @Override
    public IDDefinedEntity.Builder addId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public IDDefinedEntity.Builder addName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public IDDefinedEntity.Builder addParentContestId(Integer parentContestId) {
        this.parentContestId = parentContestId;
        return this;
    }
}
