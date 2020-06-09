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
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	
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
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
					
					
					if (currentPixel == 0xFF000000) {
						// floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
						
					} else if (currentPixel == 0xFFFFFFFF) {
						// wall
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL);
					
					} else if (currentPixel == 0xFF0026FF) {
						// player
						Game.player.setX(xx * TILE_SIZE);
						Game.player.setY(yy * TILE_SIZE);
					
					} else if (currentPixel == 0xFFFF0000){
						// enemy
						Enemy en = new Enemy(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
						
					}  else if (currentPixel == 0xFFFF6A00) {
						// weapon
						Game.entities.add(new Weapon(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.WEAPON_EN));
						
					} else if (currentPixel == 0xFF4CFF00) {
						// life pack
						Game.entities.add(new Lifepack(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.LIFEPACK_EN));
						
					}  else if (currentPixel == 0xFFFFD800) {
						// bullet
						Game.entities.add(new Bullet(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.BULLET_EN));
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean isFree(int xNext, int yNext) {
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;
		
		int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE;
		
		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
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
