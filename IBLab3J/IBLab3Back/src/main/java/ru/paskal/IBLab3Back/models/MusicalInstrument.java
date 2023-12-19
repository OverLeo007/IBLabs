package ru.paskal.IBLab3Back.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель музыкального инструмента.
 * Сущность, представляющая информацию о музыкальном инструменте в системе.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "musical_instrument")
public class MusicalInstrument {

  /**
   * Уникальный идентификатор музыкального инструмента.
   */
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**
   * Название музыкального инструмента.
   * Длина должна быть от 3 до 30 символов.
   */
  @Column(name = "instrument_name")
  @Size(min = 3, max = 30, message = "len of name is not between 3 and 30")
  private String name;

  /**
   * Тип музыкального инструмента.
   * Длина должна быть от 3 до 30 символов.
   */
  @Size(min = 3, max = 30, message = "len of type is not between 3 and 30")
  @Column(name = "instrument_type")
  private String type;

  /**
   * Бренд музыкального инструмента.
   * Длина должна быть от 3 до 30 символов.
   */
  @Size(min = 3, max = 30, message = "len of brand is not between 3 and 30")
  @Column(name = "brand")
  private String brand;

  /**
   * Цена музыкального инструмента.
   * Минимальное значение - 1.
   */
  @Min(value = 1, message = "we cant sell it for free!")
  @Column(name = "price")
  private float price;

  /**
   * Количество доступных единиц музыкального инструмента.
   * Минимальное значение - 0, максимальное значение - 9999.
   */
  @Min(value = 0, message = "What's on your mind?")
  @Max(value = 9999, message = "Too many you are lying")
  @Column(name = "quantity")
  private int quantity;


  /**
   * Пользователь, который добавил музыкальный инструмент.
   */
  @ManyToOne
  @JoinColumn(name = "uid")
  private User user;

}
