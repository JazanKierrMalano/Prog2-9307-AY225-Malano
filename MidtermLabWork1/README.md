# Data Cleaning and Validation Report

## Student Information
- Student Name: MALANO, JAZAN KIERR J.
- Course: Programming 2
- School: University of Perpetual Help System DALTA, Molino Campus

## Scenario
Audit dataset quality by detecting data issues and displaying a formatted report.

## Requirements Detected
1. **Missing values** – Empty or whitespace-only cells in any column
2. **Negative scores** – Score column values less than zero (adapted from "negative sales" for exam dataset)
3. **Invalid dates** – Exam Date values not matching MM/DD/YYYY format
4. **Duplicate records** – Rows that are identical to a previously seen row

## Program Logic
The program prompts for the full file path of the CSV dataset and validates that the file exists, is readable, and has a `.csv` extension. If invalid, it asks again until a valid path is provided. It then loads the CSV, parses rows (handling quoted strings and commas), and scans each record for the four issue types. Counts and sample issues are collected and displayed in a formatted data quality report.

## Files Included
- DataQualityReport.java
- data_quality_report.js

## How to Run

### Java
```
javac DataQualityReport.java
java DataQualityReport
```

### JavaScript (Node.js)
```
node data_quality_report.js
```

When prompted, enter the full path to your CSV file (e.g., `C:\Users\Student\Downloads\Sample_Data-Prog-2-csv.csv`).
