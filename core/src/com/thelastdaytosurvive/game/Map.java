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
	public static final int MAP_BLOCKSIZE = 40;
	public static int MAP_XNUM;
	public static int MAP_YNUM;
	
	private MainGameWorld mainGameWorld;
	private Texture testFence;
	
	private char worldMap[][];
	private Rectangle worldMapRectangle[][];
	private char aiMap[][];
	
	public Map(MainGameWorld mainGameWorld){
		this.mainGameWorld = mainGameWorld;
		testFence = new Texture("Fence/testFence.png");
		
		calculateMapSize();
		setUpMap();
		setUpRectangle();
		createFence(480,480);
		createFence(440,480);
		createFence(480,440);
		createFence(480,360);
	}
	
	public void update(){
		
	}
	
	
	
	public int xPosition(float value){
		return (int) (value / MAP_BLOCKSIZE);
	}
	
	public int yPosition(float value){
		return (int) ((MainGameWorld.MAP_Y  - value) / MAP_BLOCKSIZE);
	}
	
	public float xGamePosition(int value){
		return value * MAP_BLOCKSIZE;
	}
	
	public float yGamePosition(int value){
		return MainGameWorld.MAP_Y  - ((value + 1) * MAP_BLOCKSIZE);
	}
	
	
	public void createFence(float x, float y){
		int xPosition = xPosition(x);
		int yPosition = yPosition(y);
		
		worldMap[yPosition][xPosition] = MAP_FENCE_ORIGIN_VERTICAL;
		worldMapRectangle[yPosition][xPosition].x = xGamePosition(xPosition);
		worldMapRectangle[yPosition][xPosition].y = yGamePosition(yPosition);
		worldMapRectangle[yPosition][xPosition].height = MAP_BLOCKSIZE;
		worldMapRectangle[yPosition][xPosition].width = MAP_BLOCKSIZE;
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
		for(int i = 0 ; i < MAP_YNUM ; i++){
			for(int j = 0 ; j < MAP_XNUM ; j++){
				if (worldMap[i][j] != MAP_FREESPACE){
					batch.draw(testFence, xGamePosition(j), yGamePosition(i));
				}
				
			}
		}
	}
	
	public char[][] getWorldMap(){
		return worldMap;
	}
	
	private boolean isHitFence(Rectangle mover, char axis, float movement){
		int xPosition = xPosition(mover.getX() + (mover.getWidth() / 2));
		int yPosition = yPosition(mover.getY() + (mover.getHeight() / 2));
		boolean ans = false;
		
		System.out.println(xPosition + " / " + yPosition);
		
		if(axis == 'x'){
			mover.x += movement;
			if(movement > 0 && ((xPosition + 1) < MAP_XNUM)){
				ans = mover.overlaps(worldMapRectangle[yPosition][xPosition + 1]);
				if ((yPosition + 1) < MAP_YNUM){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition + 1][xPosition + 1]);
				}
				if (yPosition > 0){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition - 1][xPosition + 1]);
				}
				
			} else if (movement < 0 && xPosition > 0){
				ans = mover.overlaps(worldMapRectangle[yPosition][xPosition - 1]);
				if ((yPosition + 1) < MAP_YNUM){
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
				if ((xPosition + 1) < MAP_XNUM){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition - 1][xPosition + 1]);
				}
				if (xPosition > 0){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition - 1][xPosition - 1]);
				}
				
			} else if (movement < 0 && (yPosition + 1) < MAP_YNUM){
				ans = mover.overlaps(worldMapRectangle[yPosition + 1][xPosition]);
				if ((xPosition + 1) < MAP_XNUM){
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
		MAP_XNUM = (int) (MainGameWorld.MAP_X / MAP_BLOCKSIZE);
		MAP_YNUM = (int) (MainGameWorld.MAP_Y / MAP_BLOCKSIZE);
	}
	
	private void setUpMap(){
		worldMap = new char[MAP_YNUM][MAP_XNUM];
		aiMap = new char[MAP_YNUM][MAP_XNUM];
		
		for(int i = 0 ; i < MAP_YNUM ; i++){
			for(int j = 0 ; j < MAP_XNUM ; j++){
				worldMap[i][j] = MAP_FREESPACE;
				aiMap[i][j] = MAP_FREESPACE;
			}
		}
	}
	
	private void setUpRectangle(){
		worldMapRectangle = new Rectangle[MAP_YNUM][MAP_XNUM];
		
		for(int i = 0 ; i < MAP_YNUM ; i++){
			for(int j = 0 ; j < MAP_XNUM ; j++){
				worldMapRectangle[i][j] = new Rectangle();
				worldMapRectangle[i][j].x = -1;
				worldMapRectangle[i][j].y = -1;
				worldMapRectangle[i][j].height = 1;
				worldMapRectangle[i][j].width = 1;
			}
		}
	}
}
