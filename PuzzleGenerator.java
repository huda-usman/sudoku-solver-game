import java.util.Random;

class PuzzleGenerator {
    int SIZE = 9;                // Standard Sudoku size
    int BOX = 3;                 // 3x3 sub-box size
    int[][] board = new int[SIZE][SIZE];  // Working board
    Random random = new Random();         // Random number generator
    SudokuSolver solver = new SudokuSolver();  // Solver for validation

    
    //  * Generates puzzle with specified difficulty.
    int[][] generatePuzzle(int difficulty) {
        generateFullBoard();                     
        int[][] puzzle = copyBoard(board);     
        
        int removals;  // Number of cells to clear based on difficulty
        if (difficulty == 1) removals = 25;      
        else if (difficulty == 2) removals = 45;
        else removals = 57;                      
        
        // Remove cells while ensuring unique solution
        while (removals > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            
            if (puzzle[row][col] == 0) continue; 
            
            int backup = puzzle[row][col];       
            puzzle[row][col] = 0;            
            
            // Keep removal if puzzle still has unique solution
            if (solver.countSolutionsLimited(puzzle, difficulty)) {
                removals--;                      
            } else {
                puzzle[row][col] = backup;      
            }
        }
        return puzzle;
    }
    
    // Generates complete solved Sudoku board. 
    void generateFullBoard() {
        clearBoard();
        fillDiagonalBoxes();  // Fill diagonal 3x3 boxes first
        fillRemaining(0, 0);  // Fill rest using backtracking
    }
    
    /** Fills the three diagonal 3x3 boxes (independent). */
    void fillDiagonalBoxes() {
        for (int i = 0; i < SIZE; i += BOX) {
            fillBox(i, i);  // Fill box starting at (i,i)
        }
    }
    
    /** Fills one 3x3 box with shuffled numbers 1-9. */
    void fillBox(int rowStart, int colStart) {
        int[] numbers = {1,2,3,4,5,6,7,8,9};
        shuffle(numbers);  // Randomize order
        
        int k = 0;
        for (int r = 0; r < BOX; r++) {
            for (int c = 0; c < BOX; c++) {
                board[rowStart + r][colStart + c] = numbers[k++];
            }
        }
    }
    
    /** Recursively fills remaining cells using backtracking. */
    boolean fillRemaining(int row, int col) {
        if (row == SIZE) return true;           // All rows filled
        if (col == SIZE) return fillRemaining(row + 1, 0);  // Next row
        if (board[row][col] != 0) return fillRemaining(row, col + 1);  // Skip filled
        
        for (int num = 1; num <= SIZE; num++) {
            if (canPlace(board, row, col, num)) {
                board[row][col] = num;
                if (fillRemaining(row, col + 1)) return true;
                board[row][col] = 0;  // Backtrack
            }
        }
        return false;  // No valid number
    }
    
    /** Checks if number can be placed at position. */
    boolean canPlace(int[][] b, int row, int col, int num) {
        // Check row and column
        for (int i = 0; i < SIZE; i++) {
            if (b[row][i] == num || b[i][col] == num) return false;
        }
        
        // Check 3x3 box
        int startRow = (row / BOX) * BOX;
        int startCol = (col / BOX) * BOX;
        for (int r = startRow; r < startRow + BOX; r++) {
            for (int c = startCol; c < startCol + BOX; c++) {
                if (b[r][c] == num) return false;
            }
        }
        return true;
    }
    
    /** Shuffles array using Fisher-Yates algorithm. */
    void shuffle(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    
    /** Clears board (sets all cells to 0). */
    void clearBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = 0;
            }
        }
    }
    
    /** Creates deep copy of board. */
    int[][] copyBoard(int[][] src) {
        int[][] copy = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                copy[row][col] = src[row][col];
            }
        }
        return copy;
    }
}