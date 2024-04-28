package assignments.assignment3;

import assignments.assignment2.Order;

import java.util.ArrayList;

import assignments.assignment3.payment.DepeFoodPaymentSystem;

public class User {
    // Atribut class User
    private String nama; 
    private String nomorTelepon; 
    private String email; 
    private String lokasi; 
    public String role;
    private ArrayList<Order> orderHistory;
    private DepeFoodPaymentSystem payment;
    private long saldo;

    // Constructor class User
    public User(String nama, String nomorTelepon, String email, String lokasi, String role, DepeFoodPaymentSystem payment, long saldo){
       this.nama = nama;
       this.nomorTelepon = nomorTelepon;
       this.email = email;
       this.lokasi = lokasi;
       this.role = role;
       this.orderHistory = new ArrayList<>();
       this.payment = payment;
       this.saldo = saldo;
    }
    
    @Override
    public String toString() {
        return "Selamat Datang " + getNama() + "!";
    }

    // Getter dan Setter
    public String getNama() {
        return this.nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getNomorTelepon() {
        return this.nomorTelepon;
    }
    
    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getLokasi() {
        return this.lokasi;
    }
    
    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
    
    public String getRole() {
        return this.role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public ArrayList<Order> getOrderHistory() {
        return this.orderHistory;
    }
    
    public void setOrderHistory(ArrayList<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public DepeFoodPaymentSystem getPayment() {
        return this.payment;
    }

    public void setPayment(DepeFoodPaymentSystem payment) {
        this.payment = payment;
    }
    public long getSaldo() {
        return this.saldo;
    }

    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }
}
