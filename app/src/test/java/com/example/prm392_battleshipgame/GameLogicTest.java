package com.example.prm392_battleshipgame;

import com.example.prm392_battleshipgame.models.Board;
import com.example.prm392_battleshipgame.models.GameManager;
import com.example.prm392_battleshipgame.models.Player;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameLogicTest {

    @Test
    public void testBoardPlacementAndShooting() {
        Board board = new Board();

        // Đặt tàu 3 ô ngang
        boolean placed = board.placeShip(0, 0, 3, true);
        assertTrue("Should place ship successfully", placed);

        // Kiểm tra bắn trúng
        String result1 = board.shoot(0, 0);
        assertEquals("HIT", result1);

        // Kiểm tra bắn trượt
        String result2 = board.shoot(5, 5);
        assertEquals("MISS", result2);

        // Bắn nốt để chìm tàu
        board.shoot(0, 1);
        String result3 = board.shoot(0, 2);
        assertEquals("SUNK", result3);

        assertTrue("All ships should be sunk", board.allShipsSunk());
    }

    @Test
    public void testPlayerAndGameManager_vsAI() {
        Player player = new Player("You", false);
        Player ai = new Player("Computer", true);

        // Mỗi bên đặt 1 tàu 2 ô
        player.getBoard().placeShip(0, 0, 2, true);
        ai.getBoard().placeShip(0, 0, 2, true);

        GameManager gameManager = new GameManager(
                player,
                ai,
                GameManager.GameMode.VS_AI,
                new GameManager.GameListener() {
                    @Override
                    public void onPlayerMove(Player p, int x, int y, String result) {
                        System.out.println(p.getName() + " shot (" + x + "," + y + "): " + result);
                    }

                    @Override
                    public void onGameOver(Player winner, Player loser) {
                        System.out.println("Winner: " + winner.getName() + ", Loser: " + loser.getName());
                    }

                    @Override
                    public void onMessage(String msg) {
                        System.out.println(msg);
                    }
                }
        );

        // Người chơi bắn
        gameManager.playTurn(0, 0);

        // AI sẽ tự bắn lại → không kiểm tra chính xác nhưng đảm bảo game vẫn chạy
        gameManager.playTurn(0, 1);

    }

    @Test
    public void testPlayerAndGameManager_vsPlayer() {
        Player p1 = new Player("Alice", false);
        Player p2 = new Player("Bob", false);

        p1.getBoard().placeShip(0, 0, 2, true);
        p2.getBoard().placeShip(0, 0, 2, true);

        final boolean[] gameEnded = {false};
        final StringBuilder winnerName = new StringBuilder();

        GameManager gm = new GameManager(
                p1,
                p2,
                GameManager.GameMode.VS_PLAYER,
                new GameManager.GameListener() {
                    @Override
                    public void onPlayerMove(Player player, int x, int y, String result) {
                        System.out.println(player.getName() + " shot (" + x + "," + y + "): " + result);
                    }

                    @Override
                    public void onGameOver(Player winner, Player loser) {
                        gameEnded[0] = true;
                        winnerName.append(winner.getName());
                    }

                    @Override
                    public void onMessage(String msg) {
                        System.out.println(msg);
                    }
                }
        );

        // Lượt 1: Alice bắn trúng
        gm.playTurn(0, 0);
        // Lượt 2: Bob bắn
        gm.playTurn(0, 1);
        // Lượt 3: Alice bắn nốt để chìm tàu
        gm.playTurn(0, 1);

        assertTrue("Game should end after all ships are sunk", gameEnded[0]);
        assertEquals("Winner should be Alice", "Alice", winnerName.toString());
    }
}
