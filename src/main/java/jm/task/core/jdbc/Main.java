package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;


public class Main {


    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();
//
//        userService.saveUser("Иванов", "Иван", (byte) 20);
//        userService.saveUser("Петров", "Петр", (byte) 34);
//        userService.saveUser("Сидоров", "Александр", (byte) 28);
//        userService.saveUser("Попов", "Сергей", (byte) 19);
//
//        userService.getAllUsers().forEach(x -> System.out.println(x.toString()));
//
//        userService.cleanUsersTable();
//        userService.dropUsersTable();


    }
}
