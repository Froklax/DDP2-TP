package assignments.assignment3.payment;

public class CreditCardPayment implements DepeFoodPaymentSystem {
    // Biaya transaksi sebesar 2% yang perlu di bayar oleh User jika menggunakan CreditCard sebagai pembayaran
    private static final double TRANSACTION_FEE_PERCENTAGE = 0.02;

    @Override
    public long processPayment(long amount) {
        // Menghitung biaya transaksi
        long transactionFee = countTransactionFee(amount);

        // Menambahkan jumlah pembayaran dengan biaya transaksi
        long finalAmount = amount + transactionFee;

        // Mengembalikan jumlah akhir setelah dikurangi biaya transaksi
        return finalAmount;
    }

    private long countTransactionFee(long amount) {
        // Menghitung biaya transaksi sebesar 2% dari jumlah pembayaran
        return (long) (amount * TRANSACTION_FEE_PERCENTAGE);
    }
}
