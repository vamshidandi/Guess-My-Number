import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GuessMyNumberGame extends JFrame {
    private JTextField[][] inputFields;
    private JLabel[][] correctDigitsLabels;
    private JLabel[][] correctPlacesLabels;
    private ArrayList<Integer> target;
    private int currentAttempt;
    private final int MAX_ATTEMPTS = 6;
    private final int NUMBER_LENGTH = 4;

    public GuessMyNumberGame() {
        setTitle("Guess My Number");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLayout(new BorderLayout());

        initializeGame();

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        JPanel gridPanel = new JPanel(new GridLayout(MAX_ATTEMPTS, NUMBER_LENGTH));
        JPanel feedbackPanel = new JPanel(new GridLayout(MAX_ATTEMPTS, 2));

        inputFields = new JTextField[MAX_ATTEMPTS][NUMBER_LENGTH];
        correctDigitsLabels = new JLabel[MAX_ATTEMPTS][1];
        correctPlacesLabels = new JLabel[MAX_ATTEMPTS][1];

        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            for (int j = 0; j < NUMBER_LENGTH; j++) {
                inputFields[i][j] = new JTextField();
                inputFields[i][j].setEditable(false);
                gridPanel.add(inputFields[i][j]);
            }
            correctDigitsLabels[i][0] = createFeedbackLabel();
            correctPlacesLabels[i][0] = createFeedbackLabel();
            feedbackPanel.add(createFeedbackPanel(correctDigitsLabels[i][0], Color.YELLOW));
            feedbackPanel.add(createFeedbackPanel(correctPlacesLabels[i][0], Color.GREEN));
        }

        mainPanel.add(gridPanel);
        mainPanel.add(feedbackPanel);
        add(mainPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(4, 3));
        for (int i = 0; i < 10; i++) {
            int digit = i;
            JButton button = new JButton(String.valueOf(digit));
            button.addActionListener(e -> appendDigit(digit));
            controlPanel.add(button);
        }

        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(e -> processGuess());
        controlPanel.add(enterButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetGame());
        controlPanel.add(resetButton);

        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JLabel createFeedbackLabel() {
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        return label;
    }

    private JPanel createFeedbackPanel(JLabel label, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.add(label, BorderLayout.CENTER);
        panel.setOpaque(true);
        return panel;
    }

    private void initializeGame() {
        target = new ArrayList<>();
        Random random = new Random();
        while (target.size() < NUMBER_LENGTH) {
            int num = random.nextInt(10);
            if (!target.contains(num)) {
                target.add(num);
            }
        }
        currentAttempt = 0;
    }

    private void appendDigit(int digit) {
        // Check for duplicates in the current attempt
        Set<Integer> currentDigits = new HashSet<>();
        for (int j = 0; j < NUMBER_LENGTH; j++) {
            String text = inputFields[currentAttempt][j].getText();
            if (!text.isEmpty()) {
                currentDigits.add(Integer.parseInt(text));
            }
        }
        
        if (currentDigits.contains(digit)) {
            JOptionPane.showMessageDialog(this, "Duplicate digits are not allowed!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Append digit if no duplicates found
        for (int j = 0; j < NUMBER_LENGTH; j++) {
            if (inputFields[currentAttempt][j].getText().isEmpty()) {
                inputFields[currentAttempt][j].setText(String.valueOf(digit));
                break;
            }
        }
    }

    private void processGuess() {
        if (currentAttempt >= MAX_ATTEMPTS) {
            return;
        }

        ArrayList<Integer> userGuess = new ArrayList<>();
        for (int j = 0; j < NUMBER_LENGTH; j++) {
            String text = inputFields[currentAttempt][j].getText();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all digits.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            userGuess.add(Integer.parseInt(text));
        }

        int correctDigits = 0;
        int correctPlaces = 0;
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            if (target.contains(userGuess.get(i))) {
                correctDigits++;
            }
            if (target.get(i).equals(userGuess.get(i))) {
                correctPlaces++;
            }
        }

        correctDigitsLabels[currentAttempt][0].setText(String.valueOf(correctDigits));
        correctPlacesLabels[currentAttempt][0].setText(String.valueOf(correctPlaces));
        
        currentAttempt++;

        if (correctPlaces == NUMBER_LENGTH) {
            JOptionPane.showMessageDialog(this, "You guessed the number!");
            resetGame();
        } else if (currentAttempt >= MAX_ATTEMPTS) {
            JOptionPane.showMessageDialog(this, "You've used all attempts. The number was: " + target);
            resetGame();
        }
    }

    private void resetGame() {
        initializeGame();
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            for (int j = 0; j < NUMBER_LENGTH; j++) {
                inputFields[i][j].setText("");
            }
            correctDigitsLabels[i][0].setText("0");
            correctPlacesLabels[i][0].setText("0");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GuessMyNumberGame::new);
    }
}