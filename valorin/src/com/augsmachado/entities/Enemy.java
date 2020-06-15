package com.augsmachado.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.augsmachado.main.Game;
import com.augsmachado.world.Camera;
import com.augsmachado.world.World;

public class Enemy extends Entity{
	
	private double speed = 0.6;
	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 1;
	
	private BufferedImage[] sprites;
	
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, World.TILE_SIZE, World.TILE_SIZE);
		sprites[1] = Game.spritesheet.getSprite(128, 16, World.TILE_SIZE, World.TILE_SIZE);
		
	}
	
	public void tick() {
		
		if(isCollidingWithPlayer() == false) {
			
			// Function to chase the player
			if (Game.rand.nextInt(100) < 30) {
				if (x < (int) Game.player.getX() && World.isFree((int)(x+speed), this.getY())
						&& !isColliding((int)(x+speed), this.getY())) {
					x += speed;
				} else if (x > (int) Game.player.getX() && World.isFree((int)(x-speed), this.getY())
						&& !isColliding((int)(x-speed), this.getY())) {
					x -= speed;
				}
				
				if (y < (int) Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))
						&& !isColliding(this.getX(), (int)(y+speed))) {
					y += speed;
				} else if (y > (int) Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))
						&& !isColliding(this.getX(), (int)(y-speed))) {
					y -= speed;
				}
			}
		} else {
			
			// Player is colliding with enemy, then lose lives
			if (Game.rand.nextInt(100) < 10) {
				Game.player.life -= Game.rand.nextInt(5) ;
				System.out.println("Vida: " + Game.player.life);
				
				if(Game.player.life <= 0) {
					System.exit(1);
				}
			}
		}
		
		
		// Enemy animation
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}

	}
	
	
	// Checks collision with player
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX(), this.getY(), World.TILE_SIZE, World.TILE_SIZE);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), Player.PLAYER_SIZE, Player.PLAYER_SIZE);
		
		return enemyCurrent.intersects(player);
		
	}
	
	
	// Checks collision of enemies
	public boolean isColliding (int xNext, int yNext) {
		Rectangle enemyCurrent = new Rectangle(xNext, yNext, World.TILE_SIZE, World.TILE_SIZE);
		
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			
			if (e == this) continue;
			
			Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);
			
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public void render(Graphics g) {
		g.drawImage(sprites[index], this.getX() - Camera.X, this.getY() - Camera.Y, null);
		
		// See the enemies mask
		//g.setColor(Color.BLUE);
		//g.fillRect(this.getX() - Camera.X, this.getY() - Camera.Y, World.TILE_SIZE, World.TILE_SIZE);
	
	
		
	}

}
