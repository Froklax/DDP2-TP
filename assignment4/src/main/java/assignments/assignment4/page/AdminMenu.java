package assignments.assignment4.page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import assignments.assignment3.DepeFood;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenu extends MemberMenu{
    private Stage stage;
    private Scene scene;
    private User user;
    private Scene addRestaurantScene;
    private Scene addMenuScene;
    private Scene viewRestaurantsScene;
    private List<Restaurant> restoList = new ArrayList<>();
    private MainApp mainApp; // Reference to MainApp instance
    private ComboBox<String> restaurantComboBox = new ComboBox<>();
    private ListView<String> menuItemsListView = new ListView<>();

    public AdminMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user; // Store the user
        this.scene = createBaseMenu();
        this.addRestaurantScene = createAddRestaurantForm();
        this.addMenuScene = createAddMenuForm();
        this.viewRestaurantsScene = createViewRestaurantsForm();
    }

    @Override
    public Scene createBaseMenu() {
        // Method untuk menampilkan menu untuk Admin
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        // Button untuk tambah restoran
        Button tambahRestoranButton = new Button("Tambah Restoran");
        tambahRestoranButton.setOnAction(e -> stage.setScene(createAddRestaurantForm()));

        // Button untuk tambah menu di restoran
        Button tambahMenuButton = new Button("Tambah Menu Restoran");
        tambahMenuButton.setOnAction(e -> stage.setScene(createAddMenuForm()));

        // Button untuk melihat daftar menu di restoran
        Button daftarRestoranButton = new Button("Lihat Daftar Restoran");
        daftarRestoranButton.setOnAction(e-> stage.setScene(createViewRestaurantsForm()));

        // Button untuk kembali ke login form
        Button logOutButton = new Button("Log Out");
        logOutButton.setOnAction(e -> mainApp.logout());

        menuLayout.getChildren().addAll(tambahRestoranButton, tambahMenuButton, daftarRestoranButton, logOutButton);

        return new Scene(menuLayout, 400, 600);
    }

    private Scene createAddRestaurantForm() {
        // Method ini untuk menampilkan page tambah restoran
        VBox layout = new VBox(10);

        layout.setAlignment(Pos.CENTER);

        // Meminta nama restoran dari user
        Label namaRestaurantLabel = new Label("Restaurant Name:");
        TextField namaRestaurantField = new TextField();
        namaRestaurantField.setPromptText("Nama Restoran");

        // Button untuk inisiasi method handleTambahRestoran untuk menambah restoran
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleTambahRestoran(namaRestaurantField.getText()));

        // Button untuk kembali ke admin menu
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setOnAction(e -> stage.setScene(mainApp.getScene("Admin Menu")));

        layout.getChildren().addAll(namaRestaurantLabel, namaRestaurantField, submitButton, kembaliButton);

        return new Scene(layout, 400, 600);
    }

    private Scene createAddMenuForm() {
        // Method ini untuk menampilkan page tambah menu restoran
        VBox layout = new VBox(10);

        // Meminta nama restoran dari user
        layout.setAlignment(Pos.CENTER);
        Label namaRestaurantLabel = new Label("Restaurant Name:");
        TextField namaRestaurantField = new TextField();
        namaRestaurantField.setPromptText("Nama Restoran");

        // Meminta nama menu yang ingin dimasukkan ke restoran
        Label menuItemLabel = new Label("Menu Item Name:");
        TextField menuItemField = new TextField();
        menuItemField.setPromptText("Menu Item");

        // Meminta harga menu
        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        // Button yang akan inisiasi method handleTambahMenuRestoran untuk menambah menu
        Button addMenuButton = new Button("Add Menu Item");
        addMenuButton.setOnAction(e -> handleTambahMenuRestoran(DepeFood.getRestaurantByName(namaRestaurantField.getText()),
                menuItemField.getText(), Double.parseDouble(priceField.getText())));

        // Button untuk kembali ke admin menu
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setOnAction(e -> stage.setScene(mainApp.getScene("Admin Menu")));

        layout.getChildren().addAll(namaRestaurantLabel, namaRestaurantField, menuItemLabel, menuItemField,
                priceLabel, priceField, addMenuButton, kembaliButton);

        return new Scene(layout, 400, 600);
    }
    
    
    private Scene createViewRestaurantsForm() {
        // Method ini untuk menampilkan page daftar restoran
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        // Meminta nama restoran dari user
        Label restaurantNameLabel = new Label("Restaurant Name:");
        TextField restaurantNameField = new TextField();
        restaurantNameField.setPromptText("Nama Restoran");

        // Button yang akan menampilkan daftar menu di restoran
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            Restaurant restaurant = DepeFood.findRestaurant(restaurantNameField.getText());
            if (restaurant != null) {
                List<String> menuItems = new ArrayList<>();
                for (String item : restaurant.printMenu().split("\n")) {
                    menuItems.add(item);
                }
                menuItemsListView.setItems(FXCollections.observableArrayList(menuItems));
            } else { // Keluarkan error message jika restaurant tidak ditemukan
                showAlert("Error", "Error", "Restaurant not found!", Alert.AlertType.ERROR);
                menuItemsListView.getItems().clear(); // Clear the ListView if the restaurant is not found
            }
        });

        Label menuLabel = new Label("Menu:");

        // Button untuk kembali ke admin menu
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setOnAction(e -> stage.setScene(mainApp.getScene("Admin Menu")));

        layout.getChildren().addAll(restaurantNameLabel, restaurantNameField, searchButton, menuLabel, menuItemsListView, kembaliButton);

        return new Scene(layout, 400, 600);
    }


    private void handleTambahRestoran(String nama) {
        // Validasi isian nama Restoran
        String validName = DepeFood.getValidRestaurantName(nama);
        if (!validName.contains("tidak valid") && !validName.contains("sudah pernah terdaftar")) {
            DepeFood.handleTambahRestoran(validName);
            showAlert("Message", "Message", "Restaurant successfully registered!", Alert.AlertType.INFORMATION);
        } else if (validName.contains("sudah pernah terdaftar")){
            showAlert("Error", "Error", "Restaurant is already registered!", Alert.AlertType.ERROR);
        } else {
            showAlert("Error", "Error", "Invalid Restaurant Name!", Alert.AlertType.ERROR);
        }
    }

    private void handleTambahMenuRestoran(Restaurant restaurant, String itemName, double price) {
        //  Validasi isian menu Restoran
        if (restaurant != null) {
            DepeFood.handleTambahMenuRestoran(restaurant, itemName, price);
            showAlert("Message", "Message", "Menu item added successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Error", "Restaurant not found!", Alert.AlertType.ERROR);
        }
    }

    // Override getScene() yang ada di MemberMenu
    @Override
    public Scene getScene() {
        return this.scene;
    }
}
