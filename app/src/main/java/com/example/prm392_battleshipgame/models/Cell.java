package com.example.prm392_battleshipgame.models;

public class Cell {
    private final int x;
    private final int y;
    private boolean hasShip;
    private boolean isHit;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.hasShip = false;
        this.isHit = false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean hasShip() { return hasShip; }
    public boolean isHit() { return isHit; }

    public void setShip(boolean hasShip) { this.hasShip = hasShip; }
    public void hit() { this.isHit = true; }

    @Override
    public String toString() {
        if (!isHit) return "O";        // chưa bị bắn
        return hasShip ? "X" : "~";    // X = trúng, ~ = trượt
    }
}
