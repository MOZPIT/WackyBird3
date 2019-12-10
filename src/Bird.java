import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

@SuppressWarnings("unused")
public class Bird extends Rectangle{
	
	private static final long serialVersionUID = 1L;
	
	private float speed = 3;
	public boolean keyPressed = false;
	public boolean gameOver = false, restart = false, restartGame = false;
	public boolean flappingUp = false, flappingDown = false, birdDropping = false;
	private boolean objCollision = false;
	private BufferedImage flapUp;
	private BufferedImage flapDown;
	private BufferedImage flapDead;
	private static int birdsXVal;
	
	
	private ArrayList<Rectangle> pipes;
	
	private float gravity = 0.1f;
	
	public Bird(int x, int y, ArrayList<Rectangle> pipes) {
		setBounds(x,y,32,32);
		this.pipes = pipes;
		this.birdsXVal = x;
		
		try {
			flapUp = Sprite.getSprite("images/flapUpBird.png");
			flapDown = Sprite.getSprite("images/flapDownBird.png");
			flapDead = Sprite.getSprite("images/flapDownBird.png");
			//flapDead = Sprite.getSprite("images/flapDeadBird.png");
			
		}catch(IOException ex){
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		
	}
	
	public void update() {
		
		if(keyPressed) {
			speed = 2;
			y -= (speed);
			flappingUp = true;
			flappingDown = false;
		}else {
			y += speed;
			flappingUp = false;
			flappingDown = true;
			birdDropping = true;
		}
		 
		if(birdDropping) {
			speed += gravity;
			if(speed > 8) speed = 8;
		}
		
		//check for collision
		for(int i = 0; i < pipes.size(); i++) {
			if(this.intersects(pipes.get(i))){
					gameOver = true;
					GameCore.state = GameCore.STATE.MENU;
//					GameCore.pipe = new Pipe(80);
//					pipes = GameCore.pipe.pipes;
//					y = GameCore.HEIGHT/2;
					break;
			}
		}                   
		
		if(y >= GameCore.HEIGHT) {
			//restart the game
			try {
				Thread.sleep(20);
			}
			catch (InterruptedException ex) {}

			GameCore.pipe = new Pipe(80);
			pipes = GameCore.pipe.pipes;
			y = GameCore.HEIGHT/2;
			GameCore.score = 0;
			gameOver = false;
		}
	}
	
	public void render(Graphics g) {
 		g.setColor(Color.red);
		
		if(flappingUp && !gameOver) { 
			g.drawImage(flapUp,x,y,width,height,null);
		}else if(flappingDown && !gameOver) {
		    g.drawImage(flapDown,x,y,width,height,null);
		}
	
		if(gameOver) {
			//gameNotifications notify = new gameNotifications("Game Over");
			//GameCore.isGameOver(true);
			System.out.println("Bird collision: " + objCollision);
//			GameCore.isGameOver(gameOver);
//			GameCore.state = GameCore.STATE.MENU;
		}

//		if(restartGame) {
//			gameOver = false;
//			GameCore.state = GameCore.STATE.GAME;
//		}

		
	}
	
	public static int getBirdX() {
		return birdsXVal;
	}

	public static Image getBird() throws IOException {
		return Sprite.getSprite("images/flapDownBird.png");
	}

}
