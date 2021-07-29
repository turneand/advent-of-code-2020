import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 {
  public static void main(String[] args) throws Exception {
    System.out.println("part1 => " + part1());  // 247
    System.out.println("part2 => " + part2());  // 145
  }

  private static long part1() throws Exception {
    var content = Files.readString(Path.of("src/main/input/day4.txt"), StandardCharsets.UTF_8);
    var fieldSplit = Pattern.compile(" ");
    var pairSplit = Pattern.compile(":");

    var requiredKeys = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
    return Pattern.compile("(\\n){2}").splitAsStream(content)
        .map(s -> s.replace('\n', ' '))
        .map(s -> fieldSplit.splitAsStream(s).map(pairSplit::split).collect(Collectors.toMap((kvp -> kvp[0]), kvp -> kvp[1])))
        .filter(m -> m.keySet().containsAll(requiredKeys))
        .count();
  }

  private static long part2() throws Exception {
    var content = Files.readString(Path.of("src/main/input/day4.txt"), StandardCharsets.UTF_8);
    var fieldSplit = Pattern.compile(" ");
    var pairSplit = Pattern.compile(":");
    var hclPattern = Pattern.compile("#[0-9a-f]{6}");
    var eyeColors = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
    var pidPattern = Pattern.compile("[0-9]{9}");

    var validators = Map.of(
        "byr", (Predicate<String>) o -> {
          int i = Integer.parseInt(o);
          return i >= 1920 && i <= 2002;
        },
        "iyr", o -> {
          int i = Integer.parseInt(o);
          return i >= 2010 && i <= 2020;
        },
        "eyr", o -> {
          int i = Integer.parseInt(o);
          return i >= 2020 && i <= 2030;
        },
        "hgt", o -> {
          int i = Integer.parseInt(o.substring(0, o.length() - 2));
          if (o.endsWith("cm")) {
            return i >= 150 && i <= 193;
          }
          if (o.endsWith("in")) {
            return i >= 59 && i <= 76;
          }
          return false;
        },
        "hcl", o -> hclPattern.matcher(o).matches(),
        "ecl", eyeColors::contains,
        "pid", o -> pidPattern.matcher(o).matches()
    );

    var requiredKeys = validators.keySet();
    return Pattern.compile("(\\n){2}").splitAsStream(content)
        .map(s -> s.replace('\n', ' '))
        .map(s -> fieldSplit.splitAsStream(s).map(pairSplit::split).collect(Collectors.toMap((kvp -> kvp[0]), kvp -> kvp[1])))
        .filter(m -> m.keySet().containsAll(requiredKeys))
        .map(m -> !m.entrySet().stream()
            .map((e) -> {
              try {
                return !validators.getOrDefault(e.getKey(), o -> true).test(e.getValue());
              } catch (Exception ex) {
                return true;
              }
            })
            .filter(Boolean::booleanValue)
            .findAny()
            .orElse(Boolean.FALSE))
        .filter(Boolean::booleanValue)
        .count();
  }
}
