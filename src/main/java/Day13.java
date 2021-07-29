import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Day13 {
  public static void main(String[] args) throws Exception {
    List<String> allLines = Files.readAllLines(Path.of("src/main/input/day13.txt"), StandardCharsets.UTF_8);
    System.err.println("part1 -> " + part1(allLines));  // 2545
    System.err.println("part2 -> " + part2(allLines));  // 266204454441577
  }

  record BusP1(long busId, long nextTimestamp, long waitTime) {
  }

  private static long part1(List<String> allLines) {
    long timestamp = Long.parseLong(allLines.get(0));
    return Pattern.compile(",").splitAsStream(allLines.get(1))
        .filter(busId -> !"x".equals(busId))
        .map(l -> {
          long busId = Long.parseLong(l);
          long nextTimestamp = (((timestamp / busId) * busId) + busId);
          long waitTime = nextTimestamp - timestamp;
          return new BusP1(busId, nextTimestamp, waitTime);
        })
        .sorted((b1, b2) -> Objects.compare(b1.waitTime, b2.waitTime, Comparator.naturalOrder()))
        .mapToLong(bus -> bus.busId * bus.waitTime)
        .findFirst()
        .orElseThrow();
  }

  // This was NOT my first implementation :)
  // originally took over 2 hours to run, but continued investigation after day led to this
  private static long part2(List<String> allLines) {
    String[] busList = allLines.get(1).split(",");
    long index = 1;
    long increment = 1;

    // loop over all of the bus departure times in the file
    for (int busIndex = 0; busIndex < busList.length; busIndex++) {
      // if the departure time is "x", skip it as we don't care
      if (!"x".equals(busList[busIndex])) {
        // extract the departure time from the file...
        long departureTime = Long.parseLong(busList[busIndex]);
        // ...
        while ((index + busIndex) % departureTime != 0) {
          // had a remainder from the modulus operation, so increase by our increment
          index += increment;
        }
        // we found match, so multiply the increment, as
        increment *= departureTime;
      }
    }
    return index;
  }
}
