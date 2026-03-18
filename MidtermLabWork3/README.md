## Program Logic

### MP01 – Load dataset and display total number of records
The program prompts for the dataset file path, reads the CSV file, and parses it to identify the header row (containing "Candidate"). It counts all data rows after the header and displays the total number of records and the number of columns. Empty lines and metadata rows before the header are skipped.

### MP19 – Generate dataset summary report
The program reads the CSV and identifies the Exam and Result columns by header name. It counts total records and columns, then builds frequency maps for exam types and pass/fail results. The summary report displays total records, column count, pass/fail distribution, and the top five most popular exams by candidate count.

### MP20 – Convert CSV dataset into JSON format
The program loads the CSV dataset, parses each row into cells (handling quoted strings and commas), and builds an array of objects where each object's keys are the column headers and values are the cell contents. The output displays the total records converted and a preview of the first three records in formatted JSON.

## How to Run

### Java
From within the `MidtermLabWork3` folder:
```
cd java
javac MP01.java MP19.java MP20.java
java MP01
java MP19
java MP20
```

### JavaScript (Node.js)
```
node javascript/MP01.js
node javascript/MP19.js
node javascript/MP20.js
```

Each program will prompt: `Enter dataset file path:` — provide the full path to `Sample_Data-Prog-2-csv.csv` (e.g., `e:\programming\sirval\Prog2-9307-AY225-MALANO\MidtermLabWork3\Sample_Data-Prog-2-csv.csv`).
