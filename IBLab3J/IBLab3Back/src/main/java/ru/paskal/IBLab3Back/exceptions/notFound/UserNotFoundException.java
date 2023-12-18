package ru.paskal.IBLab3Back.exceptions.notFound;

public class UserNotFoundException extends ModelNotFoundException {

    public static final String entityType = "User";

    public UserNotFoundException(String msg) {
        super(entityType, msg);
    }
    public UserNotFoundException(int id) {
        super(entityType, id);
    }

}
