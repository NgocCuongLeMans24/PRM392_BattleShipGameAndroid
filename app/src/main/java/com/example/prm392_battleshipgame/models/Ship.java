package com.example.prm392_battleshipgame.models;

import java.util.List;

public class Ship {
    private final List<Cell> cells;
    private boolean sunk;

    public Ship(List<Cell> cells) {
        this.cells = cells;
        this.sunk = false;
    }

    public boolean isSunk() { return sunk; }

    public boolean contains(Cell cell) {
        return cells.contains(cell);
    }

    public boolean checkIfSunk() {
        for (Cell c : cells) {
            if (!c.isHit()) return false;
        }
        sunk = true;
        return true;
    }
}
