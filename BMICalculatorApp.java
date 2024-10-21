import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BMICalculatorApp extends JFrame {

    // Define the components needed
    private JComboBox<String> ageGroupComboBox;
    private JComboBox<String> weightUnitComboBox;
    private JComboBox<String> heightUnitComboBox;
    private JTextField weightField;
    private JTextField heightField;
    private JLabel resultLabel;
    private JPanel resultPanel;

    public BMICalculatorApp() {
        // Set title
        setTitle("Adult and Child BMI Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the title label
        JLabel titleLabel = new JLabel("Adult and Child BMI Calculator", JLabel.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.RED);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Create the form panel using GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        // Age group selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Calculate BMI for:"), gbc);

        gbc.gridx = 1;
        String[] ageGroups = {"Adult Age 20+", "Child Age 19-"};
        ageGroupComboBox = new JComboBox<>(ageGroups);
        ageGroupComboBox.setPreferredSize(new Dimension(100, 20));  // Smaller dropdown
        formPanel.add(ageGroupComboBox, gbc);

        // Weight input with unit selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Weight:"), gbc);

        gbc.gridx = 1;
        JPanel weightPanel = new JPanel(new BorderLayout());
        weightField = new JTextField();
        weightField.setPreferredSize(new Dimension(100, 20));  // Smaller input field
        String[] weightUnits = {"Kilograms", "Pounds"};
        weightUnitComboBox = new JComboBox<>(weightUnits);
        weightUnitComboBox.setPreferredSize(new Dimension(100, 20));  // Smaller dropdown
        weightPanel.add(weightField, BorderLayout.CENTER);
        weightPanel.add(weightUnitComboBox, BorderLayout.EAST);
        formPanel.add(weightPanel, gbc);

        // Height input: single field with unit selection
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Height:"), gbc);

        gbc.gridx = 1;
        JPanel heightPanel = new JPanel(new BorderLayout());
        heightField = new JTextField();
        heightField.setPreferredSize(new Dimension(60, 20));  // Smaller input field
        String[] heightUnits = {"Meters", "Inches"};
        heightUnitComboBox = new JComboBox<>(heightUnits);
        heightUnitComboBox.setPreferredSize(new Dimension(100, 20));  // Smaller dropdown
        heightPanel.add(heightField, BorderLayout.CENTER);
        heightPanel.add(heightUnitComboBox, BorderLayout.EAST);
        formPanel.add(heightPanel, gbc);

        // Button to calculate BMI
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;  // Span two columns
        JButton calculateButton = new JButton("Calculate BMI");
        formPanel.add(calculateButton, gbc);

        // Add formPanel to the center of the layout
        add(formPanel, BorderLayout.CENTER);

        // Result panel for displaying the BMI result and category table
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultLabel = new JLabel("BMI = ", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 22));
        resultPanel.add(resultLabel);

        // Add resultPanel below the form
        add(resultPanel, BorderLayout.SOUTH);

        // Action Listener for the calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateBMI();
            }
        });

        // Display the window with default size
        pack(); // Adjusts the window to fit the components
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void calculateBMI() {
        try {
            // Get weight and convert to kilograms if necessary
            double weight = Double.parseDouble(weightField.getText());
            if (weightUnitComboBox.getSelectedItem().equals("Pounds")) {
                weight = weight * 0.453592; // Convert pounds to kilograms
            }

            // Get height and convert to meters if necessary
            double height = Double.parseDouble(heightField.getText());
            if (heightUnitComboBox.getSelectedItem().equals("Inches")) {
                height = height * 0.0254; // Convert inches to meters
            }

            // Calculate BMI
            double bmi = weight / (height * height);

            // Display BMI result
            resultLabel.setText(String.format("BMI = %.2f", bmi));

            // Update the BMI category table below
            showBMICategoryTable(bmi);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for weight and height.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showBMICategoryTable(double bmi) {
        // Remove any previous table from the result panel
        resultPanel.removeAll();

        // Update the result label with the calculated BMI
        resultPanel.add(resultLabel);

        // Define the table data and headers
        String[] columnNames = {"BMI", "Status"};
        Object[][] data = {
                {"<= 18.4", "Underweight"},
                {"18.5 - 24.9", "Normal"},
                {"25 - 39.9", "Overweight"},
                {">= 40", "Obese"}
        };

        // Create the JTable
        JTable table = new JTable(data, columnNames) {
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                if (col == 1) {
                    if (row == 0 && bmi <= 18.4) {
                        comp.setBackground(Color.YELLOW);
                    } else if (row == 1 && bmi >= 18.5 && bmi <= 24.9) {
                        comp.setBackground(Color.GREEN);
                    } else if (row == 2 && bmi >= 25 && bmi <= 39.9) {
                        comp.setBackground(Color.ORANGE);
                    } else if (row == 3 && bmi >= 40) {
                        comp.setBackground(Color.RED);
                    } else {
                        comp.setBackground(Color.WHITE);
                    }
                } else {
                    comp.setBackground(Color.WHITE);
                }
                return comp;
            }
        };

        // Add the table to the resultPanel
        JScrollPane scrollPane = new JScrollPane(table);
        resultPanel.add(scrollPane);

        // Refresh the panel to show the updated table
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    public static void main(String[] args) {
        // Run the application
        new BMICalculatorApp();
    }
}

