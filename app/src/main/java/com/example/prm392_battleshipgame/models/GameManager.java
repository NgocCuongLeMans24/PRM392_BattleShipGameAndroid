package com.example.prm392_battleshipgame.models;

public class GameManager implements IBoardObserver {

    public enum GameMode {
        VS_AI,
        VS_PLAYER
    }

    private final Player player1;
    private final Player player2;
    private final GameMode mode;
    private Player currentPlayer;
    private boolean gameOver = false;

    public interface GameListener {
        void onPlayerMove(Player player, int x, int y, String result);
        void onGameOver(Player winner, Player loser);
        void onMessage(String msg);
    }

    private final GameListener listener;

    public GameManager(Player player1, Player player2, GameMode mode, GameListener listener) {
        this.player1 = player1;
        this.player2 = player2;
        this.mode = mode;
        this.listener = listener;
        this.currentPlayer = player1;

        // Đăng ký quan sát hai board
        player1.getBoard().addObserver(this);
        player2.getBoard().addObserver(this);
    }

    // -------------------------------------------------------------
    // LUẬT CHƠI
    // -------------------------------------------------------------
    public void playTurn(int x, int y) {
        if (gameOver) return;

        Player opponent = (currentPlayer == player1) ? player2 : player1;
        String result = currentPlayer.takeShot(opponent.getBoard(), x, y);
        listener.onPlayerMove(currentPlayer, x, y, result);

        if (opponent.getBoard().allShipsSunk()) {
            endGame(currentPlayer, opponent);
            return;
        }

        // Nếu là AI mode, để AI bắn lại
        if (mode == GameMode.VS_AI && opponent.isAI()) {
            opponent.takeRandomShot(player1.getBoard());
            if (player1.getBoard().allShipsSunk()) {
                endGame(opponent, player1);
                return;
            }
        }

        // Đổi lượt
        currentPlayer = opponent;
    }

    private void endGame(Player winner, Player loser) {
        gameOver = true;
        listener.onGameOver(winner, loser);
    }

    // -------------------------------------------------------------
    // OBSERVER EVENTS
    // -------------------------------------------------------------
    @Override
    public void onCellUpdated(int x, int y, String result) {
        // Không cần làm gì thêm, đã thông báo qua GameListener
    }

    @Override
    public void onShipSunk(Ship ship) {
        listener.onMessage("Một tàu đã bị chìm!");
    }

    @Override
    public void onAllShipsSunk() {
        listener.onMessage("Tất cả tàu đã bị chìm!");
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
