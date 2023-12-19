package ru.paskal.IBLab3Back.services;

import static ru.paskal.IBLab3Back.security.AES.decrypt;
import static ru.paskal.IBLab3Back.security.AES.encrypt;
import static ru.paskal.IBLab3Back.utils.MathUtils.bytesToHex;
import static ru.paskal.IBLab3Back.utils.MathUtils.getHash;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.paskal.IBLab3Back.dto.auth.AuthRegDto;
import ru.paskal.IBLab3Back.exceptions.notCreated.UserNotCreatedException;
import ru.paskal.IBLab3Back.exceptions.notFound.SessionNotFoundException;
import ru.paskal.IBLab3Back.exceptions.notFound.UserNotFoundException;
import ru.paskal.IBLab3Back.security.DiffieHellman;
import ru.paskal.IBLab3Back.dto.auth.AuthBaseDto;
import ru.paskal.IBLab3Back.dto.auth.AuthVerifyDto;
import ru.paskal.IBLab3Back.dto.auth.dhKeys.AuthFinDto;
import ru.paskal.IBLab3Back.dto.auth.dhKeys.AuthFinRespDto;
import ru.paskal.IBLab3Back.dto.auth.dhKeys.AuthInitDto;
import ru.paskal.IBLab3Back.models.User;
import ru.paskal.IBLab3Back.repositories.UserRepository;

/**
 * Сервис для управления пользователями и аутентификации.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

  /**
   * Словарь с парами Сессия - Diffie-Hellman
   */
  public HashMap<String, DiffieHellman> sessions = new HashMap<>();

  /**
   * Репозиторий для взаимодействия с данными о пользователях.
   */
  private final UserRepository repository;

  /**
   * Конструктор класса, инициализирующий репозиторий.
   *
   * @param repository Репозиторий пользователей.
   */
  @Autowired
  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  /**
   * Метод для инициализации процесса аутентификации и генерации Diffie-Hellman параметров.
   *
   * @param session DTO для инициализации аутентификации.
   * @return DTO с параметрами Diffie-Hellman для клиента.
   */
  public AuthInitDto getDH(AuthBaseDto session) {
    String sessionId = session.getSessionId();
    sessions.remove(sessionId);
    sessionId = UUID.randomUUID().toString();
    DiffieHellman newDH = new DiffieHellman();
    sessions.put(sessionId, newDH);
    return new AuthInitDto(sessionId, newDH.getP(), newDH.getG());

  }

  /**
   * Метод для установки значения A и вычисления B в процессе аутентификации.
   *
   * @param a DTO с параметром A от клиента.
   * @return DTO с параметром B для клиента.
   */
  public AuthFinRespDto setAgetB(AuthFinDto a) {
    String sessionId = a.getSessionId();
    DiffieHellman dh = sessions.get(sessionId);
    dh.setFromA(new BigInteger(a.getA()));
    return new AuthFinRespDto(sessionId, dh.getB());
  }

  /**
   * Метод для проверки авторизации пользователя.
   *
   * @param authVerifyDto DTO с данными для проверки пользователя.
   * @return HTTP статус в зависимости от результата проверки.
   * @throws Exception Исключение, которое может возникнуть при выполнении операции.
   */
  public HttpStatus verifyUser(AuthVerifyDto authVerifyDto) throws Exception {
    try {
      User user = getUserFromToken(authVerifyDto);
    } catch (UserNotFoundException e) {
      return HttpStatus.FORBIDDEN;
    } catch (SessionNotFoundException e) {
      return HttpStatus.BAD_GATEWAY;
    }
    return HttpStatus.OK;
  }

  /**
   * Метод для получения пользователя по токену из DTO.
   *
   * @param authVerifyDto DTO с токеном пользователя.
   * @return Найденный пользователь.
   * @throws Exception Исключение, которое может возникнуть при выполнении операции.
   */
  public User getUserFromToken(AuthVerifyDto authVerifyDto) throws Exception {

    byte[] key = getKeyFromSession(authVerifyDto);
    String token = decrypt(authVerifyDto.getToken(), key);
    Optional <User> user = repository.findByToken(token);
    return user.orElseThrow(() -> new UserNotFoundException("no user with token: " + token));
  }

  /**
   * Метод для регистрации нового пользователя.
   *
   * @param authRegDto DTO с данными для регистрации.
   * @return DTO с подтверждением регистрации (токеном, выданным пользователю).
   * @throws Exception Исключение, которое может возникнуть при выполнении операции.
   */
  @Transactional
  public AuthVerifyDto register(AuthRegDto authRegDto) throws Exception {

    String sessionId = authRegDto.getSessionId();
    byte[] key = getKeyFromSession(authRegDto);

    String username = decrypt(authRegDto.getUsername(), key);
    String password = decrypt(authRegDto.getPassword(), key);

    String token = UUID.randomUUID().toString();

    if (repository.findByUsername(username).isPresent()) {
      throw new UserNotCreatedException("Username is already taken");
    }
    User newUser = new User(
        username,
        bytesToHex(getHash(password)),
        token
    );

    repository.save(newUser);
    return new AuthVerifyDto(sessionId, encrypt(token, key));
  }

  /**
   * Метод для входа пользователя в систему.
   *
   * @param authLogDto DTO с данными для входа.
   * @return DTO с подтверждением входа пользователя (токеном, выданным пользователю).
   * @throws Exception Исключение, которое может возникнуть при выполнении операции.
   */
  @Transactional
  public AuthVerifyDto login(AuthRegDto authLogDto) throws Exception {

    String sessionId = authLogDto.getSessionId();
    byte[] key = getKeyFromSession(authLogDto);

    String username = decrypt(authLogDto.getUsername(), key);
    String password = decrypt(authLogDto.getPassword(), key);
    password = bytesToHex(getHash(password));

    Optional<User> optionalUser = repository.findByUsernameAndPassword(username, password);
    if (optionalUser.isEmpty()) {
      throw new UserNotFoundException(username);
    }
    User user = optionalUser.get();
    String token = UUID.randomUUID().toString();


    user.setToken(token);
    repository.save(user);
    return new AuthVerifyDto(sessionId, encrypt(token, key));
  }

  /**
   * Метод для получения общего ключа из текущей сессии по ID сессии.
   *
   * @param authDto DTO с ID сессии.
   * @return Общий ключ Diffie-Hellman для шифрования и дешифрования данных.
   */
  public <T extends AuthBaseDto> byte[] getKeyFromSession(T authDto) {
    String sessionId = authDto.getSessionId();
    if (!sessions.containsKey(authDto.getSessionId())) {
      throw new SessionNotFoundException("Session with id=" + sessionId + " not found!");
    }
    DiffieHellman dh = sessions.get(authDto.getSessionId());
    return dh.getK();
  }
}
