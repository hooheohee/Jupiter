package rpc;

import db.MySQLConnection;
import entity.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

public class ItemHistory extends HttpServlet {

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

        MySQLConnection connection = new MySQLConnection();
        Set<Item> items = connection.getFavoriteItems(userId);
        connection.close();

        JSONArray array = new JSONArray();
        for (Item item : items) {
            JSONObject obj = item.toJSONObject();
            obj.put("favorite", true);
            array.put(obj);
        }
        RpcHelper.writeJsonArray(response, array);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        JSONObject input = RpcHelper.readJSONObject(request);
        String userId = input.getString("user_id");
        if (session == null || !((String) session.getAttribute("user_id")).equalsIgnoreCase(userId)) {
            JSONObject object = new JSONObject();
            object.put("status", "Invalid Session");
            response.setStatus(403);
            RpcHelper.writeJsonObject(response, object);
            return;
        }

        MySQLConnection connection = new MySQLConnection();
        Item item = RpcHelper.parseFavoriteItem(input.getJSONObject("favorite"));

        connection.setFavoriteItems(userId, item);
        connection.close();
        RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(403, "Not Authenticated");
            response.setStatus(403);
            return;
        }

        MySQLConnection connection = new MySQLConnection();
        JSONObject input = RpcHelper.readJSONObject(request);
        String userId = input.getString("user_id");
        if (!((String) session.getAttribute("user_id")).equalsIgnoreCase(userId)) {
            response.setStatus(403);
            return;
        }
        Item item = RpcHelper.parseFavoriteItem(input.getJSONObject("favorite"));

        connection.unsetFavoriteItems(userId, item.getItemId());
        connection.close();
        RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
    }
}
