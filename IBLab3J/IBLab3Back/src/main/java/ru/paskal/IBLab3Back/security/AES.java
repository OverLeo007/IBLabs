package ru.paskal.IBLab3Back.security;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Класс, представляющий реализацию шифрования и дешифрования методом AES.
 */
public class AES {

  /**
   * Алгоритм шифрования.
   */
  private static final String ALGORITHM = "AES";

  /**
   * Режим шифрования и отступы.
   */
  private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

  /**
   * Метод для шифрования сообщения с использованием ключа.
   *
   * @param message   Сообщение, которое требуется зашифровать.
   * @param keyBytes  Ключ для шифрования.
   * @return Зашифрованное сообщение в формате Base64.
   * @throws Exception Исключение, которое может возникнуть в процессе шифрования.
   */
  public static String encrypt(String message, byte[] keyBytes) throws Exception {
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encryptedBytes = cipher.doFinal(message.getBytes());
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  /**
   * Метод для расшифровки зашифрованного сообщения с использованием ключа.
   *
   * @param encryptedMessage Зашифрованное сообщение в формате Base64.
   * @param keyBytes         Ключ для расшифровки.
   * @return Расшифрованное сообщение.
   * @throws Exception Исключение, которое может возникнуть в процессе расшифровки.
   */
  public static String decrypt(String encryptedMessage, byte[] keyBytes) throws Exception {
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
    return new String(decryptedBytes);
  }
}
