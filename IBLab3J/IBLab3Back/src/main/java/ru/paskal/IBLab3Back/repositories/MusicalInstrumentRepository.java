package ru.paskal.IBLab3Back.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.paskal.IBLab3Back.models.MusicalInstrument;
import ru.paskal.IBLab3Back.models.User;

/**
 * Репозиторий для взаимодействия с данными о музыкальных инструментах в базе данных.
 */
@Repository
public interface MusicalInstrumentRepository extends JpaRepository<MusicalInstrument, Integer> {

  /**
   * SQL-запрос для вставки примера данных музыкальных инструментов с указанным идентификатором пользователя.
   */
  String insertExampleSQL = """
      INSERT INTO musical_instrument (instrument_name, instrument_type, brand, price, quantity, uid)\s
      VALUES
          ('Acoustic Guitar', 'String', 'Fender', 799.99, 5, :userId),
          ('Electric Keyboard', 'Keyboard', 'Yamaha', 499.99, 3, :userId),
          ('Violin', 'String', 'Stradivarius', 5000.00, 2, :userId),
          ('Drum Set', 'Percussion', 'Pearl', 1200.50, 4, :userId),
          ('Saxophone', 'Wind', 'Yanagisawa', 3000.00, 2, :userId),
          ('Grand Piano', 'Keyboard', 'Steinway & Sons', 15000.00, 1, :userId),
          ('Flute', 'Wind', 'Muramatsu', 2000.00, 3, :userId),
          ('Bass Guitar', 'String', 'Ibanez', 899.99, 4, :userId),
          ('Trumpet', 'Brass', 'Bach', 1500.00, 2, :userId),
          ('Cello', 'String', 'D Z Strad', 3000.00, 2, :userId);
      """;

  /**
   * Метод для выполнения SQL-запроса по вставке примера данных музыкальных инструментов.
   *
   * @param userId Идентификатор пользователя.
   */
  @Modifying
  @Query(value = insertExampleSQL, nativeQuery = true)
  void insertExampleByUserId(@Param("userId") Integer userId);

  /**
   * Метод для поиска музыкальных инструментов, принадлежащих указанному пользователю.
   *
   * @param user Пользователь, чьи музыкальные инструменты необходимо найти.
   * @return Список музыкальных инструментов пользователя.
   */
  List<MusicalInstrument> findByUser(User user);
}
