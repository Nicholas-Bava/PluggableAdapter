package model.builder;

import model.IDDefinedEntity;
import model.impl.Site;

public class SiteBuilder implements IDDefinedEntity.Builder{

    protected int id;
    protected String name;
    protected Integer parentContestId;
    @Override
    public IDDefinedEntity build() {
        Site site = new Site();
        site.setId(id);
        site.setName(name);
        site.setParentContestId(parentContestId);
        System.out.println("Site built");
        return site;
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
