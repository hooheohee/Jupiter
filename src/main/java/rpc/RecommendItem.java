package rpc;

import entity.Item;
import org.json.JSONArray;
import org.json.JSONObject;
import recommendation.Recommendation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class RecommendItem extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("user_id");
        if (session == null || !((String) session.getAttribute("user_id")).equalsIgnoreCase(userId)) {
            JSONObject object = new JSONObject();
            object.put("status", "Invalid Session");
            response.setStatus(403);
            RpcHelper.writeJsonObject(response, object);
            return;
        }

        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));

        Recommendation recommendation = new Recommendation();
        List<Item> items = recommendation.recommendItems(userId, lat, lon);
        JSONArray array = new JSONArray();
        for (Item item : items) {
            array.put(item.toJSONObject());
        }
        RpcHelper.writeJsonArray(response, array);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
