package ru.paskal.IBLab3Back.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.paskal.IBLab3Back.exceptions.notCreated.ModelNotCreatedException;
import ru.paskal.IBLab3Back.exceptions.notDeleted.ModelNotDeletedException;
import ru.paskal.IBLab3Back.exceptions.notFound.ModelNotFoundException;
import ru.paskal.IBLab3Back.exceptions.notFound.SessionNotFoundException;
import ru.paskal.IBLab3Back.exceptions.notUpdated.ModelNotUpdatedException;
import ru.paskal.IBLab3Back.exceptions.responses.ErrorResponse;

/**
 * Класс для обработки ошибок операций CRUD.
 *
 * @param <C> Тип исключения при создании модели.
 * @param <R> Тип исключения при чтении модели.
 * @param <U> Тип исключения при обновлении модели.
 * @param <D> Тип исключения при удалении модели.
 */
public class CrudErrorHandlers
    <
    C extends ModelNotCreatedException,
    R extends ModelNotFoundException,
    U extends ModelNotUpdatedException,
    D extends ModelNotDeletedException
        > {

  /**
   * Обработчик исключения при создании модели.
   *
   * @param e Исключение при создании модели.
   * @return Ответ с сообщением об ошибке и HTTP статусом UNPROCESSABLE_ENTITY.
   */
  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleCreateException(C e) {
    return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  /**
   * Обработчик исключения при чтении модели.
   *
   * @param e Исключение при чтении модели.
   * @return Ответ с сообщением об ошибке и HTTP статусом NOT_FOUND.
   */
  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleReadException(R e) {
    return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
  }


  /**
   * Обработчик исключения при обновлении модели.
   *
   * @param e Исключение при обновлении модели.
   * @return Ответ с сообщением об ошибке и HTTP статусом UNPROCESSABLE_ENTITY.
   */
  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleUpdateException(U e) {
    return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
  }


  /**
   * Обработчик исключения при удалении модели.
   *
   * @param e Исключение при удалении модели.
   * @return Ответ с сообщением об ошибке и HTTP статусом NOT_FOUND.
   */
  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleDeleteException(D e) {
    return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  /**
   * Обработчик исключения при отсутствии сессии.
   *
   * @param e Исключение при отсутствии сессии.
   * @return Ответ с сообщением об ошибке и HTTP статусом FORBIDDEN.
   */
  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleNoSession(SessionNotFoundException e) {
    return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.FORBIDDEN);
  }
}
