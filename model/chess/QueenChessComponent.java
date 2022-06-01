package model.chess;

import controller.ClickController;
import model.ChessColor;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class QueenChessComponent extends ChessComponent {
    private static Image Queen_WHITE;
    private static Image Queen_BLACK;
    private Image queenImage;

    public QueenChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiateQueenImage(chessColor);
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
        if (source.getX() == destination.getX()) {
            int row = source.getX();
            for (int col = Math.min(source.getY(), destination.getY()) + 1;
                 col < Math.max(source.getY(), destination.getY()); col++) {
                if (!(chessboard[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getY() == destination.getY()) {
            int col = source.getY();
            for (int row = Math.min(source.getX(), destination.getX()) + 1;
                row < Math.max(source.getX(), destination.getX()); row++) {
                if (!(chessboard[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (fromX - toX == fromY - toY) { // Not on the same row or the same column.
            if (fromX > toX) {
                for (int i = 1; i < fromX-toX; i++) {
                    if (!((chessboard[fromX - i][fromY - i]) instanceof EmptySlotComponent)) {
                        return false;
                    }
                }
            } else if (fromX < toX) {
                for (int i = 1; i < toX - fromX; i++) {
                    if (!(chessboard[toX-i][toY-i] instanceof EmptySlotComponent)) {
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
        if (Queen_WHITE == null) {
            Queen_WHITE = ImageIO.read(new File("./resource/images/queen-white.png"));
        }
        if (Queen_BLACK == null) {
            Queen_BLACK = ImageIO.read(new File("./resource/images/queen-black.png"));
        }
    }

    private void initiateQueenImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.Orange) {
                queenImage = Queen_WHITE;
            } else if (color == ChessColor.BLUE) {
                queenImage = Queen_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(queenImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

    @Override
    public String toString() {
        if (getChessColor()==ChessColor.BLUE){
            return "Q";
        }else return "q";
    }
}
