package service;

import data.Data;

import model.User;

/**
 * Сервис для обслуживания CRUD-операций взаимодействия с {@link User} и {@link Data}.
 */
public class UserService {
    /**
     * Создание нового юзера
     * @param newUser данные о новом юзере
     */
    public static void addUser(User newUser) {
        if (Data.getAlreadyExistsUserNames().contains(newUser.name)) {
            throw new IllegalArgumentException("Пользователь с именем " + newUser.name + " уже существует в системе!");
        } else {
            Data.getUsersAndAdminData().add(newUser);
            Data.getAlreadyExistsUserNames().add(newUser.name);
        }
    }

    /**
     * Метод для удаления юзера из системы
     * Также удаляется его бронь!
     * @param user юзер, который удаляется
     */
    public static void deleteUser(User user) {
        CoworkingPlaceService.deleteBookingsForUser(user);
        Data.getUsersAndAdminData().remove(user);
        Data.getAlreadyExistsUserNames().remove(user.name);
    }
}
