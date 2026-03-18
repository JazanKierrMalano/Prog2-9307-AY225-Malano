import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * MP01 - Load dataset and display total number of records.
 * Student: MALANO, JAZAN KIERR J.
 */
public class MP01 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter dataset file path: ");
        String filePath = scanner.nextLine().trim();

        List<String> headers = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();

        try {
            loadDataset(filePath, headers, rows);
            runMP01(headers, rows);
        } catch (IOException e) {
            System.out.println("Error: Unable to read file. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void runMP01(List<String> headers, List<List<String>> rows) {
        int totalRecords = rows.size();
        System.out.println("\n========= DATASET RECORD COUNT =========");
        System.out.println("Total number of records: " + totalRecords);
        System.out.println("Number of columns: " + headers.size());
        System.out.println("========================================");
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
