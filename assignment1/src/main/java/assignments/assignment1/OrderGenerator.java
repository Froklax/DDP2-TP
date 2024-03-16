package assignments.assignment1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class OrderGenerator {
    private static final Scanner input = new Scanner(System.in);

    /*
     * Method  ini untuk menampilkan menu
     */
    public static void showMenu(){
        System.out.println(">>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
        System.out.println();
        System.out.println("Pilih menu:");
        System.out.println("1. Generate Order ID");
        System.out.println("2. Generate Bill");
        System.out.println("3. Keluar");
    }
    public static String calculateChecksum(String orderId) {
        int checksum1 = 0;
        int checksum2 = 0;
        // Menghitung checksum
        for (int i = 0; i < orderId.length(); i++) {
            // Mengecek apakah posisi karakter ganjil atau genap
            if (i % 2 == 0) {
                // Jika karakter adalah sebuah digit maka langsung tambahkan ke checksum
                if (Character.isDigit(orderId.charAt(i))) {
                    checksum1 += Character.getNumericValue(orderId.charAt(i));
                } else { // Jika bukan digit maka perlu menghitung karakter sesuai dengan code 39
                    int codeChar = 10 + (orderId.charAt(i) - 'A');
                    checksum1 += codeChar;
                }
            } else {
                if (Character.isDigit(orderId.charAt(i))) {
                    checksum2 += Character.getNumericValue(orderId.charAt(i));
                } else {
                    int codeChar = 10 + (orderId.charAt(i) - 'A');
                    checksum2 += codeChar;
                }
            }
        }

        String checksum = "";
        // Menghitung modulo 36 dari checksum
        int sisaChecksum1 = checksum1 % 36;
        // Menambahkan checksum ke ID pesanan
        if (sisaChecksum1 < 10) {
            checksum += Integer.toString(sisaChecksum1);
        } else { // Reverse assignment ke dalam code 39 character set
            checksum += (char) ('A' + (sisaChecksum1 - 10));
        }

        int sisaChecksum2 = checksum2 % 36;
        if (sisaChecksum2 < 10) {
            checksum += Integer.toString(sisaChecksum2);
        } else {
            checksum += (char) ('A' + (sisaChecksum2 - 10));
        }
        // Mengembalikan checksum
        return checksum;
    }

    /*
     * Method ini digunakan untuk membuat ID
     * dari nama restoran, tanggal order, dan nomor telepon
     */
    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {
        String orderId;
        // Mengubah nama restoran menjadi huruf besar dan menghapus spasi
        String idNamaRestoran = "";
        for (int i = 0; i < namaRestoran.length(); i++) {
            char charNamaRestoran = namaRestoran.charAt(i);
            if (charNamaRestoran != ' ') {
                idNamaRestoran +=  Character.toUpperCase(charNamaRestoran);
            }
        }

        // Menghapus karakter '/' dari tanggal
        String idTanggal = "";
        for (int i = 0; i < tanggalOrder.length(); i++) {
            char charTanggalOrder = tanggalOrder.charAt(i);
            if (charTanggalOrder != '/') {
                idTanggal += charTanggalOrder;
            }
        }

        // Menghitung jumlah dari digit dalam nomor telepon
        int sumNoTelepon = 0;
        for (int i = 0; i < noTelepon.length(); i++) {
            int digit = Character.getNumericValue(noTelepon.charAt(i));
            sumNoTelepon += digit;
        }

        // Menghitung hasil modulo 100 dari hasil perjumlahan noTelepon
        int idTelepon = sumNoTelepon % 100;
        String strIdTelepon;
        // Jika idTelepon hanya satu digit saja, tambahkan "0" di depannya
        if (idTelepon < 10) {
            strIdTelepon = "0" + idTelepon;
        } else {
            strIdTelepon = String.valueOf(idTelepon);
        }

        // Membuat 14 karakter pertama ID pesanan
        orderId = idNamaRestoran.substring(0, 4) + idTanggal + strIdTelepon;
        // Menambahkan checksum
        orderId += calculateChecksum(orderId);

        // Mengembalikan ID pesanan
        return orderId;
    }


    /*
     * Method ini digunakan untuk membuat bill (telah di modify untuk assignment2)
     * dari order id dan lokasi
     */
    public static String generateBill(String OrderID, String namaRestoran, String lokasi, String status, String pesanan, double biayaPesanan){
        int biayaOngkir;
        String lokasiUpper = lokasi.toUpperCase();
        // Menghitung biaya ongkir berdasarkan lokasi
        if (lokasiUpper.equals("P")) {
            biayaOngkir = 10000;
        } else if (lokasiUpper.equals("U")) {
            biayaOngkir = 20000;
        } else if (lokasiUpper.equals("T")) {
            biayaOngkir = 35000;
        } else if (lokasiUpper.equals("S")) {
            biayaOngkir = 40000;
        } else if (lokasiUpper.equals("B")) {
            biayaOngkir = 60000;
        } else {
            return null;
        }

        double totalBiaya = biayaPesanan + biayaOngkir;;

        // Mengambil tanggal dari OrderID dan memformatnya
        String tanggal = OrderID.substring(4, 12);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate localDate = LocalDate.parse(tanggal, formatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String tanggalFormatted = localDate.format(outputFormatter);

        // Menghasilkan bill sesuai ketentuan
        String bill = "Bill:\n";
        bill += "Order ID: " + OrderID + "\n";
        bill += "Tanggal Pemesanan: " + tanggalFormatted + "\n";
        bill += "Restaurant: " + namaRestoran + "\n";
        bill += "Lokasi Pengiriman: " + lokasiUpper + "\n";
        bill += "Status Pemesanan: " + status + "\n";
        bill += "Pesanan:\n" + pesanan;
        bill += "Biaya Ongkos Kirim: Rp " + biayaOngkir + "\n";
        bill += "Total Biaya: Rp " + totalBiaya + "\n";

        return bill;
    }

    public static void main(String[] args) {
        String orderId = "";
        while (true) {
            showMenu();
            System.out.println("---------------------------------");
            System.out.print("Pilihan menu: ");
            int pilihanMenu = Integer.valueOf(input.nextLine());

            if (pilihanMenu == 1) {
                while (true) {
                    System.out.print("Nama Restoran: ");
                    String namaRestoran = input.nextLine();
                    while (namaRestoran.length() < 4) {
                        System.out.println("Nama Restoran tidak valid!\n");
                        System.out.print("Nama Restoran: ");
                        namaRestoran = input.nextLine();
                    }

                    // Input untuk tanggal pemesanan
                    String tanggalPemesanan;
                    System.out.printf("Tanggal Pemesanan: ");
                    tanggalPemesanan = input.nextLine();
                    if (tanggalPemesanan.length() != 10 || tanggalPemesanan.charAt(2) != '/' || tanggalPemesanan.charAt(5) != '/') {
                        System.out.println("Tanggal Pemesanan dalam format DD/MM/YYYY!\n");
                        continue;
                    }

                    System.out.print("No. Telpon: ");
                    String noTelpon = input.nextLine();
                    boolean allDigits = true;
                    // Looping melalui setiap karakter dalam noTelpon
                    for (int i = 0; i < noTelpon.length(); i++) {
                        // Jika karakter saat ini bukan digit, maka allDigits menjadi false dan hentikan looping.
                        if (!Character.isDigit(noTelpon.charAt(i))) {
                            allDigits = false;
                            break;
                        }
                    }
                    if (allDigits && Long.valueOf(noTelpon) > -1) {
                        // Panggil fungsi untuk menghasilkan ID pesanan
                        orderId = generateOrderID(namaRestoran, tanggalPemesanan, noTelpon);
                        System.out.println("Order ID " + orderId + " diterima!");
                        System.out.println("---------------------------------");
                        break;
                    } else {
                        System.out.println("Harap masukkan nomor telepon dalam bentuk bilangan bulat positif.\n");
                        continue;
                    }
                }

            } else if (pilihanMenu == 2) {
                // Panggil fungsi untuk menghasilkan tagihan
                while (true) {
                    System.out.print("Order ID: ");
                    String order = input.nextLine();
                    // Mengecek apakah panjang Order ID kurang dari 16 karakter
                    if (order.length() < 16) {
                        System.out.println("Order ID minimal 16 karakter\n");
                        continue;
                        // Mengecek apakah Order ID ada atau tidak dalam daftar Order ID yang telah dihasilkan
                    } else if (!order.substring(0, 4).matches("[A-Z]{4}") || !order.substring(4, 12).matches("\\d{8}")) {
                        System.out.println("Silahkan masukkan Order ID yang valid!\n");
                        continue;
                    }

                    // Mengecek apakah 2 karakter terakhir memenuhi checksum
                    String calculatedChecksum = calculateChecksum(order.substring(0, order.length() - 2));
                    if (!order.endsWith(calculatedChecksum) || order.length() > 16) {
                        System.out.println("Silahkan masukkan Order ID yang valid!\n");
                        continue;
                    }
                }
            } else if (pilihanMenu == 3) { // Exit dari menu
                System.out.println("Terima kasih telah menggunakan DepeFood!");
                break;
            } else {
                System.out.println("Pilihan menu tidak valid. Silakan coba lagi.\n");
                continue;
            }
        }
    }
}
