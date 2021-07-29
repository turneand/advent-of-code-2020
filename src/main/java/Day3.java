import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Day3 {
  public static void main(String[] args) throws Exception {
    System.out.println("part1 => " + part1());  // 230
    System.out.println("part2 => " + part2());  // 9533698720
  }

  private static long part1() throws Exception {
    int[] lineNumber = {0};
    try (var stream = Files.lines(Path.of("src/main/input/day3.txt"), StandardCharsets.UTF_8)) {
      return stream
          .filter(l -> l.charAt((lineNumber[0]++ * 3) % l.length()) == '#')
          .count();
    }
  }

  private static long part2() throws Exception {
    return Stream.of(
        treeCount(1, 1),
        treeCount(3, 1),
        treeCount(5, 1),
        treeCount(7, 1),
        treeCount(1, 2)
    ).reduce(Math::multiplyExact).get();
  }

  private static long treeCount(int right, int down) throws Exception {
    int[] lineNumber = {0};
    int[] processedCount = {0};
    try (var stream = Files.lines(Path.of("src/main/input/day3.txt"), StandardCharsets.UTF_8)) {
      return stream.filter(l -> lineNumber[0]++ % down == 0)
          .filter(l -> l.charAt((processedCount[0]++ * right) % l.length()) == '#')
          .count();
    }
  }
}
