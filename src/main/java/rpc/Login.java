package rpc;

import db.MySQLConnection;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        JSONObject obj = new JSONObject();
        if (session != null) {
            MySQLConnection connection = new MySQLConnection();
            String userId = session.getAttribute("user_id").toString();
            obj.put("status", "OK").put("user_id", userId).put("name", connection.getFullName(userId));
            connection.close();
        } else {
            obj.put("status", "Invalid Session");
            resp.setStatus(403);
        }
        RpcHelper.writeJsonObject(resp, obj);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject input = RpcHelper.readJSONObject(req);
        String userId = input.getString("user_id");
        String password = input.getString("password");
        JSONObject obj = new JSONObject();
        MySQLConnection connection = new MySQLConnection();
        if (connection.verifyLogin(userId, password)) {
            HttpSession session = req.getSession();
            session.setAttribute("user_id", userId);
            session.setMaxInactiveInterval(600);
            obj.put("status", "OK")
                    .put("name", connection.getFullName(userId))
                    .put("user_id", userId);
        } else {
            obj.put("status", "Wrong username or password!");
            resp.setStatus(401);
        }
        RpcHelper.writeJsonObject(resp, obj);
    }
}
