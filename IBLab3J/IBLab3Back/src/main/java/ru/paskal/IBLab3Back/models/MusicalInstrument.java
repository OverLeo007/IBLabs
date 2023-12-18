package ru.paskal.IBLab3Back.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Profile("profileBack")
@Entity
@Data
@NoArgsConstructor
@Table(name = "musical_instrument")
public class MusicalInstrument {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "instrument_name")
  @Size(min = 3, max = 30, message = "len of name is not between 3 and 30")
  private String name;

  @Size(min = 3, max = 30, message = "len of type is not between 3 and 30")
  @Column(name = "instrument_type")
  private String type;

  @Size(min = 3, max = 30, message = "len of brand is not between 3 and 30")
  @Column(name = "brand")
  private String brand;

  @Min(value = 1, message = "we cant sell it for free!")
  @Column(name = "price")
  private float price;

  @Min(value = 0, message = "What's on your mind?")
  @Column(name = "quantity")
  private int quantity;

  @ManyToOne
  @JoinColumn(name = "uid")
  private User user;

  public MusicalInstrument(String name, String type, String brand, float price, int quantity) {
    this.name = name;
    this.type = type;
    this.brand = brand;
    this.price = price;
    this.quantity = quantity;
  }
}
