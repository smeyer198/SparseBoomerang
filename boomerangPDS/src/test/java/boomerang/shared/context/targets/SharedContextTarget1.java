package boomerang.shared.context.targets;

import java.io.File;

public class SharedContextTarget1 {

  public static void main(String...args){
    bar("bar");
  }

  private static void bar(String param) {
    String x = new String(param);
    File file = new File( new String(param));
  }
}
