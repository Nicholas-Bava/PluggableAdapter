package hybrid.model;

import java.util.ArrayList;
import java.util.List;

public abstract class IDDefinedEntity {



    protected Integer id;
    protected String name;
    protected Integer parentContestId;

    public List<IDDefinedEntity> children;

    protected IDDefinedEntity() {
        this.children = new ArrayList<>();
    }

    public interface Builder {
        public IDDefinedEntity build();

        public Builder addId(Integer id);
        public Builder addName(String name);
        public Builder addParentContestId(Integer parentContestId);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Integer getParentContestId() { return parentContestId; }
    public List<IDDefinedEntity> getChildren() { return children; }
    public void addChild(IDDefinedEntity child) { children.add(child); }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentContestId(Integer parentContestId) {
        this.parentContestId = parentContestId;
    }
}
