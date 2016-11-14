package com.thelastdaytosurvive.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Map {
	public static final char MAP_FREESPACE = '.';
	public static final char MAP_FENCE_ORIGIN_VERTICAL = '^';
	public static final char MAP_FENCE_ORIGIN_HORIZONTAL = '>';
	public static final char MAP_FENCE_BRANCE = '-';
	
	private MainGameWorld mainGameWorld;
	private Texture testFence;
	
	private int blockSize = 40;
	private char worldMap[][];
	private Rectangle worldMapRectangle[][];
	private char aiMap[][];
	private int xMapNum;
	private int yMapNum;
	
	public Map(MainGameWorld mainGameWorld){
		this.mainGameWorld = mainGameWorld;
		testFence = new Texture("Fence/testFence.png");
		
		calculateMapSize();
		setUpMap();
		setUpRectangle();
		createFence(500,500);
	}
	
	public void update(){
		
	}
	
	
	
	public int xPosition(float value){
		return (int) (value / blockSize);
	}
	
	public int yPosition(float value){
		return (int) ((MainGameWorld.MAP_Y  - value) / blockSize);
	}
	
	public void createFence(float x, float y){
		int xPosition = xPosition(x);
		int yPosition = yPosition(y);
		
		worldMap[yPosition][xPosition] = MAP_FENCE_ORIGIN_VERTICAL;
		worldMapRectangle[yPosition][xPosition].x = (xPosition * blockSize);
		worldMapRectangle[yPosition][xPosition].y = MainGameWorld.MAP_Y  - ((yPosition + 1) * blockSize);
		worldMapRectangle[yPosition][xPosition].height = blockSize;
		worldMapRectangle[yPosition][xPosition].width = blockSize;
	}
	
	public void deleteFence(float x, float y){
		int xPosition = xPosition(x);
		int yPosition = yPosition(y);
		
		worldMap[yPosition][xPosition] = MAP_FREESPACE;
		worldMapRectangle[yPosition][xPosition].x = -1;
		worldMapRectangle[yPosition][xPosition].y = -1;
		worldMapRectangle[yPosition][xPosition].height = 1;
		worldMapRectangle[yPosition][xPosition].width = 1;
	}
	
	public Vector2 playerMapMovement(Rectangle mover, Vector2 movement){
		float xMovement = movement.x;
		float yMovement = movement.y;
		if (isHitFence(mover, 'x', movement.x)){
			xMovement = 0;
		}
		if(isHitFence(mover, 'y', movement.y)){
			yMovement = 0;
		};
		
		return new Vector2(xMovement, yMovement);
	}
	
	public void draw(SpriteBatch batch){
		for(int i = 0 ; i < yMapNum ; i++){
			for(int j = 0 ; j < xMapNum ; j++){
				if (worldMap[i][j] != MAP_FREESPACE){
					batch.draw(testFence, j * blockSize , (MainGameWorld.MAP_Y) - ((i + 1 ) * blockSize));
				}
				
			}
		}
	}
	
	private boolean isHitFence(Rectangle mover, char axis, float movement){
		int xPosition = xPosition(mover.getX() + (mover.getWidth() / 2));
		int yPosition = yPosition(mover.getY() + (mover.getHeight() / 2));
		boolean ans = false;
		
		if(axis == 'x'){
			mover.x += movement;
			if(movement > 0 && ((xPosition + 1) < xMapNum)){
				ans = mover.overlaps(worldMapRectangle[yPosition][xPosition + 1]);
				if ((yPosition + 1) < yMapNum){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition + 1][xPosition + 1]);
				}
				if (yPosition > 0){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition - 1][xPosition + 1]);
				}
				
			} else if (movement < 0 && xPosition > 0){
				ans = mover.overlaps(worldMapRectangle[yPosition][xPosition - 1]);
				if ((yPosition + 1) < yMapNum){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition + 1][xPosition - 1]);
				}
				if (yPosition > 0){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition - 1][xPosition - 1]);
				}
			}
			mover.x -= movement;
		} else if(axis == 'y'){
			mover.y += movement;
			if(movement > 0 && (yPosition > 0)){
				ans = mover.overlaps(worldMapRectangle[yPosition - 1][xPosition]);
				if ((xPosition + 1) < xMapNum){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition - 1][xPosition + 1]);
				}
				if (xPosition > 0){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition - 1][xPosition - 1]);
				}
				
			} else if (movement < 0 && (yPosition + 1) < yMapNum){
				ans = mover.overlaps(worldMapRectangle[yPosition + 1][xPosition]);
				if ((xPosition + 1) < xMapNum){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition + 1][xPosition + 1]);
				}
				if (xPosition > 0){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition + 1][xPosition - 1]);
				}
			}
			mover.y -= movement;
		}
		
		return ans;
	}
	
	private void calculateMapSize(){
		xMapNum = (int) (MainGameWorld.MAP_X / blockSize);
		yMapNum = (int) (MainGameWorld.MAP_Y / blockSize);
	}
	
	private void setUpMap(){
		worldMap = new char[yMapNum][xMapNum];
		aiMap = new char[yMapNum][xMapNum];
		
		for(int i = 0 ; i < yMapNum ; i++){
			for(int j = 0 ; j < xMapNum ; j++){
				worldMap[i][j] = MAP_FREESPACE;
				aiMap[i][j] = MAP_FREESPACE;
			}
		}
	}
	
	private void setUpRectangle(){
		worldMapRectangle = new Rectangle[yMapNum][xMapNum];
		
		for(int i = 0 ; i < yMapNum ; i++){
			for(int j = 0 ; j < xMapNum ; j++){
				worldMapRectangle[i][j] = new Rectangle();
				worldMapRectangle[i][j].x = -1;
				worldMapRectangle[i][j].y = -1;
				worldMapRectangle[i][j].height = 1;
				worldMapRectangle[i][j].width = 1;
			}
		}
	}
}
