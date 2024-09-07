package org.example.briefi.controllers;

import org.example.briefi.models.Category;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;
    @FXML
    private TableColumn<Category, String> nameCol;
    @FXML
    private TableColumn<Category, String> stateCol;
    @FXML
    private TableView<Category> categoriesTableView;
    @FXML
    private Text categoriesCountText;
    @FXML
    private Text categoryLabelText;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField stateTextField;
    @FXML
    private VBox modalCoverVBox;
    private final Category category = new Category();
    private final ObservableList<Category> categories = FXCollections.observableArrayList();


    private void populateCategories() {

        nameCol.setCellValueFactory(new
                PropertyValueFactory<>("name"));
        stateCol.setCellValueFactory(new
                PropertyValueFactory<>("state"));
        stateCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    Circle circle = new Circle(5);
                    if (item.equals("inactif")) {
                        circle.setFill(Color.RED);
                    } else if (item.equals("actif")) {
                        circle.setFill(Color.GREEN);
                    } else {
                        circle.setFill(Color.GRAY);
                    }
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().add(circle);
                    stackPane.setAlignment(Pos.CENTER);

                    setGraphic(stackPane);
                }
            }
        });

        try {
            categoriesCountText.setText(category.list().size() + "élément" + (category.list().size() > 1 ? "s" : ""));

            categories.setAll(category.list());
            categoriesTableView.setItems(categories);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        TableView.TableViewSelectionModel<Category> selectionModel = categoriesTableView.getSelectionModel();

        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.clearSelection();

        ObservableList<Category> selectedItems = selectionModel.getSelectedItems();

        selectedItems.addListener((ListChangeListener<Category>)
                change -> {
                    if (!categories.isEmpty() && !selectedItems.isEmpty()) {

                        category.setId(categories.get(selectionModel.getSelectedIndex()).getId());

                        category.setName(categories.get(selectionModel.getSelectedIndex()).getName());

                        category.setState(categories.get(selectionModel.getSelectedIndex()).getState());

                        updateButton.setDisable(false);
                        deleteButton.setDisable(false);
                    }
                });
    }

    private void initComponents() {
        modalCoverVBox.setManaged(false);
        modalCoverVBox.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComponents();
        populateCategories();
    }

    @FXML
    private void onCreateCategory() {
        category.setId(0);
        categoryLabelText.setText("Créer une catégorie");
        nameTextField.setText("");
        fadeTransition("in", modalCoverVBox);
    }

    @FXML
    private void onDeleteCategory() throws SQLException {
        category.delete(category.getId());
        populateCategories();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @FXML
    private void onUpdateCategory() {
        if (!updateButton.isDisable()) {
            categoryLabelText.setText("Modifier une catégorie");
            nameTextField.setText(category.getName());
            stateTextField.setText(category.getState());
            fadeTransition("in", modalCoverVBox);
        }
    }

    @FXML
    private void onHideModal() {
        fadeTransition("out", modalCoverVBox);
    }

    @FXML
    private void onSubmitCategory() throws SQLException {

        String name = nameTextField.getText().trim();
        String state = stateTextField.getText().trim();

        if (!name.isEmpty() && !state.isEmpty()) {
            category.setName(name);
            category.setState(state);

            if (category.getId() == 0) {
                category.create(category);
            } else {
                category.update(category);
            }
            fadeTransition("out", modalCoverVBox);
            populateCategories();
        }
    }

    private void fadeTransition(String action, Node node) {

        if ("in".equals(action)) {
            modalCoverVBox.setManaged(true);
            modalCoverVBox.setVisible(true);
        }
        FadeTransition ft = new FadeTransition(Duration.millis(500), node);

        ft.setFromValue("in".equals(action) ? 0.0 : 1.0);
        ft.setToValue("in".equals(action) ? 1.0 : 0.0);
        ft.setOnFinished(ev -> {
            if ("out".equals(action)) {
                modalCoverVBox.setManaged(false);
                modalCoverVBox.setVisible(false);
                nameTextField.setText("");
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });
        ft.play();
    }
}