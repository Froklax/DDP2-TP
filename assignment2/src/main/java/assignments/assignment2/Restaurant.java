package assignments.assignment2;
import java.util.ArrayList;

public class Restaurant {
    // Atribut class Restaurant
    private String nama; 
    private ArrayList<Menu> menu; 

    // Constructor class Restaurant
    public Restaurant(String nama){
        this.nama = nama;
        this.menu = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Restaurant " + getNamaRestoran() + " Berhasil terdaftar.";
    }

    // Getter dan Setter
    public String getNamaRestoran() {
        return this.nama;
    }

    public void setNamaRestoran(String nama) {
        this.nama = nama;
    }

    public ArrayList<Menu> getMenu() {
        return this.menu;
    }

    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }
}
