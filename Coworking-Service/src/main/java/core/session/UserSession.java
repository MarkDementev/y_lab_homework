package core.session;

import data.Data;

import model.CoworkingPlace;
import model.User;

import service.CoworkingPlaceService;
import service.UserService;

import java.time.LocalDate;

import java.util.*;

/**
 * Класс - сессия для пользователя для взаимодействия с программой.
 * Обеспечивает приём консольных команд и ответы на них.
 */
public class UserSession extends Session {
    public static final String USER_OPERATIONS_LIST = """
            Выберите операцию:
            1 - Просмотр списка всех доступных рабочих мест и конференц-залов;
            2 - Просмотр доступных слотов для бронирования на конкретную дату;
            3 - Бронирование рабочего места или конференц-зала на определённое время и дату;
            4 - Отмена бронирования;
            5 - Удаление себя из списка пользователей сервиса.
            6 - Выход из системы.
            """;
    public static final String OFFER_MAKE_CHOICE_USER = "Введите числовую команду (1, 2, 3, 4, 5 или 6): ";
    public static final String SHOW_ALL_FREE_PLACES = "Свободные места в коворкинге следующие:";
    public static final String OFFER_ENTER_DATE = """
           Введите три числа - день в формате Д, месяц в формате М, год в формате ГГГГ, разделённые Enter.
           Учтите, что запись в коворкинг идёт максимум на неделю вперёд.
           К примеру, записываясь 21.06.2024, предельная дата на запись - 27.06.2024
           Ввести потребуется число 21, потом 6, потом 2024
           """;
    public static final String WRONG_DAY_INPUT = "День введён некорректно!";
    public static final String WRONG_MONTH_INPUT = "Месяц введён некорректно!";
    public static final String WRONG_YEAR_INPUT = "Год введён некорректно!";
    public static final String HOW_TO_BOOKING_INFO = """
           Для бронирования введите следующее.
           Введите три числа - день в формате Д, месяц в формате М, год в формате ГГГГ, разделённые Enter.
           К примеру, записываясь 21.06.2024, ввести потребуется число 21, потом 6, потом 2024
           Так вы выберете дату, на которую хотите забронировать.
           После этого вам будут продемонстрированы свободные часы в помещениях в эту дату, если они есть.
           Потом введите номер помещения для выбора часов бронирования. Если это помещение - конференц-зал,
           добавьте + сразу после номера помещения. Например, если хотите забронировать место в коворкинге с номером 2,
           введите 2, если место в конференц-зале с номером 1, введите 1+
           После этого потребуется завершить бронирование, указав через Enter ещё 2 числа - интервал бронирования.
           Например, если хотите забронировать время с 9:00 до 11:00, введите 9, потом 10. Это означает, что вы
           занимаете коворкинг в часы, начинающиеся в 9:00 и в 10:00. Минимальный интервал бронирования - 1 час,
           соответственно, вы забронируете интервал с 9:00 до 11:00, как задумывали. Если же хотите забронировать место
           только на час, потребуется ввести 2 одинаковых числа через Enter. Например, введя 9, потом 9, забронируется
           время на час, начиная с 9:00.
           Пожалуйста, введите теперь 3 числа, разделённые Enter:
           """;
    public static final String NO_FREE_HOURS_THIS_DAY = "К сожалению, в этот день всё занято, и забронировать" +
            " не выйдет!";
    public static final String WRONG_PLACE_TO_BOOKING_INPUT = "Данные для выбора места для бронирования введены" +
            " некорректно!";
    public static final String NOT_EXIST_PLACE_TO_BOOKING_INPUT = "с таким номером для бронирования " +
            "не существует.";
    public static final String WRONG_HOUR_TO_BOOKING_INPUT = "Данные для выбора часа для бронирования введены"
            + " некорректно!";
    public static final String TIME_BOOKED = "Время успешно забронировано!";
    public static final String BOOKINGS_DELETED = "Ваша бронь мест в коворкинге удалена.";
    public static final String USER_DELETED = "Ваши данные и возможная бронь мест в коворкинге - удалены.";
    public static final String ALREADY_BOOKED_TIME = "Это время для бронирования уже занято!";

    /**
     * Создаётся сканер. Метод используется для обеспечения бесперерывности приёма команд.
     * Если в следующем методе {userOperations(User user, String userInteractionType, Scanner userInteractionScanner)}
     * юзер ввёл команду неправильно, или может выполнить ещё команду после выполненной успешно, он возвращается снова
     * сюда, чтобы ввести команду заново, или прекратить работу с программой.
     * @param user данные о пользователе программы
     */
    static void startUserSession(User user) {
        System.out.print(USER_OPERATIONS_LIST);
        System.out.print(OFFER_MAKE_CHOICE_USER);
        Scanner userInteractionScanner = new Scanner(System.in);
        userOperations(user, userInteractionScanner.next(), userInteractionScanner);
    }

    /**
     * Метод обрабатывает консольные команды.
     * Позволяет пользователю:
     * просмотр списка всех доступных рабочих мест и конференц-залов;
     * просмотр доступных слотов для бронирования на конкретную дату;
     * бронирование рабочего места или конференц-зала на определённое время и дату;
     * отмену бронирования;
     * удаление себя из системы
     * выход из программы
     * @param user данные о юзере данной юзер-сессии
     * @param enteredCommand введённая числовая команда в консоли
     * @param userInteractionScanner сканер
     */
    private static void userOperations(User user, String enteredCommand, Scanner userInteractionScanner) {
        switch (enteredCommand) {
            case "1" -> {
                System.out.println(SHOW_ALL_FREE_PLACES);
                System.out.println(CoworkingPlaceService.getAllFreePlaces());
                startUserSession(user);
            }
            case "2" -> {
                System.out.println(OFFER_ENTER_DATE);
                LocalDate dateToShow = inputDayMonthYearAndGetLocalDate(user, userInteractionScanner);
                System.out.println(CoworkingPlaceService.getFreeHoursOnDay(dateToShow));
                startUserSession(user);
            }
            case "3" -> {
                System.out.println(HOW_TO_BOOKING_INFO);

                LocalDate dateToBook = inputDayMonthYearAndGetLocalDate(user, userInteractionScanner);
                Map<String, List<Integer>> freeHoursInCoworkings = CoworkingPlaceService.getFreeHoursOnDay(dateToBook);
                checkIsFreeHoursThisDay(freeHoursInCoworkings, user);
                System.out.println(freeHoursInCoworkings);

                CoworkingPlace coworkingPlaceToBooking = inputPlaceToBookingAndGetIt(user, userInteractionScanner);

                bookTime(user, coworkingPlaceToBooking, dateToBook, userInteractionScanner);
                System.out.println(TIME_BOOKED);
                startUserSession(user);
            }
            case "4" -> {
                CoworkingPlaceService.deleteBookingsForUser(user);
                System.out.println(BOOKINGS_DELETED);
                startUserSession(user);
            }
            case "5" -> {
                UserService.deleteUser(user);
                System.out.println(USER_DELETED);
                startSession();
            }
            case "6" -> {
                System.out.println(EXIT_TEXT);
                userInteractionScanner.close();
                System.exit(0);
            }
            default -> {
                System.out.println(WRONG_INPUT_ERROR);
                startUserSession(user);
            }
        }
    }

    /**
     * Метод обеспечивает приём и валидацию даты в формате LocalDate
     * @param user данные о юзере, вводящем команды в консоль
     * @param userInteractionScanner сканер
     * @return преобразованные в LocalDate введённые через консоль данные о дате
     */
    private static LocalDate inputDayMonthYearAndGetLocalDate(User user, Scanner userInteractionScanner) {
        String day = userInteractionScanner.next();

        if (!(Integer.parseInt(day) > 0 && Integer.parseInt(day) < 32)) {
            System.out.println(WRONG_DAY_INPUT);
            startUserSession(user);
        }
        String month = userInteractionScanner.next();

        if (!(Integer.parseInt(month) > 0 && Integer.parseInt(month) < 13)) {
            System.out.println(WRONG_MONTH_INPUT);
            startUserSession(user);
        }
        String year = userInteractionScanner.next();

        if (!(Integer.parseInt(year) > 999)) {
            System.out.println(WRONG_YEAR_INPUT);
            startUserSession(user);
        }
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }

    /**
     * Проверяет, есть ли хоть одно свободное окно для бронирования в указанный день, возвращает их, если есть
     * @param freeHoursInCoworkings свободные часы на рабочих местах
     * @param user данные о юзере, вводящем команды в консоль
     */
    private static void checkIsFreeHoursThisDay(Map<String, List<Integer>> freeHoursInCoworkings, User user) {
        for (Map.Entry<String, List<Integer>> coworkingHours : freeHoursInCoworkings.entrySet()) {
            int freeHoursCount = 0;

            for (Integer hour : coworkingHours.getValue()) {
                if (hour != null) {
                    freeHoursCount++;
                }
            }

            if (freeHoursCount == 0) {
                System.out.println(NO_FREE_HOURS_THIS_DAY);
                startUserSession(user);
            }
        }
    }

    /**
     * Проверяет, существует ли запрошенный для бронирования коворкинг/конференц-зал, возвращает его, если есть.
     * @param user данные о юзере, вводящем команды в консоль
     * @param userInteractionScanner сканер
     * @return возвращает коворкинг/конференц-зал, если запрошенный в консоли существует.
     */
    private static CoworkingPlace inputPlaceToBookingAndGetIt(User user, Scanner userInteractionScanner) {
        String placeToBooking = userInteractionScanner.next();
        Set<CoworkingPlace> coworkingPlaces = Data.getCoworkingPlaces();

        if (placeToBooking.length() != 1 && placeToBooking.length() != 2) {
            System.out.println(WRONG_PLACE_TO_BOOKING_INPUT);
            startUserSession(user);
        }

        if (placeToBooking.length() == 1) {
            Integer placeToBookingToInteger = Integer.parseInt(placeToBooking);

            for (CoworkingPlace coworkingPlace : coworkingPlaces) {
                if (!coworkingPlace.isConferenceRoom
                        && Objects.equals(coworkingPlace.placeNumber, placeToBookingToInteger)) {
                    return coworkingPlace;
                }
            }
            System.out.println("Конференц-зал " + NOT_EXIST_PLACE_TO_BOOKING_INPUT);
            startUserSession(user);
        }

        if (placeToBooking.length() == 2) {
            String[] placeToBookingArr = placeToBooking.split("");
            Integer placeToBookingToInteger;

            if (!placeToBookingArr[1].equals("+")) {
                System.out.println(WRONG_PLACE_TO_BOOKING_INPUT);
                startUserSession(user);
            }
            placeToBookingToInteger = Integer.parseInt(placeToBookingArr[0]);

            for (CoworkingPlace coworkingPlace : coworkingPlaces) {
                if (coworkingPlace.isConferenceRoom
                        && Objects.equals(coworkingPlace.placeNumber, placeToBookingToInteger)) {
                    return coworkingPlace;
                }
            }
            System.out.println("Коворкинг " + NOT_EXIST_PLACE_TO_BOOKING_INPUT);
            startUserSession(user);
        }
        return null;
    }

    /**
     * Принимает в консоли даты бронирования и бронирует, если это возможно
     * @param user данные о юзере, вводящем команды в консоль
     * @param coworkingPlaceToBooking рабочее место, выбранное для бронирования
     * @param dateToBook дата, выбранная для бронирования
     * @param userInteractionScanner сканер
     */
    private static void bookTime(User user, CoworkingPlace coworkingPlaceToBooking, LocalDate dateToBook,
                                 Scanner userInteractionScanner) {
        String startBookingHour = userInteractionScanner.next();
        String endBookingHour = userInteractionScanner.next();
        int startBookingHourInteger = Integer.parseInt(startBookingHour);
        int endBookingHourInteger = Integer.parseInt(endBookingHour);

        if (!(startBookingHourInteger > 8 && startBookingHourInteger < 21)
                || !(endBookingHourInteger > 8 && endBookingHourInteger < 21)
                || !(endBookingHourInteger >= startBookingHourInteger)) {
            System.out.println(WRONG_HOUR_TO_BOOKING_INPUT);
            startUserSession(user);
        }
        Map<Integer, User> hoursMap = coworkingPlaceToBooking.bookingHoursMap.get(dateToBook);

        if (startBookingHourInteger == endBookingHourInteger) {
            if (hoursMap.get(startBookingHourInteger) == null) {
               CoworkingPlaceService.bookHour(coworkingPlaceToBooking, dateToBook, startBookingHourInteger, user);
            } else {
                System.out.println(ALREADY_BOOKED_TIME);
                startUserSession(user);
            }
        }

//        else {
//            for (Map.Entry<Integer, User> hour : hoursMap.entrySet()) {
//                if () {
//
//                }
//            }
//        }
    }
}
