class SudokuSolver {
    int SIZE = 9;
    int BOX = 3;

    // solve the board recursively
      boolean solve(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {           // Find empty cell
                    for (int num = 1; num <= SIZE; num++) {
                        if (canPlace(board, row, col, num)) {
                            board[row][col] = num;    // Try number
                            if (solve(board)) return true;
                            board[row][col] = 0;      // Backtrack
                        }
                    }
                    return false;                     // No valid number
                }
            }
        }
        return true;                                  // All cells filled
    }

    // check if number can be placed in cell
    boolean canPlace(int[][] board, int row, int column, int number) {
        // Check row
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == number) return false;
        }
        
        // Check column
        for (int i = 0; i < SIZE; i++) {
            if (board[i][column] == number) return false;
        }

        // Check 3x3 box
        int startRow = (row / BOX) * BOX;
        int startColumn = (column / BOX) * BOX;
        for (int r = startRow; r < startRow + BOX; r++) {
            for (int c = startColumn; c < startColumn + BOX; c++) {
                if (board[r][c] == number) return false;
            }
        }
        return true;
    }

    // Get hint for a specific cell
    int getHint(int[][] board, int row, int column) {
        if (board[row][column] != 0) return -1; // Cell already filled
        
        // Create a solved version of the board
        int[][] solvedBoard = copyBoard(board);
        if (solve(solvedBoard)) {
            return solvedBoard[row][column]; // Return the correct number
        }
        return 0; // No solution found
    }

    // Get correct number for cell even with mistakes
    int getCorrectNumberForCell(int[][] board, int row, int col) {
        // Create a solved version of the board
        int[][] solvedBoard = copyBoard(board);
        if (solve(solvedBoard)) {
            return solvedBoard[row][col];
        }
        return 0;
    }

    // copy the board
    int[][] copyBoard(int[][] board) {
        int[][] copy = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(board[row], 0, copy[row], 0, SIZE);
        }
        return copy;
    }

    // count solutions with a limit (for puzzle difficulty)
    boolean countSolutionsLimited(int[][] board, int limit) {
        return countLimited(copyBoard(board), 0, 0, limit) <= limit;
    }

    // recursive helper to count solutions
    int countLimited(int[][] board, int row, int col, int limit) {
        if (row == SIZE) return 1;                    // Found solution
        if (col == SIZE) return countLimited(board, row + 1, 0, limit);
        if (board[row][col] != 0) return countLimited(board, row, col + 1, limit);

        int count = 0;
        for (int num = 1; num <= SIZE; num++) {
            if (canPlace(board, row, col, num)) {
                board[row][col] = num;
                count += countLimited(board, row, col + 1, limit);
                if (count > limit) {                  // Early exit
                    board[row][col] = 0;
                    return count;
                }
                board[row][col] = 0;                  // Backtrack
            }
        }
        return count;
    }
}