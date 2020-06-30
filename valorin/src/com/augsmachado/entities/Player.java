package com.augsmachado.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.augsmachado.graphics.Spritesheet;
import com.augsmachado.main.Game;
import com.augsmachado.world.Camera;
import com.augsmachado.world.World;

public class Player extends Entity{
	
	public boolean right = false, left = false, up = false, down = false;
	public double speed = 1.4;
	public int rightDir = 0, leftDir = 1;
	public int dir = rightDir;
	public int ammo = 0;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage playerDamage;
	private boolean hasGun = false;
	
	public static final int PLAYER_SIZE = 16;
	public static double life = 100, maxLife = 100;
	
	
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		
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
		
		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		this.checkCollisionGun();
		
		// When player collides, he blinks to show damage
		if (isDamaged) {
			this.damageFrames++;
			if (this.damageFrames == 10) {
				this.damageFrames  = 0;
				isDamaged = false;
			}
			
		}
		
		// Game over
		if(Player.life <= 0) {
			Game.entities = new ArrayList<Entity>();
			Game.enemies = new ArrayList<Enemy>();
			Game.spritesheet = new Spritesheet("/spritesheet.png");
			Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
			Game.entities.add(Game.player);
			Game.world = new World("/map.png");
			life = 100;
			return;
		}
		
		// Camera focus on the player
		Camera.X = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, (World.WIDTH * PLAYER_SIZE) - Game.WIDTH);
		Camera.Y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, (World.HEIGHT * PLAYER_SIZE) - Game.HEIGHT);
		
	}
	
	public void checkCollisionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Bullet) {
				if (Entity.isCollinding(this, e) ) {
					ammo += 10;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}
	
	public void checkCollisionGun() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Weapon) {
				if (Entity.isCollinding(this, e) ) {
					hasGun = true;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}
	
	public void checkCollisionLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Lifepack) {
				if (Entity.isCollinding(this, e) ) {
					life += 100;
					if (life >= 100) life = 100;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}
	
	
	@Override
	public void render(Graphics g) {
		if (!isDamaged) {
			// Change the sprite's orientation
			if (dir == rightDir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.X, this.getY() - Camera.Y, null);
				
				// Draw gun to right
				if (hasGun) {
					g.drawImage(Entity.GUN_RIGHT, this.getX() - Camera.X +5, this.getY() - Camera.Y, null);
				}
			} else if (dir == leftDir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.X, this.getY() - Camera.Y, null);
				
				// Draw gun to left
				if (hasGun) {
					g.drawImage(Entity.GUN_LEFT, this.getX() - Camera.X -5, this.getY() - Camera.Y, null);
				}
			}
		} else {
			// When player is wholesale
			g.drawImage(playerDamage, this.getX() - Camera.X, this.getY() - Camera.Y, null);
		}
		
		
		
	}
}
