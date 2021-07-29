import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day7 {
  public static void main(String[] args) throws Exception {
    var shinyGoldBag = getAllBags().get("shiny gold");
    System.out.println("part1 => " + (shinyGoldBag.flattenParents().map(p -> p.color).distinct().count() - 1));  // 248
    System.out.println("part2 => " + (shinyGoldBag.countChildren() - 1));  // 57281
  }

  private static Map<String, Bag> getAllBags() throws Exception {
    try (var stream = Files.lines(Path.of("src/main/input/day7.txt"), StandardCharsets.UTF_8)) {
      Map<String, Bag> allBags = new HashMap<>();
      Function<String, Bag> newBagFunction = (s) -> new Bag(s, new ArrayList<>(), new ArrayList<>());

      stream.map(line -> line.split(" bags contain ")).forEach(lineSplit -> {
        var lineBag = allBags.computeIfAbsent(lineSplit[0], newBagFunction);

        Pattern.compile(" bags?(, )?[.]?").splitAsStream(lineSplit[1])
            .filter(s2 -> !"no other".equals(s2))
            .flatMap(s2 -> {
              var count = Integer.parseInt(s2.substring(0, 1));
              var bag = allBags.computeIfAbsent((s2.substring(2)), newBagFunction);
              bag.parents.add(lineBag);
              return IntStream.range(0, count).mapToObj(i -> bag);
            })
            .forEach(content -> lineBag.children().add(content));
      });

      return allBags;
    }
  }

  public record Bag(String color, List<Bag> parents, List<Bag> children) {
    private Stream<Bag> flattenParents() {
      return Stream.concat(Stream.of(this), parents.stream().flatMap(Bag::flattenParents));
    }

    private long countChildren() {
      return 1 + children().stream().mapToLong(Bag::countChildren).sum();
    }
  }
}
