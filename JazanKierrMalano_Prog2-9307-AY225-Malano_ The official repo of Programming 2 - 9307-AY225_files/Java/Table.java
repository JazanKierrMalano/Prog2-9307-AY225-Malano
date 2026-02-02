package Java;

// Jazan Kierr Malano - 25-2295-798

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class Table extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JTextField idField, firstField, lastField;
    private JTextField lab1Field, lab2Field, lab3Field, prelimField, attendanceField;

    private final String CSV_FILE = "C:\\Users\\John Rowelle\\Documents\\Naz\\JazanKierrMalano_Prog2-9307-AY225-Malano_ The official repo of Programming 2 - 9307-AY225_files\\Java\\class_record.csv";

    public Table() {
        setTitle("Records - Jazan Kierr Malano - 25-2295-798");
        setSize(1200, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {
                "Student ID", "First Name", "Last Name",
                "Lab 1", "Lab 2", "Lab 3",
                "Prelim", "Attendance"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();

        idField = new JTextField(6);
        firstField = new JTextField(6);
        lastField = new JTextField(6);

        lab1Field = new JTextField(4);
        lab2Field = new JTextField(4);
        lab3Field = new JTextField(4);
        prelimField = new JTextField(4);
        attendanceField = new JTextField(4);

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");

        panel.add(new JLabel("ID:")); panel.add(idField);
        panel.add(new JLabel("First:")); panel.add(firstField);
        panel.add(new JLabel("Last:")); panel.add(lastField);
        panel.add(new JLabel("Lab1:")); panel.add(lab1Field);
        panel.add(new JLabel("Lab2:")); panel.add(lab2Field);
        panel.add(new JLabel("Lab3:")); panel.add(lab3Field);
        panel.add(new JLabel("Prelim:")); panel.add(prelimField);
        panel.add(new JLabel("Attendance:")); panel.add(attendanceField);
        panel.add(addButton); panel.add(deleteButton);

        add(panel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addStudent());
        deleteButton.addActionListener(e -> deleteStudent());

        loadCSV();

        setVisible(true);
    }

    private void loadCSV() {
        try {
            File file = new File(CSV_FILE);
            if (!file.exists()) {
                file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write("StudentID,first_name,last_name,LAB1,LAB2,LAB3,PRELIM,ATTENDANCE");
                bw.newLine();
                bw.close();
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                model.addRow(data);
            }
            br.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading CSV file!\n" + e.getMessage());
        }
    }

    private void addStudent() {
        String id = idField.getText().trim();
        String first = firstField.getText().trim();
        String last = lastField.getText().trim();
        String lab1 = lab1Field.getText().trim();
        String lab2 = lab2Field.getText().trim();
        String lab3 = lab3Field.getText().trim();
        String prelim = prelimField.getText().trim();
        String attendance = attendanceField.getText().trim();

        if (id.isEmpty() || first.isEmpty() || last.isEmpty() ||
            lab1.isEmpty() || lab2.isEmpty() || lab3.isEmpty() ||
            prelim.isEmpty() || attendance.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            int l1 = Integer.parseInt(lab1);
            int l2 = Integer.parseInt(lab2);
            int l3 = Integer.parseInt(lab3);
            int pre = Integer.parseInt(prelim);
            int att = Integer.parseInt(attendance);

            model.addRow(new Object[]{id, first, last, l1, l2, l3, pre, att});
            saveToCSV();

            idField.setText(""); firstField.setText(""); lastField.setText("");
            lab1Field.setText(""); lab2Field.setText(""); lab3Field.setText("");
            prelimField.setText(""); attendanceField.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Lab scores, Prelim, and Attendance!");
        }
    }

    private void deleteStudent() {
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select one or more rows to delete!");
            return;
        }

        for (int i = selectedRows.length - 1; i >= 0; i--) {
            model.removeRow(selectedRows[i]);
        }

        saveToCSV();
    }

    private void saveToCSV() {
        try {
            File file = new File(CSV_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("StudentID,first_name,last_name,LAB1,LAB2,LAB3,PRELIM,ATTENDANCE");
            bw.newLine();

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    bw.write(model.getValueAt(i, j).toString());
                    if (j < model.getColumnCount() - 1) bw.write(",");
                }
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving CSV file!\n" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Table());
    }
}

// Jazan Kierr Malano - 25-2295-798
