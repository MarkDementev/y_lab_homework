package service;

import data.Data;

import model.CoworkingPlace;
import model.User;

import java.time.LocalDate;

import java.util.*;

/**
 * Сервис для обслуживания CRUD-операций взаимодействия с {@link CoworkingPlace} и {@link Data}.
 */
public class CoworkingPlaceService {
    /**
     * Создание коворкинга/конференц-зала
     * @param newCoworkingPlace данные о новом коворкинге/конференц-зале
     */
    public static void addCoworkingPlace(CoworkingPlace newCoworkingPlace) {
        if (newCoworkingPlace.isConferenceRoom && Data.getAlreadyExistsConferenceNumbers()
                .contains(newCoworkingPlace.placeNumber)) {
            throw new IllegalArgumentException("Конференц-зал с номером " + newCoworkingPlace.placeNumber
                    + "уже существует в системе!");
        } else if (!newCoworkingPlace.isConferenceRoom && Data.getAlreadyExistsCoworkingNumbers()
                .contains(newCoworkingPlace.placeNumber)) {
            throw new IllegalArgumentException("Рабочее место с номером " + newCoworkingPlace.placeNumber
                    + "уже существует в системе!");
        } else {
            Data.getCoworkingPlaces().add(newCoworkingPlace);

            if (newCoworkingPlace.isConferenceRoom) {
                Data.getAlreadyExistsConferenceNumbers().add(newCoworkingPlace.placeNumber);
            } else {
                Data.getAlreadyExistsCoworkingNumbers().add(newCoworkingPlace.placeNumber);
            }
        }
    }

    /**
     * Возвращает список всех доступных рабочих мест и конференц-залов.
     * @return Set<String> freePlacesTypesAndNumbers - список всех доступных рабочих мест и конференц-залов
     */
    public static Set<String> getAllFreePlaces() {
        Set<String> freePlacesTypesAndNumbers = new HashSet<>();

        for (CoworkingPlace coworkingPlace : Data.getCoworkingPlaces()) {
            for (Map.Entry<LocalDate, Map<Integer, User>> coworkingDay : coworkingPlace.bookingHoursMap.entrySet()) {
                for (Map.Entry<Integer, User> coworkingDayHour : coworkingDay.getValue().entrySet()) {
                    if (coworkingDayHour.getValue() == null) {
                        if (coworkingPlace.isConferenceRoom) {
                            freePlacesTypesAndNumbers.add("Конференц-зал с номером " + coworkingPlace.placeNumber);
                        } else {
                            freePlacesTypesAndNumbers.add("Рабочее место с номером " + coworkingPlace.placeNumber);
                        }
                    }
                }
            }
        }
        return freePlacesTypesAndNumbers;
    }

    /**
     * Возвращает доступные слоты для бронирования на конкретную дату.
     * @param dateToShow дата в LocalDate, на которую показаны свободные часы
     * @return Map<String, List<Integer>> freeHoursInCoworkings - доступные слоты для бронирования на конкретную дату
     */
    public static Map<String, List<Integer>> getFreeHoursOnDay(LocalDate dateToShow) {
        Map<String, List<Integer>> freeHoursInCoworkings = new HashMap<>();

        for (CoworkingPlace coworkingPlace : Data.getCoworkingPlaces()) {
            List<Integer> freeHours = new ArrayList<>();

            for (Map.Entry<LocalDate, Map<Integer, User>> coworkingDay : coworkingPlace.bookingHoursMap.entrySet()) {
                if (coworkingDay.getKey().equals(dateToShow)) {
                    for (Map.Entry<Integer, User> coworkingDayHour : coworkingDay.getValue().entrySet()) {
                        if (coworkingDayHour.getValue() == null) {
                            freeHours.add(coworkingDayHour.getKey());
                        }
                    }
                }
            }
            Collections.sort(freeHours);

            if (coworkingPlace.isConferenceRoom) {
                freeHoursInCoworkings.put("Конференц-зал с номером " + coworkingPlace.placeNumber, freeHours);
            } else {
                freeHoursInCoworkings.put("Рабочее место с номером " + coworkingPlace.placeNumber, freeHours);
            }
        }
        return freeHoursInCoworkings;
    }

    /**
     * Метод для бронирования на определённый час
     * @param coworkingPlaceToBooking рабочее место, выбранное для бронирования
     * @param dateToBook дата, выбранная для бронирования
     * @param hourToBook час, выбранный для бронирования
     * @param user юзер, бронь на которого оформляется
     */
    public static void bookHour(CoworkingPlace coworkingPlaceToBooking, LocalDate dateToBook, Integer hourToBook,
                                User user) {
        for (CoworkingPlace coworkingPlace : Data.getCoworkingPlaces()) {
            if (coworkingPlace.placeNumber == coworkingPlaceToBooking.placeNumber) {
                Map<Integer, User> hoursMap = coworkingPlace.bookingHoursMap.get(dateToBook);
                hoursMap.put(hourToBook, user);
            }
        }
    }

    /**
     * Метод для отмены бронирования
     * @param user юзер, бронь которого удаляется
     */
    public static void deleteBookingsForUser(User user) {
        for (CoworkingPlace coworkingPlace : Data.getCoworkingPlaces()) {
            for (Map.Entry<LocalDate, Map<Integer, User>> coworkingDay : coworkingPlace.bookingHoursMap.entrySet()) {
                for (Map.Entry<Integer, User> coworkingDayHour : coworkingDay.getValue().entrySet()) {
                    if (coworkingDayHour.getValue() == user) {
                        coworkingDayHour.setValue(null);
                    }
                }
            }
        }
    }
}
