package data;

import model.User;
import model.CoworkingPlace;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс для хранения данных о сущностях.
 */
public class Data {
    private static Set<User> usersAndAdminData = new HashSet<>();
    private static Set<String> alreadyExistsUserNames = new HashSet<>();
    private static Set<CoworkingPlace> coworkingPlaces = new HashSet<>();
    private static Set<Integer> alreadyExistsCoworkingNumbers = new HashSet<>();
    private static Set<Integer> alreadyExistsConferenceNumbers = new HashSet<>();

    public static Set<User> getUsersAndAdminData() {
        return usersAndAdminData;
    }

    public static Set<String> getAlreadyExistsUserNames() {
        return alreadyExistsUserNames;
    }

    public static Set<CoworkingPlace> getCoworkingPlaces() {
        return coworkingPlaces;
    }

    public static Set<Integer> getAlreadyExistsCoworkingNumbers() {
        return alreadyExistsCoworkingNumbers;
    }

    public static Set<Integer> getAlreadyExistsConferenceNumbers() {
        return alreadyExistsConferenceNumbers;
    }
}
