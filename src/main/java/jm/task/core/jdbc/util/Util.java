package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    private static Connection connection = null;
    private static final String URL = "jdbc:mysql://localhost:3306/TaskJDBC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12345678";

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("Не удалось создать подключение к БД. " + e);
            }
        }
        return connection;
    }
}
