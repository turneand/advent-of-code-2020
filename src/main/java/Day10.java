import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day10 {
  public static void main(String[] args) throws Exception {
    try (var stream = Files.lines(Path.of("src/main/input/day10.txt"), StandardCharsets.UTF_8)) {
      var input = stream.map(Integer::valueOf).sorted().collect(Collectors.toList());
      input.add(0, 0);
      input.add(input.get(input.size() - 1) + 3);

      System.out.println("part1 => " + part1(input));  // 2170
      System.out.println("part2 => " + part2(input));  // 24803586664192
    }
  }

  private static long part1(List<Integer> input) {
    int count1 = 0, count3 = 0;

    for (int i = 1; i < input.size(); i++) {
      var diff = input.get(i) - input.get(i - 1);
      if (diff == 1) {
        count1++;
      } else if (diff == 3) {
        count3++;
      }
    }

    return count1 * count3;
  }

  private static long part2(List<Integer> input) {
    var possibles = input.stream().collect(Collectors.toMap(
        i -> i, value -> Set.of(value - 1, value - 2, value - 3).stream().filter(input::contains).collect(Collectors.toList())
    ));

    var pathCounts = new HashMap<Integer, Long>();
    pathCounts.put(0, 1L);

    return input.stream()
        .skip(1)
        .filter(possibles::containsKey)
        .mapToLong(value -> pathCounts.compute(value, (a, b) -> possibles.get(value).stream().mapToLong(pathCounts::get).sum()))
        .max()
        .getAsLong();
  }
}
