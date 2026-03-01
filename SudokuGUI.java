import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.KeyboardFocusManager;

public class SudokuGUI extends JFrame {
    private SudokuBoard board;
    private SudokuSolver solver;
    private PuzzleGenerator generator;
    
    private JTextField[][] cellFields;
    private JButton solveButton, resetButton, hintButton;
    private JButton generateEasy, generateMedium, generateHard;
    private JButton checkButton, clearButton, clearAllButton;
    private JLabel statusLabel;
    private JPanel boardPanel, controlPanel;
    
    private JLabel timerLabel;
    private JLabel hintsLabel;
    private JLabel mistakesLabel;
    private JPanel statsPanel;

    private Timer gameTimer;
    private int secondsElapsed = 0;
    private int hintsUsed = 0;
    private int mistakesMade = 0;
    private boolean gameStarted = false;
    
    private final Color BG_COLOR = new Color(240, 248, 255);
    private final Color CELL_COLOR = Color.WHITE;
    private final Color BORDER_COLOR = new Color(70, 130, 180);
    private final Color PUZZLE_CELL_COLOR = new Color(230, 230, 250);
    private final Color HINT_CELL_COLOR = new Color(200, 255, 200);
    private final Color ERROR_COLOR = new Color(255, 200, 200);
    private final Color HIGHLIGHT_COLOR = new Color(255, 255, 200);
    
    public SudokuGUI() {
        board = new SudokuBoard();
        solver = new SudokuSolver();
        generator = new PuzzleGenerator();
        
        setTitle("Sudoku Solver Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        initializeGUI();
        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeGUI() {
        boardPanel = createBoardPanel();
        controlPanel = createControlPanel();
        statsPanel = createStatsPanel();
        
        add(statsPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        
        statusLabel = new JLabel("Welcome to Sudoku Solver! Generate or enter a puzzle.");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);
        
        getContentPane().setBackground(BG_COLOR);
        initializeTimer();
    }
    
    private JPanel createBoardPanel() {
        JPanel panel = new JPanel(new GridLayout(9, 9, 1, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BORDER_COLOR);
        
        cellFields = new JTextField[9][9];
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cellFields[row][col] = new JTextField();
                cellFields[row][col].setHorizontalAlignment(JTextField.CENTER);
                cellFields[row][col].setFont(new Font("Arial", Font.BOLD, 24));
                cellFields[row][col].setBackground(CELL_COLOR);
                
                setCellBorder(row, col, cellFields[row][col]);
                
                final int r = row, c = col;
                
                // Add mouse listener for better focus
                cellFields[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        cellFields[r][c].requestFocusInWindow();
                    }
                });
                
                cellFields[row][col].addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        highlightRowColBox(r, c);
                    }
                    
                    @Override
                    public void focusLost(FocusEvent e) {
                        clearHighlights();
                        updateBoardFromGUI();
                    }
                });
                
                cellFields[row][col].addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        JTextField source = (JTextField) e.getSource();
                        String text = source.getText();
                        
                        if (!text.isEmpty() && !gameStarted) {
                            startTimer();
                        }
                        
                        // Allow only 1-9 or empty
                        if (!text.isEmpty() && !text.matches("[1-9]")) {
                            source.setText("");
                            showError("Enter numbers 1-9 only!");
                            return;
                        } else if (text.length() > 1) {
                            source.setText(text.substring(0, 1));
                        }
                        
                        // Don't allow editing of puzzle cells
                        if (board.isPuzzleCell(r + 1, c + 1)) {
                            source.setText(String.valueOf(board.getNumber(r + 1, c + 1)));
                            return;
                        }
                        
                        // Handle hint cell modification
                        if (!text.isEmpty() && board.isHintCell(r + 1, c + 1)) {
                            int response = JOptionPane.showConfirmDialog(SudokuGUI.this,
                                "This cell was filled with a hint. Change it?",
                                "Change Hint Cell",
                                JOptionPane.YES_NO_OPTION);
                            
                            if (response == JOptionPane.NO_OPTION) {
                                source.setText(String.valueOf(board.getNumber(r + 1, c + 1)));
                                return;
                            }
                        }
                        
                        // Update the board
                        if (text.isEmpty()) {
                            // Clearing the cell
                            board.clearUserCell(r + 1, c + 1);
                            source.setBackground(CELL_COLOR);
                        } else {
                            // Adding/changing a number
                            int num = Integer.parseInt(text);
                            
                            // Save the current value
                            int currentValue = board.getNumber(r + 1, c + 1);
                            
                            // Temporarily clear the cell to check if number can be placed
                            board.clearUserCell(r + 1, c + 1);
                            boolean canPlace = board.canPlace(r + 1, c + 1, num);
                            
                            // Put the number back
                            board.putNumber(r + 1, c + 1, num);
                            
                            if (canPlace) {
                                // Valid placement
                                if (board.isHintCell(r + 1, c + 1)) {
                                    source.setBackground(HINT_CELL_COLOR);
                                } else {
                                    source.setBackground(CELL_COLOR);
                                }
                                
                                // If changing from invalid to valid, don't count as new mistake
                            } else {
                                // Invalid placement
                                source.setBackground(ERROR_COLOR);
                                
                                // Only count as mistake if it's a new invalid entry
                                // (not when correcting an existing error)
                                if (currentValue == 0 || board.canPlace(r + 1, c + 1, currentValue)) {
                                    mistakesMade++;
                                    updateStatsDisplay();
                                }
                            }
                        }
                        
                        highlightErrors();
                    }
                });
                
                panel.add(cellFields[row][col]);
            }
        }
        
        return panel;
    }
    
    private boolean isCellValidBasic(int row, int col, int num) {
        // Save current value
        int currentValue = board.getNumber(row + 1, col + 1);
        
        // Temporarily clear the cell
        board.clearUserCell(row + 1, col + 1);
        
        // Check if number can be placed
        boolean canPlace = board.canPlace(row + 1, col + 1, num);
        
        // Put the original value back
        if (currentValue != 0) {
            board.putNumber(row + 1, col + 1, currentValue);
        }
        
        return canPlace;
    }
    
    private void setCellBorder(int row, int col, JTextField field) {
        int top = 1, left = 1, bottom = 1, right = 1;
        
        if (row % 3 == 0) top = 3;
        if (col % 3 == 0) left = 3;
        if (row % 3 == 2) bottom = 3;
        if (col % 3 == 2) right = 3;
        
        field.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, BORDER_COLOR));
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        panel.setBackground(BG_COLOR);
        
        JLabel title = new JLabel("SUDOKU CONTROLS");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(25, 25, 112));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel genLabel = new JLabel("Generate Puzzle:");
        genLabel.setFont(new Font("Arial", Font.BOLD, 14));
        genLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        generateEasy = createStyledButton("Easy Puzzle", new Color(144, 238, 144));
        generateMedium = createStyledButton("Medium Puzzle", new Color(255, 215, 0));
        generateHard = createStyledButton("Hard Puzzle", new Color(255, 99, 71));
        
        JLabel controlLabel = new JLabel("Game Controls:");
        controlLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        solveButton = createStyledButton("Solve Automatically", new Color(135, 206, 250));
        hintButton = createStyledButton("Get Hint (0)", new Color(221, 160, 221));
        checkButton = createStyledButton("Check Board", new Color(152, 251, 152));
        // REMOVED: clearButton
        // REMOVED: clearAllButton
        resetButton = createStyledButton("Reset Board", new Color(240, 128, 128));
        
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(genLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(generateEasy);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(generateMedium);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(generateHard);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(controlLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(solveButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(hintButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(checkButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        // REMOVED: clearButton
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        // REMOVED: clearAllButton
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(resetButton);
        
        addActionListeners();
        return panel;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void addActionListeners() {
        generateEasy.addActionListener(e -> generatePuzzle(1));
        generateMedium.addActionListener(e -> generatePuzzle(2));
        generateHard.addActionListener(e -> new Thread(() -> generatePuzzle(3)).start());
        
        solveButton.addActionListener(e -> solveAutomatically());
        hintButton.addActionListener(e -> getHint());
        checkButton.addActionListener(e -> checkBoard());
        // REMOVED: clearButton listener
        // REMOVED: clearAllButton listener
        resetButton.addActionListener(e -> resetBoard());
    }
    
    private void generatePuzzle(int difficulty) {
        int[][] puzzle = generator.generatePuzzle(difficulty);
        board.setBoard(puzzle);
        updateGUIFromBoard();
        
        resetTimer();
        hintsUsed = 0;
        mistakesMade = 0;
        updateStatsDisplay();
        hintButton.setText("Get Hint (" + hintsUsed + ")");
        startTimer();
        
        String diffName = difficulty == 1 ? "Easy" : difficulty == 2 ? "Medium" : "Hard";
        showMessage("New " + diffName + " puzzle generated! Fill the empty cells.");
    }
    
    private void solveAutomatically() {
        try {
            // Get the original puzzle (without user mistakes)
            int[][] originalPuzzle = board.getOriginalPuzzleCopy();
            
            // Solve the original puzzle
            if (solver.solve(originalPuzzle)) {
                // Apply the solution to the board
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        if (board.isEmpty(row + 1, col + 1)) {
                            board.putNumber(row + 1, col + 1, originalPuzzle[row][col]);
                            cellFields[row][col].setText(String.valueOf(originalPuzzle[row][col]));
                            cellFields[row][col].setBackground(HINT_CELL_COLOR);
                            cellFields[row][col].setEditable(true);
                        }
                    }
                }
                showMessage("Sudoku solved successfully! All cells filled.");
            } else {
                showError("No solution exists for this puzzle!");
            }
        } catch (Exception e) {
            showError("Error solving puzzle: " + e.getMessage());
        }
    }
    
    private void getHint() {
        // Get the original puzzle
        int[][] originalPuzzle = board.getOriginalPuzzleCopy();
        
        // Solve the original puzzle
        if (!solver.solve(originalPuzzle)) {
            showError("Cannot get hint - puzzle has no solution!");
            return;
        }
        
        // Find first empty cell and fill it with the solution
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.isEmpty(row + 1, col + 1)) {
                    int correctNumber = originalPuzzle[row][col];
                    
                    hintsUsed++;
                    updateStatsDisplay();
                    hintButton.setText("Get Hint (" + hintsUsed + ")");
                    
                    // Place the hint
                    board.putHint(row + 1, col + 1, correctNumber);
                    cellFields[row][col].setText(String.valueOf(correctNumber));
                    cellFields[row][col].setBackground(HINT_CELL_COLOR);
                    cellFields[row][col].setEditable(true);
                    
                    showMessage("Hint: Number " + correctNumber + " at row " + (row + 1) + ", column " + (col + 1));
                    highlightErrors();
                    return;
                }
            }
        }
        
        showMessage("No empty cells to give hint for!");
    }
    
    private void checkBoard() {
        // First check if board is full
        if (!board.isFull()) {
            showMessage("Board is not complete! Fill all cells first.");
            return;
        }
        
        // Get the correct solution
        int[][] originalPuzzle = board.getOriginalPuzzleCopy();
        if (!solver.solve(originalPuzzle)) {
            showError("Cannot check board - puzzle has no solution!");
            return;
        }
        
        // Compare user's board with solution
        boolean isCorrect = true;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int userValue = board.getNumber(row + 1, col + 1);
                int correctValue = originalPuzzle[row][col];
                if (userValue != correctValue) {
                    isCorrect = false;
                    cellFields[row][col].setBackground(ERROR_COLOR);
                }
            }
        }
        
        if (isCorrect) {
            stopTimer();
            showMessage("Congratulations! Puzzle solved correctly!");
            int minutes = secondsElapsed / 60;
            int seconds = secondsElapsed % 60;
            String timeString = String.format("%02d:%02d", minutes, seconds);
            
            // Show victory window
            finalWindow.showWindow(this, timeString, hintsUsed, mistakesMade);
        } else {
            showError("Board has errors! Incorrect cells are highlighted in red.");
        }
    }
    
    // REMOVED: clearSelectedCell() method
    
    // REMOVED: clearAllUserCells() method
    
    private void resetBoard() {
        int response = JOptionPane.showConfirmDialog(this, 
            "Reset the entire board? All progress will be lost.",
            "Reset Board", JOptionPane.YES_NO_OPTION);
        
        if (response == JOptionPane.YES_OPTION) {
            board.clearBoard();
            updateGUIFromBoard();
            
            resetTimer();
            hintsUsed = 0;
            mistakesMade = 0;
            updateStatsDisplay();
            hintButton.setText("Get Hint (" + hintsUsed + ")");
            showMessage("Board reset successfully.");
        }
    }
    
    private void updateGUIFromBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = board.getNumber(row + 1, col + 1);
                if (value == 0) {
                    cellFields[row][col].setText("");
                    cellFields[row][col].setBackground(CELL_COLOR);
                    cellFields[row][col].setEditable(true);
                } else {
                    cellFields[row][col].setText(String.valueOf(value));
                    
                    if (board.isPuzzleCell(row + 1, col + 1)) {
                        // Puzzle cell
                        cellFields[row][col].setBackground(PUZZLE_CELL_COLOR);
                        cellFields[row][col].setEditable(false);
                    } else if (board.isHintCell(row + 1, col + 1)) {
                        // Hint cell
                        cellFields[row][col].setBackground(HINT_CELL_COLOR);
                        cellFields[row][col].setEditable(true);
                    } else {
                        // User cell - check validity
                        if (isCellValidBasic(row, col, value)) {
                            cellFields[row][col].setBackground(CELL_COLOR);
                        } else {
                            cellFields[row][col].setBackground(ERROR_COLOR);
                        }
                        cellFields[row][col].setEditable(true);
                    }
                }
            }
        }
        clearHighlights();
    }
    private void updateBoardFromGUI() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = cellFields[row][col].getText();
                if (text.isEmpty()) {
                    board.clearUserCell(row + 1, col + 1);
                } else {
                    try {
                        int num = Integer.parseInt(text);
                        // Only update if it's a user cell
                        if (!board.isPuzzleCell(row + 1, col + 1)) {
                            board.putNumber(row + 1, col + 1, num);
                        }
                    } catch (NumberFormatException e) {
                        // Ignore
                    }
                }
            }
        }
    }
    
    private void highlightRowColBox(int row, int col) {
        clearHighlights();
        
        // Highlight row
        for (int c = 0; c < 9; c++) {
            if (!board.isPuzzleCell(row + 1, c + 1)) {
                cellFields[row][c].setBackground(HIGHLIGHT_COLOR);
            }
        }
        
        // Highlight column
        for (int r = 0; r < 9; r++) {
            if (!board.isPuzzleCell(r + 1, col + 1)) {
                cellFields[r][col].setBackground(HIGHLIGHT_COLOR);
            }
        }
        
        // Highlight 3x3 box
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (!board.isPuzzleCell(r + 1, c + 1)) {
                    cellFields[r][c].setBackground(HIGHLIGHT_COLOR);
                }
            }
        }
        
        // Current cell
        if (!board.isPuzzleCell(row + 1, col + 1)) {
            cellFields[row][col].setBackground(HIGHLIGHT_COLOR.brighter());
        }
    }
    
    private void clearHighlights() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = board.getNumber(row + 1, col + 1);
                if (value == 0) {
                    cellFields[row][col].setBackground(CELL_COLOR);
                } else {
                    if (board.isPuzzleCell(row + 1, col + 1)) {
                        cellFields[row][col].setBackground(PUZZLE_CELL_COLOR);
                    } else if (board.isHintCell(row + 1, col + 1)) {
                        cellFields[row][col].setBackground(HINT_CELL_COLOR);
                    } else {
                        if (isCellValidBasic(row, col, value)) {
                            cellFields[row][col].setBackground(CELL_COLOR);
                        } else {
                            cellFields[row][col].setBackground(ERROR_COLOR);
                        }
                    }
                }
            }
        }
    }
    
    private void highlightErrors() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = board.getNumber(row + 1, col + 1);
                if (value != 0 && !board.isPuzzleCell(row + 1, col + 1)) {
                    if (!isCellValidBasic(row, col, value)) {
                        cellFields[row][col].setBackground(ERROR_COLOR);
                    }
                }
            }
        }
    }
    
    private void showMessage(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(new Color(0, 100, 0));
    }
    
    private void showError(String message) {
        statusLabel.setText("Error: " + message);
        statusLabel.setForeground(Color.RED);
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panel.setBackground(new Color(220, 240, 255));
        
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        timerLabel.setForeground(new Color(25, 25, 112));
        
        hintsLabel = new JLabel("Hints: 0");
        hintsLabel.setFont(new Font("Arial", Font.BOLD, 22));
        hintsLabel.setForeground(new Color(0, 100, 0));
        
        mistakesLabel = new JLabel("Mistakes: 0");
        mistakesLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mistakesLabel.setForeground(Color.RED);
        
        panel.add(timerLabel);
        panel.add(hintsLabel);
        panel.add(mistakesLabel);
        
        return panel;
    }
    
    private void initializeTimer() {
        gameTimer = new Timer(1000, e -> {
            secondsElapsed++;
            updateTimerDisplay();
        });
    }
    
    private void updateTimerDisplay() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }
    
    private void startTimer() {
        if (!gameStarted) {
            gameStarted = true;
            gameTimer.start();
        }
    }
    
    private void stopTimer() {
        gameTimer.stop();
    }
    
    private void resetTimer() {
        gameTimer.stop();
        gameStarted = false;
        secondsElapsed = 0;
        updateTimerDisplay();
    }
    
    private void updateStatsDisplay() {
        hintsLabel.setText("Hints: " + hintsUsed);
        mistakesLabel.setText("Mistakes: " + mistakesMade);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SudokuGUI();
        });
    }
}