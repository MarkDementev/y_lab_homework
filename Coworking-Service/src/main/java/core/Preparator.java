package core;

import model.CoworkingPlace;
import model.User;

import service.UserService;
import service.CoworkingPlaceService;

/**
 * Класс создаёт в системе аккаунт администратора, 2 коворкинга и 1 конференц-зал при первом запуске
 * сессии {@link core.session.Session}.
 */
public class Preparator {
    public static final String ADMIN_NAME = "Администратор";
    public static final String ADMIN_PASSWORD = "Администратор";

    /**
     * Создаётся в системе аккаунт администратора, 2 коворкинга и 1 конференц-зал.
     */
    public static void prepareAdminAndPlaces() {
        User admin = new User(ADMIN_NAME, ADMIN_PASSWORD, true);
        CoworkingPlace firstWorkingPlace = new CoworkingPlace(1, false);
        CoworkingPlace secondWorkingPlace = new CoworkingPlace(2, false);
        CoworkingPlace conferenceRoom = new CoworkingPlace(1, true);

        UserService.addUser(admin);
        CoworkingPlaceService.addCoworkingPlace(firstWorkingPlace);
        CoworkingPlaceService.addCoworkingPlace(secondWorkingPlace);
        CoworkingPlaceService.addCoworkingPlace(conferenceRoom);
    }
}
