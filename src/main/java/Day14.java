import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day14 {
  private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("mem\\[([0-9]+)] = ([0-9]+)");

  private record Instruction(long mem, long value) {
  }

  private static Instruction parseInstruction(String line) {
    var matcher = INSTRUCTION_PATTERN.matcher(line);
    matcher.matches();
    long mem = Long.parseLong(matcher.group(1));
    long value = Long.parseLong(matcher.group(2));
    return new Instruction(mem, value);
  }

  public static void main(String[] args) throws Exception {
    List<String> allLines = Files.readAllLines(Path.of("src/main/input/day14.txt"), StandardCharsets.UTF_8);
    System.err.println("part1 -> " + part1(allLines));  // 15514035145260
    System.err.println("part2 -> " + part2(allLines));  // 3926790061594
  }

  private static long part1(List<String> allLines) {
    String mask = "";
    Map<Long, Long> map = new HashMap<>();

    for (String line : allLines) {
      if (line.startsWith("mask = ")) {
        mask = line.substring(7);
      } else {
        Instruction instruction = parseInstruction(line);
        String valueBinary = toBinary(instruction.value, 36);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < valueBinary.length(); i++) {
          char binaryStringChar = valueBinary.charAt(i);
          char maskChar = mask.charAt(i);
          sb.append(maskChar == 'X' ? binaryStringChar : maskChar);
        }

        long maskedValue = Long.valueOf(sb.toString(), 2);
        map.put(instruction.mem, maskedValue);
      }
    }

    return map.values().stream().mapToLong(i -> i).sum();
  }

  private static String toBinary(long value, long pad) {
    String binaryString = Long.toBinaryString(value);
    return String.format("%" + pad + "s", binaryString).replace(' ', '0');
  }

  private static long part2(List<String> allLines) {
    String mask = "";
    Map<String, Long> map = new HashMap<>();

    for (String line : allLines) {
      if (line.startsWith("mask = ")) {
        mask = line.substring(7);
      } else {
        Instruction instruction = parseInstruction(line);
        String memBinary = toBinary(instruction.mem, 36);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < memBinary.length(); i++) {
          char binaryStringChar = memBinary.charAt(i);
          char maskChar = mask.charAt(i);
          sb.append(maskChar == '0' ? binaryStringChar : maskChar);
        }

        String raw = sb.toString();
        long floatingCount = raw.chars().filter(c -> c == 'X').count();
        long floatingPermutations = (long) Math.pow(2, floatingCount);

        for (int i = 0; i < floatingPermutations; i++) {
          int xIndex = 0;
          String cBinary = toBinary(i, floatingCount);
          StringBuilder sb1 = new StringBuilder();

          for (int j = 0; j < raw.length(); j++) {
            if (raw.charAt(j) == 'X') {
              sb1.append(cBinary.charAt(xIndex++));
            } else {
              sb1.append(raw.charAt(j));
            }
          }

          map.put(sb1.toString(), instruction.value);
        }
      }
    }

    return map.values().stream().mapToLong(i -> i).sum();
  }
}
