package com.augsmachado.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.augsmachado.main.Game;
import com.augsmachado.world.Camera;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	private int maskx, masky, mWidth, mHeight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mWidth = width;
		this.mHeight = height;
	}
	
	public void setMask(int maskx, int masky, int mWidth, int mHeight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}

	public int getX() {
		return (int) this.x;
	}

	public int getY() {
		return (int) this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		
	}
	
	public static boolean isCollinding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mWidth, e1.mHeight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mWidth, e2.mHeight);
				
		return e1Mask.intersects(e2Mask);
	}

	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.X, this.getY() - Camera.Y, height, width, null);
		
		// Debug to test mask
		//g.setColor(Color.RED);
		//g.fillRect(this.getX() - Camera.X, this.getY() - Camera.Y, mWidth, mHeight);
	}
}
