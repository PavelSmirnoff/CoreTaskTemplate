package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.annotations.NamedQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection conn = Util.getConnection();
    private Savepoint savepoint = null;
    private Statement statement = null;

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try {
            savepoint = conn.setSavepoint("SavepointCreatedTableUser");
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Users ( " +
                    "id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "lastName VARCHAR(50) NOT NULL, " +
                    "age TINYINT NOT NULL, " +
                    "PRIMARY KEY (id)); ";
            statement.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Ошибка создания таблицы. " + e);
            try {
                conn.rollback(savepoint);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void dropUsersTable() {
        try {
            savepoint = conn.setSavepoint("SavepointDropTableUser");
            statement = conn.createStatement();
            String sql = "DROP TABLE IF EXISTS Users;";
            statement.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Ошибка уделения таблицы. " + e);
            try {
                conn.rollback(savepoint);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        PreparedStatement preparedStmt = null;

        try {
            savepoint = conn.setSavepoint("SavepointSaveUser");
            String sql = "INSERT INTO Users (name, lastName, age) values (?,?,?)";
            preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setString(1, name);
            preparedStmt.setString(2, lastName);
            preparedStmt.setInt(3, age);
            preparedStmt.execute();
            conn.commit();
            System.out.printf("User с именем – %s добавлен в базу данных.\n", name);
        } catch (SQLException e) {
            System.out.println("Ошибка добавления User в БД. " + e);
            try {
                conn.rollback(savepoint);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            try {
                preparedStmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void removeUserById(long id) {
        try {
            savepoint = conn.setSavepoint("SavepointRemoveUserById");
            statement = conn.createStatement();
            String sql = "DELETE FROM Users WHERE ID=" + id + ";";
            statement.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Ошибка уделения User с id=" + id + " из БД. " + e);
            try {
                conn.rollback(savepoint);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            savepoint = conn.setSavepoint("SavepointGetAllUsers");
            statement = conn.createStatement();
            String sql = "SELECT * FROM Users";
            ResultSet resultSet = statement.executeQuery(sql);
            conn.commit();
            while (resultSet.next()) {
                long id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                byte age = (byte) resultSet.getInt("age");
                User user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения списка Users. " + e);
            try {
                conn.rollback(savepoint);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }finally {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return users;
    }

    public void cleanUsersTable() {
        try {
            savepoint = conn.setSavepoint("SavepointClearUsersTable");
            statement = conn.createStatement();
            String sql = "TRUNCATE TABLE Users;";
            statement.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Ошибка очистки данных в таблице. " + e);
            try {
                conn.rollback(savepoint);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }finally {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
