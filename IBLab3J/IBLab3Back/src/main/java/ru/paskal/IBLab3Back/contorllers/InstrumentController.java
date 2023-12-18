package ru.paskal.IBLab3Back.contorllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
