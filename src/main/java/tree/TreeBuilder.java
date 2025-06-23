package tree;

import model.IDDefinedEntity;
import model.impl.Contest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeBuilder {
    public static List<IDDefinedEntity> buildTree(List<IDDefinedEntity> entities) {
        System.out.println("TreeBuilder: Processing " + entities.size() + " entities");

        for (IDDefinedEntity entity : entities) {
            if (entity.children == null) {
                entity.children = new ArrayList<>();
            }
            System.out.println("  Entity: ID=" + entity.getId() + ", Name=" + entity.getName() +
                    ", Type=" + entity.getClass().getSimpleName() +
                    ", ParentContestID=" + entity.getParentContestId());
        }

        Map<Integer, IDDefinedEntity> contestMap = new HashMap<>();
        for (IDDefinedEntity entity : entities) {
            if (entity instanceof Contest) {  // ← Only add Contests to the map
                contestMap.put(entity.getId(), entity);
                System.out.println("  Added CONTEST to map: ID=" + entity.getId() + " -> " + entity.getName());
            }
        }

        System.out.println("   Contest map contains IDs: " + contestMap.keySet());

        List<IDDefinedEntity> roots = new ArrayList<>();

        for (IDDefinedEntity entity : entities) {
            Integer parentContestId = entity.getParentContestId();
            System.out.println("  Processing: " + entity.getName() + " (ID=" + entity.getId() +
                    ", Type=" + entity.getClass().getSimpleName() +
                    ", ParentContestID=" + parentContestId + ")");

            if (parentContestId == null) {
                roots.add(entity);
                System.out.println("    Added as ROOT");
            } else {
                IDDefinedEntity parentContest = contestMap.get(parentContestId);
                if (parentContest != null) {
                    parentContest.children.add(entity);
                    System.out.println("    Added as child of CONTEST: " + parentContest.getName() + " (ID=" + parentContestId + ")");
                    System.out.println("    Parent Contest now has " + parentContest.children.size() + " children");
                } else {
                    roots.add(entity);
                    System.out.println("    Parent Contest ID=" + parentContestId + " not found! Adding as root.");
                }
            }
        }

        return roots;
    }
}
