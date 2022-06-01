package controller;

import java.io.IOException;

import model.OperationInfo;
import model.chess.ChessComponent;
import util.FileUtil;
import view.Chessboard;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;
    
    OperationInfo operationInfo;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public void onClick(ChessComponent chessComponent) {
    	FileUtil.saveRecordInFile("click ,caipan== "+  String.valueOf(chessboard.getChessGameFrame().caiPan)    );
    	if(!chessboard.getChessGameFrame().caiPan) {
    		System.out.println("暂时不能移动。。");
    		return ;
    	}
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                first = chessComponent;
                first.repaint();
                //让对方看到选中
                int startI=first.getChessboardPoint().getX();
                int startJ=first.getChessboardPoint().getY();
                try {
					chessboard.getChessGameFrame()
					.cat.dout.writeUTF("<#QIChICK#>" +
							chessboard.getChessGameFrame().cat.tiaoZhanZhe +";"+ startI+";" + startJ 
							
								);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        } else {
        	
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
            } else if (first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint())) {
                //repaint in swap chess method.
                chessboard.swapColor();
              //发送消息
                int startI=first.getChessboardPoint().getX();
                int startJ=first.getChessboardPoint().getY();
                int endI=chessComponent.getChessboardPoint().getX();
                int endJ=chessComponent.getChessboardPoint().getY();
                
              
                
                try {
					chessboard.getChessGameFrame()
					.cat.dout.writeUTF("<#MOVE#>" +
							chessboard.getChessGameFrame().cat.tiaoZhanZhe +";"+ startI+";" + startJ +";"+ endI +";"+ endJ
							
								);
					 FileUtil.saveRecordInFile(first.getChessColor().name()+"  click send msg");
					 chessboard.getChessGameFrame().operationInfo.setWaitOther(1);
                } catch (IOException e) {
					e.printStackTrace();
				}
                chessboard.moveChessComponent(first, chessComponent);
                
                chessboard.getChessGameFrame().caiPan = false;
                
                first.setSelected(false);
                first = null;
            }
        }
    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */
    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentColor();
    }

	public void mouseMove(ChessComponent chessComponent) {
		// TODO Auto-generated method stub
		int startI=chessComponent.getX();
        int startJ=chessComponent.getY();
        int px=chessComponent.getChessboardPoint().getX();
        int py=chessComponent.getChessboardPoint().getY();
		try {
			if( chessboard.getChessGameFrame().modeList.getSelectedIndex()==0
				&&chessboard.getChessGameFrame().operationInfo.getWaitOther()==0
				&& chessboard.getChessGameFrame().operationInfo.getStart()==1) {
				chessboard.getChessGameFrame()
				.cat.dout.writeUTF("<#MOUSEMOVE#>" +
						chessboard.getChessGameFrame().cat.tiaoZhanZhe +";"+ startI+";" + startJ
						+";"+px+";"+py
						);
				 FileUtil.saveRecordInFile(""+"  mouse move send msg "+startI+" "+startJ+";"+px+";"+py);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
