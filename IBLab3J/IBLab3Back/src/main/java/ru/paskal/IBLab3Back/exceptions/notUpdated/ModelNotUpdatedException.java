package ru.paskal.IBLab3Back.exceptions.notUpdated;

public class ModelNotUpdatedException extends RuntimeException {
  public ModelNotUpdatedException(String entityType , String msg) {
    super(entityType + " not updated because: " + msg);
  }

}
