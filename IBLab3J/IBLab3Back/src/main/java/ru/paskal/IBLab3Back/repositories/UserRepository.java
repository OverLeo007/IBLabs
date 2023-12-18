package ru.paskal.IBLab3Back.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.paskal.IBLab3Back.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsername(String username);

  Optional<User> findByUsernameAndPassword(String username, String password);
  Optional<User> findByToken(String token);

}
