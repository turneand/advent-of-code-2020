import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day6 {
  public static void main(String[] args) throws Exception {
    System.out.println("part1 => " + part1());  // 6930
    System.out.println("part2 => " + part2());  // 3585
  }

  private static long part1() throws Exception {
    var content = Files.readString(Path.of("src/main/input/day6.txt"), StandardCharsets.UTF_8);
    return Pattern.compile("(\\n){2}").splitAsStream(content)
        .mapToLong(s -> s.chars().filter(i -> i != '\n').distinct().count())
        .sum();
  }

  private static long part2() throws Exception {
    var content = Files.readString(Path.of("src/main/input/day6.txt"), StandardCharsets.UTF_8);
    return Pattern.compile("(\\n){2}").splitAsStream(content)
        .mapToLong(s -> {
          String allData = s.replace("\n", "");
          Integer numberPeople = (s.length() - allData.length()) + 1;
          var map = allData.chars().boxed().collect(Collectors.toMap(Function.identity(), x -> 1, Integer::sum));
          return map.values().stream().filter(numberPeople::equals).count();
        })
        .sum();
  }
}
