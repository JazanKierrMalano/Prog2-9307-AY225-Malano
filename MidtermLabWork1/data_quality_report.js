const fs = require('fs');
const readline = require('readline');

/**
 * Data Quality Report - Data Cleaning and Validation.
 * Student: MALANO, JAZAN KIERR J.
 *
 * Detects: missing values, negative scores, invalid dates, duplicate records.
 * Displays a formatted data quality report.
 */

const DATE_PATTERN = /^\d{2}\/\d{2}\/\d{4}$/;

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

function validateFile(path) {
    if (!path || path.trim() === '') return false;
    if (!path.toLowerCase().endsWith('.csv')) return false;
    try {
        const stat = fs.statSync(path);
        return stat.isFile() && (stat.mode & fs.constants.R_OK);
    } catch {
        return false;
    }
}

function isValidDate(s) {
    return DATE_PATTERN.test(s.trim());
}

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

function findHeaderIndex(headers, target) {
    for (let i = 0; i < headers.length; i++) {
        if (target.toLowerCase() === headers[i].trim().toLowerCase()) {
            return i;
        }
    }
    return -1;
}

function runReport(headers, rows) {
    const scoreColIndex = findHeaderIndex(headers, 'score');
    const dateColIndex = findHeaderIndex(headers, 'exam date');

    let missingCount = 0;
    const missingSamples = [];
    let negativeCount = 0;
    const negativeSamples = [];
    let invalidDateCount = 0;
    const invalidDateSamples = [];
    const seenRows = new Set();
    let duplicateCount = 0;
    const duplicateSamples = [];

    for (let r = 0; r < rows.length; r++) {
        const row = rows[r];
        const rowNum = r + 1;

        for (let c = 0; c < row.length; c++) {
            const cell = row[c];
            if (cell == null || (typeof cell === 'string' && cell.trim() === '')) {
                missingCount++;
                if (missingSamples.length < 5) {
                    const colName = c < headers.length ? headers[c] : `Column ${c + 1}`;
                    missingSamples.push(`Row ${rowNum}, ${colName}`);
                }
            }
        }

        if (scoreColIndex >= 0 && scoreColIndex < row.length) {
            const scoreStr = (row[scoreColIndex] || '').trim();
            if (scoreStr !== '') {
                const score = parseInt(scoreStr, 10);
                if (!isNaN(score) && score < 0) {
                    negativeCount++;
                    if (negativeSamples.length < 5) {
                        negativeSamples.push(`Row ${rowNum}: Score = ${score}`);
                    }
                }
            }
        }

        if (dateColIndex >= 0 && dateColIndex < row.length) {
            const dateStr = (row[dateColIndex] || '').trim();
            if (dateStr !== '' && !isValidDate(dateStr)) {
                invalidDateCount++;
                if (invalidDateSamples.length < 5) {
                    invalidDateSamples.push(`Row ${rowNum}: '${dateStr}'`);
                }
            }
        }

        const rowKey = row.join('|');
        if (seenRows.has(rowKey)) {
            duplicateCount++;
            if (duplicateSamples.length < 5) {
                duplicateSamples.push(`Row ${rowNum}`);
            }
        } else {
            seenRows.add(rowKey);
        }
    }

    console.log('\n========= DATA QUALITY REPORT =========');
    console.log('Total records analyzed:', rows.length);
    console.log('======================================');

    console.log('\n--- 1. Missing Values ---');
    console.log('Count:', missingCount);
    if (missingSamples.length > 0) {
        console.log('Sample issues:');
        missingSamples.forEach(s => console.log('  -', s));
    }

    console.log('\n--- 2. Negative Scores ---');
    console.log('Count:', negativeCount);
    if (negativeSamples.length > 0) {
        console.log('Sample issues:');
        negativeSamples.forEach(s => console.log('  -', s));
    }

    console.log('\n--- 3. Invalid Dates ---');
    console.log('Count:', invalidDateCount);
    if (invalidDateSamples.length > 0) {
        console.log('Sample issues:');
        invalidDateSamples.forEach(s => console.log('  -', s));
    }

    console.log('\n--- 4. Duplicate Records ---');
    console.log('Count:', duplicateCount);
    if (duplicateSamples.length > 0) {
        console.log('Sample duplicate row numbers:');
        duplicateSamples.forEach(s => console.log('  -', s));
    }

    console.log('\n======================================');
    console.log('Report complete.');
    console.log('======================================');
}

function promptForPath() {
    rl.question('Enter dataset file path: ', (filePath) => {
        const path = filePath.trim();

        if (!validateFile(path)) {
            console.error('Invalid file. Please ensure the file exists, is readable, and is in CSV format.');
            promptForPath();
            return;
        }

        try {
            const fileContent = fs.readFileSync(path, 'utf8');
            const lines = fileContent.split(/\r?\n/);

            let headerFound = false;
            const headers = [];
            const rows = [];

            for (const line of lines) {
                if (line.trim() === '' || (!headerFound && !line.includes('Candidate'))) {
                    continue;
                }

                const cells = parseCsvLine(line);

                if (!headerFound) {
                    headers.push(...cells);
                    headerFound = true;
                    continue;
                }

                rows.push(cells);
            }

            if (headers.length === 0) {
                console.error('Error: Dataset header row not found.');
            } else {
                runReport(headers, rows);
            }
        } catch (err) {
            console.error('Error processing file:', err.message);
        } finally {
            rl.close();
        }
    });
}

promptForPath();
