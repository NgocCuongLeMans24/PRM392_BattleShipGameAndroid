package com.example.prm392_battleshipgame.models;

public interface IBoardObserver {
    void onCellUpdated(int x, int y, String result); // HIT, MISS, SUNK
    void onShipSunk(Ship ship);
    void onAllShipsSunk();
}
