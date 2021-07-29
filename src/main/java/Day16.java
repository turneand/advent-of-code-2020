import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 {
  public static void main(String[] args) throws Exception {
    var allLines = Files.readAllLines(Path.of("src/main/input/day16.txt"), StandardCharsets.UTF_8);
    var fieldPredicates = new HashMap<String, Predicate<Integer>>();

    var fieldPattern = Pattern.compile("^([^:]+): ([0-9]+)-([0-9]+) or ([0-9]+)-([0-9]+)");
    var ticketPattern = Pattern.compile("[0-9,]+");
    var csvPattern = Pattern.compile(",");
    var yourTicket = new ArrayList<Integer>();
    var nearbyTickets = new ArrayList<List<Integer>>();

    for (String line : allLines) {
      Matcher fm = fieldPattern.matcher(line);
      if (fm.matches()) {
        String name = fm.group(1);
        int firstFrom = Integer.parseInt(fm.group(2));
        int firstTo = Integer.parseInt(fm.group(3));
        int secondFrom = Integer.parseInt(fm.group(4));
        int secondTo = Integer.parseInt(fm.group(5));

        fieldPredicates.put(name, (value) -> (value >= firstFrom && value <= firstTo) || (value >= secondFrom && value <= secondTo));
      } else if (ticketPattern.matcher(line).matches()) {
        var values = csvPattern.splitAsStream(line).map(Integer::parseInt).collect(Collectors.toList());
        if (yourTicket.isEmpty()) {
          yourTicket.addAll(values);
        } else {
          nearbyTickets.add(values);
        }
      }
    }

    System.err.println("part1 -> " + part1(fieldPredicates, nearbyTickets));       // 27850
    System.err.println("part2 -> " + part2(fieldPredicates, nearbyTickets, yourTicket)); // 491924517533
  }

  private static long part1(Map<String, Predicate<Integer>> fieldPredicates,
                            List<List<Integer>> nearbyTickets) {
    return nearbyTickets.stream()
        .flatMap(List::stream)
        .mapToInt(Integer::valueOf)
        .filter(i -> fieldPredicates.values().stream().noneMatch(fp -> fp.test(i)))
        .sum();
  }

  private static long part2(Map<String, Predicate<Integer>> fieldPredicates,
                            List<List<Integer>> nearbyTickets,
                            List<Integer> yourTicket) {
    // exclude any invalid tickets
    var validNearbyTickets = nearbyTickets.stream()
        .filter(ticket -> ticket.stream().noneMatch(i -> fieldPredicates.values().stream().noneMatch(fp -> fp.test(i))))
        .collect(Collectors.toList());

    // prefill a list indicating ALL fields are initially possible for each value
    var potentialFields = IntStream.range(0, validNearbyTickets.get(0).size())
        .mapToObj(i -> fieldPredicates)
        .map(Map::keySet)
        .map(HashSet::new)
        .collect(Collectors.toList());

    // based on the tickets, determine which fields are not possible for each item
    for (List<Integer> ticket : validNearbyTickets) {
      for (int i = 0; i < ticket.size(); i++) {
        Integer value = ticket.get(i);
        potentialFields.get(i).removeIf(fieldString -> !fieldPredicates.get(fieldString).test(value));
      }
    }

    // now keep removing fields it cannot be (due to only permitted in one other location), until everything is found
    var uniqueFields = new HashSet<String>();
    while (uniqueFields.size() != potentialFields.size()) {
      potentialFields.forEach(s -> {
        if (s.size() != 1) {
          s.removeAll(uniqueFields);
        } else {
          uniqueFields.add(s.iterator().next());
        }
      });
    }

    // finally calculate the multiple of all departure values
    long value = 1;
    for (int i = 0; i < potentialFields.size(); i++) {
      var fieldName = potentialFields.get(i).iterator().next();
      if (fieldName.startsWith("departure")) {
        value *= yourTicket.get(i);
      }
    }

    return value;
  }
}
