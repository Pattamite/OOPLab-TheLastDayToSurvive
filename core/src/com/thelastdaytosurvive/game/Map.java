package com.thelastdaytosurvive.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Map {
	public static final char MAP_FREESPACE = '.';
	public static final char MAP_WFENCE_ORIGIN_VERTICAL = '^';
	public static final char MAP_WFENCE_ORIGIN_HORIZONTAL = '>';
	public static final char MAP_MFENCE_ORIGIN_VERTICAL = 'v';
	public static final char MAP_MFENCE_ORIGIN_HORIZONTAL = '<';
	public static final char MAP_TESTFENCE = 'x';
	public static final char MAP_FENCE_BRANCE = '-';
	public static final int MAP_BLOCKSIZE = 40;
	public static int MAP_XNUM;
	public static int MAP_YNUM;
	
	private MainGameWorld mainGameWorld;
	private Texture testFence;
	private Texture wFenceHori;
	private Texture wFenceVerti;
	private Texture mFenceHori;
	private Texture mFenceVerti;
	
	private char worldMap[][];
	private Rectangle worldMapRectangle[][];
	private char aiMap[][];
	private Vector2 originFence[][];
	
	public Map(MainGameWorld mainGameWorld){
		this.mainGameWorld = mainGameWorld;
		testFence = new Texture("Fence/testFence.png");
		
		calculateMapSize();
		setUpMap();
		setUpRectangle();
		setUpTexture();
		
		createTestFence(480,480);
		createTestFence(440,480);
		createTestFence(480,440);
		createTestFence(480,360);
		createFence(900, 900, Crafting.CRAFTING_MFENCE_TYPE, Crafting.CREAFTING_HORIZONTAL);
		createFence(900, 700, Crafting.CRAFTING_WFENCE_TYPE, Crafting.CREAFTING_HORIZONTAL);
		createFence(1100, 1100, Crafting.CRAFTING_MFENCE_TYPE, Crafting.CREAFTING_VERTICAL);
		createFence(1300, 900, Crafting.CRAFTING_WFENCE_TYPE, Crafting.CREAFTING_VERTICAL);
	}
	
	public void update(){
		
	}
	
	public void createFence(float x, float y, int type, int rotation){
		int xPosition = xPosition(x);
		int yPosition = yPosition(y);
		
		if (rotation == Crafting.CREAFTING_HORIZONTAL){
			createFenceHori(xPosition, yPosition, type);
		} else if (rotation == Crafting.CREAFTING_VERTICAL){
			createFenceVerti(xPosition, yPosition, type);
		}
	}
	
	public void createTestFence(float x, float y){
		int xPosition = xPosition(x);
		int yPosition = yPosition(y);
		
		worldMap[yPosition][xPosition] = MAP_TESTFENCE;
		enableRectangle(xPosition, yPosition);
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
				if (worldMap[i][j] == MAP_TESTFENCE){
					batch.draw(testFence, xGamePosition(j), yGamePosition(i));
				} else if (worldMap[i][j] == MAP_WFENCE_ORIGIN_HORIZONTAL){
					batch.draw(wFenceHori, xGamePosition(j - 2), yGamePosition(i));
				} else if (worldMap[i][j] == MAP_WFENCE_ORIGIN_VERTICAL){
					batch.draw(wFenceVerti, xGamePosition(j), yGamePosition(i + 2));
				} else if (worldMap[i][j] == MAP_MFENCE_ORIGIN_HORIZONTAL){
					batch.draw(mFenceHori, xGamePosition(j - 2), yGamePosition(i));
				} else if (worldMap[i][j] == MAP_MFENCE_ORIGIN_VERTICAL){
					batch.draw(mFenceVerti, xGamePosition(j), yGamePosition(i + 2));
				}
				
			}
		}
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
	
	public char[][] getWorldMap(){
		return worldMap;
	}
	
	private void calculateMapSize(){
		MAP_XNUM = (int) (MainGameWorld.MAP_X / MAP_BLOCKSIZE);
		MAP_YNUM = (int) (MainGameWorld.MAP_Y / MAP_BLOCKSIZE);
	}
	
	private void setUpMap(){
		worldMap = new char[MAP_YNUM][MAP_XNUM];
		aiMap = new char[MAP_YNUM][MAP_XNUM];
		originFence = new Vector2[MAP_YNUM][MAP_XNUM];
		
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
	
	private void setUpTexture(){
		wFenceHori = new Texture("Fence/WoodenFenceHorizontal.png");
		wFenceVerti = new Texture("Fence/WoodenFenceVertical.png");
		mFenceHori = new Texture("Fence/MetalFenceHorizontal.png");
		mFenceVerti = new Texture("Fence/MetalFenceVertical.png");
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
	
	private void createFenceVerti(int x, int y, int type){
		if (type == Crafting.CRAFTING_WFENCE_TYPE){
			worldMap[y][x] = MAP_WFENCE_ORIGIN_VERTICAL;
			enableRectangle(x, y);
			
		} else if (type == Crafting.CRAFTING_MFENCE_TYPE){
			worldMap[y][x] = MAP_MFENCE_ORIGIN_VERTICAL;
			enableRectangle(x, y);
		} 
		
		if (y + 1 < MAP_YNUM){
			worldMap[y + 1][x] = MAP_FENCE_BRANCE;
			originFence[y + 1][x] = new Vector2(x, y);
			enableRectangle(x, y + 1);
		}
		if (y + 2 < MAP_YNUM){
			worldMap[y + 2][x] = MAP_FENCE_BRANCE;
			originFence[y + 2][x] = new Vector2(x, y);
			enableRectangle(x, y + 2);
		}
		if (y - 1 >= 0){
			worldMap[y - 1][x] = MAP_FENCE_BRANCE;
			originFence[y - 1][x] = new Vector2(x, y);
			enableRectangle(x, y - 1);
		}
		if (y - 2 >= 0){
			worldMap[y - 2][x] = MAP_FENCE_BRANCE;
			originFence[y - 2][x] = new Vector2(x, y);
			enableRectangle(x, y - 2);
		}
	}
	
	private void createFenceHori(int x, int y, int type){
		if (type == Crafting.CRAFTING_WFENCE_TYPE){
			worldMap[y][x] = MAP_WFENCE_ORIGIN_HORIZONTAL;
			enableRectangle(x, y);
		} else if (type == Crafting.CRAFTING_MFENCE_TYPE){
			worldMap[y][x] = MAP_MFENCE_ORIGIN_HORIZONTAL;
			enableRectangle(x, y);
		} 
		
		if(x + 1 < MAP_XNUM){
			worldMap[y][x + 1] = MAP_FENCE_BRANCE;
			originFence[y][x + 1] = new Vector2(x, y);
			enableRectangle(x + 1, y);
		}
		if(x + 2 < MAP_XNUM){
			worldMap[y][x + 2] = MAP_FENCE_BRANCE;
			originFence[y][x + 2] = new Vector2(x, y);
			enableRectangle(x + 2, y);
		}
		if(x - 1 >= 0){
			worldMap[y][x - 1] = MAP_FENCE_BRANCE;
			originFence[y][x - 1] = new Vector2(x, y);
			enableRectangle(x - 1, y);
		}
		if(x - 2 >= 0){
			worldMap[y][x - 2] = MAP_FENCE_BRANCE;
			originFence[y][x - 2] = new Vector2(x, y);
			enableRectangle(x - 2, y);
		}
	}
	
	private void enableRectangle(int xPosition, int yPosition){
		worldMapRectangle[yPosition][xPosition].x = xGamePosition(xPosition);
		worldMapRectangle[yPosition][xPosition].y = yGamePosition(yPosition);
		worldMapRectangle[yPosition][xPosition].height = MAP_BLOCKSIZE;
		worldMapRectangle[yPosition][xPosition].width = MAP_BLOCKSIZE;
	}
}