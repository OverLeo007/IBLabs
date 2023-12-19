package ru.paskal.IBLab3Back.contorllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.paskal.IBLab3Back.dto.auth.AuthVerifyDto;
import ru.paskal.IBLab3Back.dto.instruments.InstrumentCreateDto;
import ru.paskal.IBLab3Back.dto.instruments.InstrumentDeleteDto;
import ru.paskal.IBLab3Back.dto.instruments.InstrumentDto;
import ru.paskal.IBLab3Back.dto.instruments.InstrumentEditDto;
import ru.paskal.IBLab3Back.dto.instruments.InstrumentListDto;
import ru.paskal.IBLab3Back.exceptions.notCreated.MusicalInstrumentNotCreatedException;
import ru.paskal.IBLab3Back.exceptions.notDeleted.MusicalInstrumentNotDeletedException;
import ru.paskal.IBLab3Back.exceptions.notFound.MusicalInstrumentNotFoundException;
import ru.paskal.IBLab3Back.exceptions.notUpdated.MusicalInstrumentNotUpdatedException;
import ru.paskal.IBLab3Back.services.MusialInstrumentService;
import ru.paskal.IBLab3Back.utils.CrudErrorHandlers;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/instrument")
public class InstrumentController extends CrudErrorHandlers<
    MusicalInstrumentNotCreatedException,
    MusicalInstrumentNotFoundException,
    MusicalInstrumentNotUpdatedException,
    MusicalInstrumentNotDeletedException
    > {

  private final MusialInstrumentService service;

  @Autowired
  public InstrumentController(MusialInstrumentService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<InstrumentListDto> getAllByUser(@RequestBody AuthVerifyDto token)  {
    try {
      return new ResponseEntity<>(service.findByUser(token), HttpStatus.OK);
    } catch (Exception e) {
      throw new MusicalInstrumentNotFoundException(e.getMessage());
    }
  }

  @PostMapping("/del")
  public ResponseEntity<HttpStatus> deleteInstrument(@RequestBody InstrumentDeleteDto token) {
    try {
      service.delete(token);
    } catch (Exception e) {
      throw new MusicalInstrumentNotDeletedException(e.getMessage());
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/edit")
  public ResponseEntity<HttpStatus> editInstrument(
      @RequestBody InstrumentEditDto instrumentEditDto
  ) {
    try {
      service.update(instrumentEditDto);
    } catch (Exception e) {
      throw new MusicalInstrumentNotUpdatedException(e.getMessage());
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/new")
  public ResponseEntity<InstrumentDto> newInstrument(
      @RequestBody InstrumentCreateDto instrumentCreateDto) {
    try {
      return new ResponseEntity<>(service.save(instrumentCreateDto), HttpStatus.OK);
    } catch (Exception e) {
      throw new MusicalInstrumentNotCreatedException(e.getMessage());
    }
  }
}
