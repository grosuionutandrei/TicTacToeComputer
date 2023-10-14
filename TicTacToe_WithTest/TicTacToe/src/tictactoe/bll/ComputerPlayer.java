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

    private int[][] board = {{EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE},
            {EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE},
            {EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE}};

    public void addPlayerMoves(int row, int col) {
        board[row][col] = Players.PLAYER.getSymbol();
    }

    public void addComputerMoveWhenFirst(int row, int col) {
        board[row][col] = Players.COMPUTER.getSymbol();
    }

    public int[] computerMove() {
        int[] randomPosition = getRandomEmptyPosition(board);
        if (randomPosition != null) {
            board[randomPosition[0]][randomPosition[1]] = Players.COMPUTER.getSymbol();
            return randomPosition;
        }
        return null;
    }

    public int[] computerMoveSmart() {
        int[] pos;
        if (isFirstMove) {
            pos = firstMoveWhenXStarts(board);
            board[pos[0]][pos[1]] = Players.COMPUTER.getSymbol();
            isFirstMove = false;
            return pos;
        }
        pos = smartMove(board);
        System.out.println(pos[0] + pos[1] + "smart move");
        board[pos[0]][pos[1]] = Players.COMPUTER.getSymbol();
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
//        to implement a method that player is going for a fork;


        //Computer playing on diagonals
        int[] playMainDiag = findWinningPositionDiagonals(board);
        if (playMainDiag[0] >= 0) {
            return playMainDiag;
        }

        /* Computer playing on the rows and columns where he can win */
        int[] playRowOrColumn = findWinningPositions(board);
        if (playRowOrColumn != null) {
            return playRowOrColumn;
        }

//    block the rows for opponent
        int[] blockRow = checkRows(board, 1);
        if (blockRow[0] >= 0) return blockRow;

//block the columns for opponent
        int[] blockCol = checkColumns(board, 1);
        if (blockCol[0] >= 0) {
            return blockCol;
        }

//        if no block and winning moves left take the empty spot left
        int[] emptySpot = choseEmptySpot(board);
        if (emptySpot[0] >= 0) {
            return emptySpot;
        }


        return null;
    }

    private int[] checkRows(int[][] board, int playerValue) {
        int[] move = {-1, -1};
        int[] mainDiagonal = new int[board.length];
        int[] secondDiagonal = new int[board.length];
        for (int i = 0; i < board.length; i++) {
            int[] currentRow = board[i];

            if (areTwoInRow(currentRow, playerValue)) {
                int emptyPos = emptyPosition(currentRow);
                if (emptyPos >= 0) {
                    move[0] = i;
                    move[1] = emptyPos;
                    return move;
                }
            }
            mainDiagonal[i] = board[i][i];
            secondDiagonal[i] = board[i][board.length - 1 - i];
        }
        if (areTwoInRow(mainDiagonal, playerValue)) {
            int emptyPos = emptyPosition(mainDiagonal);
            if (emptyPos >= 0) {
                move[0] = emptyPos;
                move[1] = emptyPos;
                return move;
            }
        }
        if (areTwoInRow(secondDiagonal, playerValue)) {
            int emptyPos = emptyPosition(mainDiagonal);
            if (emptyPos >= 0) {
                move[0] = emptyPos;
                move[1] = board.length - 1 - emptyPos;
                return move;
            }
        }

        return move;
    }

    public int[] checkColumns(int[][] board, int playerValue) {
        int[] moves = {-1, -1};
        for (int i = 0; i < board.length; i++) {
            int playerCount = 0;
            int emptyRow = -1;

            for (int j = 0; j < board[0].length; j++) {
                if (board[j][i] == playerValue) {
                    playerCount++;
                } else if (board[j][i] == EMPTY_VALUE) {
                    emptyRow = j;
                }
            }
            if (playerCount == 2 && emptyRow != -1) {
                return new int[]{emptyRow, i};
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
    }

    private int[] findWinningPositionDiagonals(int[][] board) {
        int[] moves = {-1, -1};
        if (board[1][1] == 0) {
            boolean playerOnMainDiagonal = board[0][0] == Players.PLAYER.getSymbol() || board[2][2] == Players.PLAYER.getSymbol();
            boolean playerOnAntiDiagonal = board[0][2] == Players.PLAYER.getSymbol() || board[2][0] == Players.PLAYER.getSymbol();
            if (!playerOnMainDiagonal && board[0][0] == EMPTY_VALUE && board[2][2] == EMPTY_VALUE) {
                return new int[]{0, 0};
            } else if (!playerOnMainDiagonal && board[2][2] == EMPTY_VALUE) {
                return new int[]{2, 2};
            }
            if (!playerOnAntiDiagonal && board[0][2] == EMPTY_VALUE && board[2][0] == EMPTY_VALUE) {
                return new int[]{0, 2};
            } else if (!playerOnAntiDiagonal && board[2][0] == EMPTY_VALUE) {
                return new int[]{2, 0};
            }
        }
        return moves;
    }

    private int[] findWinningPositions(int[][] board) {
        int[] column = new int[3];
        for (int i = 0; i < board.length; i++) {
            if (areTwoInRow(board[i], Players.COMPUTER.getSymbol())) {
                int emptySpot = emptyPosition(board[i]);
                if (emptySpot >= 0) {
                    return new int[]{i, emptySpot};
                }
            }
            for (int j = 0; j < board[0].length; j++) {
                column[j] = board[j][i];
            }
            if (areTwoInRow(column, Players.COMPUTER.getSymbol())) {
                int emptySpot = emptyPosition(column);
                if (emptySpot >= 0) {
                    return new int[]{emptySpot, i};
                }
            }
        }
        return null;
    }

    private int[] choseEmptySpot(int[][] board) {
        int[] moves = {-1, -1};
        for (int i = 0; i < board.length; i++) {
            int empty = emptyPosition(board[i]);
            if (empty >= 0) {
                moves[0] = i;
                moves[1] = empty;
                return moves;
            }
        }
        return moves;
    }
}
