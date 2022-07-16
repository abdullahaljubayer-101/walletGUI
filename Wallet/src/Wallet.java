import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Wallet implements ActionListener {

    // Global Variable.
    int lineNo;
    long deposit, cost, cash;
    long transportation, smoking, meal, utility, other;
    String[] costItemLst = {"Transportation", "Smoking", "Meal", "Utility", "Other"};

    // Global Object of Classes.
    JFrame frame = new JFrame();
    JPanel costPanel = new JPanel();
    JComboBox costComboBox = new JComboBox(costItemLst);
    JTextField costTextField = new JTextField();
    JButton costButton = new JButton();
    JPanel depositPanel = new JPanel();
    JTextField depositTextField = new JTextField();
    JButton depositButton = new JButton();
    JPanel totalPanel = new JPanel();
    JLabel totalDeposit = new JLabel();
    JLabel totalCost = new JLabel();
    JLabel totalInCash = new JLabel();
    JPanel buttonPanel = new JPanel();
    JButton buttonCostDetails = new JButton();
    JButton buttonClear = new JButton();

    // Reading from DataBase.
    void readingFromDatabase () throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("costDetails.txt"));
        deposit = Long.parseLong(reader.readLine());
        reader.readLine();
        reader.readLine();
        transportation = Long.parseLong(reader.readLine());
        smoking = Long.parseLong(reader.readLine());
        meal = Long.parseLong(reader.readLine());
        utility = Long.parseLong(reader.readLine());
        other = Long.parseLong(reader.readLine());
        reader.close();

        cost = transportation + smoking + meal + utility + other;
        cash = deposit - cost;

    }

    // Rewriting to DataBase.
    void rewritingToDatabase (int amount, int lineNo) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("costDetails.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"));

        for (int i = 1; i <= 8; ++i) {
            if (i == lineNo) {
                reader.readLine();
                writer.write("" + amount + "\n");
            }
            else {
                writer.write(reader.readLine() + "\n");
            }
        }

        reader.close();
        writer.close();

        File costDetails = new File("costDetails.txt");
        File temp = new File("temp.txt");

        costDetails.delete();
        temp.renameTo(costDetails);

    }


    public Wallet () throws IOException {

        // Reading from DataBase.
        readingFromDatabase();

        // frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setTitle("Wallet");
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(costPanel);
        frame.add(depositPanel);
        frame.add(totalPanel);
        frame.add(buttonPanel);


        // costPanel
        costPanel.setBounds(0,0,300,200);
        costPanel.setLayout(null);
        costPanel.add(costComboBox);
        costPanel.add(costTextField);
        costPanel.add(costButton);

        costComboBox.setBounds(50,30,200,50);
        costComboBox.addActionListener(this);

        costTextField.setBounds(100,70,100,30);

        costButton.setText("Add");
        costButton.setBounds(100,100,100,30);
        costButton.addActionListener(this);


        // depositPanel
        depositPanel.setBounds(300,0,300,200);
        depositPanel.setLayout(null);
        depositPanel.add(depositTextField);
        depositPanel.add(depositButton);

        depositTextField.setBounds(100,70,100,30);

        depositButton.setText("Deposit");
        depositButton.setBounds(100,100,100,30);
        depositButton.addActionListener(this);


        // totalPanel
        totalPanel.setBounds(0,200,600,100);
        totalPanel.setLayout(null);
        totalPanel.add(totalDeposit);
        totalPanel.add(totalCost);
        totalPanel.add(totalInCash);

        totalDeposit.setText("Total Deposit: " + deposit);
        totalDeposit.setBounds(30,25,150,50);

        totalCost.setText("Total Cost: " + cost);
        totalCost.setBounds(240,25,150,50);

        totalInCash.setText("In Cash: " + cash);
        totalInCash.setBounds(430,25,150,50);


        // buttonPanel
        buttonPanel.setBounds(0,300,600,75);
        buttonPanel.setLayout(null);
        buttonPanel.add(buttonCostDetails);
        buttonPanel.add(buttonClear);

        buttonCostDetails.setText("Cost Details");
        buttonCostDetails.setBounds(100,20,150,30);
        buttonCostDetails.addActionListener(this);

        buttonClear.setText("Clear");
        buttonClear.setBounds(350,20,150,30);
        buttonClear.addActionListener(this);


        // for good animation.
        frame.setVisible(true);

    }

    public static void main(String[] args) throws IOException {

        // Constructor for GUI.
        new Wallet();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object event = e.getSource();

        // ComboBox
        if (event == costComboBox) {

            lineNo = costComboBox.getSelectedIndex() + 4;

        }

        // Cost Add Button
        else if (event == costButton) {

            switch (lineNo) {
                case 4:
                    transportation += Long.parseLong(costTextField.getText());
                    try {
                        rewritingToDatabase((int) transportation, lineNo);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case 5:
                    smoking += Long.parseLong(costTextField.getText());
                    try {
                        rewritingToDatabase((int) smoking, lineNo);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case 6:
                    meal += Long.parseLong(costTextField.getText());
                    try {
                        rewritingToDatabase((int) meal, lineNo);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case 7:
                    utility += Long.parseLong(costTextField.getText());
                    try {
                        rewritingToDatabase((int) utility, lineNo);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case 8:
                    other += Long.parseLong(costTextField.getText());
                    try {
                        rewritingToDatabase((int) other, lineNo);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
            }

            cost = transportation + smoking + meal + utility + other;
            totalCost.setText("Total Cost: " + cost);
            costTextField.setText("");

            try {
                rewritingToDatabase((int) cost, 2);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            cash = deposit - cost;
            totalInCash.setText("In Cash: " + cash);

            try {
                rewritingToDatabase((int) cash, 3);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            JOptionPane.showMessageDialog(null, "Add Successfully", "Cost Details", JOptionPane.PLAIN_MESSAGE);

        }

        // Deposit Button
        else if (event == depositButton) {

            deposit += Long.parseLong(depositTextField.getText());
            totalDeposit.setText("Total Deposit: " + deposit);
            depositTextField.setText("");

            try {
                rewritingToDatabase((int) deposit, 1);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            cash = deposit - cost;
            totalInCash.setText("In Cash: " + cash);

            try {
                rewritingToDatabase((int) cash, 3);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            JOptionPane.showMessageDialog(null, "Deposit Successful", "Cost Details", JOptionPane.PLAIN_MESSAGE);

        }

        // Cost Details Button
        else if (event == buttonCostDetails) {

            JOptionPane.showMessageDialog(null,
                    "Total Deposit: " + deposit + "\n" +
                            "Total Cost: " + cost + "\n" +
                            "In Cost: " + cash + "\n\n" +
                            "Transportation: " + transportation + "\n" +
                            "Smoking: " + smoking + "\n" +
                            "Meal: " + meal + "\n" +
                            "Utility: " + utility + "\n" +
                            "Other: " + other,
                    "Cost Details",
                    JOptionPane.PLAIN_MESSAGE);

        }

        // Clear Button
        else if (event == buttonClear) {

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("costDetails.txt"));
                writer.write("0\n0\n0\n0\n0\n0\n0\n0\n");
                writer.close();

                readingFromDatabase();

                totalDeposit.setText("Total Deposit: " + deposit);
                totalCost.setText("Total Cost: " + cost);
                totalInCash.setText("In Cash: " + cash);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            JOptionPane.showMessageDialog(null, "Clear Successfully", "Cost Details", JOptionPane.PLAIN_MESSAGE);

        }

    }
}