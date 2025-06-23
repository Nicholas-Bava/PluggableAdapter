package tree;

import model.IDDefinedEntity;
import model.impl.Contest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeBuilder {
    public static List<IDDefinedEntity> buildTree(List<IDDefinedEntity> entities) {
        System.out.println("=== DEBUG TreeBuilder: Processing " + entities.size() + " entities");

        // Initialize children lists for all entities
        for (IDDefinedEntity entity : entities) {
            if (entity.children == null) {
                entity.children = new ArrayList<>();
            }
            System.out.println("  Entity: ID=" + entity.getId() + ", Name=" + entity.getName() +
                    ", Type=" + entity.getClass().getSimpleName() +
                    ", ParentContestID=" + entity.getParentContestId());
        }

        // Create map for quick lookup by ID - ONLY FOR CONTESTS
        Map<Integer, IDDefinedEntity> contestMap = new HashMap<>();
        for (IDDefinedEntity entity : entities) {
            if (entity instanceof Contest) {  // ← Only add Contests to the map
                contestMap.put(entity.getId(), entity);
                System.out.println("  Added CONTEST to map: ID=" + entity.getId() + " -> " + entity.getName());
            }
        }

        System.out.println("=== Contest map contains IDs: " + contestMap.keySet());

        // Build parent-child relationships and find roots
        List<IDDefinedEntity> roots = new ArrayList<>();

        for (IDDefinedEntity entity : entities) {
            Integer parentContestId = entity.getParentContestId();
            System.out.println("  Processing: " + entity.getName() + " (ID=" + entity.getId() +
                    ", Type=" + entity.getClass().getSimpleName() +
                    ", ParentContestID=" + parentContestId + ")");

            if (parentContestId == null) {
                // This is a root
                roots.add(entity);
                System.out.println("    -> Added as ROOT");
            } else {
                // Find parent CONTEST and add this as child
                IDDefinedEntity parentContest = contestMap.get(parentContestId);
                if (parentContest != null) {
                    parentContest.children.add(entity);
                    System.out.println("    -> Added as child of CONTEST: " + parentContest.getName() + " (ID=" + parentContestId + ")");
                    System.out.println("    -> Parent Contest now has " + parentContest.children.size() + " children");
                } else {
                    // Parent Contest not found, treat as root
                    roots.add(entity);
                    System.out.println("    -> WARNING: Parent Contest ID=" + parentContestId + " not found! Adding as root.");
                }
            }
        }

        System.out.println("=== Final: Found " + roots.size() + " root entities");
        for (IDDefinedEntity root : roots) {
            System.out.println("  Root: " + root.getName() + " (" + root.getClass().getSimpleName() + ") has " + root.children.size() + " children");
        }

        return roots;
    }

    /**
     * Simple console print of tree structure
     */
    public static void printTree(List<IDDefinedEntity> roots) {
        System.out.println("Tree Structure:");
        for (IDDefinedEntity root : roots) {
            printNode(root, 0);
        }
    }

    private static void printNode(IDDefinedEntity entity, int depth) {
        String indent = "  ".repeat(depth);
        String icon = entity instanceof Contest ? "🏆" : "📍";
        System.out.println(indent + icon + " " + entity);

        for (IDDefinedEntity child : entity.children) {
            printNode(child, depth + 1);
        }
    }
}
