package model;

import java.util.Objects;

/**
 * Класс-сущность пользователя системы.
 * Это либо обычный юзер, либо администратор, если isAdmin == true.
 */
public class User {
    public final String name;
    public final String password;
    public final Boolean isAdmin;

    public User(String name, String password, Boolean isAdmin) {
        this.name = name;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;

        return Objects.equals(name, user.name) && Objects.equals(password, user.password)
                && Objects.equals(isAdmin, user.isAdmin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, isAdmin);
    }
}
