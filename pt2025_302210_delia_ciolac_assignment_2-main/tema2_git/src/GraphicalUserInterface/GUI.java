package GraphicalUserInterface;

import BusinessLogic.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class GUI extends JFrame {
    private final JTextField clientsField = new JTextField(10);
    private final JTextField queuesField = new JTextField(10);
    private final JTextField timeLimitField = new JTextField(10);
    private final JTextField minArrivalField = new JTextField(10);
    private final JTextField maxArrivalField = new JTextField(10);
    private final JTextField minServiceField = new JTextField(10);
    private final JTextField maxServiceField = new JTextField(10);
    private final JButton startButton = new JButton("Start Simulation");
    private final JTextArea logArea = new JTextArea(20, 50);

    public GUI() {
        setTitle("Queue Management Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        inputPanel.add(new JLabel("Number of Clients:"));
        inputPanel.add(clientsField);
        inputPanel.add(new JLabel("Number of Queues:"));
        inputPanel.add(queuesField);
        inputPanel.add(new JLabel("Simulation Time:"));
        inputPanel.add(timeLimitField);
        inputPanel.add(new JLabel("Min Arrival Time:"));
        inputPanel.add(minArrivalField);
        inputPanel.add(new JLabel("Max Arrival Time:"));
        inputPanel.add(maxArrivalField);
        inputPanel.add(new JLabel("Min Service Time:"));
        inputPanel.add(minServiceField);
        inputPanel.add(new JLabel("Max Service Time:"));
        inputPanel.add(maxServiceField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(startButton);

        // Log area
        JScrollPane scrollPane = new JScrollPane(logArea);
        logArea.setEditable(false);

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button action
        startButton.addActionListener(this::startSimulation);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startSimulation(ActionEvent e) {
        try {
            int numberOfClients = Integer.parseInt(clientsField.getText());
            int numberOfQueues = Integer.parseInt(queuesField.getText());
            int timeLimit = Integer.parseInt(timeLimitField.getText());
            int minArrival = Integer.parseInt(minArrivalField.getText());
            int maxArrival = Integer.parseInt(maxArrivalField.getText());
            int minService = Integer.parseInt(minServiceField.getText());
            int maxService = Integer.parseInt(maxServiceField.getText());

            if (minArrival > maxArrival || minService > maxService) {
                throw new IllegalArgumentException("Invalid range values");
            }
            logArea.setText("");
            new Thread(() -> {
                try {
                    SimulationManager simulation = new SimulationManager(
                            numberOfClients, numberOfQueues, timeLimit,
                            minArrival, maxArrival, minService, maxService,
                            "simulation_log.txt",  logArea);

                    simulation.run();
                    logArea.append("Simulation completed. Check simulation_log.txt for details.\n");
                } catch (IOException ex) {
                    logArea.append("Error: " + ex.getMessage() + "\n");
                }
            }).start();

        } catch (NumberFormatException ex) {
            logArea.append("Invalid input: Please enter valid numbers\n");
        } catch (IllegalArgumentException ex) {
            logArea.append("Error: " + ex.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
