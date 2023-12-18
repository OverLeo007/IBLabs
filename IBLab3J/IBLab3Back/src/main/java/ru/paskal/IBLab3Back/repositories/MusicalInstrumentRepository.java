package ru.paskal.IBLab3Back.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.paskal.IBLab3Back.models.MusicalInstrument;
import ru.paskal.IBLab3Back.models.User;

/**
 * Репозиторий для работы с музыкальными инструментами.
 */
//@Profile("profileBack")
@Repository
public interface MusicalInstrumentRepository extends JpaRepository<MusicalInstrument, Integer> {

  List<MusicalInstrument> findByUser(User user);

}
