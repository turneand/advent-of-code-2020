import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Day1 {
  public static void main(String[] args) throws Exception {
    System.out.println("part1 => " + part1());  // 956091
    System.out.println("part2 => " + part2());  // 79734368
  }

  private static long part2() throws Exception {
    try (var stream = Files.lines(Path.of("src/main/input/day1.txt"), StandardCharsets.UTF_8)) {
      var list = stream.map(Integer::valueOf).sorted().collect(Collectors.toList());

      for (int i = 0; i < list.size() - 2; i++) {
        int x1 = list.get(i);

        for (int j = i + 1; j < list.size() - 1; j++) {
          int x2 = list.get(j);
          int x1x2 = x1 + x2;

          if (x1x2 > 2020) {
            break;
          }

          for (int k = j + 1; k < list.size(); k++) {
            int x3 = list.get(k);
            int x1x2x3 = x1 + x2 + x3;

            if (x1x2x3 == 2020) {
              return (x1 * x2 * x3);
            } else if (x1x2x3 > 2020) {
              break;
            }
          }
        }
      }
    }

    throw new IllegalStateException("No match");
  }

  private static long part1() throws Exception {
    try (var stream = Files.lines(Path.of("src/main/input/day1.txt"), StandardCharsets.UTF_8)) {
      var set = stream.map(Integer::valueOf).collect(Collectors.toSet());

      var x1 = set.stream().filter(i -> set.contains(2020 - i)).findFirst().orElseThrow(IllegalStateException::new);
      var x2 = 2020 - x1;
      return x1 * x2;
    }
  }
}
