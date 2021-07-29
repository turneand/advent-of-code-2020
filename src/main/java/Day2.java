import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 {
  public static void main(String[] args) throws Exception {
    System.out.println("part1 => " + part1());  // 643
    System.out.println("part2 => " + part2());  // 388
  }

  private static long part2() throws Exception {
    Pattern p = Pattern.compile("^([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)$");

    try (var stream = Files.lines(Path.of("src/main/input/day2.txt"), StandardCharsets.UTF_8)) {
      return stream
          .map(p::matcher)
          .filter(Matcher::matches)
          .filter(m -> {
            int index1 = Integer.parseInt(m.group(1)) - 1;
            int index2 = Integer.parseInt(m.group(2)) - 1;
            char c = m.group(3).charAt(0);
            String test = m.group(4);

            boolean index1Match = test.charAt(index1) == c;
            boolean index2Match = test.charAt(index2) == c;

            return index1Match ^ index2Match;
          })
          .count();
    }
  }

  private static long part1() throws Exception {
    Pattern p = Pattern.compile("^([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)$");

    try (var stream = Files.lines(Path.of("src/main/input/day2.txt"), StandardCharsets.UTF_8)) {
      return stream
          .map(p::matcher)
          .filter(Matcher::matches)
          .filter(m -> {
            int min = Integer.parseInt(m.group(1));
            int max = Integer.parseInt(m.group(2));
            char c = m.group(3).charAt(0);
            String test = m.group(4);
            long characterCount = test.chars().filter(a -> a == c).count();

            // System.err.println(min + " : " + max + " : " + c + " : " + test + " ::: " + characterCount);
            return characterCount >= min && characterCount <= max;
          })
          .count();
    }
  }
}
