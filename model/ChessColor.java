package model;

import java.awt.*;

/**
 * 包装Color对象，用于Chess游戏使用。
 */
public enum ChessColor {
    BLUE("Blue",1), Orange("Orange",0), NONE("No Player",2);

    private  String name;
    private  int num;

    ChessColor(String name,int num) {
        this.name = name;
        this.num=num;
    }

    public String getName() {
        return name;
    }
    
    public void setNum(int num) {
    	this.num=num;
    }
    
    public int getNum() {
    	return num;
    }
    
    
}
