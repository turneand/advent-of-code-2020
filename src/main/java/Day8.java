import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8 {
  public static void main(String[] args) throws Exception {
    System.out.println("part1 => " + part1());  // 1684
    System.out.println("part2 => " + part2());  // 2188
  }

  private static long part1() throws Exception {
    List<Line> lines = readLines();

    for (int index = 0, accumulator = 0; index < lines.size(); ) {
      Line l = lines.get(index);

      if (l.visited) {
        return accumulator;
      }

      l.visited = true;

      if ("acc".equals(l.operation)) {
        accumulator += l.argument;
        index++;
      } else if ("nop".equals(l.operation)) {
        index++;
      } else {
        index += l.argument;
      }
    }

    return -1;
  }

  private static long part2() throws Exception {
    outer:
    for (int nopJmp = 0; ; nopJmp++) {
      List<Line> lines = readLines();
      lines.stream()
          .filter(l -> "nop".equals(l.operation) || "jmp".equals(l.operation))
          .skip(nopJmp)
          .findFirst().ifPresent(l -> l.operation = "nop".equals(l.operation) ? "jmp" : "nop");

      int accumulator = 0;
      for (int index = 0; index < lines.size(); ) {
        Line l = lines.get(index);

        if (l.visited) {
          continue outer;
        }

        l.visited = true;

        if ("acc".equals(l.operation)) {
          accumulator += l.argument;
          index++;
        } else if ("nop".equals(l.operation)) {
          index++;
        } else {
          index += l.argument;
        }
      }

      return accumulator;
    }
  }

  private static List<Line> readLines() throws Exception {
    try (var stream = Files.lines(Path.of("src/main/input/day8.txt"), StandardCharsets.UTF_8)) {
      return stream.map(Line::new).collect(Collectors.toList());
    }
  }

  private static class Line {
    private static final Pattern LINE_PATTERN = Pattern.compile("(nop|acc|jmp) ([+-][0-9]+)");
    private String operation;
    private final int argument;
    private boolean visited;

    public Line(String line) {
      Matcher m = LINE_PATTERN.matcher(line);
      m.matches();
      operation = m.group(1);
      argument = Integer.parseInt(m.group(2));
    }
  }
}
