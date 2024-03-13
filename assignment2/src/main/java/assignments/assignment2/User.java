package assignments.assignment2;

import java.util.ArrayList;

public class User {
    // Atribut class User
    private String nama; 
    private String nomorTelepon; 
    private String email; 
    private String lokasi; 
    private String role; 
    private ArrayList<Order> orderHistory;

    // Constructor class User
    public User(String nama, String nomorTelepon, String email, String lokasi, String role){
       this.nama = nama;
       this.nomorTelepon = nomorTelepon;
       this.email = email;
       this.lokasi = lokasi;
       this.role = role;
       this.orderHistory = new ArrayList<>();
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
}
