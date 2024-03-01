package assignments.assignment1;

import java.util.Scanner;

public class OrderGenerator {
    private static final Scanner input = new Scanner(System.in);

    /* 
    Anda boleh membuat method baru sesuai kebutuhan Anda
    Namun, Anda tidak boleh menghapus ataupun memodifikasi return type method yang sudah ada.
    */

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

    /*
     * Method ini digunakan untuk membuat ID
     * dari nama restoran, tanggal order, dan nomor telepon
     * 
     * @return String Order ID dengan format sesuai pada dokumen soal
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
        
        // Menghitung modulo 36 dari checksum
        int sisaChecksum1 = checksum1 % 36;
        // Menambahkan checksum ke ID pesanan 
        if (sisaChecksum1 < 10) {
            orderId += Integer.toString(sisaChecksum1);
        } else { // Reverse assignment ke dalam code 39 character set
            orderId += (char) ('A' + (sisaChecksum1 - 10));
        }

        int sisaChecksum2 = checksum2 % 36;
        if (sisaChecksum2 < 10) {
            orderId += Integer.toString(sisaChecksum2);
        } else {
            orderId += (char) ('A' + (sisaChecksum2 - 10));
        }
        // Mengembalikan ID pesanan
        return orderId;
    }


    /*
     * Method ini digunakan untuk membuat bill
     * dari order id dan lokasi
     * 
     * @return String Bill dengan format sesuai di bawah:
     *          Bill:
     *          Order ID: [Order ID]
     *          Tanggal Pemesanan: [Tanggal Pemesanan]
     *          Lokasi Pengiriman: [Kode Lokasi]
     *          Biaya Ongkos Kirim: [Total Ongkos Kirim]
     */
    public static String generateBill(String OrderID, String lokasi){
        // TODO:Lengkapi method ini sehingga dapat mengenerate Bill sesuai ketentuan
        return "Bill";
    }

    public static void main(String[] args) {
        // TODO: Implementasikan program sesuai ketentuan yang diberikan
    }

    
}
