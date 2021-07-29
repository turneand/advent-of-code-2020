import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Day18 {
  public static void main(String[] args) throws Exception {
    List<String> allLines = Files.readAllLines(Path.of("src/main/input/day18.txt"), StandardCharsets.UTF_8);
    System.err.println("part1 -> " + part1(allLines));  // 45283905029161
    System.err.println("part2 -> " + part2(allLines));  // 216975281211165
  }

  private static long part1(List<String> allLines) {
    return allLines.stream()
        .map(line -> line.replace(" ", ""))
        .map(String::chars)
        .map(IntStream::iterator)
        .map(Day18::evaluatePart1)
        .reduce(BigInteger::add).orElseThrow().longValue();
  }

  private static long part2(List<String> allLines) {
    return allLines.stream()
        .map(line -> line.replace(" ", ""))
        .map(String::chars)
        .map(IntStream::iterator)
        .map(Day18::evaluatePart2)
        .reduce(BigInteger::add).orElseThrow().longValue();
  }

  private static BigInteger evaluatePart1(PrimitiveIterator.OfInt iterator) {
    BigInteger firstValue = null;
    Function<BigInteger, BigInteger> function = null;

    while (iterator.hasNext()) {
      char c = (char) iterator.next().intValue();

      if (c == ')') {
        return firstValue;
      } else if (c == '*') {
        function = firstValue::multiply;
      } else if (c == '+') {
        function = firstValue::add;
      } else {
        BigInteger value = null;

        if (c >= '0' && c <= '9') {
          value = BigInteger.valueOf(Character.digit(c, 10));
        } else if (c == '(') {
          value = evaluatePart1(iterator);
        } else {
          throw new IllegalArgumentException("Unknown " + c);
        }

        firstValue = (firstValue == null) ? value : function.apply(value);
      }
    }

    return firstValue;
  }

  private static BigInteger evaluatePart2(PrimitiveIterator.OfInt iterator) {
    List<BigInteger> numbers = new ArrayList<>();
    List<Character> functions = new ArrayList<>();

    while (iterator.hasNext()) {
      char c = (char) iterator.next().intValue();

      if (c == ')') {
        break;
      } else if (c == '*' || c == '+') {
        functions.add(c);
      } else if (c >= '0' && c <= '9') {
        numbers.add(BigInteger.valueOf(Character.digit(c, 10)));
      } else if (c == '(') {
        numbers.add(evaluatePart2(iterator));
      } else {
        throw new IllegalArgumentException("Unknown " + c);
      }
    }

    int index;
    while ((index = functions.indexOf('+')) != -1) {
      functions.remove(index);
      numbers.set(index, numbers.get(index).add(numbers.remove(index + 1)));
    }

    while ((index = functions.indexOf('*')) != -1) {
      functions.remove(index);
      numbers.set(index, numbers.get(index).multiply(numbers.remove(index + 1)));
    }

    return numbers.get(0);
  }
}
