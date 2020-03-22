package rpc;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.MySQLConnection;
import entity.Item;
import external.GitHubClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class SearchItem extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            return;
        }

        String userId = request.getParameter("user_id");
        if (!((String) session.getAttribute("user_id")).equalsIgnoreCase(userId)) {
            response.setStatus(403);
            return;
        }
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));

        GitHubClient client = new GitHubClient();
        List<Item> items = client.search(lat, lon, null);

        MySQLConnection connection = new MySQLConnection();
        Set<String> favoriteItemIds = connection.getFavoriteItemIds(userId);
        connection.close();

        JSONArray array = new JSONArray();
        for (Item item : items) {
            JSONObject obj = item.toJSONObject();
            obj.put("favorite", favoriteItemIds.contains(item.getItemId()));
            array.put(obj);
        }
        RpcHelper.writeJsonArray(response, array);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
