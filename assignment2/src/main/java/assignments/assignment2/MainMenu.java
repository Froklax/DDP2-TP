package assignments.assignment2;

import java.util.ArrayList;
import java.util.Scanner;
/*
* Karena terdapat error saat import dari assignments.assignment1,
* maka saya menaruh OrderGenerator.java di assignments.assignment2
*/
import static assignments.assignment2.OrderGenerator.*;

public class MainMenu {
    private static final Scanner input = new Scanner(System.in);
    private static ArrayList<Restaurant> restoList;
    private static ArrayList<User> userList;

    public static void main(String[] args) {
        boolean programRunning = true;
        restoList = new ArrayList<Restaurant>();
        while(programRunning){
            printHeader();
            startMenu();
            int command = input.nextInt();
            input.nextLine();

            if(command == 1){
                System.out.println("\nSilakan Login:");
                System.out.print("Nama: ");
                String nama = input.nextLine();
                System.out.print("Nomor Telepon: ");
                String noTelp = input.nextLine();

                // Validasi input login
                initUser();

                User userLoggedIn = getUser(nama, noTelp);
        
                boolean isLoggedIn;
                if (userLoggedIn != null) {
                    System.out.println(userLoggedIn);
                    isLoggedIn = true;
    
                } else {
                    System.out.println("Pengguna dengan data tersebut tidak ditemukan!");
                    continue;
                }

                if(userLoggedIn.getRole().equals("Customer")){
                    while (isLoggedIn){
                        menuCustomer();
                        int commandCust = input.nextInt();
                        input.nextLine();

                        switch(commandCust){
                            case 1 -> handleBuatPesanan(userLoggedIn);
                            case 2 -> handleCetakBill(userLoggedIn);
                            case 3 -> handleLihatMenu();
                            case 4 -> handleUpdateStatusPesanan(userLoggedIn);
                            case 5 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                }else{
                    while (isLoggedIn){
                        menuAdmin();
                        int commandAdmin = input.nextInt();
                        input.nextLine();

                        switch(commandAdmin){
                            case 1 -> handleTambahRestoran();
                            case 2 -> handleHapusRestoran();
                            case 3 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                }
            }else if(command == 2){
                programRunning = false;
            }else{
                System.out.println("Perintah tidak diketahui, silakan periksa kembali.");
            }
        }
        System.out.println("\nTerima kasih telah menggunakan DepeFood ^___^");
    }

    public static User getUser(String nama, String nomorTelepon){
        // Method untuk mendapat user dari userList
        for (User user : userList) {
            if (user.getNama().equals(nama) && user.getNomorTelepon().equals(nomorTelepon)) {
                return user;
            }
        }
        return null;
    }

    public static void handleBuatPesanan(User userLoggedIn){
        // Method untuk handle ketika customer membuat pesanan
        while (true) {
            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine();

            // Mengecek apakah restoran terdaftar pada sistem
            Restaurant restaurant = null;
            for (Restaurant resto : restoList) {
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

    public static void handleCetakBill(User userLoggedIn){
        // Method untuk handle ketika customer ingin cetak bill
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
                    pesanan += "- " + item.getNamaMakanan() + " " + item.getHarga() + "\n";
                }

                // Mendapatkan nama restoran dan biaya pesanan
                String namaRestoran = order.getRestaurant().getNamaRestoran();
                double biayaPesanan = order.calculateTotal();
                // Generate bill
                String bill = generateBill(orderId, namaRestoran, lokasi, status, pesanan, biayaPesanan);
                System.out.println(bill);
                break;
            } else {
                System.out.println("Order ID tidak dapat ditemukan.\n");
            }
        }
    }

    public static void handleLihatMenu(){
        // Method untuk handle ketika customer ingin melihat menu
        System.out.print("Nama Restoran: ");
        String namaRestoran = input.nextLine();

        Restaurant restaurant = null;
        // Mengecek apakah restoran sudah pernah terdaftar dalam sistem
        for (Restaurant resto : restoList) {
            if (resto.getNamaRestoran().equalsIgnoreCase(namaRestoran)) {
                restaurant = resto;
                break;
            }
        }

        if (restaurant != null) {
            System.out.println("Menu:");
            ArrayList<Menu> menuItems = restaurant.getMenu();
            // Mencetak menu restoran
            for (int i = 0; i < menuItems.size(); i++) {
                Menu item = menuItems.get(i);
                System.out.println((i+1) + ". " + item.getNamaMakanan() + " " + item.getHarga());
            }
        } else {
            System.out.println("Restoran tidak terdaftar pada sistem.");
        }
    }

    public static void handleUpdateStatusPesanan(User userLoggedIn){
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
            } else if ("Not Finished".equalsIgnoreCase(status) && order.isOrderFinished()) {
                order.setOrderFinished(false);
                System.out.println("Status pesanan dengan ID " + orderId + " berhasil diupdate!");
            } else {
                System.out.println("Status pesanan dengan ID " + orderId + " tidak berhasil diupdate!");
            }
        } else {
            System.out.println("Order ID tidak dapat ditemukan.");
        }
    }

    public static void handleTambahRestoran(){
        // Method untuk handle ketika admin ingin tambah restoran
        while (true) {
            System.out.print("Nama: ");
            String namaRestoran = input.nextLine();

            // Mengecek apakah nama restoran minimal 4 karakter
            if(namaRestoran.length() < 4) {
                System.out.println("Nama Restoran tidak valid!\n");
                continue;
            }

            boolean restoTerdaftar = false;
            for (Restaurant restaurant : restoList) {
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
            Restaurant newRestaurant = new Restaurant(namaRestoran);

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
                restoList.add(newRestaurant);
                System.out.println(newRestaurant);
                break;

            } else {
                System.out.println("Jumlah makanan tidak boleh negatif!\n");
            }
        }
    }

    public static void handleHapusRestoran(){
        // Method untuk handle ketika admin ingin hapus restoran
        while (true) {
            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine();

            // Loop daftar restoran
            for (int i = 0; i < restoList.size(); i++) {
                if (restoList.get(i).getNamaRestoran().equalsIgnoreCase(namaRestoran)) {
                    // Jika restoran ditemukan, hapus dari daftar restoran
                    restoList.remove(i);
                    System.out.println("Restoran berhasil dihapus.");
                    return;
                }
            }

            // Jika restoran tidak ditemukan, balik ke input awal loop
            System.out.println("Restoran tidak terdaftar pada sistem.\n");
        }
    }

    public static void initUser(){
       userList = new ArrayList<User>();
       userList.add(new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer"));
       userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer"));
       userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer"));
       userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer"));
       userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer"));

       userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin"));
       userList.add(new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin"));
    }

    public static void printHeader(){
        System.out.println("\n>>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
    }

    public static void startMenu(){
        System.out.println("Selamat datang di DepeFood!");
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Login");
        System.out.println("2. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuAdmin(){
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuCustomer() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Update Status Pesanan");
        System.out.println("5. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }
}
