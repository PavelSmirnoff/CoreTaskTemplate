package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    Session session = Util.getSessionFactory().openSession();
    Transaction transaction = null;

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try {
            transaction = session.beginTransaction();
            String sql = "CREATE TABLE IF NOT EXISTS Users ( " +
                    "id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "lastName VARCHAR(50) NOT NULL, " +
                    "age TINYINT NOT NULL, " +
                    "PRIMARY KEY (id)); ";
            session.createNativeQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка создания таблицы. " + e);
            transaction.rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            transaction = session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS Users;";
            session.createNativeQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка уделения таблицы. " + e);
            transaction.rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        EntityManager entityManager = Util.getSessionFactory().openSession();
        ;
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            String sql = "INSERT INTO Users (name, lastName, age) values (?,?,?)";
            entityTransaction.begin();
            entityManager.createNativeQuery(sql)
                    .setParameter(1, name)
                    .setParameter(2, lastName)
                    .setParameter(3, age)
                    .executeUpdate();
            entityTransaction.commit();
            System.out.printf("User с именем – %s добавлен в базу данных.\n", name);
        } catch (Exception e) {
            System.out.println("Ошибка добавления User в БД. " + e);
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            transaction = session.beginTransaction();
            String sql = "DELETE FROM Users WHERE ID=" + id + ";";
            session.createNativeQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка уделения User с id=" + id + " из БД. " + e);
            transaction.rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            transaction = session.beginTransaction();
            String sql = "SELECT * FROM Users";
            List<Object[]> rows = session.createNativeQuery(sql).getResultList();
            for(Object[] row : rows) {
                long id = ((BigInteger) row[0]).longValue();
                String name = (String) row[1];
                String lastName = (String) row[2];
                byte age = (byte) row[3];
                User user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка получения списка Users. " + e);
            transaction.rollback();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try {
            transaction = session.beginTransaction();
            String sql = "TRUNCATE TABLE Users;";
            session.createNativeQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка очистки данных в таблице. " + e);
            transaction.rollback();
        }
    }
}
