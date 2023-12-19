package ru.paskal.IBLab3Back.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.paskal.IBLab3Back.models.User;

/**
 * Репозиторий для взаимодействия с данными о пользователях в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  /**
   * Метод для поиска пользователя по его имени.
   *
   * @param username Имя пользователя для поиска.
   * @return Объект Optional с найденным пользователем (если существует).
   */
  Optional<User> findByUsername(String username);

  /**
   * Метод для поиска пользователя по его имени и паролю.
   *
   * @param username Имя пользователя.
   * @param password Пароль пользователя.
   * @return Объект Optional с найденным пользователем (если существует).
   */
  Optional<User> findByUsernameAndPassword(String username, String password);

  /**
   * Метод для поиска пользователя по его токену.
   *
   * @param token Токен пользователя для поиска.
   * @return Объект Optional с найденным пользователем (если существует).
   */
  Optional<User> findByToken(String token);

}
