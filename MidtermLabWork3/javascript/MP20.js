const fs = require('fs');
const readline = require('readline');

/**
 * MP20 – Convert CSV dataset into JSON format.
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
        let headers = [];
        const jsonArray = [];

        for (let line of lines) {
            if (line.trim() === "" || (!headerFound && !line.includes("Candidate"))) {
                continue;
            }

            const cells = parseCsvLine(line);

            if (!headerFound) {
                headers = cells.map((h, i) => h.trim() || `column${i + 1}`);
                headerFound = true;
                continue;
            }

            const obj = {};
            headers.forEach((h, i) => {
                obj[h] = (cells[i] !== undefined ? cells[i].trim() : "") || "";
            });
            jsonArray.push(obj);
        }

        const json = JSON.stringify(jsonArray, null, 2);
        const preview = jsonArray.slice(0, 3);
        const previewJson = JSON.stringify(preview, null, 2);

        console.log("\n========= CSV TO JSON OUTPUT =========");
        console.log("Total records converted:", jsonArray.length);
        console.log("\n--- JSON (first 3 records shown) ---");
        console.log(previewJson);
        if (jsonArray.length > 3) {
            console.log(`... and ${jsonArray.length - 3} more records`);
        }
        console.log("=====================================");

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
