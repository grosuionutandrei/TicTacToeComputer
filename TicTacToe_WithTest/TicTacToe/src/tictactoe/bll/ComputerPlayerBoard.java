package tictactoe.bll;

public class ComputerPlayerBoard implements ComputerGameBoard{
    private int currentPlayer;
    private int winner = -1;
    private int turn;

    private int[][] board = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};


    public int getNextPlayer() {
        if(currentPlayer==0){
            currentPlayer=1;
            return 1;
        }else {
            currentPlayer=0;
            return 0;
        }

    }

    /**
     * Attempts to let the current player play at the given coordinates. It the
     * attempt is succesfull the current player has ended his turn and it is the
     * next players turn.
     *
     * @param col column to place a marker in.
     * @param row row to place a marker in.
     * @return true if the move is accepted, otherwise false. If gameOver == true
     * this method will always return false.
     */

    public boolean play(int col, int row) {
        if (isGameOver() || board[row][col] >= 0) {
            return false;
        }
        board[row][col] = currentPlayer;
        printBoard(board);
        turn++;
        System.out.println(turn + " " + "turn");
        return true;
    }

    public boolean isGameOver() {
        if (checkWinnerRowsAndDiagonals(board)) {
            return true;
        }
        if (checkColumns(board)){
            return true;
        }
        if(turn==9){
            winner=-1;
            return true;
        }
        return false;
    }


    public int getWinner() {
        return winner;
    }

    /**
     * Resets the game to a new game state.
     */
    public void newGame() {
        this.currentPlayer = 0;
        this.turn=0;
        resetBoard(this.board);
    }

    @Override
    public void setPlayer(String player) {
        System.out.println(player + "playeerwww");
       int playerId = player.equalsIgnoreCase("x")?0:1;
        this.currentPlayer=(player.equalsIgnoreCase("X"))?0:1;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    public int changePlayer(){
        if(currentPlayer==0){
            currentPlayer=1;
            return 0;
        }else {
            currentPlayer=0;
            return 1;
        }
    }

    private boolean checkWinnerRowsAndDiagonals(int[][] data) {
        int[] mainDiagonal = new int[data.length];
        int[] secondDiagonal = new int[data.length];

        for (int i = 0; i < data.length; i++) {
            int[] currentRow = data[i];

            if (isFullRow(currentRow)) {
                this.winner = decideWinner(currentRow);
                return true;
            }

            mainDiagonal[i] = data[i][i];
            secondDiagonal[i] = data[i][data.length - 1 - i];
        }

        if (isFullRow(mainDiagonal)) {
            this.winner = decideWinner(mainDiagonal);
            return true;
        }

        if (isFullRow(secondDiagonal)) {
            this.winner = decideWinner(secondDiagonal);
            return true;
        }

        return false;
    }


    private boolean isFullRow(int[] row) {
        boolean theSame = false;
        if(isFullOne(row)||isFullZero(row)){
            theSame=true;
        }
        return theSame;
    }

    private boolean isFullZero(int[] toCheck) {
        int zero = 0;
        for (int i = 0; i < toCheck.length; i++) {
            if (toCheck[i] != zero) {
                return false;
            }
        }
        return true;
    }

    public boolean isFullOne(int[] toCheck) {
        int one= 1;
        for(int i = 0;i<toCheck.length;i++){
            if(toCheck[i]!=one){
                return false;
            }
        }
        return true;
    }

    private int decideWinner(int[] row) {
        if (row[0] == 0) {
            return 0;
        } else if (row[0] == 1) {
            return 1;
        }
        return -1;
    }



    private void resetBoard(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = -1;
            }
        }
    }


    public int[][] getBoard() {
        return board;
    }

    public boolean checkColumns(int[][] board) {
        int rows = board.length;
        int columns = board[0].length;
        int[] columnValues = new int[rows];

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                columnValues[j] = board[j][i];
            }

            if (isFullRow(columnValues)) {
                this.winner = decideWinner(columnValues);
                return true;
            }
        }
        return false;
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
