package core.session;

import core.Preparator;

import data.Data;

import model.User;

import service.UserService;

import java.util.Scanner;

/**
 * Класс - общая сессия для выбора пользователем программы своей роли в системе - юзер или администратор.
 * Обеспечивает приём консольных команд и ответы на них.
 * По результатам взаимодействия переводит пользователя на одну из 2-х сессий - сессия юзера или сессия админа.
 */
public class Session {
    public static final String OFFER_MAKE_CHOICE = "Введите числовую команду (1, 2 или 3): ";
    public static final String EXIT_TEXT = "До свидания!";
    public static final String WRONG_INPUT_ERROR = "Пожалуйста, в следующий раз введите иные команды!";
    public static final String HOW_TO_LOGIN = "Введите 2 слова. После введения первого слова" +
            " нажмите Enter и введите следующее слово. Первое - имя пользователя. Второе - пароль.";
    public static final String REGISTER_OR_LOGIN = HOW_TO_LOGIN + " Вы будете либо зарегистрированы с" +
            " указанными Вами данными, либо авторизованы в системе, если ранее были зарегистрированы.";
    public static final String WRONG_ADMIN_LOGIN_DATA = "Неверные данные администратора, в доступе отказано!";
    public static final String ADMIN_WRONG_COMMAND_ERROR = "Некорректный ввод данных!";
    public static final String LOGIN_SUCCESS = "Вы успешно авторизованы в системе.";
    public static final String USER_REGISTERED = "Пользователь создан успешно!";
    public static Boolean isFirstStartApp = true;

    /**
     * Создаётся сканер. Метод используется для обеспечения бесперерывности приёма команд.
     * Если в следующем методе {startApp(String userInteractionType, Scanner userInteractionScanner)} юзер ввёл команду
     * неправильно, он возвращается снова сюда, чтобы ввести команду заново.
     */
    public static void startSession() {
        System.out.print(OFFER_MAKE_CHOICE);
        Scanner userInteractionScanner = new Scanner(System.in);
        startApp(userInteractionScanner.next(), userInteractionScanner);
    }

    /**
     * Метод обрабатывает консольные команды.
     * Либо регистрирует/авторизует пользователя как юзера и перенаправляет
     * в свою сессию {@link UserSession}
     * Либо авторизует пользователя как админа и перенаправляет в свою сессию {@link AdminSession}
     * Либо заканчивает работу программы.
     * При первом вызове метода создаёт в системе аккаунт администратора, 2 коворкинга и 1 конференц-зал
     * используя класс {@link Preparator}
     * @param userInteractionType введённая числовая команда для выбора статуса в системе
     * @param userInteractionScanner сканер для приёма команд от пользователя в консоли
     */
    private static void startApp(String userInteractionType, Scanner userInteractionScanner) {
        if (isFirstStartApp) {
            Preparator.prepareAdminAndPlaces();
            isFirstStartApp = false;
        }

        switch (userInteractionType) {
            case "1" -> {
                System.out.println(HOW_TO_LOGIN);
                registerOrLogin(userInteractionScanner, true);
            }
            case "2" -> {
                System.out.println(REGISTER_OR_LOGIN);
                registerOrLogin(userInteractionScanner, false);
            }
            case "3" -> {
                System.out.println(EXIT_TEXT);
                userInteractionScanner.close();
                System.exit(0);
            }
            default -> {
                System.out.println(WRONG_INPUT_ERROR);
                startSession();
            }
        }
    }

    /**
     * Метод регистрирует юзера при первом вводе личных данных, либо авторизует при повторном вводе.
     * Если указан статус - работа с админом, то авторизует его.
     * @param userInteractionScanner сканер для приёма команд от пользователя в консоли
     * @param isAdminToWorkWith статус, работает ли метод с администратором, или нет
     */
    public static void registerOrLogin(Scanner userInteractionScanner, Boolean isAdminToWorkWith) {
        String name = userInteractionScanner.next();
        String password = userInteractionScanner.next();

        User userToWorkWith;

        if (isAdminToWorkWith) {
            userToWorkWith = new User(name, password, true);

            if (Data.getUsersAndAdminData().contains(userToWorkWith)) {
                System.out.println(LOGIN_SUCCESS);
                AdminSession.startAdminSession(userToWorkWith);
            } else {
                System.out.println(WRONG_ADMIN_LOGIN_DATA);
                startSession();
            }
        } else {
            userToWorkWith = new User(name, password, false);

            if (name.equals(Preparator.ADMIN_NAME)) {
                System.out.println(ADMIN_WRONG_COMMAND_ERROR);
                startSession();
            } else if (Data.getUsersAndAdminData().contains(userToWorkWith)) {
                System.out.println(LOGIN_SUCCESS);
                UserSession.startUserSession(userToWorkWith);
            } else {
                UserService.addUser(userToWorkWith);
                System.out.println(USER_REGISTERED);
                startSession();
            }
        }
    }
}
