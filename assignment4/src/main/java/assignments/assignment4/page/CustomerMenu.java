package assignments.assignment4.page;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import assignments.assignment3.DepeFood;
import assignments.assignment3.Menu;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;
import assignments.assignment4.components.BillPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerMenu extends MemberMenu{
    private Stage stage;
    private Scene scene;
    private Scene addOrderScene;
    private Scene printBillScene;
    private Scene payBillScene;
    private Scene cekSaldoScene;
    private BillPrinter billPrinter; // Instance of BillPrinter
    private ComboBox<String> restaurantComboBox = new ComboBox<>();
    private static Label label = new Label();
    private MainApp mainApp;
    private List<Restaurant> restoList = new ArrayList<>();
    private User user;


    public CustomerMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user; // Store the user
        this.scene = createBaseMenu();
        this.addOrderScene = createTambahPesananForm();
        this.billPrinter = new BillPrinter(stage, mainApp, this.user); // Pass user to BillPrinter constructor
        this.printBillScene = createBillPrinter();
        this.payBillScene = createBayarBillForm();
        this.cekSaldoScene = createCekSaldoScene();
        // Save scene createBillPrinter() untuk kembali
        mainApp.addScene("Print Bill", this.printBillScene);
    }

    @Override
    public Scene createBaseMenu() {
        // Method ini untuk menampilkan menu untuk Customer
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        // Button untuk membuat pesanan
        Button buatPesananButton = new Button("Buat Pesanan");
        buatPesananButton.setOnAction(e -> stage.setScene(createTambahPesananForm()));

        // Button untuk mencetak bill
        Button cetakBillButton = new Button("Cetak Bill");
        cetakBillButton.setOnAction(e -> stage.setScene(createBillPrinter()));

        // Button untuk membayar bill
        Button bayarBillButton  = new Button("Bayar Bill");
        bayarBillButton.setOnAction(e -> stage.setScene(createBayarBillForm()));

        // Button untuk cek saldo user
        Button cekSaldoButton = new Button("Cek Saldo");
        cekSaldoButton.setOnAction(e -> stage.setScene(createCekSaldoScene()));

        // Button untuk kembali ke login form
        Button logOutButton = new Button("Log Out");
        logOutButton.setOnAction(e -> mainApp.logout());

        menuLayout.getChildren().addAll(buatPesananButton, cetakBillButton, bayarBillButton, cekSaldoButton, logOutButton);

        return new Scene(menuLayout, 400, 600);
    }

    private Scene createTambahPesananForm() {
        // Method ini untuk menampilkan page tambah pesanan
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        // Membuat choice box untuk restoran
        Label restaurantLabel = new Label("Restaurant:");
        ComboBox<String> restaurantComboBox = new ComboBox<>();
        restaurantComboBox.setPromptText("Select a Restaurant");
        for (Restaurant restaurant : DepeFood.getRestoList()) {
            restaurantComboBox.getItems().add(restaurant.getNama());
        }

        // Meminta tanggal dari user
        Label dateLabel = new Label("Date (DD/MM/YYYY):");
        TextField dateField = new TextField();

        // Button yang akan menampilkan dafar menu dari restoran
        Button menuButton = new Button("Menu");
        ListView<String> menuItemsListView = new ListView<>();
        // Set agar daftar makanan di menu bisa ditekan lebih dari 1 oleh user
        menuItemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        menuButton.setOnAction(e -> {
            Restaurant selectedRestaurant = DepeFood.getRestaurantByName(restaurantComboBox.getValue());
            if (selectedRestaurant != null) {
                menuItemsListView.getItems().clear();
                for (Menu menu : selectedRestaurant.getMenu()) {
                    menuItemsListView.getItems().add(menu.getNamaMakanan());
                }
            }
        });

        // Tambahkan makanan yang dipilih oleh user ke list menuItems
        List<String> menuItems = new ArrayList<>();
        menuItemsListView.setOnMouseClicked(e -> {
            ObservableList<String> selectedItems = menuItemsListView.getSelectionModel().getSelectedItems();
            for (String item : selectedItems) {
                if (!menuItems.contains(item)) {
                    menuItems.add(item);
                }
            }
        });

        // Button untuk inisiasi handleBuatPesanan() yang akan membuat pesanan
        Button buatPesananButton = new Button("Buat Pesanan");
        buatPesananButton.setOnAction(e -> handleBuatPesanan(restaurantComboBox.getValue(), dateField.getText().trim(), menuItems));

        // Button untuk kembali ke customer menu
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setOnAction(e -> stage.setScene(mainApp.getScene("Customer Menu")));

        menuLayout.getChildren().addAll(restaurantLabel, restaurantComboBox, dateLabel, dateField, menuButton, menuItemsListView, buatPesananButton, kembaliButton);

        return new Scene(menuLayout, 400, 650);
    }

    private Scene createBillPrinter(){
        // Method untuk mencetak bill
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        // Meminta orderId dari user
        TextField orderIdField = new TextField();
        orderIdField.setPromptText("Order ID");

        // Pindah ke scene createBillPrinterForm() di BillPrinter
        Button printButton = new Button("Print Bill");
        printButton.setOnAction(e -> {
            billPrinter.setOrderId(orderIdField.getText().trim());
            stage.setScene(billPrinter.getScene());
        });

        // Button untuk kembali ke customer menu
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setOnAction(e -> stage.setScene(mainApp.getScene("Customer Menu")));

        menuLayout.getChildren().addAll(orderIdField, printButton, kembaliButton);

        return new Scene(menuLayout, 400, 600);
    }

    private Scene createBayarBillForm() {
        // Method ini untuk menampilkan page bayar bill
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        // Mendapatkan orderId dari user
        TextField orderIdField = new TextField();
        orderIdField.setPromptText("Order ID");

        // ChoiceBox untuk memilih opsi pembayaran
        ComboBox<String> paymentMethodComboBox = new ComboBox<>();
        paymentMethodComboBox.setPromptText("Pilih Opsi Pembayaran");
        paymentMethodComboBox.getItems().addAll("Credit Card", "Debit");

        // Button untuk inisiasi handleBayarBill() yang akan melakukan pembayaran bill
        Button bayarButton = new Button("Bayar");
        bayarButton.setOnAction(e -> {
            int pemilihanPembayaran = paymentMethodComboBox.getSelectionModel().getSelectedIndex();
            handleBayarBill(orderIdField.getText().trim(), pemilihanPembayaran);
        });

        // Button untuk kembali ke customer menu
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setOnAction(e -> stage.setScene(mainApp.getScene("Customer Menu")));

        menuLayout.getChildren().addAll(orderIdField, paymentMethodComboBox, bayarButton, kembaliButton);

        return new Scene(menuLayout, 400,600);
    }


    private Scene createCekSaldoScene() {
        // Method ini untuk menampilkan page cetak saldo
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);

        // Mencetak saldo user
        Label nameLabel = new Label(this.user.getNama());
        Label saldoLabel = new Label("Saldo: Rp " + this.user.getSaldo());

        // Button untuk kembali ke customer menu
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setOnAction(e -> stage.setScene(mainApp.getScene("Customer Menu")));

        menuLayout.getChildren().addAll(nameLabel, saldoLabel, kembaliButton);

        return new Scene(menuLayout, 400,600);
    }

    private void handleBuatPesanan(String namaRestoran, String tanggalPemesanan, List<String> menuItems) {
        // Validasi isian pesanan
        try {
            String orderId = DepeFood.handleBuatPesanan(namaRestoran, tanggalPemesanan, menuItems);
            showAlert("Message", "Message", "Order dengan ID " + orderId + " berhasil ditambahkan", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
                showAlert("Error", "Error", e.getMessage(), AlertType.ERROR);
        }
    }

    private void handleBayarBill(String orderID, int pilihanPembayaran) {
        // Validasi pembayaran
        try {
            String pilihan;
            if (pilihanPembayaran == 0) {
                pilihan = "Credit Card";
            } else {
                pilihan = "Debit";
            }
            long saldoUser = user.getSaldo();
            DepeFood.handleBayarBill(orderID, pilihan);

            long biayaDenganTransaksi = saldoUser - user.getSaldo();

            // Mendapatkan total biaya sebelum ditambah biaya transaksi
            long biayaTanpaTransaksi = biayaDenganTransaksi * 100 / 102;
            // Mendapatkan biaya transaksi
            long biayaTransaksi = biayaDenganTransaksi * 2 / 102;

            // Tunjukkan pesan berhasil membayar bill
            showAlert("Message", null, "Berhasil Membayar Bill sebesar Rp " + biayaTanpaTransaksi +
                    " dengan biaya transaksi sebesar Rp " + biayaTransaksi, Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Error", e.getMessage(), AlertType.ERROR);
        }
    }

    // Override getScene() yang ada di MemberMenu
    @Override
    public Scene getScene() {
        return this.scene;
    }
}