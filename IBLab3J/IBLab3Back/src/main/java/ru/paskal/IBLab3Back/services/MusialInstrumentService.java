package ru.paskal.IBLab3Back.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.paskal.IBLab3Back.exceptions.notFound.MusicalInstrumentNotFoundException;
import ru.paskal.IBLab3Back.models.MusicalInstrument;
import ru.paskal.IBLab3Back.repositories.MusicalInstrumentRepository;

/**
 * Сервис для работы с музыкальными инструментами.
 */
//@Profile("profileBack")
@Service
@Transactional(readOnly = true)
public class MusialInstrumentService {

  private final MusicalInstrumentRepository repository;

  /**
   * Конструктор сервиса.
   *
   * @param repository репозиторий музыкальных инструментов
   */
  @Autowired
  public MusialInstrumentService(
      MusicalInstrumentRepository repository) {
    this.repository = repository;
  }

  /**
   * Получает все музыкальные инструменты.
   *
   * @return список музыкальных инструментов
   */
  public List<MusicalInstrument> findAll() {
    return repository.findAll();
  }

  /**
   * Находит музыкальный инструмент по идентификатору.
   *
   * @param id идентификатор инструмента
   * @return найденный инструмент или null, если не найден
   */
  public MusicalInstrument findOne(int id) throws MusicalInstrumentNotFoundException {
    return repository.findById(id).orElseThrow(() -> new MusicalInstrumentNotFoundException(id));
  }

  /**
   * Сохраняет новый музыкальный инструмент.
   *
   * @param musicalInstrument объект музыкального инструмента
   */
  @Transactional
  public void save(MusicalInstrument musicalInstrument) {
    repository.save(musicalInstrument);
  }

  /**
   * Обновляет информацию о музыкальном инструменте.
   *
   * @param id                идентификатор инструмента
   * @param musicalInstrument объект музыкального инструмента
   */
  @Transactional
  public void update(int id, MusicalInstrument musicalInstrument) throws MusicalInstrumentNotFoundException {
    if (repository.existsById(id)) {
      musicalInstrument.setId(id);
      repository.save(musicalInstrument);
    } else {
      throw new MusicalInstrumentNotFoundException(id);
    }

  }

  /**
   * Удаляет музыкальный инструмент по идентификатору.
   *
   * @param id идентификатор инструмента
   */
  @Transactional
  public void delete(int id) throws MusicalInstrumentNotFoundException {
    if (repository.existsById(id)) {
      repository.deleteById(id);
    } else {
      throw new MusicalInstrumentNotFoundException(id);
    }
  }

}
