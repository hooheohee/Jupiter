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
        JSONObject input = RpcHelper.readJSONObject(req);
        String userId = input.getString("user_id");
        String password = input.getString("password");
        String firstName = input.getString("first_name");
        String lastName = input.getString("last_name");
        MySQLConnection connection = new MySQLConnection();
        JSONObject obj = new JSONObject();
        if (connection.addUser(userId, password, firstName, lastName)) {
            obj.put("status", "OK");
        } else {
            obj.put("status", "User already exists.");
        }
        connection.close();
        RpcHelper.writeJsonObject(resp, obj);
    }
}
