import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Data Quality Report - Data Cleaning and Validation.
 * Student: MALANO, JAZAN KIERR J.
 *
 * Detects: missing values, negative scores, invalid dates, duplicate records.
 * Displays a formatted data quality report.
 */
public class DataQualityReport {

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath;

        while (true) {
            System.out.print("Enter dataset file path: ");
            filePath = scanner.nextLine().trim();

            if (validateFile(filePath)) {
                break;
            }
            System.out.println("Invalid file. Please ensure the file exists, is readable, and is in CSV format.");
        }

        List<String> headers = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();

        try {
            loadDataset(filePath, headers, rows);
            runDataQualityReport(headers, rows);
        } catch (IOException e) {
            System.out.println("Error: Unable to read file. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static boolean validateFile(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            return false;
        }
        if (!file.canRead()) {
            return false;
        }
        if (!path.toLowerCase().endsWith(".csv")) {
            return false;
        }
        return true;
    }

    private static void runDataQualityReport(List<String> headers, List<List<String>> rows) {
        int scoreColIndex = findHeaderIndex(headers, "score");
        int dateColIndex = findHeaderIndex(headers, "exam date");

        int missingCount = 0;
        List<String> missingSamples = new ArrayList<>();
        int negativeCount = 0;
        List<String> negativeSamples = new ArrayList<>();
        int invalidDateCount = 0;
        List<String> invalidDateSamples = new ArrayList<>();
        Set<String> seenRows = new HashSet<>();
        int duplicateCount = 0;
        List<String> duplicateSamples = new ArrayList<>();

        for (int r = 0; r < rows.size(); r++) {
            List<String> row = rows.get(r);
            int rowNum = r + 1;

            for (int c = 0; c < row.size(); c++) {
                String cell = row.get(c);
                if (cell == null || cell.trim().isEmpty()) {
                    missingCount++;
                    if (missingSamples.size() < 5) {
                        String colName = (c < headers.size()) ? headers.get(c) : "Column " + (c + 1);
                        missingSamples.add("Row " + rowNum + ", " + colName);
                    }
                }
            }

            if (scoreColIndex >= 0 && scoreColIndex < row.size()) {
                String scoreStr = row.get(scoreColIndex).trim();
                if (!scoreStr.isEmpty()) {
                    try {
                        int score = Integer.parseInt(scoreStr);
                        if (score < 0) {
                            negativeCount++;
                            if (negativeSamples.size() < 5) {
                                negativeSamples.add("Row " + rowNum + ": Score = " + score);
                            }
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            if (dateColIndex >= 0 && dateColIndex < row.size()) {
                String dateStr = row.get(dateColIndex).trim();
                if (!dateStr.isEmpty() && !isValidDate(dateStr)) {
                    invalidDateCount++;
                    if (invalidDateSamples.size() < 5) {
                        invalidDateSamples.add("Row " + rowNum + ": '" + dateStr + "'");
                    }
                }
            }

            String rowKey = String.join("|", row);
            if (seenRows.contains(rowKey)) {
                duplicateCount++;
                if (duplicateSamples.size() < 5) {
                    duplicateSamples.add("Row " + rowNum);
                }
            } else {
                seenRows.add(rowKey);
            }
        }

        printReport(rows.size(), missingCount, missingSamples, negativeCount, negativeSamples,
                invalidDateCount, invalidDateSamples, duplicateCount, duplicateSamples);
    }

    private static boolean isValidDate(String s) {
        return DATE_PATTERN.matcher(s).matches();
    }

    private static int findHeaderIndex(List<String> headers, String target) {
        for (int i = 0; i < headers.size(); i++) {
            if (target.equalsIgnoreCase(headers.get(i).trim())) {
                return i;
            }
        }
        return -1;
    }

    private static void printReport(int totalRows, int missingCount, List<String> missingSamples,
            int negativeCount, List<String> negativeSamples, int invalidDateCount,
            List<String> invalidDateSamples, int duplicateCount, List<String> duplicateSamples) {
        System.out.println("\n========= DATA QUALITY REPORT =========");
        System.out.println("Total records analyzed: " + totalRows);
        System.out.println("======================================");

        System.out.println("\n--- 1. Missing Values ---");
        System.out.println("Count: " + missingCount);
        if (!missingSamples.isEmpty()) {
            System.out.println("Sample issues:");
            missingSamples.forEach(s -> System.out.println("  - " + s));
        }

        System.out.println("\n--- 2. Negative Scores ---");
        System.out.println("Count: " + negativeCount);
        if (!negativeSamples.isEmpty()) {
            System.out.println("Sample issues:");
            negativeSamples.forEach(s -> System.out.println("  - " + s));
        }

        System.out.println("\n--- 3. Invalid Dates ---");
        System.out.println("Count: " + invalidDateCount);
        if (!invalidDateSamples.isEmpty()) {
            System.out.println("Sample issues:");
            invalidDateSamples.forEach(s -> System.out.println("  - " + s));
        }

        System.out.println("\n--- 4. Duplicate Records ---");
        System.out.println("Count: " + duplicateCount);
        if (!duplicateSamples.isEmpty()) {
            System.out.println("Sample duplicate row numbers:");
            duplicateSamples.forEach(s -> System.out.println("  - " + s));
        }

        System.out.println("\n======================================");
        System.out.println("Report complete.");
        System.out.println("======================================");
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
