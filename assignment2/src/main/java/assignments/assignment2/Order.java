package assignments.assignment2;

import java.util.ArrayList;
import java.util.Arrays;

public class Order {
    // Atribut class Order
    private String orderId;
    private String tanggalPemesanan;
    private int biayaOngkosKirim;
    private Restaurant restaurant;
    private ArrayList<Menu> items;
    private boolean orderFinished;
    
    // Constructor class Order
    public Order(String orderId, String tanggal, int ongkir, Restaurant resto, Menu[] items){
        this.orderId = orderId;
        this.tanggalPemesanan = tanggal;
        this.biayaOngkosKirim = ongkir;
        this.restaurant = resto;
        this.items = new ArrayList<>(Arrays.asList(items));
        this.orderFinished = false;
    }

    // Method untuk menghitung total biaya
    public double calculateTotal() {
        double total = 0;
        for (Menu item : items) {
            total += item.getHarga();
        }
        return total;
    }

    // Method untuk mendapatkan status
    public String getStatus() {
        if (orderFinished) {
            return "Selesai";
        } else {
            return "Not Finished";
        }
    }

    // Getter dan Setter
    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTanggalPemesanan() {
        return this.tanggalPemesanan;
    }

    public void setTanggalPemesanan(String tanggalPemesanan) {
        this.tanggalPemesanan = tanggalPemesanan;
    }

    public int getBiayaOngkosKirim() {
        return this.biayaOngkosKirim;
    }

    public void setBiayaOngkosKirim(int biayaOngkosKirim) {
        this.biayaOngkosKirim = biayaOngkosKirim;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ArrayList<Menu> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<Menu> items) {
        this.items = items;
    }

    public boolean isOrderFinished() {
        return this.orderFinished;
    }

    public void setOrderFinished(boolean orderFinished) {
        this.orderFinished = orderFinished;
    }
}
