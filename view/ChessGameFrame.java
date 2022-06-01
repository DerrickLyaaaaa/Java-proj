package view;

import controller.GameController;
import model.ChessColor;
import model.OperationInfo;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import client.ClientAgentThread;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Vector;

/**
 * 游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame  implements ActionListener {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGHT;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;
    public Chessboard chessboard;
    public JLabel statusLabel;
    public JLabel warningLabel;
    private final JLabel background = new JLabel();

    
    //局域网入战信息
    public JLabel jlHost = new JLabel("主机名");
    public  JLabel jlPort = new JLabel("端口号");
    public  JLabel jlNickName = new JLabel("昵    称");

    public  JTextField jtfHost = new JTextField("127.0.0.1");
    public  JTextField jtfPort = new JTextField("9999");
    public  JTextField jtfNickName = new JTextField("Play1");

    public  JButton jbConnect = new JButton("连  接");
    public  JButton jbDisconnect = new JButton("断  开");
    public  JButton jbFail = new JButton("认  输");
    public  JButton jbChallenge = new JButton("挑  战");

    public  JComboBox jcbNickList = new JComboBox();
    public  JButton jbYChallenge = new JButton("接受挑战");
    public  JButton jbNChallenge = new JButton("拒绝挑战");
    
    public  JComboBox modeList = new JComboBox();
    
    public Socket sc;

    public  ClientAgentThread cat;
    
    public boolean caiPan = false;//可否走棋的标志位
    public int color = 0;//0 代表橘色，1代表蓝色   代表自己的颜色
    public OperationInfo operationInfo=new OperationInfo();
    
    
    public ChessGameFrame(int width, int height) {
        setTitle("2022 CS102A Project"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;
        this.CHESSBOARD_SIZE = HEIGHT * 4 / 5;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        //局域网入战信息
        initialComponent();
        addListener();
        
        
        addChessboard();//棋盘
        addStatusLabel();//标题
        addWarningLabel();
        addSaveButton();
        addLoadButton();
        addRestartButton();

        URL resource = getClass().getResource("/images/background.jpg"); // 获取背景图片路径
        ImageIcon icon = new ImageIcon(resource); // 创建背景图片对象
        background.setIcon(icon); // 设置标签组件要显示的图标
        background.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight()); // 设置组件的显示位置及大小
        getContentPane().add(background); // 将组件添加到面板中\

        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File("resource/sound/BGM.wav")));
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }catch (LineUnavailableException| UnsupportedAudioFileException|IOException e){
            e.printStackTrace();
        }
    }

    
    
    public void initialComponent() {
        

        this.jlHost.setBounds(HEIGHT, HEIGHT / 10 + 400, 50, 20);
        add(this.jlHost);

        this.jtfHost.setBounds(HEIGHT+60, HEIGHT / 10 + 400, 80, 20);
        add(this.jtfHost);

        this.jlPort.setBounds(HEIGHT, HEIGHT / 10 + 430, 50, 20);
        add(this.jlPort);

        this.jtfPort.setBounds(HEIGHT+60, HEIGHT / 10 + 430, 80, 20);
        add(this.jtfPort);

        this.jlNickName.setBounds(HEIGHT, HEIGHT / 10 + 460, 50, 20);
        add(this.jlNickName);

        this.jtfNickName.setBounds(HEIGHT+60, HEIGHT / 10 + 460, 80, 20);
        add(this.jtfNickName);
        
        
        this.modeList.setBounds(HEIGHT+150, HEIGHT / 10 + 460, 130, 20);
        add(this.modeList);
        
        Vector v = new Vector();
        v.add("player");
        v.add("watcher");
        modeList.setModel(new DefaultComboBoxModel(v));
        

        this.jbConnect.setBounds(HEIGHT, HEIGHT / 10 + 490, 80, 20);
        add(this.jbConnect);

        this.jbDisconnect.setBounds(HEIGHT+100, HEIGHT / 10 + 490, 80, 20);
        add(this.jbDisconnect);

        this.jcbNickList.setBounds(HEIGHT, HEIGHT / 10 + 520, 130, 20);
        add(this.jcbNickList);
        
        
       
        

        this.jbChallenge.setBounds(HEIGHT, HEIGHT / 10 + 550, 80, 20);
        add(this.jbChallenge);

        this.jbFail.setBounds(HEIGHT+100, HEIGHT / 10 + 550, 80, 20);
        add(this.jbFail);

        this.jbYChallenge.setBounds(HEIGHT, HEIGHT / 10 + 580, 86, 20);
        add(this.jbYChallenge);

        this.jbNChallenge.setBounds(HEIGHT+100, HEIGHT / 10 + 580, 86, 20);
        add(this.jbNChallenge);
        
        
       

    }
    
    public void displayMouth(int fromX1,int fromY1,int row,int col) {
		chessboard.mouthMove(fromX1, fromY1, row, col);
    }
    
    
    public void addListener() {

        this.jbConnect.addActionListener(this);
        this.jbDisconnect.addActionListener(this);
        this.jbChallenge.addActionListener(this);
        this.jbFail.addActionListener(this);
        this.jbYChallenge.addActionListener(this);
        this.jbNChallenge.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.jbConnect) {
            this.jbConnect_event();
        } else if (e.getSource() == this.jbDisconnect) {
            this.jbDisconnect_event();
        } else if (e.getSource() == this.jbChallenge) {
            this.jbChallenge_event();
        } else if (e.getSource() == this.jbYChallenge) {
            this.jbYChallenge_event();
        } else if (e.getSource() == this.jbNChallenge) {
            this.jbNChallenge_event();
        } else if (e.getSource() == this.jbFail) {
            this.jbFail_event();
        }
    }
    
    
    
    
    
    public void initialState() {

        this.jbDisconnect.setEnabled(false);
        this.jbChallenge.setEnabled(false);
        this.jbYChallenge.setEnabled(false);
        this.jbNChallenge.setEnabled(false);
        this.jbFail.setEnabled(false);
    }
    
    
    
    
    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE, this);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGHT / 10, HEIGHT / 10);
        this.chessboard=chessboard;
        add(chessboard);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addStatusLabel(){
//        statusLabel = new JLabel("Current player: Orange");
        statusLabel = new JLabel("");
        statusLabel.setLocation(HEIGHT, HEIGHT / 10);
        statusLabel.setSize(350, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }

    private void addWarningLabel(){
        warningLabel = new JLabel("");
        warningLabel.setLocation(HEIGHT,HEIGHT/10+90);
        warningLabel.setSize(300,60);
        warningLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(warningLabel);
    }

    private void addRestartButton(){
        JButton button=new JButton("Restart");
        button.setLocation(HEIGHT, HEIGHT /10+180);
        button.setSize(200,60);
        button.setFont(new Font("Rockwell",Font.BOLD,20));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click Restart");
            Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE, this);
            gameController = new GameController(chessboard);
            chessboard.setLocation(HEIGHT / 10, HEIGHT / 10);
            remove(this.chessboard);
            add(chessboard);

            URL resource = getClass().getResource("/images/background.jpg"); // 获取背景图片路径
            ImageIcon icon = new ImageIcon(resource); // 创建背景图片对象
            background.setIcon(icon); // 设置标签组件要显示的图标
            background.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight()); // 设置组件的显示位置及大小
            getContentPane().add(background); // 将组件添加到面板中

            repaint();
        });
    }

    private void addSaveButton(){
        JButton button = new JButton("Save game");
        button.setLocation(HEIGHT, HEIGHT / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(action -> {
            JFileChooser fileChooser = new JFileChooser("./resource"){{//设置默认路径
                setSelectedFile(new File("save.txt"));//设置默认文件名
                setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));//设置文件过滤器
            }};
            int res = fileChooser.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {//如果点击了“保存”
                try(FileWriter writer = new FileWriter(fileChooser.getSelectedFile())){
                    writer.write(chessboard.toString());
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addLoadButton() {
        JButton button = new JButton("Load game");
        button.setLocation(HEIGHT, HEIGHT / 10 + 300);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(action -> {
            JFileChooser fileChooser = new JFileChooser("./resource");
            int res = fileChooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {//如果点击了“打开”
                File file = fileChooser.getSelectedFile();
                if(!file.getName().split("\\.")[1].equals("txt")){
                    JOptionPane.showMessageDialog(this, "The selected save must be txt!");
                    return;
                }
                gameController.loadGameFromPath(file.getAbsolutePath());
            }
        });
    }



	public void next() {
		// TODO Auto-generated method stub
		 Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE, this);
         gameController = new GameController(chessboard);
         chessboard.setLocation(HEIGHT / 10, HEIGHT / 10);
         remove(this.chessboard);
         add(chessboard);

         URL resource = getClass().getResource("/images/background.jpg"); // 获取背景图片路径
         ImageIcon icon = new ImageIcon(resource); // 创建背景图片对象
         background.setIcon(icon); // 设置标签组件要显示的图标
         background.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight()); // 设置组件的显示位置及大小
         getContentPane().add(background); // 将组件添加到面板中

         chessboard.setCurrentColor(Chessboard.defaultChessColor);
         repaint();
	}
	
	public void jbConnect_event() {
        int port = 0;
        
        	
        	
        
        
        
        try {
            port = Integer.parseInt(this.jtfPort.getText().trim());
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "端口号只能是整数", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (port > 65535 || port < 0) {
            JOptionPane.showMessageDialog(this, "端口号只能是0-65535的整数", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = this.jtfNickName.getText().trim();

        if (name.length() == 0) {
            JOptionPane.showMessageDialog(this, "玩家姓名不能为空", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            sc = new Socket(this.jtfHost.getText().trim(), port);
            cat = new ClientAgentThread(this);
            cat.start();

            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

            JOptionPane.showMessageDialog(this, "已连接到服务器", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "连接服务器失败", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void jbDisconnect_event() {
        try {
            this.cat.dout.writeUTF("<#CLIENT_LEAVE#>");
            this.cat.flag = false;
            this.cat = null;
            this.jtfHost.setEnabled(!false);
            this.jtfPort.setEnabled(!false);
            this.jtfNickName.setEnabled(!false);
            this.jbConnect.setEnabled(!false);
            this.jbDisconnect.setEnabled(!true);
            this.jbChallenge.setEnabled(!true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void jbChallenge_event() {

        Object o = this.jcbNickList.getSelectedItem();

        if (o == null || ((String) o).equals("")) {
            JOptionPane.showMessageDialog(this, "请选择对方名字", "错误",
                    JOptionPane.ERROR_MESSAGE);//当未选中挑战对象，给出错误提示信息
        } else {

            String name2 = (String) this.jcbNickList.getSelectedItem();

            try {
                this.jtfHost.setEnabled(false);
                this.jtfPort.setEnabled(false);
                this.jtfNickName.setEnabled(false);
                this.jbConnect.setEnabled(false);
                this.jbDisconnect.setEnabled(!true);
                this.jbChallenge.setEnabled(!true);
                this.jbYChallenge.setEnabled(false);
                this.jbNChallenge.setEnabled(false);
                this.jbFail.setEnabled(false);

                this.cat.tiaoZhanZhe = name2;
                this.caiPan = true;
                this.color = 0;

                this.cat.dout.writeUTF("<#TIAO_ZHAN#>" + name2);
                JOptionPane.showMessageDialog(this, "已提出挑战,请等待恢复...", "提示",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    public void jbYChallenge_event() {

        try {

            this.cat.dout.writeUTF("<#TONG_YI#>" + this.cat.tiaoZhanZhe);
            this.caiPan = false;//将caiPan设为false
            this.color = 1;//将color设为1

            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(!true);
            this.jbChallenge.setEnabled(!true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(!false);
            
            
            chessboard.loadYourColorInfo(this.color);
            this.operationInfo.setStart(1);
            
            
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    
    public void loadYourColorInfo() {
    	chessboard.loadYourColorInfo(this.color);
    	
    }
    
    
    
    
    public void jbNChallenge_event() {

        try {

            this.cat.dout.writeUTF("<#BUTONG_YI#>" + this.cat.tiaoZhanZhe);
            this.cat.tiaoZhanZhe = null;
            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void jbFail_event() {

        try {

            this.cat.dout.writeUTF("<#RENSHU#>" + this.cat.tiaoZhanZhe);
            this.cat.tiaoZhanZhe = null;
            this.color = 0;
            this.caiPan = false;

            this.next();

            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }



    
    
    
}
