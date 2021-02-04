package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    Transaction transaction = null;
    SessionFactory sessionFactory = Util.getSessionFactory();
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {

        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            String sql = "CREATE TABLE IF NOT EXISTS Users ( " +
                    "id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "lastName VARCHAR(50) NOT NULL, " +
                    "age TINYINT NOT NULL, " +
                    "PRIMARY KEY (id)); ";
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Ошибка создания таблицы. " + e);
            transaction.rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS Users;";
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Ошибка уделения таблицы. " + e);
            transaction.rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.persist(new User(name,lastName,age));
//            String sql = " INSERT INTO Users (name, lastName, age) values (:name,:lastname,:age)";
//            Query<User> query = session.createNativeQuery(sql,User.class);
//            query.setParameter("name", name);
//            query.setParameter("lastname", lastName);
//            query.setParameter("age", age);
//            query.executeUpdate();
            transaction.commit();
            System.out.printf("User с именем – %s добавлен в базу данных.\n", name);
        } catch (Exception e) {
            System.out.println("Ошибка добавления User в БД. " + e);
            transaction.rollback();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            String sql = "DELETE FROM Users WHERE id=" + id;
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Ошибка уделения User с id=" + id + " из БД. " + e);
            transaction.rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = "from User";
            users = session.createQuery(sql, User.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Ошибка получения списка Users. " + e);
            transaction.rollback();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            String sql = "TRUNCATE TABLE Users;";
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Ошибка очистки данных в таблице. " + e);
            transaction.rollback();
        }
    }
}
