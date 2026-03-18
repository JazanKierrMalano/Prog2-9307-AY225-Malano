import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * MP20 - Convert CSV dataset into JSON format.
 * Student: MALANO, JAZAN KIERR J.
 */
public class MP20 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter dataset file path: ");
        String filePath = scanner.nextLine().trim();

        List<String> headers = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();

        try {
            loadDataset(filePath, headers, rows);
            runMP20(headers, rows);
        } catch (IOException e) {
            System.out.println("Error: Unable to read file. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void runMP20(List<String> headers, List<List<String>> rows) {
        List<Map<String, String>> jsonArray = new ArrayList<>();

        for (List<String> row : rows) {
            Map<String, String> obj = new LinkedHashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                String key = headers.get(i).trim();
                if (key.isEmpty()) {
                    key = "column" + (i + 1);
                }
                String value = (i < row.size()) ? row.get(i).trim() : "";
                obj.put(key, value);
            }
            jsonArray.add(obj);
        }

        String json = toJsonString(jsonArray);
        System.out.println("\n========= CSV TO JSON OUTPUT =========");
        System.out.println("Total records converted: " + jsonArray.size());
        System.out.println("\n--- JSON (first 3 records shown) ---");
        System.out.println(json);
        System.out.println("=====================================");
    }

    private static String toJsonString(List<Map<String, String>> jsonArray) {
        StringBuilder sb = new StringBuilder("[\n");
        int limit = Math.min(3, jsonArray.size());
        for (int i = 0; i < limit; i++) {
            Map<String, String> obj = jsonArray.get(i);
            sb.append("  {");
            int j = 0;
            for (Map.Entry<String, String> entry : obj.entrySet()) {
                if (j > 0) sb.append(", ");
                sb.append("\"").append(escapeJson(entry.getKey())).append("\": ");
                sb.append("\"").append(escapeJson(entry.getValue())).append("\"");
                j++;
            }
            sb.append("}");
            if (i < limit - 1) sb.append(",");
            sb.append("\n");
        }
        if (jsonArray.size() > 3) {
            sb.append("  ... and ").append(jsonArray.size() - 3).append(" more records\n");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static void loadDataset(String filePath, List<String> headers, List<List<String>> rows)
            throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerFound = false;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> cells = parseCsvLine(line);

                if (!headerFound) {
                    boolean isHeader = false;
                    for (String cell : cells) {
                        if ("candidate".equalsIgnoreCase(cell.trim())) {
                            isHeader = true;
                            break;
                        }
                    }

                    if (isHeader) {
                        headers.addAll(cells);
                        headerFound = true;
                    }
                    continue;
                }

                rows.add(cells);
            }
        }

        if (headers.isEmpty()) {
            throw new IllegalArgumentException("Dataset header row not found.");
        }
    }

    private static List<String> parseCsvLine(String line) {
        List<String> cells = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                cells.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        cells.add(current.toString());
        return cells;
    }
}
