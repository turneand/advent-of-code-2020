import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class Day5 {
  public static void main(String[] args) throws Exception {
    System.out.println("part1 => " + part1());  // 855
    System.out.println("part2 => " + part2());  // 552
  }

  private static long part1() throws Exception {
    try (var stream = Files.lines(Path.of("src/main/input/day5.txt"), StandardCharsets.UTF_8)) {
      return stream.map(Day5::calculateSeatId)
          .max(Integer::compareTo)
          .orElseThrow(IllegalStateException::new);
    }
  }

  private static long part2() throws Exception {
    try (var stream = Files.lines(Path.of("src/main/input/day5.txt"), StandardCharsets.UTF_8)) {
      return stream.map(Day5::calculateSeatId)
          .sorted()
          .filter(new Predicate<>() {
            private Integer previous;

            @Override
            public boolean test(Integer integer) {
              Integer p = previous;
              previous = integer;
              return p != null && !integer.equals(p + 1);
            }
          })
          .map(i -> i - 1)
          .findFirst()
          .orElseThrow(IllegalStateException::new);
    }
  }

  private static Integer calculateSeatId(String s) {
    return Integer.parseUnsignedInt(
        s.replace('F', '0')
            .replace('B', '1')
            .replace('L', '0')
            .replace('R', '1'), 2);
  }
}
