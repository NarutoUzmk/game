package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.Timer;

public class PinBall {
	private int count = 0;
	private final int TABLE_WIDTH = 500;
	private final int TABLE_HEIGHT = 600;//桌宽和高
	private final int RACKET_Y = 460;//球拍的初始垂直Y高度
	private final int RACKET_HEIGHT = 20;//球拍的大小
	private final int RACKET_WIDTH = 60;
	private final int BALL_SIZE = 16;//球的大小
	private Frame fa = new Frame("弹球游戏");
	Random rand = new Random();//设定一个随机值
	private int Yspeed = 16;
	private double xyRate = rand.nextDouble()-0.63;//设定一个比率值来控制小球方向
	private int Xspeed = (int)(Yspeed*xyRate*2);//小球横向运行速度
	private int ballY = rand.nextInt(200)+20;//小球的坐标
	private int ballX = rand.nextInt(10)+20;
	private int racketX = rand.nextInt(200);//球拍水平位置
	Timer timer;
	private MyCanvas tableArea = new MyCanvas();
	private boolean isLose = false;
	public void init(){
		tableArea.setPreferredSize(new Dimension(TABLE_WIDTH,TABLE_HEIGHT));
		fa.add(tableArea);
		KeyAdapter keyPressor = new KeyAdapter(){
			//@SuppressWarnings("unused")
			public void keyPressed(KeyEvent ke){
				if(ke.getKeyCode() == KeyEvent.VK_LEFT)
				{
					if(racketX > 0)
						racketX -= 20;
				}
				if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					if(racketX < TABLE_WIDTH - RACKET_WIDTH)
						racketX += 20;
				}
			}
		};
		fa.addKeyListener(keyPressor);
		tableArea.addKeyListener(keyPressor);
		ActionListener taskPerformer = evt ->//执行小球监视器，并定义每0.1s刷新一次
		{
			if(ballX <= 0 || ballX >= TABLE_WIDTH-BALL_SIZE)
			{
				Xspeed = -Xspeed;
			}
			if(ballY > RACKET_Y - BALL_SIZE && 
					(ballX < racketX || ballX > racketX + RACKET_WIDTH))
			{
				timer.stop();
				isLose = true;
				tableArea.repaint();
			}
			else if(ballY <= 0 ||
					(ballY >= RACKET_Y - BALL_SIZE && ballX > racketX && ballX <= racketX + RACKET_WIDTH))
			{
				Yspeed = -Yspeed;
				count += 10;
			}
			ballX += Xspeed;
			ballY += Yspeed;
			tableArea.repaint();
		};
		timer = new Timer(95,taskPerformer);
		timer.start();
		fa.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		fa.pack();
		fa.setVisible(true);
	}
	public static void main(String[] args)
	{
		new PinBall().init();
	}
	class MyCanvas extends Canvas
	{
		public void paint(Graphics g){
			if(isLose)
			{
				g.setColor(new Color(255,0,0));
				g.setFont(new Font("Times",Font.BOLD,30));
				g.drawString("游戏已结束您的分数为:"+count, 50, 200);
			}
			else{
				g.setColor(new Color(240,240,80));
				g.fillOval(ballX,ballY,BALL_SIZE,BALL_SIZE);
				g.setColor(new Color(80,80,200));
				g.fillRect(racketX,RACKET_Y ,RACKET_WIDTH, RACKET_HEIGHT);
			}
		}
	}
}
