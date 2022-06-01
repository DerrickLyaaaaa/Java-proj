package model.chess;

import controller.ClickController;
import model.ChessColor;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

//兵
public class PawnChessComponent extends ChessComponent{
    private static Image Pawn_WHITE;
    private static Image Pawn_BLACK;
    private Image pawnImage;

    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiatePawnImage(chessColor);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        int fromX=source.getX();
        int fromY=source.getY();
        int toX=destination.getX();
        int toY=destination.getY();
        if(toX<0||toX>7||toY<0||toY>7||chessboard[toX][toY].chessColor == this.chessColor){
            return false;
        }
        if (getChessColor() == ChessColor.BLUE) {
            if (chessboard[fromX+1][fromY] instanceof EmptySlotComponent&&chessboard[fromX+2][fromY] instanceof EmptySlotComponent && fromX == 1&&destination.getX()-fromX==2){
                return true;
            }else if (destination.getX()-fromX==1&&toY==fromY&&chessboard[toX][toY] instanceof EmptySlotComponent){
                return true;
            }else if (toX-fromX==1&&chessboard[toX][toY].getChessColor()!=ChessColor.NONE){
                if (toY-fromY==1){
                    return true;
                }else return toY - fromY == -1;
            }
        }else if (getChessColor()==ChessColor.Orange){
            if (chessboard[fromX-1][fromY] instanceof EmptySlotComponent&&chessboard[fromX-2][fromY] instanceof EmptySlotComponent &&fromX==6&&destination.getX()-fromX==-2){
                return true;
            } else if (destination.getX()-fromX==-1&&toY==fromY&&chessboard[toX][toY] instanceof EmptySlotComponent) {
                return true;
            }else if (toX-fromX==-1&&chessboard[toX][toY].getChessColor()!=ChessColor.NONE){
                if (toY-fromY==1){
                    return true;
                }else return toY - fromY == -1;
            }
        }
        return false;
    }

    @Override
    public void loadResource() throws IOException {
        if (Pawn_WHITE == null) {
            Pawn_WHITE = ImageIO.read(new File("./resource/images/pawn-white.png"));
        }
        if (Pawn_BLACK == null) {
            Pawn_BLACK = ImageIO.read(new File("./resource/images/pawn-black.png"));
        }

    }

    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     * @param color 棋子颜色
     */
    private void initiatePawnImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.Orange) {
                pawnImage = Pawn_WHITE;
            } else if (color == ChessColor.BLUE) {
                pawnImage = Pawn_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(pawnImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(pawnImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

    @Override
    public String toString() {
       if (getChessColor()==ChessColor.BLUE){
           return "P";
       }else return "p";
    }
}
