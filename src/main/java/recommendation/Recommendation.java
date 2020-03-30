package recommendation;

import db.MySQLConnection;
import entity.Item;
import external.GitHubClient;

import java.util.*;
import java.util.Map.Entry;

public class Recommendation {

    public List<Item> recommendItems(String userId, double lat, double lon) {
        List<Item> recommendedItems = new ArrayList<>();

        // Step 1, get all favorite items
        MySQLConnection connection = new MySQLConnection();
        Set<String> favoriteItemIds = connection.getFavoriteItemIds(userId);

        // Step 2, get all categories, sort by count
        // {"software engineer": 6, "backend": 4, "san francisco": 3, "remote": 1}
        Map<String, Integer> allKeywords = new HashMap<>();
        for (String itemId : favoriteItemIds) {
            Set<String> keywords = connection.getKeywords(itemId);
            for (String keyword : keywords) {
                allKeywords.put(keyword, allKeywords.getOrDefault(keyword, 0) + 1);
            }
        }
        connection.close();

        List<Entry<String, Integer>> keywordList = new ArrayList<>(allKeywords.entrySet());
        keywordList.sort((Entry<String, Integer> e1, Entry<String, Integer> e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        // Cut down search list only top 3
        if (keywordList.size() > 3) {
            keywordList = keywordList.subList(0, 3);
        }

        // Step 3, search based on keywords, filter out favorite items
        Set<String> visitedItems = new HashSet<>();
        GitHubClient client = new GitHubClient();

        for (Entry<String, Integer> keyword : keywordList) {
            List<Item> items = client.search(lat, lon, keyword.getKey());
            for (Item item : items) {
                if (!favoriteItemIds.contains(item.getItemId()) && visitedItems.add(item.getItemId())) {
                    recommendedItems.add(item);
                }
            }
        }
        return recommendedItems;
    }
}