package assignments.assignment3.systemCLI;
import assignments.assignment2.Menu;
import assignments.assignment2.Order;
import assignments.assignment2.Restaurant;
import assignments.assignment3.User;
import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DebitPayment;

import static assignments.assignment1.OrderGenerator.*;
import assignments.assignment3.MainMenu;

import java.util.ArrayList;
import java.util.Scanner;

// Extend abstract class
public class CustomerSystemCLI extends UserSystemCLI {
    // Override dari abstract class
    @Override
    public boolean handleMenu(int choice, User userLoggedIn){
        switch(choice){
            case 1 -> handleBuatPesanan(userLoggedIn);
            case 2 -> handleCetakBill(userLoggedIn);
            case 3 -> handleLihatMenu();
            case 4 -> handleBayarBill(userLoggedIn);
            case 5 -> handleCekSaldo(userLoggedIn);
            case 6 -> {
                return false;
            }
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }

    // Override dari abstract class
    @Override
    public void displayMenu() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Bayar Bill");
        System.out.println("5. Cek Saldo");
        System.out.println("6. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    void handleBuatPesanan(User userLoggedIn){
        System.out.println("--------------Buat Pesanan----------------");
        while (true) {
            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine();

            // Mengecek apakah restoran terdaftar pada sistem
            Restaurant restaurant = null;
            for (Restaurant resto : MainMenu.restoList) {
                if (resto.getNamaRestoran().equalsIgnoreCase(namaRestoran)) {
                    restaurant = resto;
                    break;
                }
            }

            // Jika restoran tidak ditemukan kembali ke awal input
            if (restaurant == null) {
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }

            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            String tanggalOrder = input.nextLine();
            // Mengecek format tanggal apakah sudah benar
            if (tanggalOrder.length() != 10 || tanggalOrder.charAt(2) != '/' || tanggalOrder.charAt(5) != '/') {
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)!\n");
                continue;
            }

            boolean tanggalValid = true;
            // Memisahkan tanggal menjadi hari, bulan, dan tahun
            String[] bagianTanggal = tanggalOrder.split("/");
            // Mengecek apakah setiap bagian hanya berisi angka
            for (String bagian : bagianTanggal) {
                for (char karakter : bagian.toCharArray()) {
                    if (!Character.isDigit(karakter)) {
                        tanggalValid = false;
                        break;
                    }
                }
            }

            if (!tanggalValid) {
                System.out.println("Masukkan tanggal dengan angka (tidak ada huruf dan simbol)!\n");
                continue;
            }

            System.out.print("Jumlah Pesanan: ");
            int jumlahPesanan = Integer.parseInt(input.nextLine());

            // List untuk menyimpan pesanan
            ArrayList<Menu> items = new ArrayList<>();
            System.out.println("Order: ");
            boolean menuTersedia = true;
            for (int i = 0; i < jumlahPesanan; i++) {
                String namaMenu = input.nextLine();
                Menu menu = null;

                // Mengecek apakah menu tersedia di restoran ini
                for (Menu makanan : restaurant.getMenu()) {
                    if (makanan.getNamaMakanan().equals(namaMenu)) {
                        menu = makanan;
                        break;
                    }
                }
                // Jika menu ada tambahkan ke daftar pesanan
                if (menu != null) {
                    items.add(menu);
                } else {
                    System.out.println("Mohon memesan menu yang tersedia di restoran!\n");
                    menuTersedia = false;
                    break;
                }
            }

            if (!menuTersedia) {
                continue;
            }

            // Mendapatkan nomor telpon dari user sekarang
            String noTelepon = userLoggedIn.getNomorTelepon();
            // Generate OrderId
            String orderId = generateOrderID(namaRestoran, tanggalOrder, noTelepon);
            // Membuat objek order dan menambahkannya ke orderHistory di user
            Order order = new Order(orderId, tanggalOrder, 0, restaurant, items.toArray(new Menu[0]));
            userLoggedIn.getOrderHistory().add(order);
            System.out.println("Pesanan dengan ID " + orderId + " diterima!");
            break;
        }
    }

    void handleCetakBill(User userLoggedIn){
        // Method untuk handle ketika customer ingin cetak bill
        System.out.println("--------------Cetak Bill----------------");
        while (true) {
            System.out.print("Masukkan Order ID: ");
            String orderId = input.nextLine();
            System.out.println();

            Order order = null;
            // Mengecek apakah orderId sudah terdaftar dalam sistem
            for (Order classOrder : userLoggedIn.getOrderHistory()) {
                if (classOrder.getOrderId().equals(orderId)) {
                    order = classOrder;
                    break;
                }
            }

            if (order != null) {
                String lokasi = userLoggedIn.getLokasi();
                String status = order.getStatus();
                String pesanan = "";
                // Mengambil pesanan user
                for (Menu item : order.getItems()) {
                    pesanan += "- " + item.getNamaMakanan() + " " + (int) item.getHarga() + "\n";
                }

                // Mendapatkan nama restoran dan biaya pesanan
                String namaRestoran = order.getRestaurant().getNamaRestoran();
                double biayaPesanan = order.calculateTotal();


                int biayaOngkir = 0;
                if (lokasi.equalsIgnoreCase("P")) {
                    biayaOngkir = 10000;
                } else if (lokasi.equalsIgnoreCase("U")) {
                    biayaOngkir = 20000;
                } else if (lokasi.equalsIgnoreCase("T")) {
                    biayaOngkir = 35000;
                } else if (lokasi.equalsIgnoreCase("S")) {
                    biayaOngkir = 40000;
                } else if (lokasi.equalsIgnoreCase("B")) {
                    biayaOngkir = 60000;
                }

                order.setBiayaOngkosKirim(biayaOngkir);

                // Generate bill
                String bill = generateBill(orderId, namaRestoran, lokasi, status, pesanan, biayaPesanan);
                System.out.println(bill);
                break;
            } else {
                System.out.println("Order ID tidak dapat ditemukan.\n");
            }
        }
    }

    void handleLihatMenu(){
        // Method untuk handle ketika customer ingin melihat menu
        System.out.println("--------------Lihat Menu----------------");
        System.out.print("Nama Restoran: ");
        String namaRestoran = input.nextLine();

        Restaurant restaurant = null;
        // Mengecek apakah restoran sudah pernah terdaftar dalam sistem
        for (Restaurant resto : MainMenu.restoList) {
            if (resto.getNamaRestoran().equalsIgnoreCase(namaRestoran)) {
                restaurant = resto;
                break;
            }
        }

        if (restaurant != null) {
            System.out.println("Menu:");
            ArrayList<Menu> menuItems = restaurant.getMenu();
            // Mengurutkan menu berdasarkan harga menggunakan bubble sort
            for (int i = 0; i < menuItems.size() - 1; i++) {
                for (int j = 0; j < menuItems.size() - i - 1; j++) {
                    if (menuItems.get(j).getHarga() > menuItems.get(j + 1).getHarga()) {
                        Menu menuSementara = menuItems.get(j);
                        menuItems.set(j, menuItems.get(j + 1));
                        menuItems.set(j + 1, menuSementara);
                    }
                }
            }
            // Mencetak menu restoran
            for (int i = 0; i < menuItems.size(); i++) {
                Menu item = menuItems.get(i);
                System.out.println((i + 1) + ". " + item.getNamaMakanan() + " " + (int) item.getHarga());
            }
        } else {
            System.out.println("Restoran tidak terdaftar pada sistem.");
        }
    }

    void handleBayarBill(User userLoggedIn){
        // Method untuk handle ketika customer ingin melihat bayar bill
        System.out.println("--------------Bayar Bill----------------");
        while (true) {
            System.out.print("Masukkan Order ID: ");
            String orderId = input.nextLine();
            System.out.println();

            Order order = null;
            // Mengecek apakah orderId sudah terdaftar dalam sistem
            for (Order classOrder : userLoggedIn.getOrderHistory()) {
                if (classOrder.getOrderId().equals(orderId)) {
                    order = classOrder;
                    break;
                }
            }

            if (order != null) {
                // Jika status order sudah selesai
                if (order.getStatus().equals("Selesai")) {
                    System.out.println("Pesanan dengan ID ini sudah lunas!");
                    return;
                }

                String pesanan = "";
                // Mengambil pesanan user
                for (Menu item : order.getItems()) {
                    pesanan += "- " + item.getNamaMakanan() + " " + (int) item.getHarga() + "\n";
                }

                // Mencetak bill
                String bill = generateBill(orderId, order.getRestaurant().getNamaRestoran(), userLoggedIn.getLokasi(), order.getStatus(), pesanan, order.calculateTotal());
                System.out.println(bill);

                // Menghitung total biaya pesanan
                long totalBiaya = (long) order.calculateTotal() + order.getBiayaOngkosKirim();

                // Meminta opsi pembayaran kepada user
                System.out.println("\nOpsi Pembayaran:");
                System.out.println("1. Credit Card");
                System.out.println("2. Debit");
                System.out.print("Pilihan Metode Pembayaran: ");
                int pilihanPembayaran = input.nextInt();
                input.nextLine();

                // Mengecek apakah metode pembayaran valid
                if ((pilihanPembayaran == 1 && userLoggedIn.getPayment() instanceof CreditCardPayment) || (pilihanPembayaran == 2 && userLoggedIn.getPayment() instanceof DebitPayment)) {
                    // Jika harga pesanan melebihi saldo debit
                    if (pilihanPembayaran == 2 && userLoggedIn.getPayment().processPayment(totalBiaya) > userLoggedIn.getSaldo()) {
                        System.out.println("Saldo tidak mencukupi mohon menggunakan metode pembayaran yang lain\n");
                        continue;
                    }
                    // Menghitung biaya transaksi
                    long biayaTransaksi = 0;
                    if (userLoggedIn.getPayment() instanceof CreditCardPayment) {
                        biayaTransaksi = userLoggedIn.getPayment().processPayment(totalBiaya) - totalBiaya;
                    }
                    // Menghitung dan set saldo sekarang user
                    long saldoSekarang = userLoggedIn.getSaldo() - userLoggedIn.getPayment().processPayment(totalBiaya);
                    userLoggedIn.setSaldo(saldoSekarang);

                    // Menghitung dan set saldo sekarang restaurant
                    long saldoRestaurant = order.getRestaurant().getSaldo() + userLoggedIn.getPayment().processPayment(totalBiaya);
                    order.getRestaurant().setSaldo(saldoRestaurant);

                    System.out.println("\nBerhasil Membayar Bill sebesar Rp " + totalBiaya + " dengan biaya transaksi sebesar Rp " + biayaTransaksi + ".");
                    // Set status pesanan menjadi selesai
                    order.setOrderFinished(true);
                    break;
                } else {
                    System.out.println("User belum memiliki metode pembayaran ini!\n");
                }
            } else {
                System.out.println("Order ID tidak dapat ditemukan.\n");
            }
        }
    }

    void handleUpdateStatusPesanan(User userLoggedIn){
        // Method untuk handle ketika customer ingin update status pesanan
        System.out.print("Masukkan Order ID: ");
        String orderId = input.nextLine();

        Order order = null;
        // Mengecek apakah orderId pernah terdaftar di sistem
        for (Order classOrder : userLoggedIn.getOrderHistory()) {
            if (classOrder.getOrderId().equals(orderId)) {
                order = classOrder;
                break;
            }
        }

        if (order != null) {
            System.out.print("Status: ");
            String status = input.nextLine();
            // Mengupdate status tergantung status "Selesai" atau "Not Finished"
            if ("Selesai".equalsIgnoreCase(status) && !order.isOrderFinished()) {
                order.setOrderFinished(true);
                System.out.println("Status pesanan dengan ID " + orderId + " berhasil diupdate!");
            } else {
                System.out.println("Status pesanan dengan ID " + orderId + " tidak berhasil diupdate!");
            }
        } else {
            System.out.println("Order ID tidak dapat ditemukan.");
        }
    }

    void handleCekSaldo(User userLoggedIn){
        // Method untuk handle ketika customer ingin mengecek saldo yang dimiliki
        System.out.println("\nSisa saldo sebesar Rp " + userLoggedIn.getSaldo());
    }
}
