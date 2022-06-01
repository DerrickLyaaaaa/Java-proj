package model.chess;

import controller.ClickController;
import model.ChessColor;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class KingChessComponent extends ChessComponent{

    private static Image KING_WHITE;
    private static Image KING_BLACK;
    private Image kingImage;

    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiateKingImage(chessColor);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        int fromX = source.getX();
        int fromY = source.getY();
        int toX = destination.getX();
        int toY = destination.getY();
        if(toX<0||toX>7||toY<0||toY>7||chessboard[toX][toY].chessColor == this.chessColor){
            return false;
        }
        if (fromX-toX==1&&fromY==toY){
            return true;
        }else if (fromX-toX==-1&&fromY==toY){
            return true;
        }else if (fromX==toX&&fromY-toY==1){
            return true;
        }else if (fromX==toX&&fromY-toY==-1){
            return true;
        }else if (fromX-toX==1&&fromY-toY==1){
            return true;
        }else if (fromX-toX==1&&fromY-toY==-1){
            return true;
        }else if (fromX-toX==-1&&fromY-toY==1){
            return true;
        }else return fromX - toX == -1 && fromY - toY == -1;
    }

    @Override
    public void loadResource() throws IOException {
        if (KING_WHITE == null) {
            KING_WHITE = ImageIO.read(new File("./resource/images/king-white.png"));
        }
        if (KING_BLACK == null) {
            KING_BLACK = ImageIO.read(new File("./resource/images/king-black.png"));
        }
    }

    private void initiateKingImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.Orange) {
                kingImage = KING_WHITE;
            } else if (color == ChessColor.BLUE) {
                kingImage = KING_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(kingImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

    @Override
    public String toString() {
        if (getChessColor()==ChessColor.BLUE){
            return "K";
        }else {return "k";}
    }
}
