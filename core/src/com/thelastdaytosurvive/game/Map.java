package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;

public class Map {
	public static final char MAP_FREESPACE = '.';
	
	public static final char MAP_WFENCE_ORIGIN_VERTICAL = '^';
	public static final char MAP_WFENCE_ORIGIN_HORIZONTAL = '>';
	public static final char MAP_MFENCE_ORIGIN_VERTICAL = 'v';
	public static final char MAP_MFENCE_ORIGIN_HORIZONTAL = '<';
	public static final char MAP_TESTFENCE = 'x';
	public static final char MAP_FENCE_BRANCE = '-';
	
	public static final char MAP_AI_LEFT = 'l';
	public static final char MAP_AI_RIGHT = 'r';
	public static final char MAP_AI_UP = 'u';
	public static final char MAP_AI_DOWN = 'd';
	public static final char MAP_AI_UNKNOWN = 'x';
	public static final char MAP_AI_PLAYER = 'p';
	public static final char MAP_AI_QUEUE = 'q';
	
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
	private Vector2 fenceOrigin[][];
	private int fenceHealth[][];
	private Queue<AIQueueNode> aiQueue;
	private Sound woodDeploy;
	private Sound metalDeploy;
	private Sound woodDestroy;
	private Sound metalDestroy;
	private float woodDeployVolume = 1f;
	private float metalDeployVolume = 1f;
	private float woodDestroyVolume = 1f;
	private float metalDestroyVolume = 1f;
	
	private int lastPlayerX = 0;
	private int lastPlayerY = 0;
	private boolean isNeedUpdateAI;
	private int aiCount = 0;
	
	private int queueSize = 0;
	private int queueSizePeak = 0;
	
	public Map(MainGameWorld mainGameWorld){
		this.mainGameWorld = mainGameWorld;
		testFence = new Texture("Fence/testFence.png");
		
		calculateMapSize();
		setUpMap();
		setUpRectangle();
		setUpTexture();
		
		woodDeploy = Gdx.audio.newSound(Gdx.files.internal("Sound/WoodFenceDeploy.wav"));
		metalDeploy = Gdx.audio.newSound(Gdx.files.internal("Sound/MetalFenceDeploy.wav"));
		woodDestroy = Gdx.audio.newSound(Gdx.files.internal("Sound/WoodFenceDestroy.wav"));
		metalDestroy = Gdx.audio.newSound(Gdx.files.internal("Sound/MetalFenceDestroy.wav"));
	}
	
	public void update(){
		updatePlayerPosition();
		//System.out.println(isNeedUpdateAI);
		if(isNeedUpdateAI){
			updateAI();
			isNeedUpdateAI = false;
		}
	}
	
	public void createFence(float x, float y, int type, int rotation){
		int xPosition = xPosition(x);
		int yPosition = yPosition(y);
		
		if (rotation == Crafting.CREAFTING_HORIZONTAL){
			createFenceHori(xPosition, yPosition, type);
		} else if (rotation == Crafting.CREAFTING_VERTICAL){
			createFenceVerti(xPosition, yPosition, type);
		}
		
		isNeedUpdateAI = true;
	}
	
	public void createTestFence(float x, float y){
		int xPosition = xPosition(x);
		int yPosition = yPosition(y);
		
		worldMap[yPosition][xPosition] = MAP_TESTFENCE;
		enableRectangle(xPosition, yPosition);
	}
	
	
	public Vector2 playerMapMovement(Rectangle mover, Vector2 movement){
		float xMovement = movement.x;
		float yMovement = movement.y;
		if (isHitFence(mover, 'x', movement.x, 0)){
			xMovement = 0;
		}
		if(isHitFence(mover, 'y', movement.y, 0)){
			yMovement = 0;
		};
		
		return new Vector2(xMovement, yMovement);
	}
	
	public Vector3 enemyDumbMapMovement(Rectangle mover, Vector3 movement,int damage){
		float xMovement = movement.x;
		float yMovement = movement.y;
		float isAttacked = 0;
		if (isHitFence(mover, 'x', movement.x, damage)){
			xMovement = 0;
			if (damage > 0){
				isAttacked = 1f;
			}
		}
		if (isHitFence(mover, 'y', movement.y, damage)){
			yMovement = 0;
			if (damage > 0){
				isAttacked = 1f;
			}
		}
		
		return new Vector3(xMovement, yMovement, isAttacked);
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
				
//				if (worldMap[i][j] != MAP_FREESPACE){
//					batch.draw(testFence, xGamePosition(j), yGamePosition(i));
//				}
			}
		}
	}
	
	public char aiDirection(float xPosition, float yPosition){
		int x = xPosition(xPosition);
		int y = yPosition(yPosition);
		
		if(x >= 0 && x < MAP_XNUM && y >= 0 && y < MAP_YNUM){
			return aiMap[y][x];
		} else {
			return MAP_AI_UNKNOWN;
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
	
	public int[][] getFenceHealth(){
		return fenceHealth;
	}
	
	public char[][] getAIMap(){
		return aiMap;
	}
	
	private void calculateMapSize(){
		MAP_XNUM = (int) (MainGameWorld.MAP_X / MAP_BLOCKSIZE);
		MAP_YNUM = (int) (MainGameWorld.MAP_Y / MAP_BLOCKSIZE);
	}
	
	private void setUpMap(){
		worldMap = new char[MAP_YNUM][MAP_XNUM];
		aiMap = new char[MAP_YNUM][MAP_XNUM];
		fenceOrigin = new Vector2[MAP_YNUM][MAP_XNUM];
		fenceHealth = new int[MAP_YNUM][MAP_XNUM];
		aiQueue = new Queue<AIQueueNode>();
		
		for(int i = 0 ; i < MAP_YNUM ; i++){
			for(int j = 0 ; j < MAP_XNUM ; j++){
				worldMap[i][j] = MAP_FREESPACE;
				aiMap[i][j] = MAP_AI_UNKNOWN;
				fenceHealth[i][j] = 0;
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
	
	private boolean isHitFence(Rectangle mover, char axis, float movement,int damage){
		int xPosition = xPosition(mover.getX() + (mover.getWidth() / 2));
		int yPosition = yPosition(mover.getY() + (mover.getHeight() / 2));
		boolean ans = false;
		boolean isAttacked = false;
		
		//System.out.println(xPosition + " / " + yPosition);
		
		if(axis == 'x'){
			mover.x += movement;
			if(movement > 0 && ((xPosition + 1) >= 0) && ((xPosition + 1) < MAP_XNUM) 
					&& (yPosition >= 0) && (yPosition < MAP_YNUM)){
				ans = mover.overlaps(worldMapRectangle[yPosition][xPosition + 1]);
				
				if(ans == true && isAttacked == false && damage > 0){
					attackFence(xPosition + 1, yPosition, damage);
					isAttacked = true;
				}
				
				if ((yPosition + 1) < MAP_YNUM){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition + 1][xPosition + 1]);
					if(ans == true && isAttacked == false && damage > 0){
						attackFence(xPosition + 1, yPosition + 1, damage);
						isAttacked = true;
					}
				}
				
				if (yPosition > 0){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition - 1][xPosition + 1]);
					if(ans == true && isAttacked == false && damage > 0){
						attackFence(xPosition + 1, yPosition - 1, damage);
						isAttacked = true;
					}
				}
				
			} else if (movement < 0 && xPosition > 0 && ((xPosition - 1) < MAP_XNUM) 
					&& (yPosition >= 0) && (yPosition < MAP_YNUM)){
				ans = mover.overlaps(worldMapRectangle[yPosition][xPosition - 1]);
				
				if(ans == true && isAttacked == false && damage > 0){
					attackFence(xPosition - 1, yPosition , damage);
					isAttacked = true;
				}
				
				if ((yPosition + 1) < MAP_YNUM){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition + 1][xPosition - 1]);
					if(ans == true && isAttacked == false && damage > 0){
						attackFence(xPosition - 1, yPosition + 1, damage);
						isAttacked = true;
					}
				}
				
				if (yPosition > 0){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition - 1][xPosition - 1]);
					if(ans == true && isAttacked == false && damage > 0){
						attackFence(xPosition - 1, yPosition - 1, damage);
						isAttacked = true;
					}
				}
			}
			mover.x -= movement;
		} else if(axis == 'y'){
			mover.y += movement;
			if(movement > 0 && (yPosition > 0) && ((yPosition - 1) < MAP_YNUM)
					&& (xPosition >= 0) && (xPosition < MAP_XNUM)){
				ans = mover.overlaps(worldMapRectangle[yPosition - 1][xPosition]);
				
				if(ans == true && isAttacked == false && damage > 0){
					attackFence(xPosition, yPosition - 1, damage);
					isAttacked = true;
				}
				
				if ((xPosition + 1) < MAP_XNUM){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition - 1][xPosition + 1]);
					if(ans == true && isAttacked == false && damage > 0){
						attackFence(xPosition + 1, yPosition - 1, damage);
						isAttacked = true;
					}
				}
				
				if (xPosition > 0){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition - 1][xPosition - 1]);
					if(ans == true && isAttacked == false && damage > 0){
						attackFence(xPosition - 1, yPosition - 1, damage);
						isAttacked = true;
					}
				}
				
			} else if (movement < 0 && (yPosition + 1) < MAP_YNUM && ((yPosition + 1) >= 0)
					&& (xPosition >= 0) && (xPosition < MAP_XNUM)){
				ans = mover.overlaps(worldMapRectangle[yPosition + 1][xPosition]);
				
				if(ans == true && isAttacked == false && damage > 0){
					attackFence(xPosition, yPosition + 1, damage);
					isAttacked = true;
				}
				
				if ((xPosition + 1) < MAP_XNUM){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition + 1][xPosition + 1]);
					if(ans == true && isAttacked == false && damage > 0){
						attackFence(xPosition + 1, yPosition + 1, damage);
						isAttacked = true;
					}
				}
				if (xPosition > 0){
					ans = ans || mover.overlaps(worldMapRectangle[yPosition + 1][xPosition - 1]);
					if(ans == true && isAttacked == false && damage > 0){
						attackFence(xPosition - 1, yPosition + 1, damage);
						isAttacked = true;
					}
				}
			}
			mover.y -= movement;
		}
		
		return ans;
	}
	
	private void createFenceVerti(int x, int y, int type){
		if (type == Crafting.CRAFTING_WFENCE_TYPE){
			woodDeploy.play(woodDeployVolume);
			worldMap[y][x] = MAP_WFENCE_ORIGIN_VERTICAL;
			fenceHealth[y][x] = Crafting.CRAFTING_WFENCE_HEALTH;
			enableRectangle(x, y);
			
		} else if (type == Crafting.CRAFTING_MFENCE_TYPE){
			metalDeploy.play(metalDeployVolume);
			worldMap[y][x] = MAP_MFENCE_ORIGIN_VERTICAL;
			fenceHealth[y][x] = Crafting.CRAFTING_MFENCE_HEALTH;
			enableRectangle(x, y);
		} 
		
		if (y + 1 < MAP_YNUM){
			worldMap[y + 1][x] = MAP_FENCE_BRANCE;
			fenceOrigin[y + 1][x] = new Vector2(x, y);
			enableRectangle(x, y + 1);
		}
		if (y + 2 < MAP_YNUM){
			worldMap[y + 2][x] = MAP_FENCE_BRANCE;
			fenceOrigin[y + 2][x] = new Vector2(x, y);
			enableRectangle(x, y + 2);
		}
		if (y - 1 >= 0){
			worldMap[y - 1][x] = MAP_FENCE_BRANCE;
			fenceOrigin[y - 1][x] = new Vector2(x, y);
			enableRectangle(x, y - 1);
		}
		if (y - 2 >= 0){
			worldMap[y - 2][x] = MAP_FENCE_BRANCE;
			fenceOrigin[y - 2][x] = new Vector2(x, y);
			enableRectangle(x, y - 2);
		}
	}
	
	private void createFenceHori(int x, int y, int type){
		if (type == Crafting.CRAFTING_WFENCE_TYPE){
			woodDeploy.play(woodDeployVolume);
			worldMap[y][x] = MAP_WFENCE_ORIGIN_HORIZONTAL;
			fenceHealth[y][x] = Crafting.CRAFTING_WFENCE_HEALTH;
			enableRectangle(x, y);
		} else if (type == Crafting.CRAFTING_MFENCE_TYPE){
			metalDeploy.play(metalDeployVolume);
			worldMap[y][x] = MAP_MFENCE_ORIGIN_HORIZONTAL;
			fenceHealth[y][x] = Crafting.CRAFTING_MFENCE_HEALTH;
			enableRectangle(x, y);
		} 
		
		if(x + 1 < MAP_XNUM){
			worldMap[y][x + 1] = MAP_FENCE_BRANCE;
			fenceOrigin[y][x + 1] = new Vector2(x, y);
			enableRectangle(x + 1, y);
		}
		if(x + 2 < MAP_XNUM){
			worldMap[y][x + 2] = MAP_FENCE_BRANCE;
			fenceOrigin[y][x + 2] = new Vector2(x, y);
			enableRectangle(x + 2, y);
		}
		if(x - 1 >= 0){
			worldMap[y][x - 1] = MAP_FENCE_BRANCE;
			fenceOrigin[y][x - 1] = new Vector2(x, y);
			enableRectangle(x - 1, y);
		}
		if(x - 2 >= 0){
			worldMap[y][x - 2] = MAP_FENCE_BRANCE;
			fenceOrigin[y][x - 2] = new Vector2(x, y);
			enableRectangle(x - 2, y);
		}
	}
	
	private void enableRectangle(int xPosition, int yPosition){
		worldMapRectangle[yPosition][xPosition].x = xGamePosition(xPosition);
		worldMapRectangle[yPosition][xPosition].y = yGamePosition(yPosition);
		worldMapRectangle[yPosition][xPosition].height = MAP_BLOCKSIZE;
		worldMapRectangle[yPosition][xPosition].width = MAP_BLOCKSIZE;
	}
	
	private void disableRectangle(int xPosition, int yPosition){
		worldMapRectangle[yPosition][xPosition].x = -1;
		worldMapRectangle[yPosition][xPosition].y = -1;
		worldMapRectangle[yPosition][xPosition].height = 1;
		worldMapRectangle[yPosition][xPosition].width = 1;
	}
	
	private void attackFence(int x,int y,int damage){
		int targetX = 0;
		int targetY = 0;
		
		if(worldMap[y][x] == MAP_WFENCE_ORIGIN_VERTICAL
				|| worldMap[y][x] == MAP_MFENCE_ORIGIN_VERTICAL 
				|| worldMap[y][x] == MAP_WFENCE_ORIGIN_HORIZONTAL
				|| worldMap[y][x] == MAP_MFENCE_ORIGIN_HORIZONTAL ){
			targetX = x;
			targetY = y;
		} else if (worldMap[y][x] == MAP_FENCE_BRANCE){
			targetX = (int) fenceOrigin[y][x].x;
			targetY = (int) fenceOrigin[y][x].y;
		}
		
		fenceHealth[targetY][targetX] -= damage;
		if(fenceHealth[targetY][targetX] <= 0){
			if(worldMap[y][x] == MAP_WFENCE_ORIGIN_VERTICAL 
					|| worldMap[y][x] == MAP_WFENCE_ORIGIN_HORIZONTAL){
				woodDestroy.play(woodDestroyVolume);
			} else if(worldMap[y][x] == MAP_MFENCE_ORIGIN_VERTICAL 
					|| worldMap[y][x] == MAP_MFENCE_ORIGIN_HORIZONTAL){
				metalDestroy.play(metalDestroyVolume);
			}
			fenceHealth[targetY][targetX] = 0;
			destroyFence(targetX, targetY);
		}
	}
	
	private void destroyFence(int x, int y){
		if (worldMap[y][x] == MAP_WFENCE_ORIGIN_VERTICAL
				|| worldMap[y][x] == MAP_MFENCE_ORIGIN_VERTICAL){
			destroyFenceVertical(x, y);
		} else if (worldMap[y][x] == MAP_WFENCE_ORIGIN_HORIZONTAL
				|| worldMap[y][x] == MAP_MFENCE_ORIGIN_HORIZONTAL){
			destroyFenceHorizontal(x, y);
		}
	}
	
	private void destroyFenceVertical(int x, int y){
		worldMap[y][x] = MAP_FREESPACE;
		disableRectangle(x, y);
		
		if(y - 1 >= 0){
			worldMap[y - 1][x] = MAP_FREESPACE;
			disableRectangle(x, y - 1);
		}
		if(y - 2 >= 0){
			worldMap[y - 2][x] = MAP_FREESPACE;
			disableRectangle(x, y - 2);
		}
		if(y + 1 < MAP_YNUM){
			worldMap[y + 1][x] = MAP_FREESPACE;
			disableRectangle(x, y + 1);
		}
		if(y + 2 < MAP_YNUM){
			worldMap[y + 2][x] = MAP_FREESPACE;
			disableRectangle(x, y + 2);
		}
	}
	
	private void destroyFenceHorizontal(int x, int y){
		worldMap[y][x] = MAP_FREESPACE;
		disableRectangle(x, y);
		
		if(x - 1 >= 0){
			worldMap[y][x - 1] = MAP_FREESPACE;
			disableRectangle(x - 1, y);
		}
		if(x - 2 >= 0){
			worldMap[y][x - 2] = MAP_FREESPACE;
			disableRectangle(x - 2, y);
		}
		if(x + 1 < MAP_XNUM){
			worldMap[y][x + 1] = MAP_FREESPACE;
			disableRectangle(x + 1, y);
		}
		if(x + 2 < MAP_XNUM){
			worldMap[y][x + 2] = MAP_FREESPACE;
			disableRectangle(x + 2, y);
		}
	}
	private void updatePlayerPosition(){
		int newX = xPosition( mainGameWorld.getPlayer().getSprite().getX()  
				+ (mainGameWorld.getPlayer().getSprite().getWidth() / 2));
		int newY = yPosition( mainGameWorld.getPlayer().getSprite().getY() 
				+ (mainGameWorld.getPlayer().getSprite().getHeight() / 2));
		
		if(newX != lastPlayerX || newY != lastPlayerY){
			lastPlayerX = newX;
			lastPlayerY = newY;
			isNeedUpdateAI = true;
		}
		//System.out.println(newX + " / " + newY);
	}
	
	private void updateAI(){
		aiQueue.clear();
		clearAIMap();
		aiQueue.addLast(new AIQueueNode(lastPlayerX, lastPlayerY, MAP_AI_PLAYER));
		queueSize = 1;
		while(updateAINode()) {
			
		}
	}
	
	private boolean updateAINode(){
		//aiCount++;
		
		AIQueueNode node = null;
		if(queueSize > 0){
			node = aiQueue.removeFirst();
			queueSize--;
		}
		int x = -1;
		int y = -1;
		char value = ' ';
		
		if(node != null){
			x = node.xPosition;
			y = node.yPosition;
			value = node.directionValue;
		}
		//System.out.println("" + queueSizePeak);
		
		if (x >= 0 && x < MAP_XNUM  && y >= 0 && y < MAP_YNUM){
			
			if(aiMap[y][x] == MAP_AI_UNKNOWN || aiMap[y][x] == MAP_AI_QUEUE){
				aiMap[y][x] = value;
				if (x - 1 >= 0){
					if(aiMap[y][x - 1] == MAP_AI_UNKNOWN && worldMap[y][x - 1] == MAP_FREESPACE){
						aiQueue.addLast(new AIQueueNode(x - 1, y, MAP_AI_RIGHT));
						aiMap[y][x - 1] = MAP_AI_QUEUE;
						queueSize++;
					}
				}
				if (x + 1 < MAP_XNUM){
					if(aiMap[y][x + 1] == MAP_AI_UNKNOWN && worldMap[y][x + 1] == MAP_FREESPACE){
						aiQueue.addLast(new AIQueueNode(x + 1, y, MAP_AI_LEFT));
						aiMap[y][x + 1] = MAP_AI_QUEUE;
						queueSize++;
					}
				}
				
				if (y - 1 >= 0){
					if(aiMap[y - 1][x] == MAP_AI_UNKNOWN && worldMap[y - 1][x] == MAP_FREESPACE){
						aiQueue.addLast(new AIQueueNode(x, y - 1, MAP_AI_DOWN));
						aiMap[y - 1][x] = MAP_AI_QUEUE;
						queueSize++;
					}
				}
				if (y + 1 < MAP_YNUM){
					if(aiMap[y + 1][x] == MAP_AI_UNKNOWN && worldMap[y + 1][x] == MAP_FREESPACE){
						aiQueue.addLast(new AIQueueNode(x, y + 1, MAP_AI_UP));
						aiMap[y + 1][x] = MAP_AI_QUEUE;
						queueSize++;
					} 
				}
			}
			if(queueSize > queueSizePeak){
				queueSizePeak = queueSize;
			}
			return true;
		}
		
		return false;
	}
	
	private void clearAIMap(){
		for(int i = 0; i < MAP_YNUM ; i ++){
			for(int j = 0 ; j < MAP_XNUM ; j++){
				aiMap[i][j] = MAP_AI_UNKNOWN;
			}
		}
	}
}
