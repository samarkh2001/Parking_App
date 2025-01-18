package com.example.parking.ui.parking;

import android.widget.ImageView;

public class ParkingSlot {
    private ImageView img;
    private int row;
    private int col;

    public ParkingSlot(ImageView img, int row, int col){
        this.img = img;
        this.row = row;
        this.col = col;
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
