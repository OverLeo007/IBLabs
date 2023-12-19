package ru.paskal.IBLab3Back.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель пользователя.
 * Сущность, представляющая информацию о пользователе в системе.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

  /**
   * Уникальный идентификатор пользователя.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  /**
   * Имя пользователя.
   * Длина должна быть от 2 до 100 символов.
   */
  @Column(name = "username")
  @NotEmpty
  @Size(min = 2, max = 100)
  private String username;

  /**
   * Пароль пользователя.
   * Не может быть пустым.
   */
  @NotEmpty
  @Column(name = "u_password")
  private String password;

  /**
   * Токен пользователя.
   * Не может быть пустым.
   */
  @NotEmpty
  @Column(name = "token")
  private String token;

  /**
   * Конструктор для создания объекта пользователя с заданными параметрами.
   *
   * @param username Имя пользователя.
   * @param password Пароль пользователя.
   * @param token    Токен пользователя.
   */
  public User(String username, String password, String token) {
    this.username = username;
    this.password = password;
    this.token = token;
  }
}
