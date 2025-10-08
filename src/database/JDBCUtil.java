package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class JDBCUtil {
    // private Connection conn;

    // public JDBCUtil() {}

    public static Connection getConnection(){
        Connection conn = null;
        try {
            // tạo driver bằng driver manager
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            String url = "jdbc:mySQL://localhost:3306/khohang";
            String username = "root";
            String password = "";

            // kết nối database
            conn = DriverManager.getConnection(url, username, password);
        } catch(SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Không thể kết nối đến MYSQL","Lỗi SQL",JOptionPane.ERROR_MESSAGE);
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
