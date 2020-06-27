package com.augsmachado.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.augsmachado.entities.Player;
import com.augsmachado.main.Game;

public class UI {
	public void render(Graphics g) {
		// Visual score of player's lives
		g.setColor(Color.RED);
		g.fillRect(10, 5, 50, 10);
		
		g.setColor(Color.GREEN);
		g.fillRect(10, 5, (int)((Player.life / Player.maxLife) * 50), 10);
		
		
	}
}
