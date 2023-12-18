package ru.paskal.IBLab3Back.services;

import static ru.paskal.IBLab3Back.security.AES.decrypt;
import static ru.paskal.IBLab3Back.security.AES.encrypt;
import static ru.paskal.IBLab3Back.utils.MathUtils.bytesToHex;
import static ru.paskal.IBLab3Back.utils.MathUtils.getHash;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.paskal.IBLab3Back.dto.auth.AuthRegDto;
import ru.paskal.IBLab3Back.exceptions.notCreated.UserNotCreatedException;
import ru.paskal.IBLab3Back.exceptions.notFound.UserNotFoundException;
import ru.paskal.IBLab3Back.security.DiffieHellman;
import ru.paskal.IBLab3Back.dto.auth.AuthBaseDto;
import ru.paskal.IBLab3Back.dto.auth.AuthVerifyDto;
import ru.paskal.IBLab3Back.dto.auth.dhKeys.AuthFinDto;
import ru.paskal.IBLab3Back.dto.auth.dhKeys.AuthFinRespDto;
import ru.paskal.IBLab3Back.dto.auth.dhKeys.AuthInitDto;
import ru.paskal.IBLab3Back.models.User;
import ru.paskal.IBLab3Back.repositories.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {

  public HashMap<String, DiffieHellman> sessions = new HashMap<>();

  private final UserRepository repository;


  @Autowired
  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public AuthInitDto getDH(AuthBaseDto session) {
    String sessionId = session.getSessionId();
    sessions.remove(sessionId);
    sessionId = UUID.randomUUID().toString();
    DiffieHellman newDH = new DiffieHellman();
    sessions.put(sessionId, newDH);
    return new AuthInitDto(sessionId, newDH.getP(), newDH.getG());

  }

  public AuthFinRespDto setAgetB(AuthFinDto a) {
    String sessionId = a.getSessionId();
    DiffieHellman dh = sessions.get(sessionId);
    dh.setFromA(new BigInteger(a.getA()));
    return new AuthFinRespDto(sessionId, dh.getB());
  }

  public HttpStatus verifySession(AuthVerifyDto authVerifyDto) throws Exception {
    String sessionId = authVerifyDto.getSessionId();
    DiffieHellman dh = sessions.get(sessionId);

    System.out.println("Token to verify -> " + decrypt(authVerifyDto.getToken(), dh.getK()));

    if (sessions.containsKey(sessionId) &&
        repository.findByToken(
            decrypt(authVerifyDto.getToken(), dh.getK())
        ).isPresent()) {
      return HttpStatus.OK;
    }
    return HttpStatus.FORBIDDEN;
  }


  @Transactional
  public AuthVerifyDto register(AuthRegDto authRegDto) throws Exception {

    String sessionId = authRegDto.getSessionId();
    DiffieHellman dh = sessions.get(sessionId);

    String username = decrypt(authRegDto.getUsername(), dh.getK());
    String password = decrypt(authRegDto.getPassword(), dh.getK());

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
    return new AuthVerifyDto(sessionId, encrypt(token, dh.getK()));
  }

  @Transactional
  public AuthVerifyDto login(AuthRegDto authLogDto) throws Exception {

    String sessionId = authLogDto.getSessionId();
    DiffieHellman dh = sessions.get(sessionId);

    String username = decrypt(authLogDto.getUsername(), dh.getK());
    String password = decrypt(authLogDto.getPassword(), dh.getK());
    password = bytesToHex(getHash(password));

    Optional<User> optionalUser = repository.findByUsernameAndPassword(username, password);
    if (optionalUser.isEmpty()) {
      throw new UserNotFoundException(username);
    }
    User user = optionalUser.get();
    String token = UUID.randomUUID().toString();


    user.setToken(token);
    repository.save(user);
    return new AuthVerifyDto(sessionId, encrypt(token, dh.getK()));
  }
}
