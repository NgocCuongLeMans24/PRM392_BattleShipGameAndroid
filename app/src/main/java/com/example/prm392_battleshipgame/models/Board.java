package com.example.prm392_battleshipgame.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class Board quản lý toàn bộ bản đồ trò chơi:
 *  - Lưu các ô (Cell)
 *  - Lưu danh sách tàu (Ship)
 *  - Xử lý bắn và kết quả
 *  - Gửi sự kiện cho observer (theo Observer pattern)
 */
public class Board {
    public static final int SIZE = 10; // kích thước mặc định 10x10
    private final Cell[][] cells;
    private final List<Ship> ships;
    private final List<IBoardObserver> observers;

    // ----------------------------------------
    // Constructor
    // ----------------------------------------
    public Board() {
        cells = new Cell[SIZE][SIZE];
        ships = new ArrayList<>();
        observers = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    // ----------------------------------------
    // Observer methods
    // ----------------------------------------
    public void addObserver(IBoardObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IBoardObserver observer) {
        observers.remove(observer);
    }

    private void notifyCellUpdated(int x, int y, String result) {
        for (IBoardObserver o : observers) {
            o.onCellUpdated(x, y, result);
        }
    }

    private void notifyShipSunk(Ship ship) {
        for (IBoardObserver o : observers) {
            o.onShipSunk(ship);
        }
    }

    private void notifyAllShipsSunk() {
        for (IBoardObserver o : observers) {
            o.onAllShipsSunk();
        }
    }

    // ----------------------------------------
    // Getter
    // ----------------------------------------
    public Cell getCell(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return null;
        return cells[x][y];
    }

    public List<Ship> getShips() {
        return ships;
    }

    // ----------------------------------------
    // Đặt tàu
    // ----------------------------------------

    private boolean canPlaceShip(int startX, int startY, int length, boolean horizontal) {
        if (horizontal && startY + length > SIZE) return false;
        if (!horizontal && startX + length > SIZE) return false;

        for (int i = 0; i < length; i++) {
            int x = startX + (horizontal ? 0 : i);
            int y = startY + (horizontal ? i : 0);
            if (cells[x][y].hasShip()) return false;
        }
        return true;
    }

    public boolean placeShip(int startX, int startY, int length, boolean horizontal) {
        if (!canPlaceShip(startX, startY, length, horizontal)) return false;

        List<Cell> shipCells = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int x = startX + (horizontal ? 0 : i);
            int y = startY + (horizontal ? i : 0);
            cells[x][y].setShip(true);
            shipCells.add(cells[x][y]);
        }
        ships.add(new Ship(shipCells));
        return true;
    }

    public void placeShipRandomly(int length) {
        Random random = new Random();
        boolean placed = false;

        while (!placed) {
            int x = random.nextInt(SIZE);
            int y = random.nextInt(SIZE);
            boolean horizontal = random.nextBoolean();
            placed = placeShip(x, y, length, horizontal);
        }
    }

    // ----------------------------------------
    // Xử lý bắn
    // ----------------------------------------
    public String shoot(int x, int y) {
        Cell cell = getCell(x, y);
        if (cell == null || cell.isHit()) {
            return "INVALID";
        }

        cell.hit();
        String result;

        if (cell.hasShip()) {
            result = "HIT";
            for (Ship ship : ships) {
                if (ship.contains(cell)) {
                    ship.checkIfSunk();
                    if (ship.isSunk()) {
                        result = "SUNK";
                        notifyShipSunk(ship);
                    }
                    break;
                }
            }
        } else {
            result = "MISS";
        }

        // 🔔 Gửi thông báo cập nhật ô vừa bắn
        notifyCellUpdated(x, y, result);

        // 🔔 Nếu tất cả tàu đã chìm
        if (allShipsSunk()) {
            notifyAllShipsSunk();
        }

        return result;
    }

    // ----------------------------------------
    // Kiểm tra kết thúc
    // ----------------------------------------
    public boolean allShipsSunk() {
        for (Ship s : ships) {
            if (!s.isSunk()) return false;
        }
        return true;
    }

    // ----------------------------------------
    // In ra bàn cờ (debug)
    // ----------------------------------------
    public void printBoard(boolean showShips) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell c = cells[i][j];
                if (!showShips && c.hasShip() && !c.isHit())
                    System.out.print("O ");
                else
                    System.out.print(c.toString() + " ");
            }
            System.out.println();
        }
    }
}
