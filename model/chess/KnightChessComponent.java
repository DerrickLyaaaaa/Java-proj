package model.chess;

import controller.ClickController;
import model.ChessColor;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

//马
public class KnightChessComponent extends ChessComponent {

    private static Image KNIGHT_WHITE;
    private static Image KNIGHT_BLACK;
    private Image knightImage;

    @Override
    public void loadResource() throws IOException {
        if (KNIGHT_WHITE == null) {
            KNIGHT_WHITE = ImageIO.read(new File("./resource/images/knight-white.png"));
        }
        if (KNIGHT_BLACK == null) {
            KNIGHT_BLACK = ImageIO.read(new File("./resource/images/knight-black.png"));
        }
    }

    public KnightChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiateKnightImage(chessColor);
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
        if (toX-fromX==2&&toY-fromY==1){
            return true;
        }else if (toX-fromX==2&&toY-fromY==-1){
            return true;
        }else if (toX-fromX==-2&&toY-fromY==1){
            return true;
        }else if (toX-fromX==-2&&toY-fromY==-1){
            return true;
        }else if (toX-fromX==1&&toY-fromY==2){
            return true;
        }else if (toX-fromX==1&&toY-fromY==-2){
            return true;
        }else if (toX-fromX==-1&&toY-fromY==2){
            return true;
        }else return toX - fromX == -1 && toY - fromY == -2;
    }

    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     * @param color 棋子颜色
     */
    private void initiateKnightImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.Orange) {
                knightImage = KNIGHT_WHITE;
            } else if (color == ChessColor.BLUE) {
                knightImage = KNIGHT_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(knightImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

    @Override
    public String toString() {
      if (getChessColor()==ChessColor.BLUE){
          return "N";
      }else return "n";
    }
}
