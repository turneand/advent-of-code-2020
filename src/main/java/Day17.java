import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class Day17 {
  record CoOrdinates(int x, int y, int z, int w) {
  }

  static class Point {
    boolean active;
    boolean nextActive;
    final Set<CoOrdinates> linked;
    final Map<CoOrdinates, Point> coOrdinatesPointMap;
    final Function<CoOrdinates, Set<CoOrdinates>> neighbourCalculationFunction;

    public Point(Map<CoOrdinates, Point> coOrdinatesPointMap,
                 Function<CoOrdinates, Set<CoOrdinates>> neighbourCalculationFunction,
                 CoOrdinates coOrdinates,
                 boolean active) {
      this.coOrdinatesPointMap = coOrdinatesPointMap;
      this.neighbourCalculationFunction = neighbourCalculationFunction;
      this.nextActive = active;
      this.linked = neighbourCalculationFunction.apply(coOrdinates);
      update();
    }

    public void calculateNextActive() {
      long activeCount = linked.stream().map(coOrdinatesPointMap::get).filter(Objects::nonNull).filter(p -> p.active).count();

      if (active) {
        this.nextActive = (activeCount == 2 || activeCount == 3);
      } else {
        this.nextActive = activeCount == 3;
      }
    }

    public void update() {
      if (active != nextActive) {
        this.active = nextActive;
        linked.stream().filter(p -> !coOrdinatesPointMap.containsKey(p)).forEach(p -> {
          coOrdinatesPointMap.put(p, new Point(coOrdinatesPointMap, neighbourCalculationFunction, p, false));
        });
      }
    }
  }

  public static void main(String[] args) throws Exception {
    var allLines = Files.readAllLines(Path.of("src/main/input/day17.txt"), StandardCharsets.UTF_8);
    System.err.println("part1 -> " + part1(allLines));  // 372
    System.err.println("part2 -> " + part2(allLines));  // 1896
  }

  private static long part1(List<String> allLines) {
    return process(allLines, coOrdinates -> {
      var linked = new HashSet<CoOrdinates>();
      for (int x1 = coOrdinates.x - 1; x1 <= coOrdinates.x + 1; x1++) {
        for (int y1 = coOrdinates.y - 1; y1 <= coOrdinates.y + 1; y1++) {
          for (int z1 = coOrdinates.z - 1; z1 <= coOrdinates.z + 1; z1++) {
            if (x1 != coOrdinates.x || y1 != coOrdinates.y || z1 != coOrdinates.z) {
              linked.add(new CoOrdinates(x1, y1, z1, 0));
            }
          }
        }
      }
      return linked;
    });
  }

  private static long part2(List<String> allLines) {
    return process(allLines, coOrdinates -> {
      var linked = new HashSet<CoOrdinates>();
      for (int x1 = coOrdinates.x - 1; x1 <= coOrdinates.x + 1; x1++) {
        for (int y1 = coOrdinates.y - 1; y1 <= coOrdinates.y + 1; y1++) {
          for (int z1 = coOrdinates.z - 1; z1 <= coOrdinates.z + 1; z1++) {
            for (int w1 = coOrdinates.w - 1; w1 <= coOrdinates.w + 1; w1++) {
              if (x1 != coOrdinates.x || y1 != coOrdinates.y || z1 != coOrdinates.z || w1 != coOrdinates.w) {
                linked.add(new CoOrdinates(x1, y1, z1, w1));
              }
            }
          }
        }
      }
      return linked;
    });
  }

  private static long process(List<String> allLines,
                              Function<CoOrdinates, Set<CoOrdinates>> neighbourCalculationFunction) {
    var coOrdinatesPointHashMap = new HashMap<CoOrdinates, Point>();

    for (int y = 0; y < allLines.size(); y++) {
      String line = allLines.get(y);
      for (int x = 0; x < line.length(); x++) {
        var xyz = new CoOrdinates(x, y, 0, 0);
        var point = new Point(coOrdinatesPointHashMap, neighbourCalculationFunction, xyz, line.charAt(x) == '#');
        coOrdinatesPointHashMap.put(xyz, point);
      }
    }

    for (int cycle = 0; cycle < 6; cycle++) {
      coOrdinatesPointHashMap.values().forEach(Point::calculateNextActive);
      new ArrayList<>(coOrdinatesPointHashMap.values()).forEach(Point::update);
    }

    return coOrdinatesPointHashMap.values().stream().filter(p -> p.active).count();
  }
}
