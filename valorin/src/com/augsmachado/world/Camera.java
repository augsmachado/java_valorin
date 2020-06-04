package com.augsmachado.world;

public class Camera {

	public static int X = 0;
	public static int Y = 0;
	
	// Clamp in relation to the X axis
	public static int clamp(int current, int min, int max) {
		if (current < min) {
			current = min;
		}
		
		if (current > max) {
			current = max;
		}
		return current;
	}
}
