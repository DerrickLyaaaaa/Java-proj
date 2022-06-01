package view;

import model.*;
import controller.ClickController;
import model.chess.*;
import util.FileUtil;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 棋盘组件对象
 */
public class Chessboard extends JComponent {
	
	
    
	public static ChessColor defaultChessColor;
	
    private static final int CHESSBOARD_SIZE = 8; //棋盘是8*8的

    private final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentColor = ChessColor.Orange; //当前行棋方
    //all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    private static ChessGameFrame FRAME;
    private final ArrayList<ChessComponent> blueChesses=new ArrayList<>(16);
    private final ArrayList<ChessComponent> orangeChesses=new ArrayList<>(16);
    private KingChessComponent blueKing;
    private KingChessComponent orangeKing;
    private static final int[][] KING_MOVE={{-1,-1},{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1}};
    JLabel label;
    
    public void mouthMove(int x,int y,int row,int col) {
    	
//			Image image= ImageIO.read(new File("./resource/images/demo.jpg"));
			
			//取消之前的选中
			for (ChessComponent[] tmp1:  chessComponents) {
				for (ChessComponent tmp2:  tmp1) {
					if(!tmp2.getRivalClick()) {
						tmp2.setSelected(false);
						tmp2.repaint();
					}
					
				}
				
			}
			
			ChessComponent nowComponent=chessComponents[row][col];
			nowComponent.setSelected(true);
			nowComponent.repaint();
			
			
    }
    
    public void chessClick(int row, int col) {
		// TODO Auto-generated method stub
    	for (ChessComponent[] tmp1:  chessComponents) {
			for (ChessComponent tmp2:  tmp1) {
				if(!tmp2.getRivalClick()) {
					tmp2.setSelected(false);
					tmp2.repaint();
				}
			}
			
		}
		
		ChessComponent nowComponent=chessComponents[row][col];
		nowComponent.setSelected(true);
		nowComponent.setRivalClick(true);
		nowComponent.repaint();
		
	}
    
    
    
    
    
    public Chessboard(int width, int height, ChessGameFrame frame) {
    	
    	
        FRAME=frame;
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);
        initialAll();
        
        
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(ChessColor color) {
    	currentColor=color;
    }
    
    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX();
        int col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
        chessComponent.repaint();
    }

    public void moveChessComponent(ChessComponent from, ChessComponent to) {
        // Note that "from" has higher priority, 'destroys' "to" if exists.
        if (!(to instanceof EmptySlotComponent)) {
            remove(to);
            add(to = new EmptySlotComponent(to.getChessboardPoint(), to.getLocation(), clickController, CHESS_SIZE));
        }
        from.swapLocation(to);
        int fromX = from.getChessboardPoint().getX(), fromY = from.getChessboardPoint().getY();
        chessComponents[fromX][fromY] = from;
        int toX = to.getChessboardPoint().getX(), toY = to.getChessboardPoint().getY();
        chessComponents[toX][toY] = to;

        from.repaint();
        to.repaint();

        handleWarning();
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File("resource/sound/Move.wav")));
            clip.loop(0);
        }catch (LineUnavailableException|UnsupportedAudioFileException|IOException e){
            e.printStackTrace();
        }
    }

    public void rivalMoveChessComponent(int fromX1,int fromY1,int toX1,int toY1) {
    	
    	FileUtil.saveRecordInFile("对手移动棋子。。。。");
    	ChessComponent from=chessComponents[fromX1][fromY1];
    	ChessComponent to=chessComponents[toX1][toY1];
    	from.setRivalClick(false);
    	to.setRivalClick(false);
    	to.setSelected(false);
        to.repaint();
        
        
//        try {
//			Thread.sleep(500);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//        to.setSelected(false);
//        to.repaint();
        
        
        // Note that "from" has higher priority, 'destroys' "to" if exists.
        if (!(to instanceof EmptySlotComponent)) {
            remove(to);
            add(to = new EmptySlotComponent(to.getChessboardPoint(), to.getLocation(), clickController, CHESS_SIZE));
        }
        
        
        
        
        from.swapLocation(to);
        int fromX = from.getChessboardPoint().getX(), fromY = from.getChessboardPoint().getY();
        chessComponents[fromX][fromY] = from;
        int toX = to.getChessboardPoint().getX(), toY = to.getChessboardPoint().getY();
        chessComponents[toX][toY] = to;

        from.repaint();
        to.repaint();
        //更换对手
        swapColor();
        handleWarning();
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File("resource/sound/Move.wav")));
            clip.loop(0);
        }catch (LineUnavailableException|UnsupportedAudioFileException|IOException e){
            e.printStackTrace();
        }
        try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      //取消之前的选中
		for (ChessComponent[] tmp1:  chessComponents) {
			for (ChessComponent tmp2:  tmp1) {
				tmp2.setSelected(false);
				tmp2.repaint();
			}
			
		}
        
        
    }
    
    
    
    
    private boolean win(ChessComponent killer,KingChessComponent king,ArrayList<ChessComponent> attackChesses,ArrayList<ChessComponent> defenseChesses){
        int kingX=king.getChessboardPoint().getX();
        int kingY=king.getChessboardPoint().getY();
        for (ChessComponent defenseChess : defenseChesses) {//If the killer can be killed, the king will survive
            if(defenseChess.canMoveTo(chessComponents, killer.getChessboardPoint())){
                return false;
            }
        }
        out:for(int[] move:KING_MOVE){//Check whether in all possible movements, the king will be killed
            if(king.canMoveTo(chessComponents,new ChessboardPoint(kingX+move[0],kingY+move[1]))){
                for(ChessComponent attackChess:attackChesses){
                    if(attackChess.canMoveTo(chessComponents,new ChessboardPoint(kingX+move[0],kingY+move[1]))){
                        continue out;
                    }
                }
                return false;
            }
        }
        //Although all the king's movements can be killed, the defenser may use some chess to "block" the killing path
        //But here I ignore it, because the judge logic is complex
        return true;
    }

    private void handleWarning(){
        KingChessComponent king=currentColor==ChessColor.Orange?orangeKing:blueKing;
        ArrayList<ChessComponent> attackChesses=currentColor==ChessColor.Orange?blueChesses:orangeChesses;
        ArrayList<ChessComponent> defenseChesses=currentColor==ChessColor.Orange?orangeChesses:blueChesses;
        for (ChessComponent attackChess : attackChesses) {
            if(attackChess.canMoveTo(chessComponents,king.getChessboardPoint())){
                FRAME.warningLabel.setText(currentColor.getName()+" king is in danger!");
                if(win(attackChess,king,attackChesses,defenseChesses)){
                    try {
                        Clip clip = AudioSystem.getClip();
                        clip.open(AudioSystem.getAudioInputStream(new File("resource/sound/Win.wav")));
                        clip.loop(0);
                    }catch (LineUnavailableException|UnsupportedAudioFileException|IOException e){
                        e.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(this,currentColor.getName()+" Win!");

                    FRAME.setVisible(false);
                    SwingUtilities.invokeLater(() -> {
                        ChessGameFrame mainFrame = new ChessGameFrame(1050, 760);
                        mainFrame.setVisible(true);
                    });
                }
                return;
            }
        }
        FRAME.warningLabel.setText("");
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE));
            }
        }
    }

    private void initQueenOnBoard(int row,int col,ChessColor color){
        ChessComponent chessComponent=new QueenChessComponent(new ChessboardPoint(row,col),calculatePoint(row,col),color,clickController,CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        if(color==ChessColor.BLUE){
            blueChesses.add(chessComponent);
        }else{orangeChesses.add(chessComponent);}
    }

    private void initKingOnBoard(int row,int col,ChessColor color){
        ChessComponent chessComponent=new KingChessComponent(new ChessboardPoint(row,col),calculatePoint(row,col),color,clickController,CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        if(color==ChessColor.BLUE){
            blueChesses.add(chessComponent);
            blueKing=(KingChessComponent)chessComponent;
        }else{
            orangeChesses.add(chessComponent);
            orangeKing=(KingChessComponent)chessComponent;
        }
    }

    private void initPawnOnBoard(int row,int col,ChessColor color){
        ChessComponent chessComponent=new PawnChessComponent(new ChessboardPoint(row,col),calculatePoint(row,col),color,clickController,CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        if(color==ChessColor.BLUE){
            blueChesses.add(chessComponent);
        }else{orangeChesses.add(chessComponent);}
    }

    private void initBishopOnBoard(int row,int col,ChessColor color){
        ChessComponent chessComponent=new BishopChessComponent(new ChessboardPoint(row,col),calculatePoint(row,col),color,clickController,CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        if(color==ChessColor.BLUE){
            blueChesses.add(chessComponent);
        }else{orangeChesses.add(chessComponent);}
    }

    private void initKnightOnBoard(int row,int col,ChessColor color){
        ChessComponent chessComponent=new KnightChessComponent(new ChessboardPoint(row,col),calculatePoint(row,col),color,clickController,CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        if(color==ChessColor.BLUE){
            blueChesses.add(chessComponent);
        }else{orangeChesses.add(chessComponent);}
    }

    private void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        if(color==ChessColor.BLUE){
            blueChesses.add(chessComponent);
        }else{orangeChesses.add(chessComponent);}
    }

    public void swapColor() {
    	String alertInfo="";
        currentColor = currentColor == ChessColor.BLUE ? ChessColor.Orange : ChessColor.BLUE;
        if(currentColor.getNum()==getChessGameFrame().color) {
        	alertInfo="（你）";
        }else {
        	alertInfo="（对手）";
        }
        if(getChessGameFrame().modeList.getSelectedIndex()==1) {
        	alertInfo="（观战模式）";
        }
        
        
        FileUtil.saveRecordInFile("Current player: "+currentColor.getName()+alertInfo);
        FRAME.statusLabel.setText("Current player: "+currentColor.getName()+alertInfo);
    }

    public void initialAll(){
        initiateEmptyChessboard();

        initRookOnBoard(0, 0, ChessColor.BLUE); //车
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLUE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.Orange);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.Orange);
        initBishopOnBoard(0,2,ChessColor.BLUE); //象
        initBishopOnBoard(0,5,ChessColor.BLUE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1,2,ChessColor.Orange);
        initBishopOnBoard(CHESSBOARD_SIZE - 1,5,ChessColor.Orange);
        initKingOnBoard(0,4,ChessColor.BLUE);
        initKingOnBoard(CHESSBOARD_SIZE - 1,4,ChessColor.Orange);
        initQueenOnBoard(0,3,ChessColor.BLUE);
        initQueenOnBoard(CHESSBOARD_SIZE-1,3,ChessColor.Orange);
        for (int i = 0; i < CHESSBOARD_SIZE; i++) {
            initPawnOnBoard(1,i,ChessColor.BLUE); //兵
            initPawnOnBoard(CHESSBOARD_SIZE-2,i,ChessColor.Orange);
        }
        initKnightOnBoard(0, 1, ChessColor.BLUE); //马
        initKnightOnBoard(0, CHESSBOARD_SIZE - 2, ChessColor.BLUE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.Orange);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.Orange);
    }

    @Override
    protected void paintComponent(Graphics g) {
    	System.out.println("paintComponent=======");
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        ((Graphics2D) g).drawOval(100, 10, 10, 10);
//        try {
//			Image image = ImageIO.read(new File("./resource/images/demo.jpg"));
//			g.drawImage(image, 0, 0, 10, 10, this);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
    }

    public Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    public void loadYourColorInfo(int  yourColor) {
    	
    	String alertInfo="";
        if(currentColor.getNum()==getChessGameFrame().color) {
        	alertInfo="（你）";
        }else {
        	alertInfo="（对手）";
        }
        
        
        
        FRAME.statusLabel.setText("Current player: "+currentColor.getName()+alertInfo);
    	
    }
    
    
    public void loadGame(String chessData) {
        char c = chessData.charAt(chessData.length()-1);//最后一个字符表示当前行棋方
        if(c=='o'){
            currentColor=ChessColor.Orange;
          
        }else if(c=='b'){
            currentColor=ChessColor.BLUE;
        }else{JOptionPane.showMessageDialog(this, "The selected save contains no current player!");return;}
        FRAME.statusLabel.setText("Current player: "+currentColor.getName());
        
        this.defaultChessColor=currentColor;  

        //1.检查存档
        String[] rows = chessData.substring(0, chessData.length() - 2).split("\n");
        //1.1棋盘行数不为8
        if(rows.length!=8){
            JOptionPane.showMessageDialog(this, "The row number is not 8!");
            return;
        }
        char[][] chesses = new char[8][8];
        for (int i = 0; i < 8; i++) {
            //1.2棋盘列数不为8
            if(rows[i].length()!=8){
                JOptionPane.showMessageDialog(this, "The column number is not 8!");
                return;
            }
            chesses[i]=rows[i].toCharArray();
        }
        HashSet<Character> validChesses = new HashSet<>(Set.of('_', 'R', 'r', 'N', 'n', 'B', 'b', 'Q', 'q', 'K', 'k', 'P', 'p'));
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if(!validChesses.contains(chesses[i][j])){
                    JOptionPane.showMessageDialog(this, "The selected save contains invalid chess!");
                    return;
                }

        //2.开始加载
        blueChesses.clear();
        orangeChesses.clear();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                switch(chesses[i][j]){
                    case '_'->putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE));
                    case 'R'->initRookOnBoard(i,j,ChessColor.BLUE);
                    case 'r'->initRookOnBoard(i,j,ChessColor.Orange);
                    case 'N'->initKnightOnBoard(i,j,ChessColor.BLUE);
                    case 'n'->initKnightOnBoard(i,j,ChessColor.Orange);
                    case 'B'->initBishopOnBoard(i,j,ChessColor.BLUE);
                    case 'b'->initBishopOnBoard(i,j,ChessColor.Orange);
                    case 'Q'->initQueenOnBoard(i,j,ChessColor.BLUE);
                    case 'q'->initQueenOnBoard(i,j,ChessColor.Orange);
                    case 'K'->initKingOnBoard(i,j,ChessColor.BLUE);
                    case 'k'->initKingOnBoard(i,j,ChessColor.Orange);
                    case 'P'->initPawnOnBoard(i,j,ChessColor.BLUE);
                    case 'p'->initPawnOnBoard(i,j,ChessColor.Orange);
                }
        handleWarning();
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                sb.append(chessComponents[i][j].toString());
            }
            sb.append("\n");
        }
        if (getCurrentColor()==ChessColor.Orange){
            sb.append("o");
        }else {sb.append("b");}
        return sb.toString();
    }
    
    public ChessGameFrame getChessGameFrame() {
    	return FRAME;
    	
    	
    }



	
    
    
}
