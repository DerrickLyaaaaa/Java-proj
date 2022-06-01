package client;

import javax.swing.*;

import util.FileUtil;
import view.ChessGameFrame;
import view.Chessboard;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class ClientAgentThread extends Thread {

	public ChessGameFrame father;
	public  boolean flag = true;

	public DataInputStream din;
	public DataOutputStream dout;

	public String tiaoZhanZhe = null;

    public ClientAgentThread(ChessGameFrame father) {
        this.father = father;
        try {

            din = new DataInputStream(father.sc.getInputStream());
            dout = new DataOutputStream(father.sc.getOutputStream());

            String name = father.jtfNickName.getText().trim();
            String mode=(String)father.modeList.getSelectedItem();
            
            dout.writeUTF("<#NICK_NAME#>" + name+";"+mode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (flag) {
            try {
              
                String msg = din.readUTF().trim();
                FileUtil.saveRecordInFile("client run method ,reciew msg ====="+msg);
                System.out.println(this.currentThread()+"client read msg"+msg);
                if (msg.startsWith("<#NAME_CHONGMING#>")) {
                    this.name_chongming();
                } else if (msg.startsWith("<#NICK_LIST#>")) {
                    this.nick_list(msg);
                } else if (msg.startsWith("<#SERVER_DOWN#>")) {
                    this.server_down();
                } else if (msg.startsWith("<#TIAO_ZHAN#>")) {
                    this.tiao_zhan(msg);
                } else if (msg.startsWith("<#TONG_YI#>")) {
                    this.tong_yi();
                } else if (msg.startsWith("<#BUTONG_YI#>")) {
                    this.butong_yi();
                } else if (msg.startsWith("<#BUSY#>")) {
                    this.busy();
                } else if (msg.startsWith("<#MOVE#>")) {
                    this.move(msg);
                } else if (msg.startsWith("<#RENSHU#>")) {
                    this.renshu();
                } else if (msg.startsWith("<#MOUSEMOVE#>")) {
                    this.mouseMove(msg);
                }else if (msg.startsWith("<#QIChICK#>")) {
                    this.qiChick(msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    

	public void name_chongming() {
        try {
            JOptionPane.showMessageDialog(this.father, "该玩家名称已经被占用，请重新填写！",
                    "错误", JOptionPane.ERROR_MESSAGE);

            din.close();
            dout.close();

            this.father.jtfHost.setEnabled(!false);
            this.father.jtfPort.setEnabled(!false);
            this.father.jtfNickName.setEnabled(!false);
            this.father.jbConnect.setEnabled(!false);
            this.father.jbDisconnect.setEnabled(!true);
            this.father.jbChallenge.setEnabled(!true);
            this.father.jbYChallenge.setEnabled(false);
            this.father.jbNChallenge.setEnabled(false);
            this.father.jbFail.setEnabled(false);

            father.sc.close();
            father.sc = null;
            father.cat = null;
            flag = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nick_list(String msg) {
        String s = msg.substring(13);
        String[] na = s.split("\\|");

        Vector v = new Vector();

        for (int i = 0; i < na.length; i++) {
            if (na[i].trim().length() != 0 && (!na[i].trim().equals(father.jtfNickName.getText().trim()))) {
                v.add(na[i]);
            }
        }

        father.jcbNickList.setModel(new DefaultComboBoxModel(v));
    }

    public void server_down() {

        this.father.jtfHost.setEnabled(!false);
        this.father.jtfPort.setEnabled(!false);
        this.father.jtfNickName.setEnabled(!false);
        this.father.jbConnect.setEnabled(!false);
        this.father.jbDisconnect.setEnabled(!true);
        this.father.jbChallenge.setEnabled(!true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);

        this.flag = false;
        father.cat = null;

        JOptionPane.showMessageDialog(this.father, "服务器停止！！！", "提示",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void tiao_zhan(String msg) {

        try {

            String name = msg.substring(13);

            if (this.tiaoZhanZhe == null) {

                tiaoZhanZhe = msg.substring(13);

                this.father.jtfHost.setEnabled(false);
                this.father.jtfPort.setEnabled(false);
                this.father.jtfNickName.setEnabled(false);
                this.father.jbConnect.setEnabled(false);
                this.father.jbDisconnect.setEnabled(!true);
                this.father.jbChallenge.setEnabled(!true);
                this.father.jbYChallenge.setEnabled(!false);
                this.father.jbNChallenge.setEnabled(!false);
                this.father.jbFail.setEnabled(false);

                JOptionPane.showMessageDialog(this.father, tiaoZhanZhe + "向你挑战!!!",
                        "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.dout.writeUTF("<#BUSY#>" + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tong_yi() {

        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(!true);
        this.father.jbChallenge.setEnabled(!true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(!false);
        //加载当前下棋信息
        this.father.loadYourColorInfo();
        father.operationInfo.setStart(1);
        father.operationInfo.setWaitOther(0);
        JOptionPane.showMessageDialog(this.father, "对方接受您的挑战!您先走棋(橘色)",
                "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public void butong_yi() {

        this.father.caiPan = false;
        this.father.color = 0;
        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(true);
        this.father.jbChallenge.setEnabled(true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);

        JOptionPane.showMessageDialog(this.father, "对方拒绝您的挑战!", "提示",
                JOptionPane.INFORMATION_MESSAGE);

        this.tiaoZhanZhe = null;
    }

    public void busy() {

        this.father.caiPan = false;
        this.father.color = 0;

        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(true);
        this.father.jbChallenge.setEnabled(true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);

        JOptionPane.showMessageDialog(this.father, "对方忙碌中！！！", "提示",
                JOptionPane.INFORMATION_MESSAGE);

        this.tiaoZhanZhe = null;
    }

    public void move(String msg) {
    	 System.out.println("move msg=="+msg);
    	 FileUtil.saveRecordInFile(msg);
    	 
    	
    	 
    	 
    	 String[] arr=msg.split(";");
    	 int fromX1=Integer.valueOf(arr[1]);
    	 int fromY1=Integer.valueOf(arr[2]);
    	 int toX1=Integer.valueOf(arr[3]);
    	 int toY1=Integer.valueOf(arr[4]);
    	 
    	 
    	 
    	 FileUtil.saveRecordInFile("数组索引=="+  fromX1+" "+fromY1+" "+toX1+" "+toY1);
    	 
    	 
    	 this.father.chessboard.rivalMoveChessComponent(fromX1, fromY1, toX1, toY1);
    	 father.operationInfo.setWaitOther(0);
//        this.father.jpz.move(startI, startJ, endI, endJ);
//        this.father.chessboard.move();
    	if(this.father.modeList.getSelectedIndex()==0) {
    		 this.father.caiPan = true;
    		 father.operationInfo.setWaitOther(0);
    	}
       
    }
    
   
    
    public void mouseMove(String msg) {
      	 System.out.println("mouthMove msg=="+msg);
      	 FileUtil.saveRecordInFile(msg);
      	 String[] arr=msg.split(";");
      	 int fromX1=Integer.valueOf(arr[1]);
      	 int fromY1=Integer.valueOf(arr[2]);
      	 
      	 int potx=Integer.valueOf(arr[3]);
      	 int poty=   Integer.valueOf(arr[4]);
        FileUtil.saveRecordInFile("移动位置=="+  fromX1+" "+fromY1+"--->"+potx+";"+poty);
        try {
   			Robot rbt = new Robot();
   			this.father.displayMouth(fromX1, fromY1,potx,poty);
   			
   		} catch (Exception e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
         
         
      }
    
    
     public void qiChick(String msg) {
      	 System.out.println("qiChick msg=="+msg);
      	 FileUtil.saveRecordInFile(msg);
      	 String[] arr=msg.split(";");
      	 int fromX1=Integer.valueOf(arr[1]);
      	 int fromY1=Integer.valueOf(arr[2]);
      	 
        FileUtil.saveRecordInFile("点击位置=="+  fromX1+" "+fromY1);
        try {
   			this.father.chessboard.
   			chessClick(fromX1, fromY1);
   			
   		} catch (Exception e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
         
         
      }
    
    
    
    public void renshu() {

        JOptionPane.showMessageDialog(this.father, "恭喜你,你获胜,对方认输", "提示",
                JOptionPane.INFORMATION_MESSAGE);

        this.tiaoZhanZhe = null;
        this.father.color = 0;
        this.father.caiPan = false;
        this.father.next();

        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(true);
        this.father.jbChallenge.setEnabled(true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);
    }
}