import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day15 {
  public static void main(String[] args) throws Exception {
    var allLines = Files.readAllLines(Path.of("src/main/input/day15.txt"), StandardCharsets.UTF_8);
    var numbers = Pattern.compile(",").splitAsStream(allLines.get(0)).map(Integer::valueOf).collect(Collectors.toList());

    System.err.println("part1 -> " + run(numbers, 2020));   // 1696
    System.err.println("part2 -> " + run(numbers, 30000000)); // 37385
  }

  private static long run(List<Integer> numbers, int turns) {
    Map<Integer, Integer> lastIndexes = new HashMap<>();
    Integer lastNumber = null;

    for (int i = 0; i < numbers.size(); i++) {
      lastIndexes.put(lastNumber, i);
      lastNumber = numbers.get(i);
    }

    for (int i = numbers.size(); i < turns; i++) {
      var lastIndexVal = lastIndexes.get(lastNumber);
      var number = lastIndexVal == null ? 0 : i - lastIndexVal;
      lastIndexes.put(lastNumber, i);
      lastNumber = number;
    }

    return lastNumber;
  }
}
