package model.chess;

import model.ChessColor;
import view.ChessboardPoint;
import controller.ClickController;

import java.awt.*;

//空位置
public class EmptySlotComponent extends ChessComponent {

    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location, ClickController listener, int size) {
        super(chessboardPoint, location, ChessColor.NONE, listener, size);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }

    @Override
    public void loadResource(){
        //No resource!
    }

    @Override
    public String toString() {
        return "_";
    }
}
