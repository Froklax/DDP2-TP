package assignments.assignment3.systemCLI;

import assignments.assignment2.Menu;
import assignments.assignment2.Restaurant;
import assignments.assignment3.MainMenu;
import assignments.assignment3.User;

import java.util.Scanner;

// Extends Abstract UserSystemCLI
public class AdminSystemCLI extends UserSystemCLI {
    // Override dari abstract class
    @Override
    public boolean handleMenu(int command, User userLoggedIn){
        switch(command){
            case 1 -> handleTambahRestoran();
            case 2 -> handleHapusRestoran();
            case 3 -> {return false;}
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }

    // Override dari abstract class
    @Override
    public void displayMenu() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    protected void handleTambahRestoran(){
        // Method untuk handle ketika admin ingin tambah restoran
        System.out.println("--------------Tambah Restoran----------------");
        while (true) {
            System.out.print("Nama: ");
            String namaRestoran = input.nextLine();

            // Mengecek apakah nama restoran minimal 4 karakter
            if(namaRestoran.length() < 4) {
                System.out.println("Nama Restoran tidak valid!\n");
                continue;
            }

            // Mengecek apakah nama restoran hanya terdiri dari spasi
            if(namaRestoran.isBlank()) {
                System.out.println("Nama Restoran tidak valid!\n");
                continue;
            }

            // Mengecek apakah nama restoran memiliki spasi berlebihan
            if(namaRestoran.strip().contains("  ")) {
                System.out.println("Nama Restoran tidak valid!\n");
                continue;
            }

            boolean restoTerdaftar = false;
            for (Restaurant restaurant : MainMenu.restoList) {
                // Mengecek apakah restaurant sudah ada di daftar restaurant
                if (restaurant.getNamaRestoran().equals(namaRestoran)) {
                    System.out.println("Restoran dengan nama " + namaRestoran + " sudah pernah terdaftar. Mohon masukkan nama yang berbeda!\n");
                    restoTerdaftar = true;
                    break;
                }
            }

            // Jika restaurant sudah ada dalam daftar maka kembali ke awal input
            if (restoTerdaftar) {
                continue;
            }

            // Membuat new restaurant tapi belum add ke list dulu
            Restaurant newRestaurant = new Restaurant(namaRestoran, 0);

            System.out.print("Jumlah Makanan: ");
            int jumlahMakanan = Integer.parseInt(input.nextLine());

            boolean hargaInvalid = false;
            if (jumlahMakanan > -1) {
                for (int i = 0; i < jumlahMakanan; i++) {
                    String inputMakanan = input.nextLine();
                    // Membagi input menjadi nama makanan dan harga
                    String namaMakanan = inputMakanan.substring(0, inputMakanan.lastIndexOf(" "));
                    String harga = inputMakanan.substring(inputMakanan.lastIndexOf(" ") + 1);

                    // Mengecek apakah harga adalah bilangan bulat positif
                    boolean allDigits = true;
                    for (int j = 0; j < harga.length(); j++) {
                        // Jika karakter saat ini bukan digit, maka allDigits menjadi false dan hentikan looping.
                        if (!Character.isDigit(harga.charAt(j))) {
                            allDigits = false;
                            break;
                        }
                    }

                    // Jika bukan bilangan bulat positif
                    if (!allDigits) {
                        System.out.println("Harga menu harus bilangan bulat!\n");
                        hargaInvalid = true;
                        break;
                    } else if (Double.parseDouble(harga) < 0) {
                        System.out.println("Harga menu harus bilangan bulat!\n");
                        hargaInvalid = true;
                        break;
                    }

                    // Menambahkan menu makanan ke restoran
                    Menu menuMakanan = new Menu(namaMakanan, Double.parseDouble(harga));
                    newRestaurant.getMenu().add(menuMakanan);

                }

                // Jika harga bukan bilangan bulat positif maka kembali ke awal input
                if (hargaInvalid) {
                    continue;
                }

                // Menambahkan restaurant ke daftar restaurant
                MainMenu.restoList.add(newRestaurant);
                System.out.println(newRestaurant);
                break;

            } else {
                System.out.println("Jumlah makanan tidak boleh negatif!\n");
            }
        }
    }

    protected void handleHapusRestoran(){
        // Method untuk handle ketika admin ingin hapus restoran
        System.out.println("--------------Hapus Restoran----------------");
        while (true) {
            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine();

            // Loop daftar restoran
            for (int i = 0; i < MainMenu.restoList.size(); i++) {
                if (MainMenu.restoList.get(i).getNamaRestoran().equalsIgnoreCase(namaRestoran)) {
                    // Jika restoran ditemukan, hapus dari daftar restoran
                    MainMenu.restoList.remove(i);
                    System.out.println("Restoran berhasil dihapus.");
                    return;
                }
            }

            // Jika restoran tidak ditemukan, balik ke input awal loop
            System.out.println("Restoran tidak terdaftar pada sistem.\n");
        }
    }
}
