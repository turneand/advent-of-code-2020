import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Day12 {
  enum Direction {N, E, S, W}

  static class Position {
    Direction direction;
    long waypointY, waypointX, boatY, boatX;
  }

  public static void main(String[] args) throws Exception {
    System.err.println("part1 -> " + part1());  // 1687
    System.err.println("part2 -> " + part2());  // 20873
  }

  private static long part1() throws Exception {
    var actions = new HashMap<String, BiConsumer<Position, Integer>>();
    actions.put("N", (p, i) -> p.boatY += i);
    actions.put("E", (p, i) -> p.boatX += i);
    actions.put("S", (p, i) -> p.boatY -= i);
    actions.put("W", (p, i) -> p.boatX -= i);
    actions.put("L", (p, i) -> p.direction = Direction.values()[((4 - (i / 90)) + p.direction.ordinal()) % 4]);
    actions.put("R", (p, i) -> p.direction = Direction.values()[(p.direction.ordinal() + (i / 90)) % 4]);
    actions.put("F", (p, i) -> actions.get(p.direction.name()).accept(p, i));

    var position = new Position();
    position.direction = Direction.E;
    return processActions(actions, position);
  }

  private static long part2() throws Exception {
    var actions = new HashMap<String, BiConsumer<Position, Integer>>();
    actions.put("N", (p, i) -> p.waypointY += i);
    actions.put("E", (p, i) -> p.waypointX += i);
    actions.put("S", (p, i) -> p.waypointY -= i);
    actions.put("W", (p, i) -> p.waypointX -= i);
    actions.put("L", (p, i) -> {
      for (int j = 0; j < i / 90; j++) {
        long oldWaypointX = p.waypointX;
        p.waypointX = -p.waypointY;
        p.waypointY = oldWaypointX;
      }
    });
    actions.put("R", (p, i) -> {
      for (int j = 0; j < i / 90; j++) {
        long oldWaypointX = p.waypointX;
        p.waypointX = p.waypointY;
        p.waypointY = -oldWaypointX;
      }
    });
    actions.put("F", (p, i) -> {
      for (int j = 0; j < i; j++) {
        p.boatY += p.waypointY;
        p.boatX += p.waypointX;
      }
    });

    var position = new Position();
    position.waypointY = 1;
    position.waypointX = 10;
    return processActions(actions, position);
  }

  private static long processActions(Map<String, BiConsumer<Position, Integer>> actions,
                                     Position position) throws Exception {
    Files.readAllLines(Path.of("src/main/input/day12.txt"), StandardCharsets.UTF_8).forEach(line -> {
      var action = actions.get(line.substring(0, 1));
      var unit = Integer.valueOf(line.substring(1));
      action.accept(position, unit);
    });

    return Math.abs(position.boatY) + Math.abs(position.boatX);
  }
}
