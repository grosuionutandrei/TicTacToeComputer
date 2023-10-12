package tictactoe.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayer {
    private static final int EMPTY_VALUE = -1;
    private int currentPlayer;
    private int winner = -1;
    private int turn;

    private int[][] board = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};

    public void addPlayerMoves(int row, int col) {
        board[row][col] = 1;
    }

    public int[] computerMove() {
        int[] randomPosition= randomPosition = getRandomEmptyPosition(board);
        if(randomPosition!=null){
            board[randomPosition[0]][randomPosition[1]]= 1;
            return randomPosition;
        }
        return null;
    }


//    int[] position = getRandomEmptyPosition(array);
//        if (position != null) {
//        System.out.println("Random empty position: [" + position[0] + ", " + position[1] + "]");
//    } else {
//        System.out.println("No empty position found.");
//    }
//}

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
}
