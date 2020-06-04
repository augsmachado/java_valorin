package com.augsmachado.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.augsmachado.entities.Bullet;
import com.augsmachado.entities.Enemy;
import com.augsmachado.entities.Entity;
import com.augsmachado.entities.Lifepack;
import com.augsmachado.entities.Weapon;
import com.augsmachado.main.Game;

public class World {
	
	private Tile[] tiles;
	public static int WIDTH, HEIGHT;
	
	
	public World(String path) {
		try {	
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			
			
			int[] pixels = new int[WIDTH * HEIGHT];
			tiles = new Tile[WIDTH * HEIGHT];
			
			map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
			
			// Scroll the map looking for the corresponding color to be used by a sprite (e.g. Wall)
			for (int xx = 0; xx < WIDTH; xx++) {
				for (int yy = 0; yy < HEIGHT; yy++) {
					int currentPixel = pixels[xx + (yy * WIDTH)];
					
					// Everyone is floor
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					
					
					if (currentPixel == 0xFF000000) {
						// floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
						
					} else if (currentPixel == 0xFFFFFFFF) {
						// wall
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					
					} else if (currentPixel == 0xFF0026FF) {
						// player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					
					} else if (currentPixel == 0xFFFF0000){
						// enemy
						Game.entities.add(new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN));
					
					}  else if (currentPixel == 0xFFFF6A00) {
						// weapon
						Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
						
					} else if (currentPixel == 0xFF4CFF00) {
						// life pack
						Game.entities.add(new Lifepack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN));
						
					}  else if (currentPixel == 0xFFFFD800) {
						// bullet
						Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void render(Graphics g) {
		
		// Render only a part of map
		int xStart = Camera.X >> 4;
		int yStart = Camera.Y >> 4;
		
		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4);
		
		for (int xx = xStart; xx <= xFinal; xx++) {
			for (int yy = yStart; yy <= yFinal; yy++) {
				
				// Case negative index to camera
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) continue;
				
				Tile tile = tiles[xx + (yy* WIDTH)];
				tile.render(g);
			}
		}
	}
}
