package mbarix4j3.util;

import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;

public class URIUtils {

  /**
   * Maps a local path to a remote URI
   * @param path THe path to a local file
   * @param pathRoot The root fo the path that's mounted on a web server
   * @param urlRoot The URL on the web server that points to pathRoot
   * @return
   */
  public static Optional<URI> pathToUri(Path path, String pathRoot, String urlRoot) {
    var p = path.toString();
    var idx = p.indexOf(pathRoot);
    var start = idx + pathRoot.length();
    var fragment = p.substring(start);
    return Optional.of(URI.create(urlRoot + fragment));
  }

}
