import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Renderer;

public class GameCore extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 960, HEIGHT = 800;
	private Thread thread;

	public static enum STATE{
		GAME,
		MENU
	};
	public static STATE state = STATE.GAME;

	private Menu menu = new Menu();
	private boolean started = false;
	private static boolean gameOver = true;
	public static double score = 0;
	
	public static Pipe pipe;
	public Bird bird;
	public BackDrop backdrop;

	
	public static final int UP = KeyEvent.VK_UP;
	public static final int DN = KeyEvent.VK_DOWN;
	public static final int LT = KeyEvent.VK_LEFT;
	public static final int RT = KeyEvent.VK_RIGHT;

	public static final int _A = KeyEvent.VK_A;
	public static final int _S = KeyEvent.VK_S;
	
	public static final int _1 = KeyEvent.VK_1;
	public static final int _2 = KeyEvent.VK_2;
	public static final int _3 = KeyEvent.VK_3;
	public static final int _4 = KeyEvent.VK_4;
	
	//Constructor
	public GameCore() {
		Dimension d = new Dimension(GameCore.WIDTH, GameCore.HEIGHT);
		Camera.setLocation(90, 0);
		
		requestFocus();
		setPreferredSize(d);
		addKeyListener(this);
		
		pipe = new Pipe(60);
		bird = new Bird(20,GameCore.HEIGHT/2,pipe.pipes);
	}
	
	public synchronized void start() {		
		if(started) return;
		started = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if(!started) return;
		started = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

	public static void main(String[] args) {
		JFrame jFrame = new JFrame();
		GameCore game = new GameCore();
		jFrame.add(game);
		
		
		//jFrame.setResizable(false);
		jFrame.pack();
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setTitle("Wacky Bird");
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		jFrame.setFocusable(true);

	
		Object Sound;
		Sound = new Sound();
		((Sound) Sound).playBackgroundMusic();

		game.start();
		
	}

	@Override
	public void run() {
		int fps = 0;
		long lastUpdated = System.nanoTime();
		double timer = System.currentTimeMillis();
		double delta = 0;
		double nanosecs = 1000000000 / 60;
		
		/* game loop */
		while(started) {
			long now = System.nanoTime();
			delta += (now - lastUpdated) / nanosecs;
			lastUpdated = now;
			
			while(delta >= 1) {
				update();
				try {
					render();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fps++;
				delta--;
				
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + fps);
				System.out.println("isGameOver: " + gameOver);
				System.out.print("Game State: " + state);
			}
			
			if(score <= 3) {
				backdrop = new BackDrop(1);
			}else {
				backdrop = new BackDrop(2);
			}
			
			if(bird.keyPressed && state == STATE.GAME) Camera.moveRight(5);
		}
		
		stop();
		
	}

	private void render() throws IOException {
		BufferStrategy buf = getBufferStrategy();
		if(buf == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = buf.getDrawGraphics();

		if(state == STATE.GAME){
			start();
			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana",Font.BOLD, 21));
			g.drawString("Score: "+ (int)GameCore.score, 10, 20);

			g.setColor(Color.green);
			g.fillRect(0, HEIGHT - 5, WIDTH, 20);

			backdrop.render(g);
			pipe.render(g);
			bird.render(g);
		}else if(state == STATE.MENU){
			stop();
			menu.render(g);
		}

		g.dispose();
		buf.show();
	}

	public static boolean isGameOver(boolean gameStatus) {
		return gameOver = gameStatus;
	}

//	public static BufferedImage loadImages() {
////		return bgImgLevel1 = loadImage("airadventurelevel2.png");
//	}

	private void update() {
		if(state == STATE.GAME){
			backdrop.update();
			pipe.update();
			bird.update();
		}
//		else if(state == STATE.MENU){
//			//do somethin
//		}

	}
	
//	public static BufferedImage loadImage(String fileName){
//		//return new ImageIcon(fileName).getImage();
//		return new BufferedImage(fileName).getSource();
//	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			bird.keyPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			bird.keyPressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_R) {
			state = STATE.GAME;
			bird.restart = true;
			bird.restartGame = true;
		}

		if(e.getKeyCode() == KeyEvent.VK_S) {
			state = STATE.GAME;
			bird.restart = true;
			bird.restartGame = true;
		}

		if(e.getKeyCode() == KeyEvent.VK_Q) {
			System.exit(0);
		}
	}
}
