package assignments.assignment3.payment;

public class DebitPayment implements DepeFoodPaymentSystem {
    // Total harga pesanan minimal sebesar Rp50000 jika ingin menggunakan DebitCard sebagai pembayaran.
    private static final double MINIMUM_TOTAL_PRICE = 50000;

    @Override
    public long processPayment(long amount) {
        // Memeriksa apakah harga pesanan kurang dari 50000
        if (amount < MINIMUM_TOTAL_PRICE) {
            System.out.println("Jumlah pesanan < 50000 mohon menggunakan metode pembayaran yang lain\n");
            return 0;
        } else {
            return amount;
        }
    }
}
