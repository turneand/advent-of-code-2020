import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {
  public static void main(String[] args) throws Exception {
    final long startTime = System.currentTimeMillis();

    try (var stream = Files.lines(Path.of("src/main/input/day9.txt"), StandardCharsets.UTF_8)) {
      var input = stream.map(Long::valueOf).collect(Collectors.toList());
      var part1 = part1(input, 25);
      var part2 = part2(input, part1);

      System.out.println("part1 => " + part1);  // 177777905
      System.out.println("part2 => " + part2);  // 23463012
    }

    System.err.println("Took: " + (System.currentTimeMillis() - startTime) + "ms");
  }

  private static long part1(List<Long> input, int preamble) {
    for (int i = preamble, n = input.size(); i < n; i++) {
      long currentValue = input.get(i);
      List<Long> window = input.subList(i - preamble, i);

      if (window.stream().noneMatch(j -> window.contains(currentValue - j))) {
        return currentValue;
      }
    }

    throw new IllegalStateException("no match");
  }

  private static long part2(List<Long> input, long expectedSum) {
    for (int i = 0, n = input.size(); i < n; i++) {
      long sum = 0;
      long min = Long.MAX_VALUE;
      long max = Long.MIN_VALUE;

      for (int j = i; j < input.size() && sum < expectedSum; j++) {
        long currentValue = input.get(j);
        sum += currentValue;
        min = Math.min(currentValue, min);
        max = Math.max(currentValue, max);

        if (sum == expectedSum) {
          return min + max;
        }
      }
    }

    throw new IllegalStateException("no match");
  }
}
