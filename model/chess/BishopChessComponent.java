package model.chess;

import controller.ClickController;
import model.ChessColor;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

//象
public class BishopChessComponent extends ChessComponent {

    private static Image BISHOP_WHITE;
    private static Image BISHOP_BLACK;
    /**
     *
     * 棋子对象自身的图片，是上面两种中的一种
     */
    private Image bishopImage;

    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiateBishopImage(chessColor);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source=getChessboardPoint();
        int fromX = source.getX();
        int fromY = source.getY();
        int toX = destination.getX();
        int toY = destination.getY();
        if(toX<0||toX>7||toY<0||toY>7){
            return false;
        }
        if (fromX - toX == fromY - toY) { // Not on the same row or the same column.
            if (fromX > toX) {
                for (int i = 1; i <fromX-toX; i++) {
                    if (!((chessboard[fromX - i][fromY - i]) instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
            } else if (fromX < toX) {
                for (int i = 1; i < toX - fromX; i++) {
                    if (!(chessboard[toX - i][toY - i] instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
            }
        }else if (fromX-toX==toY-fromY){
            if (fromX>toX){
                for (int i = 1; i < fromX-toX; i++) {
                    if (!(chessboard[fromX-i][fromY+i] instanceof EmptySlotComponent)){
                        return false;
                    }
                }
            }else {
                for (int i = 1; i <toX-fromX ; i++) {
                    if (!(chessboard[fromX+i][fromY-i] instanceof EmptySlotComponent)){
                        return false;
                    }
                }
            }
        } else {//Not on the same row or the same column or on the Slash
            return false;
        }
        return chessboard[toX][toY].chessColor != this.chessColor;
    }

    @Override
    public void loadResource() throws IOException {
        if (BISHOP_WHITE == null) {
            BISHOP_WHITE = ImageIO.read(new File("./resource/images/bishop-white.png"));
        }
        if (BISHOP_BLACK == null) {
            BISHOP_BLACK = ImageIO.read(new File("./resource/images/bishop-black.png"));
        }
    }

    /**
     * 注意：每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(bishopImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
    }

    private void initiateBishopImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.Orange) {
                bishopImage = BISHOP_WHITE;
            } else if (color == ChessColor.BLUE) {
                bishopImage = BISHOP_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        if (getChessColor()==ChessColor.BLUE){
            return "B";
        }else return "b";
    }
}
