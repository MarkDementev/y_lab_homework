import core.session.Session;

/**
 * Стартовый класс проекта
 * @author MarkDementev a.k.a JavaMarkDem
 */
public class App {
    public static final String INTRODUCTION_MESSAGE = "Здравствуйте! Выберете свою роль, введя нужную цифру. " +
            "Введите 1 - если вы администратор сервиса, 2 - если пользователь, 3 - если хотите прекратить работу" +
            " программы.";

    /**
     * В методе приветствуется пользователь, ему поясняется, как взаимодействовать с программой.
     * После этого начинается общая сессия приёма команд в консоли от пользователя для выбора роли в системе.
     */
    public static void main(String[] args) {
        System.out.println(INTRODUCTION_MESSAGE);
        Session.startSession();
    }
}
