package assignments.assignment4.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import assignments.assignment3.DepeFood;
import assignments.assignment3.Menu;
import assignments.assignment3.Order;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;
import java.util.ArrayList;


public class BillPrinter {
    private Stage stage;
    private MainApp mainApp;
    private User user;
    private String orderId;
    private boolean orderValid;

    public BillPrinter(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
    }

    private Scene createBillPrinterForm(){
        orderValid = true;
        // Layout utama menggunakan VBox
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        // Validasi orderId dan cetak bill
        printBill(layout, this.orderId);

        if (!orderValid) {
            // Jika orderId tidak ditemukan, kembali ke scene createBillPrinter()
            return mainApp.getScene("Print Bill");
        }

        // Button untuk kembali ke scene createBillPrinter()
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setOnAction(e -> stage.setScene(mainApp.getScene("Print Bill")));

        layout.getChildren().add(kembaliButton);

        // Buat dan kembalikan Scene
        return new Scene(layout, 500, 500);
    }

    private void printBill(VBox layout, String orderId) {
        // Validasi Order Id
        Order order = DepeFood.findUserOrderById(orderId);
        if (order != null) {

            // Cetak bill menggunakan label
            Label billLabel = new Label("Bill");
            Label orderIdLabel = new Label("Order ID: " + orderId);
            Label tanggalPemesananLabel = new Label("Tanggal Pemesanan: " + order.getTanggal());
            Label restaurantLabel = new Label("Restaurant: " + order.getRestaurant().getNama());
            Label lokasiPengirimanLabel = new Label("Lokasi Pengiriman: " + this.user.getLokasi());
            Label biayaOngkosKirimLabel = new Label("Biaya Ongkos Kirim: Rp " + order.getOngkir());

            // Mendapatkan order status
            String orderStatus;
            if (order.getOrderFinished()) {
                orderStatus = "Selesai";
            } else {
                orderStatus = "Not Finished";
            }
            Label orderStatusLabel = new Label("Status Pengiriman: " + orderStatus);

            Label pesananLabel = new Label("Pesanan:");

            layout.getChildren().addAll(billLabel, orderIdLabel, tanggalPemesananLabel, restaurantLabel,
                    lokasiPengirimanLabel, orderStatusLabel, pesananLabel);

            // Mengambil pesanan user
            for (Menu item : order.getItems()) {
                String pesanan = "- " + item.getNamaMakanan() + " " + (int) item.getHarga();
                Label foodLabel = new Label(pesanan);
                layout.getChildren().add(foodLabel);
            }

            Label totalBiayaLabel = new Label("Total Biaya: Rp " + (int) order.getTotalHarga());

            layout.getChildren().addAll(biayaOngkosKirimLabel, totalBiayaLabel);
        } else {
            // Jika orderId tidak ditemukan, tampilkan error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("OrderId is not found!");
            alert.showAndWait();
            orderValid = false;
        }
    }

    public Scene getScene() {
        return this.createBillPrinterForm();
    }

    // Class ini opsional
    public class MenuItem {
        private final StringProperty itemName;
        private final StringProperty price;

        public MenuItem(String itemName, String price) {
            this.itemName = new SimpleStringProperty(itemName);
            this.price = new SimpleStringProperty(price);
        }

        public StringProperty itemNameProperty() {
            return itemName;
        }

        public StringProperty priceProperty() {
            return price;
        }

        public String getItemName() {
            return itemName.get();
        }

        public String getPrice() {
            return price.get();
        }
    }

    // Add setter untuk orderId
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
