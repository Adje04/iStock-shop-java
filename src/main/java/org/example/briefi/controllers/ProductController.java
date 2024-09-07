
package org.example.briefi.controllers;

import org.example.briefi.models.Category;
import org.example.briefi.models.Product;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class ProductController implements Initializable {
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;
    @FXML
    private ComboBox<String> categoriesComboBox;
    @FXML
    private TableColumn<Product, String> nameCol;
    @FXML
    private TableColumn<Product, String> descriptionCol;
    @FXML
    private TableColumn<Product, String> quantityCol;
    @FXML
    private TableColumn<Product, String> thresholdQuantityCol;
    @FXML
    private TableView<Product> productsTableView;
    @FXML
    private Text productsCountText;
    @FXML
    private Text productLabelText;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField thresholdQuantityTextField;
    @FXML
    private VBox modalCoverVBox;
    private int currentCategoryIndex = 0;
    private Category category;
    private final Product product = new Product();
    private final ObservableList<Product> products =
            FXCollections.observableArrayList();

    private void populateProducts() throws SQLException {
        category = new Category();
        categoriesComboBox.getItems().clear();
        for (int i = 0; i < category.list().size(); i++) {
            categoriesComboBox.getItems().add(category.list().get(i).getName()
            );
        }
        EventHandler<ActionEvent> categoriesEvent =
                e -> {
                    currentCategoryIndex =
                            categoriesComboBox.getSelectionModel().getSelectedIndex();
                };
        categoriesComboBox.setOnAction(categoriesEvent);
        nameCol.setCellValueFactory(new
                PropertyValueFactory<>("name"));
        descriptionCol.setCellValueFactory(new
                PropertyValueFactory<>("description"));
        quantityCol.setCellValueFactory(new
                PropertyValueFactory<>("quantity"));
        thresholdQuantityCol.setCellValueFactory(new
                PropertyValueFactory<>("thresholdQuantity"));
        try {
            productsCountText.setText(product.list().size() + " élément" + (product.list().size() > 1 ? "s" : ""));

            products.setAll(product.list());
            productsTableView.setItems(products);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TableView.TableViewSelectionModel<Product> selectionModel =
                productsTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.clearSelection();
        ObservableList<Product> selectedItems =
                selectionModel.getSelectedItems();
        selectedItems.addListener((ListChangeListener<Product>)
                change -> {
                    if (!products.isEmpty() && !selectedItems.isEmpty()) {
                        product.setId(products.get(selectionModel.getSelectedIndex()).getId());

                        product.setName(products.get(selectionModel.getSelectedIndex()).getName());

                        product.setDescription(products.get(selectionModel.getSelectedIndex()).getDescription());

                        product.setQuantity(products.get(selectionModel.getSelectedIndex()
                        ).getQuantity());
                        product.setThresholdQuantity(products.get(selectionModel.getSelectedIndex()).getThresholdQuantity());

                        product.setCategory(products.get(selectionModel.getSelectedIndex()
                        ).getCategory());
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
        try {
            populateProducts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onCreateProduct() {
        product.setId(0);
        productLabelText.setText("Créer un produit");
        nameTextField.setText("");
        descriptionTextField.setText("");
        quantityTextField.setText("");
        thresholdQuantityTextField.setText("");
        fadeTransition("in", modalCoverVBox);
    }

    @FXML
    private void onDeleteProduct() throws SQLException {
        product.delete(product.getId());
        populateProducts();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @FXML
    private void onUpdateProduct() {
        if (!updateButton.isDisable()) {
            productLabelText.setText("Modifier un produit");
            nameTextField.setText(product.getName());
            descriptionTextField.setText(product.getDescription());
            quantityTextField.setText(String.valueOf(product.getQuantity()));
            thresholdQuantityTextField.setText(String.valueOf(product.getThresholdQuantity()));

            fadeTransition("in", modalCoverVBox);
        }
    }

    @FXML
    private void onHideModal() {
        fadeTransition("out", modalCoverVBox);
    }

    @FXML
    private void onSubmitProduct() throws SQLException {
        String name = nameTextField.getText().trim();
        String description = descriptionTextField.getText().trim();
        String quantity = quantityTextField.getText().trim();
        String thresholdQuantity =
                thresholdQuantityTextField.getText().trim();
        if (!name.isEmpty() && !description.isEmpty() &&
                !quantity.isEmpty() && !thresholdQuantity.isEmpty()) {
            try {
                product.setName(name);
                product.setDescription(description);
                product.setCategory(category.list().get(currentCategoryIndex));
                product.setQuantity(Integer.parseInt(quantity));
                product.setThresholdQuantity(Integer.parseInt(thresholdQuantity));
                if (product.getId() == 0) {
                    product.create(product);
                } else {
                    product.update(product);
                }
                fadeTransition("out", modalCoverVBox);
                populateProducts();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void fadeTransition(String action, Node node) {
        if ("in" .equals(action)) {
            modalCoverVBox.setManaged(true);
            modalCoverVBox.setVisible(true);
        }
        FadeTransition ft = new
                FadeTransition(Duration.millis(500), node);
        ft.setFromValue("in" .equals(action) ? 0.0 : 1.0);
        ft.setToValue("in" .equals(action) ? 1.0 : 0.0);
        ft.setOnFinished(ev -> {
            if ("out" .equals(action)) {
                modalCoverVBox.setManaged(false);
                modalCoverVBox.setVisible(false);
                nameTextField.setText("");
                descriptionTextField.setText("");
                quantityTextField.setText("");
                thresholdQuantityTextField.setText("");
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });
        ft.play();
    }
}