package ru.paskal.IBLab3Back.exceptions.notFound;

public class SessionNotFoundException extends ModelNotFoundException {

    public static final String entityType = "Session";

    public SessionNotFoundException(String msg) {
        super(entityType, msg);
    }
}
