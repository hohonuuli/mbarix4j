package mbarix4j3.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class Checksum {

  private static final Logger log = Logger.getLogger(Checksum.class.getName());
  private final String digestName;

  /**
   * 
   * @param digestName e.g. SHA-256
   */
  public Checksum(String digestName) {
    this.digestName = digestName;
  }

  public byte[] fromBytes(byte[] bytes) {
      try {
          var digest = MessageDigest.getInstance(digestName);
          return digest.digest(bytes);
      }
      catch (NoSuchAlgorithmException e) {
          throw new RuntimeException(e);
      }

  }

  public byte[] fromInputStream(InputStream inputStream) {
      try {
          var digest = MessageDigest.getInstance(digestName);
          var buffer = new byte[1048576]; // 1MB
          var sizeRead = -1;
          var ok = true;
          while (ok) {
              sizeRead = inputStream.read(buffer);
              if (sizeRead == -1) {
                  ok = false;
              } else {
                  digest.update(buffer, 0, sizeRead);
              }
          }
          return digest.digest();
      }
      catch (IOException | NoSuchAlgorithmException e) {
          log.warning("Failed to generate sha-256 checksum");
          throw new RuntimeException(e);
      }
  }


  public byte[] fromUrl(URL url) {
      try(var in = new BufferedInputStream(url.openStream())) {
          return fromInputStream(in);
      }
      catch (IOException e) {
          log.warning("Failed to generate sha-256 checksum from " + url.toExternalForm());
          throw new RuntimeException(e);
      }
  }

  public byte[] fromPath(Path path) {
      Preconditions.require(!Files.isDirectory(path),
              () -> String.format("%s is a directory", path));
      try(var in = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
          return fromInputStream(in);
      }
      catch (IOException e) {
          log.warning("Failed to generate sha-256 checksum from " + path.toString());
          throw new RuntimeException(e);
      }
  }
  
}
