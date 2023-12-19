package ru.paskal.IBLab3Back.utils;

import static ru.paskal.IBLab3Back.security.AES.decrypt;
import static ru.paskal.IBLab3Back.security.AES.encrypt;

import java.util.Objects;
import org.springframework.stereotype.Component;
import ru.paskal.IBLab3Back.dto.instruments.InstrumentDto;
import ru.paskal.IBLab3Back.models.MusicalInstrument;

/**
 * Компонент для преобразования между объектами музыкальных инструментов
 * и их зашифрованными версиями для отправки.
 */
@Component
public class InstrumentAESMapper {

  /**
   * Метод для шифрования музыкального инструмента в объект DTO.
   *
   * @param instrument Музыкальный инструмент для шифрования.
   * @param key        Общий ключ для шифрования данных.
   * @return DTO с зашифрованными данными музыкального инструмента.
   * @throws Exception Исключение, которое может возникнуть при выполнении операции шифрования.
   */
  public static InstrumentDto encryptInstrument(MusicalInstrument instrument, byte[] key)
      throws Exception {
    InstrumentDto instrumentDto = new InstrumentDto();
    instrumentDto.setId(encrypt(String.valueOf(instrument.getId()), key));
    instrumentDto.setName(encrypt(instrument.getName(), key));
    instrumentDto.setBrand(encrypt(instrument.getBrand(), key));
    instrumentDto.setBrand(encrypt(instrument.getBrand(), key));
    instrumentDto.setType(encrypt(instrument.getType(), key));
    instrumentDto.setQuantity(encrypt(String.valueOf(instrument.getQuantity()), key));
    instrumentDto.setPrice(encrypt(String.valueOf(instrument.getPrice()), key));
    return instrumentDto;
  }

  /**
   * Метод для дешифрования объекта DTO с музыкальным инструментом.
   *
   * @param instrumentDto DTO с зашифрованными данными музыкального инструмента.
   * @param key           Общий ключ для дешифрования данных.
   * @return Музыкальный инструмент с дешифрованными данными.
   * @throws Exception Исключение, которое может возникнуть при выполнении операции дешифрования.
   */
  public static MusicalInstrument decryptInstrument(InstrumentDto instrumentDto, byte[] key)
      throws Exception {
    MusicalInstrument instrument = new MusicalInstrument();
    if (!Objects.equals(instrumentDto.getId(), "null")) {
      instrument.setId(Integer.parseInt(decrypt(instrumentDto.getId(), key)));
    }
    instrument.setName(decrypt(instrumentDto.getName(), key));
    instrument.setBrand(decrypt(instrumentDto.getBrand(), key));
    instrument.setBrand(decrypt(instrumentDto.getBrand(), key));
    instrument.setType(decrypt(instrumentDto.getType(), key));
    instrument.setQuantity(Integer.parseInt(decrypt(instrumentDto.getQuantity(), key)));
    instrument.setPrice(Float.parseFloat(decrypt(instrumentDto.getPrice(), key)));
    return instrument;
  }

}
