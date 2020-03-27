package external;

import com.monkeylearn.ExtraParam;
import com.monkeylearn.MonkeyLearn;
import com.monkeylearn.MonkeyLearnException;
import com.monkeylearn.MonkeyLearnResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MonkeyLearnClient {

    private static final String API_KEY = System.getenv("MONKEY_LEARN_API_KEY");

    public static void main(String[] args) {
        String[] textList = {"Elon Musk has shared a photo of the spacesuit designed by SpaceX. This is the second image shared of the new design and the first to feature the spacesuit’s full-body look."};
        List<List<String>> words = extractKeywords(textList);
        for (List<String> ws : words) {
            for (String w : ws) {
                System.out.println(w);
            }
            System.out.println();
        }
    }

    public static List<List<String>> extractKeywords(String[] text) {
        if (text == null || text.length == 0) {
            return new ArrayList<>();
        }
        MonkeyLearn ml = new MonkeyLearn(API_KEY);
        ExtraParam[] extraParams = {new ExtraParam("max_keywords", "3")};
        MonkeyLearnResponse res;
        List<List<String>> keywords = null;
        try {
            res = ml.extractors.extract("ex_YCya9nrn", text, extraParams);
            keywords = getKeywords(res.arrayResult);
        } catch (MonkeyLearnException e) {
            e.printStackTrace();
        }
        return keywords;
    }

    private static List<List<String>> getKeywords(JSONArray arr) {
        List<List<String>> topKeywords = new ArrayList<>();
        for (Object a : arr) {
            List<String> keywords = new ArrayList<>();
            JSONArray keywordArray = (JSONArray) a;
            for (Object o : keywordArray) {
                JSONObject obj = (JSONObject) o;
                String keyword = (String) obj.get("keyword");
                keywords.add(keyword);
            }
            topKeywords.add(keywords);
        }
        return topKeywords;
    }
}
