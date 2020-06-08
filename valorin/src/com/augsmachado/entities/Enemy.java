package com.augsmachado.entities;

import java.awt.image.BufferedImage;

import com.augsmachado.main.Game;
import com.augsmachado.world.World;

public class Enemy extends Entity{
	
	private double speed = 0.6;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
	}
	
	public void tick() {
		
		// Function to chase the player
		if (Game.rand.nextInt(100) < 30) {
			if (x < (int) Game.player.getX() && World.isFree((int)(x+speed), this.getY())) {
				x += speed;
			} else if (x > (int) Game.player.getX() && World.isFree((int)(x-speed), this.getY())) {
				x -= speed;
			}
			
			if (y < (int) Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))) {
				y += speed;
			} else if (y > (int) Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))) {
				y -= speed;
			}
		}
	}

}
