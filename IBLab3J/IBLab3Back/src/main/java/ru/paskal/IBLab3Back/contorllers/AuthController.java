package ru.paskal.IBLab3Back.contorllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/auth")
public class AuthController extends CrudErrorHandlers<
    UserNotCreatedException,
    UserNotFoundException,
    UserNotUpdatedException,
    UserNotDeletedException
    > {

  private final UserService service;

  @Autowired
  public AuthController(UserService service) {
    this.service = service;
  }

  @PostMapping("/get_dh")
  public ResponseEntity<AuthInitDto> getSession(@RequestBody AuthBaseDto session) {
    AuthInitDto authInitDto = service.getDH(session);
   return new ResponseEntity<>(authInitDto, HttpStatus.OK);
  }

  @PostMapping("/get_dh_key")
  public ResponseEntity<AuthFinRespDto> getBKey(@RequestBody AuthFinDto A) {
    AuthFinRespDto authFinRespDto = service.setAgetB(A);
    return new ResponseEntity<>(authFinRespDto, HttpStatus.OK);
  }

  @PostMapping("/verify")
  public ResponseEntity<HttpStatus> verifySession(@RequestBody AuthVerifyDto verifyDto)
      throws Exception {
    return new ResponseEntity<>(service.verifySession(verifyDto));
  }

  @PostMapping("/register")
  public ResponseEntity<AuthVerifyDto> register(@RequestBody AuthRegDto authRegDto)
      throws Exception {
    AuthVerifyDto token =  service.register(authRegDto);
    return new ResponseEntity<>(token, HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthVerifyDto> login(@RequestBody AuthRegDto authLogDto) throws Exception {
    AuthVerifyDto token = service.login(authLogDto);
    return new ResponseEntity<>(token, HttpStatus.OK);
  }


}
