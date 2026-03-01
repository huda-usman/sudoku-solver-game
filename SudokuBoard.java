class SudokuBoard {
    int SIZE = 9;  // Standard Sudoku grid size
    int BOX = 3;    // Size of each 3x3 sub-box


     // Board state arrays
    int[][] board;          // Current state of the board (including user moves)
    int[][] originalPuzzle; // Stores the original puzzle (immutable clues)
    boolean[][] userFilled; // Tracks cells filled by the user (not original puzzle)
    boolean[][] hintCells;  // Tracks cells filled using hints

    // Constructor initializes all board arrays to empty state.
    SudokuBoard() {
        board = new int[SIZE][SIZE];
        originalPuzzle = new int[SIZE][SIZE];
        userFilled = new boolean[SIZE][SIZE];
        hintCells = new boolean[SIZE][SIZE];
        clearBoard();
    }

    //  Resets the entire board to empty state.
    void clearBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                board[row][column] = 0;
                originalPuzzle[row][column] = 0;
                userFilled[row][column] = false;
                hintCells[row][column] = false;
            }
        }
    }

    //  Places a number entered by the user.
    void putNumber(int row, int column, int number) {
        row = row - 1;
        column = column - 1;
        board[row][column] = number;
        userFilled[row][column] = true;
        hintCells[row][column] = false; // User fills are not hint cells
    }

    void setNumber(int row, int column, int number) {
        row = row - 1;
        column = column - 1;
        board[row][column] = number;
        originalPuzzle[row][column] = number;
        userFilled[row][column] = false;   // Not user-filled
        hintCells[row][column] = false; // Not a hint
    }

    void putHint(int row, int column, int number) {
        row = row - 1;
        column = column - 1;
        board[row][column] = number;
        userFilled[row][column] = true; // Hint counts as user-filled
        hintCells[row][column] = true; // This is a hint cell
    }

    int getNumber(int row, int column) {
        return board[row - 1][column - 1];
    }

    int getOriginalNumber(int row, int column) {
        return originalPuzzle[row - 1][column - 1];
    }

     //  Checks if a number can be legally placed at a given position.
    boolean canPlace(int row, int column, int number) {
        row = row - 1;
        column = column - 1;

        if (board[row][column] != 0)
            return false;
      // Check row
        for (int c = 0; c < SIZE; c++)
            if (board[row][c] == number)
                return false;
   // Check column
        for (int r = 0; r < SIZE; r++)
            if (board[r][column] == number)
                return false;

        // Check 3x3 box
        int startRow = (row / BOX) * BOX;
        int startColumn = (column / BOX) * BOX;
        for (int r = startRow; r < startRow + BOX; r++)
            for (int c = startColumn; c < startColumn + BOX; c++)
                if (board[r][c] == number)
                    return false;

        return true;
    }

        // Checks if all cells on the board are filled.
    boolean isFull() {
        for (int row = 0; row < SIZE; row++)
            for (int column = 0; column < SIZE; column++)
                if (board[row][column] == 0)
                    return false;
        return true;
    }

        //  Validates if the completely filled board is a correct Sudoku solution.
    boolean checkBoard() {
        if (!isFull()) {
            return false;
        }
        return isValidCompleteBoard();
    }

    boolean isValidCompleteBoard() {
        // Check rows
        for (int row = 0; row < SIZE; row++) {
            boolean[] seen = new boolean[SIZE + 1];
            for (int col = 0; col < SIZE; col++) {
                int num = board[row][col];
                if (num < 1 || num > 9 || seen[num]) {
                    return false;
                }
                seen[num] = true;
            }
        }
        
        // Check columns
        for (int col = 0; col < SIZE; col++) {
            boolean[] seen = new boolean[SIZE + 1];
            for (int row = 0; row < SIZE; row++) {
                int num = board[row][col];
                if (num < 1 || num > 9 || seen[num]) {
                    return false;
                }
                seen[num] = true;
            }
        }
        
        // Check 3x3 boxes
        for (int boxRow = 0; boxRow < BOX; boxRow++) {
            for (int boxCol = 0; boxCol < BOX; boxCol++) {
                boolean[] seen = new boolean[SIZE + 1];
                int startRow = boxRow * BOX;
                int startCol = boxCol * BOX;
                for (int row = startRow; row < startRow + BOX; row++) {
                    for (int col = startCol; col < startCol + BOX; col++) {
                        int num = board[row][col];
                        if (num < 1 || num > 9 || seen[num]) {
                            return false;
                        }
                        seen[num] = true;
                    }
                }
            }
        }
        
        return true;
    }

        //  Checks if the current board state (including partial fills) is valid.
    boolean isValidBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                int number = board[row][column];
                if (number != 0) {
                    board[row][column] = 0;
                    if (!canPlace(row + 1, column + 1, number)) {
                        board[row][column] = number;
                        return false;
                    }
                    board[row][column] = number;
                }
            }
        }
        return true;
    }

    //    Creates a deep copy of the current board state.
    int[][] getBoardCopy() {
        int[][] copy = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++)
            for (int column = 0; column < SIZE; column++)
                copy[row][column] = board[row][column];
        return copy;
    }

    // Creates a deep copy of the original puzzle.
    int[][] getOriginalPuzzleCopy() {
        int[][] copy = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++)
            for (int column = 0; column < SIZE; column++)
                copy[row][column] = originalPuzzle[row][column];
        return copy;
    }

    //  Replaces the entire board with a new puzzle.
    void setBoard(int[][] newBoard) {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                board[row][column] = newBoard[row][column];
                originalPuzzle[row][column] = newBoard[row][column];
                userFilled[row][column] = (newBoard[row][column] == 0);
                hintCells[row][column] = false;
            }
        }
    }

    boolean isEmpty(int row, int column) {
        return board[row - 1][column - 1] == 0;
    }
    
    boolean isUserFilled(int row, int column) {
        return userFilled[row - 1][column - 1];
    }
    
    boolean isHintCell(int row, int column) {
        return hintCells[row - 1][column - 1];
    }
    
    boolean isPuzzleCell(int row, int column) {
        return originalPuzzle[row - 1][column - 1] != 0;
    }
    
    void clearUserCell(int row, int column) {
        row = row - 1;
        column = column - 1;
        if (userFilled[row][column]) {
            board[row][column] = 0;
            userFilled[row][column] = false;
            hintCells[row][column] = false;
        }
    }
    void clearAllUserCells() {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (userFilled[row][column]) {
                    board[row][column] = 0;
                    userFilled[row][column] = false;
                    hintCells[row][column] = false;
                }
            }
        }
    }
}