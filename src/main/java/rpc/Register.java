package rpc;

import db.MySQLConnection;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Register extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject obj = RpcHelper.readJSONObject(req);
        String userId = obj.getString("user_id");
        String password = obj.getString("password");
        String firstName = obj.getString("first_name");
        String lastName = obj.getString("last_name");
        MySQLConnection connection = new MySQLConnection();
        JSONObject res = new JSONObject();
        if (connection.addUser(userId, password, firstName, lastName)) {
            res.put("status", "OK");
        } else {
            res.put("status", "User already exists.");
        }
        connection.close();
        RpcHelper.writeJsonObject(resp, res);
    }
}
