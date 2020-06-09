package com.augsmachado.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.augsmachado.main.Game;
import com.augsmachado.world.Camera;
import com.augsmachado.world.World;

public class Player extends Entity{
	
	public boolean right, left, up, down;
	public double speed = 1.4;
	
	public int rightDir = 0, leftDir = 1;
	public int dir = rightDir;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	public static final int PLAYER_SIZE = 16;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * PLAYER_SIZE), 0, PLAYER_SIZE, PLAYER_SIZE);
		}
		
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * PLAYER_SIZE), PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE);
		}
		
	}
	
	public void tick() {
		moved = false;
		
		// Player moves according key pressed
		if (right && World.isFree((int) (x + speed), this.getY())){
			moved = true;
			dir = rightDir;
			x += speed;
		} else if (left && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			dir = leftDir;
			x -= speed;
		}
		
		if (up && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			y -= speed;
		} else if (down && World.isFree(this.getX(), (int) (y + speed))) {
			moved = true;
			y += speed;
		}
		
		// Player animation
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		// Camera focus on the player
		Camera.X = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, (World.WIDTH * PLAYER_SIZE) - Game.WIDTH);
		Camera.Y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, (World.HEIGHT * PLAYER_SIZE) - Game.HEIGHT);
		
		
	}
	
	@Override
	public void render(Graphics g) {
		
		// Change the sprite's orientation
		if (dir == rightDir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.X,this.getY() - Camera.Y, null);
		} else if (dir == leftDir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.X,this.getY() - Camera.Y, null);
		}
	}
}
