package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements KeyListener, ActionListener {
	
	// random number generator
	Random rand = new Random();

	// initialize the game to be 'not over'
	private boolean game_over = false;

	// initialize the game to be 'not playing'
	private boolean isPlaying = false;
	
	// player 1 paddle
	private int player1_paddle;
	
	// player 2 paddle
	private int player2_paddle;
	
	// boolean variables
	private boolean w_pressed = false;
	private boolean s_pressed = false;
	private boolean up_pressed = false;
	private boolean down_pressed = false;
	
	// initialize variables that will denote the ball location + speed
	private int xBall = 375;
	private int yBall = 275;
	private int dxBall = rand.nextInt(7) + 2;
	private int dyBall = rand.nextInt(7) + 2;
	
	// initialize timer
	private Timer timer;

	public Game() {
		player1_paddle = 225;
		player2_paddle = 225;
		
		// enable keypad inputs
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		// initialize timer
		timer = new Timer(5, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		if (game_over) {
			isPlaying = false;
			w_pressed = false;
			s_pressed = false;
			up_pressed = false;
			down_pressed = false;
			game_over = true;
			g.setColor(Color.RED);
			g.setFont(new Font("Times New Roman", Font.BOLD, 40));
			g.drawString("GAME OVER!", 270, 310);
			g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
			g.drawString("Press Enter to Play Again", 300, 330);
		} else {
			// fill background of game window
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 600);

			// fill borders of game window
			g.setColor(Color.MAGENTA);
			g.fillRect(0, 0, 5, 600);
			g.fillRect(0, 0, 800, 5);
			g.fillRect(780, 0, 5, 600);
			g.fillRect(0, 558, 800, 5);
			
			// draw player 1 & player 2 paddles
			g.setColor(Color.CYAN);
			g.fillRect(50, player1_paddle, 10, 100);
			g.setColor(Color.ORANGE);
			g.fillRect(725, player2_paddle, 10, 100);
			
			// draw ball
			g.setColor(Color.GREEN);
			g.fillOval(xBall, yBall, 20, 20);
		}
		
		g.dispose();
	}
	
	public void updateBall() {
		// if the ball is touching a wall, change its x mvmt
		if (xBall + 20 >= 775 || xBall <= 5) {
			game_over = true;
		}
		
		// update ball's x pos.
		xBall += dxBall;
		
		// if the ball is touching a wall, change its y mvmt
		if (yBall + 20 >= 560 || yBall <= 5) {
			dyBall = dyBall * -1;
		}
		
		// update ball's y pos.
		yBall += dyBall;
	}

	public void movePaddle() {
		if (w_pressed) {
			s_pressed = false;
			if (this.player1_paddle <= 10) {
				this.player1_paddle = 10;
			} else {
				this.player1_paddle -= 5;
			}
		} else if (s_pressed) {
			w_pressed = false;
			if (this.player1_paddle + 100 >= 555) {
				this.player1_paddle = 555 - 100;
			} else {
				this.player1_paddle += 5;
			}
		}
		
		if (up_pressed) {
			down_pressed = false;
			if (this.player2_paddle <= 10) {
				this.player2_paddle = 10;
			} else {
				this.player2_paddle -= 5;
			}
		} else if (down_pressed) {
			up_pressed = false;
			if (this.player2_paddle + 100 >= 555) {
				this.player2_paddle = 555 - 100;
			} else {
				this.player2_paddle += 5;
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// get the code of the key pressed
		int key_code = e.getKeyCode();

		// move player 1 up if W is pressed
		if (key_code == KeyEvent.VK_W) {
			w_pressed = true;
			s_pressed = false;
			isPlaying = true;
		}
		
		// move player 1 down if S is pressed
		if (key_code == KeyEvent.VK_S) {
			s_pressed = true;
			w_pressed = false;
			isPlaying = true;
		}
		
		if (key_code == KeyEvent.VK_UP) {
			up_pressed = true;
			down_pressed = false;
			isPlaying = true;
		}
		
		if (key_code == KeyEvent.VK_DOWN) {
			down_pressed = true;
			up_pressed = false;
			isPlaying = true;
		}
		
		// if the current game is over, user can restart game on ENTER
		if (game_over && key_code == KeyEvent.VK_ENTER) {
			game_over = false;
			player1_paddle = 225;
			player2_paddle = 225;
			xBall = 375;
			yBall = 275;
			int rand_num = rand.nextInt(2) + 1;
			double neg_pos = Math.pow(-1.0, (double)rand_num);
			dxBall = (rand.nextInt(7) + 2) * (int)neg_pos;
			dyBall = (rand.nextInt(7) + 2) * (int)neg_pos;
			timer.restart();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if (isPlaying) {
			Rectangle ballRect = new Rectangle(xBall, yBall, 20, 20);
			Rectangle player1 = new Rectangle(50, player1_paddle, 10, 100);
			Rectangle player2 = new Rectangle(725, player2_paddle, 10, 100);
			if (ballRect.intersects(player1) || ballRect.intersects(player2)) {
				dxBall = dxBall * -1;
			}
			updateBall();
			movePaddle();
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {}
}
