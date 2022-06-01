package model.chess;

import model.ChessColor;
import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

//车
public class RookChessComponent extends ChessComponent {
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image ROOK_WHITE;
    private static Image ROOK_BLACK;
    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image rookImage;

    public void loadResource() throws IOException {
        if (ROOK_WHITE == null) {
            ROOK_WHITE = ImageIO.read(new File("./resource/images/rook-white.png"));
        }
        if (ROOK_BLACK == null) {
            ROOK_BLACK = ImageIO.read(new File("./resource/images/rook-black.png"));
        }
    }

    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     * @param color 棋子颜色
     */
    private void initiateRookImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.Orange) {
                rookImage = ROOK_WHITE;
            } else if (color == ChessColor.BLUE) {
                rookImage = ROOK_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RookChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateRookImage(color);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        int fromX = source.getX();
        int fromY = source.getY();
        int toX = destination.getX();
        int toY = destination.getY();
        if(toX<0||toX>7||toY<0||toY>7){
            return false;
        }
        if (fromX == toX) {
            for (int col = Math.min(source.getY(), toY) + 1;
                 col < Math.max(source.getY(), toY); col++) {
                if (!(chessboard[fromX][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else {
            if (fromY == toY) {
                int col = source.getY();
                for (int row = Math.min(fromX, toX) + 1;
                     row < Math.max(fromX, toX); row++) {
                    if (!(chessboard[row][col] instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
            } else { // Not on the same row or the same column.
                return false;
            }
        }
        return chessboard[toX][toY].chessColor != this.chessColor;
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
        g.drawImage(rookImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

    @Override
    public String toString() {
        if (getChessColor()==ChessColor.BLUE){
            return "R";
        }else return "r";
    }
}
