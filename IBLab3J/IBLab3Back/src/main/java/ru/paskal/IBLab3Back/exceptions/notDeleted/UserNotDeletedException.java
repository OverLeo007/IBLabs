package ru.paskal.IBLab3Back.exceptions.notDeleted;

public class UserNotDeletedException extends ModelNotDeletedException {

    public static final String entityType = "User";

    public UserNotDeletedException(String msg) {
        super(entityType, msg);
    }
}
