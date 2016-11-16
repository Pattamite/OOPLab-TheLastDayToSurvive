package com.thelastdaytosurvive.game;

public class AIQueueNode {
	public int xPosition;
	public int yPosition;
	public char directionValue;
	
	public AIQueueNode(int x, int y, char value){
		xPosition = x;
		yPosition = y;
		directionValue = value;
	}
}
