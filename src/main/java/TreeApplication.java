import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.IDDefinedEntity;
import model.builder.Director;
import model.impl.Contest;
import tree.TreeBuilder;

import java.io.File;
import java.util.List;

public class TreeApplication extends Application {
    private TreeView<IDDefinedEntity> treeView;
    private TextArea detailsArea;
    private List<IDDefinedEntity> currentRoots;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tree Representation of Data");

        BorderPane layout = new BorderPane();

        HBox toolbar = createToolbar();
        layout.setTop(toolbar);

        SplitPane centerPane = createCenterPane();
        layout.setCenter(centerPane);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        loadData();
    }

    private HBox createToolbar() {
        HBox toolbar = new HBox(10);
        toolbar.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        Button expandButton = new Button("Expand All");
        Button collapseButton = new Button("Collapse All");

        expandButton.setOnAction(e -> expandAll(treeView.getRoot()));
        collapseButton.setOnAction(e -> collapseAll(treeView.getRoot()));

        toolbar.getChildren().addAll(expandButton, collapseButton);
        return toolbar;
    }

    private SplitPane createCenterPane() {
        SplitPane splitPane = new SplitPane();

        VBox treePanel = new VBox(5);
        treePanel.setStyle("-fx-padding: 10;");

        Label treeLabel = new Label("Tree Structure:");
        treeLabel.setStyle("-fx-font-weight: bold;");

        treeView = new TreeView<>();
        treeView.setCellFactory(tv -> new TreeCellAdapter());
        treeView.setShowRoot(false);

        treeView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        showEntityDetails(newVal.getValue());
                    }
                }
        );

        treePanel.getChildren().addAll(treeLabel, treeView);
        VBox.setVgrow(treeView, Priority.ALWAYS);

        VBox detailsPanel = new VBox(5);
        detailsPanel.setStyle("-fx-padding: 10;");

        Label detailsLabel = new Label("Details:");
        detailsLabel.setStyle("-fx-font-weight: bold;");

        detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setText("Select an item to see details");

        detailsPanel.getChildren().addAll(detailsLabel, detailsArea);
        VBox.setVgrow(detailsArea, Priority.ALWAYS);

        splitPane.getItems().addAll(treePanel, detailsPanel);
        splitPane.setDividerPositions(0.6);

        return splitPane;
    }

    private static class TreeCellAdapter extends TreeCell<IDDefinedEntity> {
        @Override
        protected void updateItem(IDDefinedEntity entity, boolean empty) {
            super.updateItem(entity, empty);

            if (empty || entity == null) {
                setText(null);
                setStyle("");
            } else {
                String icon = entity instanceof Contest ? "🏆" : "📍";
                setText(icon + " " + entity.getName() + " (ID: " + entity.getId() + ")");

                if (entity instanceof Contest) {
                    setStyle("-fx-text-fill: #2E8B57; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: #4169E1;");
                }
            }
        }
    }

    private static class TreeItemAdapter extends TreeItem<IDDefinedEntity> {
        public TreeItemAdapter(IDDefinedEntity entity) {
            super(entity);

            for (IDDefinedEntity child : entity.children) {
                getChildren().add(new TreeItemAdapter(child));
            }
        }
    }

    private void populateTreeView(List<IDDefinedEntity> tree) {
        TreeItem<IDDefinedEntity> root = new TreeItem<>();

        for (IDDefinedEntity entity : tree) {
            TreeItemAdapter item = new TreeItemAdapter(entity);
            item.setExpanded(true);
            root.getChildren().add(item);
        }

        root.setExpanded(true);
        treeView.setRoot(root);
        this.currentRoots = tree;
    }

    private void openCSVFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showOpenDialog(treeView.getScene().getWindow());
        if (file != null) {
            loadDataFromFile(file.getAbsolutePath());
        }
    }

    private void loadDataFromFile(String filePath) {
        try {

            List<IDDefinedEntity> entities = Director.importFromCSV(filePath);

            List<IDDefinedEntity> tree = TreeBuilder.buildTree(entities);

            populateTreeView(tree);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Could not load file: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void loadData() {
        String csvPath = "src/main/resources/contest_data.csv";

        try {
            var resource = getClass().getResourceAsStream("/contest_data.csv");
            if (resource != null) {
                java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("contest_data", ".csv");
                java.nio.file.Files.copy(resource, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                csvPath = tempFile.toString();
            }
        } catch (Exception e) {
            System.out.println("Loading from file system: " + csvPath);
        }

        if (new File(csvPath).exists()) {
            loadDataFromFile(csvPath);
        } else {
            System.out.println("Sample CSV file not found at: " + csvPath);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Welcome");
            alert.setContentText("Data file not found.");
            alert.showAndWait();
        }
    }

    private void showEntityDetails(IDDefinedEntity entity) {
        if (entity == null) return;

        StringBuilder details = new StringBuilder();
        details.append("Entity Details\n");
        details.append("==============\n\n");
        details.append("Type: ").append(entity instanceof Contest ? "Contest" : "Site").append("\n");
        details.append("ID: ").append(entity.getId()).append("\n");
        details.append("Name: ").append(entity.getName()).append("\n");
        details.append("Parent ID: ").append(
                entity.getParentContestId() != null ? entity.getParentContestId() : "ROOT"
        ).append("\n");
        details.append("Children: ").append(entity.children.size()).append("\n");

        if (!entity.children.isEmpty()) {
            details.append("\nChild Entities:\n");
            for (IDDefinedEntity child : entity.children) {
                String icon = child instanceof Contest ? "🏆" : "📍";
                details.append("  ").append(icon).append(" ")
                        .append(child.getName()).append(" (ID: ").append(child.getId()).append(")\n");
            }
        }

        detailsArea.setText(details.toString());
    }

    private void expandAll(TreeItem<IDDefinedEntity> item) {
        if (item != null) {
            item.setExpanded(true);
            for (TreeItem<IDDefinedEntity> child : item.getChildren()) {
                expandAll(child);
            }
        }
    }

    private void collapseAll(TreeItem<IDDefinedEntity> item) {
        if (item != null) {
            item.setExpanded(false);
            for (TreeItem<IDDefinedEntity> child : item.getChildren()) {
                collapseAll(child);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
