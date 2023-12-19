package ru.paskal.IBLab3Back.contorllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.paskal.IBLab3Back.dto.auth.AuthBaseDto;
import ru.paskal.IBLab3Back.dto.auth.AuthRegDto;
import ru.paskal.IBLab3Back.dto.auth.AuthVerifyDto;
import ru.paskal.IBLab3Back.dto.auth.dhKeys.AuthFinDto;
import ru.paskal.IBLab3Back.dto.auth.dhKeys.AuthFinRespDto;
import ru.paskal.IBLab3Back.dto.auth.dhKeys.AuthInitDto;
import ru.paskal.IBLab3Back.exceptions.notCreated.UserNotCreatedException;
import ru.paskal.IBLab3Back.exceptions.notDeleted.UserNotDeletedException;
import ru.paskal.IBLab3Back.exceptions.notFound.UserNotFoundException;
import ru.paskal.IBLab3Back.exceptions.notUpdated.UserNotUpdatedException;
import ru.paskal.IBLab3Back.services.UserService;
import ru.paskal.IBLab3Back.utils.CrudErrorHandlers;

/**
 * Контроллер для аутентификации и авторизации пользователей.
 */
@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/auth")
public class AuthController extends CrudErrorHandlers<
    UserNotCreatedException,
    UserNotFoundException,
    UserNotUpdatedException,
    UserNotDeletedException
    > {

  /**
   * Сервис для обработки запросов аутентификации и авторизации пользователей.
   */
  private final UserService service;

  /**
   * Конструктор контроллера.
   *
   * @param service Сервис для обработки запросов аутентификации и авторизации пользователей.
   */
  @Autowired
  public AuthController(UserService service) {
    this.service = service;
  }

  /**
   * Обработчик запроса на получение Diffie-Hellman сессии.
   *
   * @param session Объект сессии для аутентификации.
   * @return ResponseEntity с объектом AuthInitDto и статусом OK.
   */
  @PostMapping("/get_dh")
  public ResponseEntity<AuthInitDto> getSession(@RequestBody AuthBaseDto session) {
    AuthInitDto authInitDto = service.getDH(session);
   return new ResponseEntity<>(authInitDto, HttpStatus.OK);
  }

  /**
   * Обработчик запроса на получение открытого ключа B и завершение Diffie-Hellman обмена.
   *
   * @param A Объект с данными A для Diffie-Hellman обмена.
   * @return ResponseEntity с объектом AuthFinRespDto и статусом OK.
   */
  @PostMapping("/get_dh_key")
  public ResponseEntity<AuthFinRespDto> getBKey(@RequestBody AuthFinDto A) {
    AuthFinRespDto authFinRespDto = service.setAgetB(A);
    return new ResponseEntity<>(authFinRespDto, HttpStatus.OK);
  }

  /**
   * Обработчик запроса на верификацию пользователя.
   *
   * @param verifyDto Объект с данными для верификации пользователя.
   * @return ResponseEntity с HttpStatus в зависимости от результата верификации.
   * @throws Exception Исключение, которое может возникнуть при выполнении верификации.
   */
  @PostMapping("/verify")
  public ResponseEntity<HttpStatus> verifyUser(@RequestBody AuthVerifyDto verifyDto)
      throws Exception {
    return new ResponseEntity<>(service.verifyUser(verifyDto));
  }

  /**
   * Обработчик запроса на регистрацию нового пользователя.
   *
   * @param authRegDto Объект с данными для регистрации нового пользователя.
   * @return ResponseEntity с объектом AuthVerifyDto и статусом OK.
   * @throws UserNotCreatedException Исключение, которое может возникнуть при неудачной регистрации.
   */
  @PostMapping("/register")
  public ResponseEntity<AuthVerifyDto> register(@RequestBody AuthRegDto authRegDto) {
    try {
      AuthVerifyDto token =  service.register(authRegDto);
      return new ResponseEntity<>(token, HttpStatus.OK);
    } catch (Exception e) {
      throw new UserNotCreatedException(e.getMessage());
    }
  }

  /**
   * Обработчик запроса на вход пользователя в систему.
   *
   * @param authLogDto Объект с данными для входа пользователя.
   * @return ResponseEntity с объектом AuthVerifyDto и статусом OK.
   * @throws Exception Исключение, которое может возникнуть при выполнении входа пользователя.
   */
  @PostMapping("/login")
  public ResponseEntity<AuthVerifyDto> login(@RequestBody AuthRegDto authLogDto) throws Exception {
    AuthVerifyDto token = service.login(authLogDto);
    return new ResponseEntity<>(token, HttpStatus.OK);
  }
}
