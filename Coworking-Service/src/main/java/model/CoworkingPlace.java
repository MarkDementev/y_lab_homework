package model;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс-сущность места для бронирования.
 * Это либо коворкинг, либо конференц-зал, если isConferenceRoom == true.
 * При создании сущности создаёт для неё окна для записи в виде часов с 9 утра до 20 часов вечера.
 * И на 7 дней вперёд. Т.е. занять рабочее место можно в интервале 9-21:00 на 7 дней вперёд.
 */
public class CoworkingPlace {
    public final Integer placeNumber;
    public final Boolean isConferenceRoom;
    public final Map<LocalDate, Map<Integer, User>> bookingHoursMap;

    public CoworkingPlace(Integer placeNumber, Boolean isConferenceRoom) {
        this.placeNumber = placeNumber;
        this.isConferenceRoom = isConferenceRoom;
        this.bookingHoursMap = formBookingHoursMap();
    }

    /**
     * При создании сущности создаёт для неё окна для записи в виде часов.
     * @return Map<LocalDate, Map<Integer, User>> bookingHoursMap - окна для записи.
     */
    private Map<LocalDate, Map<Integer, User>> formBookingHoursMap() {
        LocalDate day = LocalDate.now();
        Map<LocalDate, Map<Integer, User>> bookingHoursMap = new HashMap<>();

        for (int i = 0; i < 7; i++) {
            day = day.plusDays(1);
            Map<Integer, User> dayHoursToBooking = new HashMap<>();

            for (int i2 = 9; i2 < 21; i2++) {
                dayHoursToBooking.put(i2, null);
            }
            bookingHoursMap.put(day, dayHoursToBooking);
        }
        return bookingHoursMap;
    }
}
