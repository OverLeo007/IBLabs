package ru.paskal.IBLab3Back.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "username")
  @NotEmpty
  @Size(min = 2, max = 100)
  private String username;

  @NotEmpty
  @Column(name = "u_password")
  private String password;

  @NotEmpty
  @Column(name = "token")
  private String token;

  @OneToMany(mappedBy = "user")
  private List<MusicalInstrument> instruments;

  public User(String username, String password, String token) {
    this.username = username;
    this.password = password;
    this.token = token;
  }
}
