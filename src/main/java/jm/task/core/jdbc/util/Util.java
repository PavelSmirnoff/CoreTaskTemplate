package jm.task.core.jdbc.util;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

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
            Driver driver;
            {
                try {
                    driver = new FabricMySQLDriver();
                    DriverManager.deregisterDriver(driver);
                    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    if(!connection.isClosed()){
                        System.out.println("Соединение с БД установленно");
                    }
                } catch (SQLException throwables) {
                    System.out.println("Не удалось загрузить драйвер");
                }
            }
        }
        return connection;
    }
}
