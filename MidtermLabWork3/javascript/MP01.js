const fs = require('fs');
const readline = require('readline');

/**
 * MP01 – Load dataset and display total number of records.
 * Student: MALANO, JAZAN KIERR J.
 */

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

rl.question('Enter dataset file path: ', (filePath) => {
    if (!fs.existsSync(filePath)) {
        console.error('Error: File not found.');
        rl.close();
        return;
    }

    try {
        const fileContent = fs.readFileSync(filePath, 'utf8');
        const lines = fileContent.split(/\r?\n/);

        let headerFound = false;
        let totalRecords = 0;
        let columnCount = 0;

        for (let line of lines) {
            if (line.trim() === "" || (!headerFound && !line.includes("Candidate"))) {
                continue;
            }

            const cells = parseCsvLine(line);

            if (!headerFound) {
                columnCount = cells.length;
                headerFound = true;
                continue;
            }

            totalRecords++;
        }

        console.log("\n========= DATASET RECORD COUNT =========");
        console.log("Total number of records:", totalRecords);
        console.log("Number of columns:", columnCount);
        console.log("========================================");

    } catch (err) {
        console.error('Error processing file:', err.message);
    } finally {
        rl.close();
    }
});

function parseCsvLine(line) {
    const cells = [];
    let current = '';
    let inQuotes = false;

    for (let i = 0; i < line.length; i++) {
        const ch = line[i];

        if (ch === '"') {
            if (inQuotes && line[i + 1] === '"') {
                current += '"';
                i++;
            } else {
                inQuotes = !inQuotes;
            }
        } else if (ch === ',' && !inQuotes) {
            cells.push(current);
            current = '';
        } else {
            current += ch;
        }
    }

    cells.push(current);
    return cells;
}
