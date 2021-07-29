import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day11 {
  enum Location {FLOOR, EMPTY, OCCUPIED}

  record Point(int y, int x) {
  }

  record Board(Map<Point, Location> map) {
    boolean findSeat(Point point, int distance, int yModifier, int xModifier) {
      for (int i = 1; i <= distance; i++) {
        var newPoint = new Point(point.y + (i * yModifier), point.x + (i * xModifier));

        if (!map.containsKey(newPoint)) {
          break;
        }

        if (map.get(newPoint) != Location.FLOOR) {
          return map.get(newPoint) == Location.OCCUPIED;
        }
      }

      return false;
    }

    private Board update(int maxOccupied, int distance) {
      Map<Point, Location> newBoard = new HashMap<>();

      map.forEach((point, location) -> {
        Location newLocation = location;

        if (location != Location.FLOOR) {
          long count = Stream.of(findSeat(point, distance, -1, -1),
                  findSeat(point, distance, -1, 0),
                  findSeat(point, distance, -1, +1),
                  findSeat(point, distance, 0, -1),
                  findSeat(point, distance, 0, +1),
                  findSeat(point, distance, +1, -1),
                  findSeat(point, distance, +1, 0),
                  findSeat(point, distance, +1, +1))
              .filter(Boolean::booleanValue)
              .count();

          if (location == Location.OCCUPIED && count >= maxOccupied) {
            newLocation = Location.EMPTY;
          } else if (count == 0) {
            newLocation = Location.OCCUPIED;
          }
        }

        newBoard.put(point, newLocation);
      });

      return new Board(newBoard);
    }
  }

  private static long process(Board board, int maxOccupied, int distance) {
    long count = 0;
    long newCount = Integer.MAX_VALUE;

    while (count != newCount) {
      count = newCount;
      board = board.update(maxOccupied, distance);
      newCount = board.map.values().stream().filter(l -> l == Location.OCCUPIED).count();
    }

    return count;
  }

  public static void main(String[] args) throws Exception {
    List<String> allLines = Files.readAllLines(Path.of("src/main/input/day11.txt"), StandardCharsets.UTF_8);
    Board board = new Board(new HashMap<>());

    for (int y = 0; y < allLines.size(); y++) {
      char[] line = allLines.get(y).toCharArray();
      for (int x = 0; x < line.length; x++) {
        board.map().put(new Point(y, x), line[x] == '.' ? Location.FLOOR : Location.EMPTY);
      }
    }

    System.err.println("part1 -> " + process(board, 4, 1));     // 2126
    System.err.println("part2 -> " + process(board, 5, Integer.MAX_VALUE)); // 1914
  }
}
