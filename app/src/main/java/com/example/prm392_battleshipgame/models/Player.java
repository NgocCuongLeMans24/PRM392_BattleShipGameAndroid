package com.example.prm392_battleshipgame.models;

public class Player implements IBoardObserver {
    private final String name;
    private final Board board;
    private boolean isAI;

    public Player(String name, boolean isAI) {
        this.name = name;
        this.isAI = isAI;
        this.board = new Board();
        // Người chơi có thể tự quan sát board của mình (nếu cần hiển thị)
        this.board.addObserver(this);
    }

    public String getName() {
        return name;
    }

    public boolean isAI() {
        return isAI;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Người chơi bắn vào board đối thủ
     * @param opponentBoard board của đối thủ
     * @param x tọa độ X
     * @param y tọa độ Y
     * @return kết quả "HIT", "MISS", "SUNK", hoặc "INVALID"
     */
    public String takeShot(Board opponentBoard, int x, int y) {
        return opponentBoard.shoot(x, y);
    }

    /**
     * AI bắn ngẫu nhiên
     */
    public void takeRandomShot(Board opponentBoard) {
        if (!isAI) return;

        int x, y;
        String result;
        do {
            x = (int) (Math.random() * Board.SIZE);
            y = (int) (Math.random() * Board.SIZE);
            result = opponentBoard.shoot(x, y);
        } while (result.equals("INVALID"));

        System.out.println("[AI] " + name + " bắn vào (" + x + "," + y + "): " + result);
    }

    // ---------------------------------------------------
    // IMPLEMENTATION OF OBSERVER (Board observer methods)
    // ---------------------------------------------------
    @Override
    public void onCellUpdated(int x, int y, String result) {
        System.out.println("[" + name + "] Cell updated at (" + x + "," + y + "): " + result);
    }

    @Override
    public void onShipSunk(Ship ship) {
        System.out.println("[" + name + "] Một tàu đã bị chìm!");
    }

    @Override
    public void onAllShipsSunk() {
        System.out.println("[" + name + "] Tất cả tàu đã bị đánh chìm!");
    }
}
