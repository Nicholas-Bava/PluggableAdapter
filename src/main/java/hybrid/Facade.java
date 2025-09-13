package hybrid;

import hybrid.model.IDDefinedEntity;
import hybrid.model.builder.Director;
import hybrid.tree.TreeBuilder;
import javafx.application.Application;

import java.util.List;

public class Facade {

    private static final String CSV_FILE_PATH = "src/main/resources/contest_data.csv";

    public void run() {
        System.out.println(" FACADE: Starting data processing pipeline...");

        List<IDDefinedEntity> entities = Director.importFromCSV(CSV_FILE_PATH);
        System.out.println(" FACADE: Loaded " + entities.size() + " entities");

        List<IDDefinedEntity> tree = TreeBuilder.buildTree(entities);
        System.out.println(" FACADE: Created tree with " + tree.size() + " root nodes");

        System.out.println(" FACADE: Passing data to TreeApplication...");
        TreeApplication.setParsedData(tree);

        System.out.println(" FACADE: Launching JavaFX Application...");
        Application.launch(TreeApplication.class);
    }

    public static void main(String[] args) {
        Facade facade = new Facade();
        facade.run();
    }
}
