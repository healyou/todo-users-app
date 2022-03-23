package ru.lappi.users.controller.data;

import ru.lappi.users.entity.User;

import java.util.Set;

/**
 * Данные пользователя, возвращаемые rest контроллером
 *
 * @author Nikita Gorodilov
 */
public class UserData {
    private final Long userId;
    private final Set<String> privilegeCodes;

    public UserData(User user) {
        this.userId = user.getId();
        this.privilegeCodes = user.getAllPrivilegeCodes();
    }

    public Long getUserId() {
        return userId;
    }

    public Set<String> getPrivilegeCodes() {
        return privilegeCodes;
    }
}
