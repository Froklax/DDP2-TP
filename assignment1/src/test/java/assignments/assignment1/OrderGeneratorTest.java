package assignments.assignment1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class OrderGeneratorTest {
    @Test
    public void testGenerateOrderID1() {
        assertEquals("HOLY1802202453C3", OrderGenerator.generateOrderID("Holycow!", "18/02/2024", "9928765403"));
    }

    @Test
    public void testGenerateBill1() {
        String solution = "Bill:\n" +
                "Order ID: HOLY1802202453C3\n" +
                "Tanggal Pemesanan: 18/02/2024\n" +
                "Restaurant: Holycow!\n" +
                "Lokasi Pengiriman: S\n" +
                "Status Pemesanan: Selesai\n" + // Ganti dengan status pesanan yang sesuai
                "Pesanan:\n" + "1. Pisang 20000\n" +
                "Biaya Ongkos Kirim: Rp " + 40000 + "\n" +
                "Total Biaya: Rp " + (20000 + 40000) + "\n";
        assertEquals(solution, OrderGenerator.generateBill("HOLY1802202453C3", "Holycow!", "s", "Selesai", "1. Pisang 20000\n", 20000.0));
    }
}
