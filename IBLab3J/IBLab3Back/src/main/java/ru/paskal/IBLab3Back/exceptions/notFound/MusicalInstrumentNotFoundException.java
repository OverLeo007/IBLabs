package ru.paskal.IBLab3Back.exceptions.notFound;

public class MusicalInstrumentNotFoundException extends ModelNotFoundException {

    public static final String entityType = "MusicalInstrument";

    public MusicalInstrumentNotFoundException(String msg) {
        super(entityType, msg);
    }

    public MusicalInstrumentNotFoundException(int id) {
        super(entityType, id);
    }

}
