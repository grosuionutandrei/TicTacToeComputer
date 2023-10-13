package tictactoe.bll;

import java.util.*;

public class ComputerPlayer {
    private static final int EMPTY_VALUE = -1;

    private enum Players {
        PLAYER(1),
        COMPUTER(0);
        private final int symbol;

        Players(int symbol) {
            this.symbol = symbol;
        }

        public int getSymbol() {
            return this.symbol;
        }
    }


    private enum Edges {
        EDGEUP(new int[]{0, 1}),
        EDGEDOWN(new int[]{2, 1}),
        EDGERIGHT(new int[]{1, 2}),
        EDGELEFT(new int[]{1, 0});

        private final int[] coords;

        Edges(int[] coords) {
            this.coords = coords;
        }

        public int[] getCoords() {
            return coords;
        }
    }

    private enum Corners {
        CORNERUPLEFT(new int[]{0, 0}),
        CORNERUPRIGHT(new int[]{0, 2}),
        CORNERDOWNRIGHT(new int[]{2, 2}),
        CORNERDOWNLEFT(new int[]{2, 0});
        private final int[] coords;

        Corners(int[] coords) {
            this.coords = coords;
        }

        public int[] getCoords() {
            return coords;
        }
    }

    private boolean isFirstMove = false;

    private int[][] board = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};

    public void addPlayerMoves(int row, int col) {
        board[row][col] = 1;
    }

    public void addComputerMoveWhenFirst(int row, int col) {
        board[row][col] = 0;
    }

    public int[] computerMove() {
        int[] randomPosition = randomPosition = getRandomEmptyPosition(board);
        if (randomPosition != null) {
            board[randomPosition[0]][randomPosition[1]] = 0;
            return randomPosition;
        }
        printBoard(board);
        return null;
    }

    public int[] computerMoveSmart() {
        int[] pos;
        if (isFirstMove) {
            System.out.println("My first move");
            pos = firstMoveWhenXStarts(board);
            board[pos[0]][pos[1]] = 0;
            isFirstMove = false;
            return pos;
        }
        pos = smartMove(board);
        board[pos[0]][pos[1]] = 0;
        System.out.println("Computer representation");
        printBoard(board);
        return pos;
    }

    private int[] getRandomEmptyPosition(int[][] array) {
        List<int[]> emptyPositions = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == EMPTY_VALUE) {
                    emptyPositions.add(new int[]{i, j});
                }
            }
        }
        if (emptyPositions.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return emptyPositions.get(random.nextInt(emptyPositions.size()));
    }


    private void printBoard(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }


    public int[] firstMoveWhenXStarts(int[][] board) {
        int[] centerMove = {1, 1};
        if (board[1][1] == EMPTY_VALUE) {
            return centerMove;
        }
        return takeTheCorner();
    }

    private int[] takeTheCorner() {
        Random random = new Random();
        int randomMove = random.nextInt(Corners.values().length);
        Corners[] corners = Corners.values();
        return corners[randomMove].coords;
    }

    private int[] smartMove(int[][] board) {
        int[] move = new int[2];
        //Computer optimizing his win
        int[] playMainDiag = findWinningPositionDiagonals(board);
        if (playMainDiag[0] >= 0) {
            return playMainDiag;
        }


//    block the rows for opponent
        int[] blockRow = checkRows(board, 1);
        if (blockRow != null) return blockRow;

//block the columns for opponent
        int[] blockCol = checkColumns(board, 1);
        if (blockCol[0] >= 0) {
            return blockCol;
        }

        return move;
    }

    private int[] checkRows(int[][] board, int playerValue) {
        int[] move = {-1, -1};
        for (int i = 0; i < board.length; i++) {
            int[] currentRow = board[i];
            if (areTwoInRow(currentRow, playerValue)) {
                int emptyPos = emptyPosition(currentRow);
                if (emptyPos >= 0) {
                    move[0] = i;
                    move[1] = emptyPos;
                    System.out.println(move[0] + " " + move[1] + " computer Move");
                    return move;
                }
            }

        }
        return move;
    }

    public int[] checkColumns(int[][] board, int playerValue) {
        int[] moves = {-1, -1};
        int rows = board.length;
        int columns = board[0].length;
        int[] columnValues = new int[rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                columnValues[j] = board[j][i];
            }
            if (areTwoInRow(columnValues, playerValue)) {
                int emptyPosition = emptyPosition(columnValues);
                if (emptyPosition >= 0) {
                    moves[0] = emptyPosition;
                    moves[1] = i;
                    return moves;
                }
            }

        }
        return moves;
    }

    private boolean areTwoInRow(int[] currentRow, int val) {
        int equal = 0;
        for (int i = 0; i < currentRow.length; i++) {
            if (currentRow[i] == val) {
                equal++;
                if (equal == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private int emptyPosition(int[] currentRow) {
        for (int i = 0; i < currentRow.length; i++) {
            if (currentRow[i] == EMPTY_VALUE) {
                return i;
            }
        }
        return -1;
    }


    public void setFirstMove(boolean value) {
        this.isFirstMove = value;
        System.out.println("First move initialized ");
    }

    private int[] findWinningPositionDiagonals(int[][] board) {
        int[] moves = {-1, -1};

        // Check if the center is occupied by the computer
        if (board[1][1] == 0) {
            boolean playerOnMainDiagonal = board[0][0] == Players.PLAYER.getSymbol() || board[2][2] == Players.PLAYER.getSymbol();
            boolean playerOnAntiDiagonal = board[0][2] == Players.PLAYER.getSymbol() || board[2][0] == Players.PLAYER.getSymbol();

            // Upper-left and lower-right corners (main diagonal)
            if (!playerOnMainDiagonal && board[0][0] == EMPTY_VALUE && board[2][2] == EMPTY_VALUE) {
                return new int[]{0, 0};
            }
            // Only the lower-right corner of the main diagonal
            else if (!playerOnMainDiagonal && board[2][2] == EMPTY_VALUE) {
                return new int[]{2, 2};
            }

            // Upper-right and lower-left corners (anti-diagonal)
            if (!playerOnAntiDiagonal && board[0][2] == EMPTY_VALUE && board[2][0] == EMPTY_VALUE) {
                return new int[]{0, 2};
            }
            // Only the lower-left corner of the anti-diagonal
            else if (!playerOnAntiDiagonal && board[2][0] == EMPTY_VALUE) {
                return new int[]{2, 0};
            }
        }

        // Return the default move if none of the above conditions are met
        return moves;
    }

    private int[] blockPlayerOnDiagonal(){

    }

}
