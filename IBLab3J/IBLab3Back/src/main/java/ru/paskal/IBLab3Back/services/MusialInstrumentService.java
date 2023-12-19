package ru.paskal.IBLab3Back.services;

import static ru.paskal.IBLab3Back.security.AES.decrypt;
import static ru.paskal.IBLab3Back.utils.InstrumentAESMapper.decryptInstrument;
import static ru.paskal.IBLab3Back.utils.InstrumentAESMapper.encryptInstrument;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.paskal.IBLab3Back.dto.auth.AuthVerifyDto;
import ru.paskal.IBLab3Back.dto.instruments.InstrumentCreateDto;
import ru.paskal.IBLab3Back.dto.instruments.InstrumentDeleteDto;
import ru.paskal.IBLab3Back.dto.instruments.InstrumentDto;
import ru.paskal.IBLab3Back.dto.instruments.InstrumentEditDto;
import ru.paskal.IBLab3Back.dto.instruments.InstrumentListDto;
import ru.paskal.IBLab3Back.exceptions.notFound.MusicalInstrumentNotFoundException;
import ru.paskal.IBLab3Back.models.MusicalInstrument;
import ru.paskal.IBLab3Back.models.User;
import ru.paskal.IBLab3Back.repositories.MusicalInstrumentRepository;
import ru.paskal.IBLab3Back.security.DiffieHellman;

/**
 * Сервис для обработки операций с музыкальными инструментами.
 */
@Service
@Transactional(readOnly = true)
public class MusialInstrumentService {

  /**
   * Репозиторий для взаимодействия с данными о музыкальных инструментах.
   */
  private final MusicalInstrumentRepository repository;

  /**
   * Сервис для работы с пользователями.
   */
  private final UserService userService;

  /**
   * Объект для отображения данных между сущностями и DTO.
   */
  private final ModelMapper modelMapper;

  /**
   * Конструктор класса, инициализирующий необходимые зависимости.
   *
   * @param repository   Репозиторий музыкальных инструментов.
   * @param userService  Сервис пользователей.
   * @param modelMapper  Объект для отображения данных между сущностями и DTO.
   */
  @Autowired
  public MusialInstrumentService(MusicalInstrumentRepository repository, UserService userService,
      ModelMapper modelMapper) {
    this.repository = repository;
    this.userService = userService;
    this.modelMapper = modelMapper;
  }

  /**
   * Метод для поиска музыкальных инструментов, принадлежащих пользователю.
   *
   * @param token DTO для аутентификации пользователя.
   * @return DTO с информацией о музыкальных инструментах пользователя.
   * @throws Exception Исключение, которое может возникнуть при выполнении операции.
   */
  @Transactional
  public InstrumentListDto findByUser(AuthVerifyDto token) throws Exception {
    byte[] key = userService.getKeyFromSession(token);
    User user = userService.getUserFromToken(token);
    List<MusicalInstrument> instruments = repository.findByUser(user);
    if (instruments.isEmpty()) {
      repository.insertExampleByUserId(user.getId());
      return mapInstruments(repository.findByUser(user), key);
    } else {
      return mapInstruments(instruments, key);
    }
  }

  /**
   * Метод для сохранения нового музыкального инструмента.
   *
   * @param instrumentCreateDto DTO с информацией о создаваемом инструменте.
   * @return DTO с информацией о сохраненном инструменте.
   * @throws Exception Исключение, которое может возникнуть при выполнении операции.
   */
  @Transactional
  public InstrumentDto save(InstrumentCreateDto instrumentCreateDto) throws Exception {
    byte[] key = userService.getKeyFromSession(instrumentCreateDto);
    InstrumentDto instrumentDto = modelMapper.map(instrumentCreateDto, InstrumentDto.class);
    instrumentDto.setId("null");
    MusicalInstrument newInstrument = decryptInstrument(instrumentDto, key);
    newInstrument.setUser(userService.getUserFromToken(instrumentCreateDto));
    repository.save(newInstrument);
    return encryptInstrument(newInstrument, key);
  }

  /**
   * Метод для обновления информации о существующем музыкальном инструменте.
   *
   * @param instrumentEditDto DTO с информацией об изменяемом инструменте.
   * @throws Exception Исключение, которое может возникнуть при выполнении операции.
   */
  @Transactional
  public void update(InstrumentEditDto instrumentEditDto) throws Exception {
    byte[] key = userService.getKeyFromSession(instrumentEditDto);
    InstrumentDto instrumentDto = modelMapper.map(instrumentEditDto, InstrumentDto.class);
    MusicalInstrument edited = decryptInstrument(instrumentDto, key);
    if (!repository.existsById(edited.getId())) {
      throw new MusicalInstrumentNotFoundException(edited.getId());
    }
    edited.setUser(userService.getUserFromToken(instrumentEditDto));
    repository.save(edited);
  }


  /**
   * Метод для удаления музыкального инструмента по его идентификатору.
   *
   * @param token DTO с информацией об удаляемом инструменте.
   * @throws Exception Исключение, которое может возникнуть при выполнении операции.
   */
  @Transactional
  public void delete(InstrumentDeleteDto token) throws Exception {
    byte[] key = userService.getKeyFromSession(token);
    Integer id = Integer.parseInt(decrypt(token.getId(), key));
    if (repository.existsById(id)) {
      repository.deleteById(id);
    } else {
      throw new MusicalInstrumentNotFoundException(id);
    }
  }

  /**
   * Метод для отображения списка музыкальных инструментов с использованием общего ключа.
   *
   * @param instruments Список музыкальных инструментов.
   * @param key         Общий ключ для шифрования и дешифрования данных.
   * @return DTO с информацией о музыкальных инструментах.
   * @throws RuntimeException Исключение, которое может возникнуть при выполнении операции.
   */
  private InstrumentListDto mapInstruments(List<MusicalInstrument> instruments, byte[] key)
      throws RuntimeException {
    return new InstrumentListDto(instruments.stream().map(instrument -> {
      try {
        return encryptInstrument(instrument, key);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).toList());
  }

}
