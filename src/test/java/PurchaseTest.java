import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PurchaseTest {

    Purchase purchase;
    Map<String, Integer> purchasesTest;
    Statistic statistic;

    @BeforeEach
    public void BeforeEach() {
        purchase = new Purchase("булка", "2021.03.05", 1000);
        purchasesTest = new HashMap<>();
    }

    @Test
    public void getCategoryTest() throws IOException {
        String category = purchase.getCategory();
        Assertions.assertEquals("еда", category);
    }

    @Test
    public void getCategoryNotInFile() throws IOException {
        purchase = new Purchase("шоколадка", "2021.03.05", 1000);
        String category = purchase.getCategory();
        Assertions.assertEquals("другое", category);
    }

    @Test
    public void savePurchaseTest() throws IOException {
        purchase.savePurchase(purchasesTest);
        Assertions.assertFalse(purchasesTest.isEmpty());
    }

    @Test
    public void getStatisticTest() {
        purchasesTest.put("еда", 3_000);
        purchasesTest.put("одежда", 10_000);
        statistic = purchase.getStatistic(purchasesTest);
        int res = statistic.getMaxCategory().getSum();
        Assertions.assertEquals(10_000, res);
    }
}
