package controller;

import view.Chessboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import model.ChessColor;

public class GameController {
    private Chessboard chessboard;

    
    
    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public void loadGameFromPath(String path) {
        try{
            chessboard.loadGame(Files.readString(Path.of(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
