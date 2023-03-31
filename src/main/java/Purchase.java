import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.*;
import java.util.*;

public class Purchase {

    public static final File file = new File("categories.tsv");

    private final String title;
    private final String date;
    private final int sum;

    public Purchase(
            @JsonProperty("title") String title,
            @JsonProperty("date") String date,
            @JsonProperty("sum") int sum
    ) {
        this.title = title;
        this.date = date;
        this.sum = sum;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getSum() {
        return sum;
    }

    private String getCategory() throws IOException {
        Map<String, String> categories = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String s;
            while ((s = reader.readLine()) != null) {
                String[] parts = s.split("\t");
                categories.put(parts[0], parts[1]);
            }
        }
        String category = categories.get(getTitle());
        if (category == null) {
            return "другое";
        }
        return category;
    }

    public void savePurchase(Map<String, Integer> purchaseArchive) throws IOException {
        String category = getCategory();
        if (purchaseArchive.containsKey(category)) {
            int sum = purchaseArchive.get(category);
            purchaseArchive.put(category, getSum() + sum);
        } else {
            purchaseArchive.put(category, getSum());
        }
    }

    public Statistic getStatistic(Map<String, Integer> purchaseArchive) {

        return purchaseArchive.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> new Statistic(new MaxCategory(e.getKey(), e.getValue())))
                .get();
    }
}
