package com.thelastdaytosurvive.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Crafting {
	Map map;
	
	public static final int CRAFTING_WFENCE_TYPE = 0;
	public static final int CRAFTING_MFENCE_TYPE = 1;
	
	public static final int CREAFTING_HORIZONTAL = 0;
	public static final int CREAFTING_VERTICAL = 1;
	
	public static final int CRAFTING_WFENCE_WOOD = 10;
	public static final int CRAFTING_WFENCE_METAL = 0;
	public static final int CRAFTING_MFENCE_WOOD = 5;
	public static final int CRAFTING_MFENCE_METAL = 10;
	
	private Texture wFenceHoriPositive;
	private Texture wFenceHoriNegative;
	private Texture wFenceVertiPositive;
	private Texture wFenceVertiNegative;
	private Texture mFenceHoriPositive;
	private Texture mFenceHoriNegative;
	private Texture mFenceVertiPositive;
	private Texture mFenceVertiNegative;
	
	
	private int currentWood = 40;
	private int currentMetal = 100;
	private boolean isPreview = false;
	private boolean isPlaceable = false;
	private int previewType;
	private int previewRotation;
	private float previewPositionX;
	private float previewPositionY;
	
	public Crafting(Map map){
		this.map = map;
		setUpTexture();
		
	}
	
	public boolean preview(Vector2 position, int type,int rotation){
		isPreview = true;
		
		if (type == CRAFTING_WFENCE_TYPE){
			previewType = CRAFTING_WFENCE_TYPE;
			
			if(rotation == CREAFTING_HORIZONTAL){
				isPlaceable = previewFenceHori(position);
			} else {
				isPlaceable = previewFenceVerti(position);
			}
		} else if (type == CRAFTING_MFENCE_TYPE){
			previewType = CRAFTING_MFENCE_TYPE;
			
			if(rotation == CREAFTING_HORIZONTAL){
				isPlaceable = previewFenceHori(position);
			} else {
				isPlaceable = previewFenceVerti(position);
			}
		}
		
		return isPlaceable;
	}
	
	public void canclePreview(){
		isPreview = false;
	}
	
	public void draw(SpriteBatch batch){
		if(isPreview){
			if(previewType == CRAFTING_WFENCE_TYPE ){
				if (previewRotation == CREAFTING_HORIZONTAL && isPlaceable == true){
					batch.draw(wFenceHoriPositive, previewPositionX, previewPositionY);
				} else if (previewRotation == CREAFTING_HORIZONTAL && isPlaceable == false){
					batch.draw(wFenceHoriNegative, previewPositionX, previewPositionY);
				} else if (previewRotation == CREAFTING_VERTICAL && isPlaceable == true){
					batch.draw(wFenceVertiPositive, previewPositionX, previewPositionY);
				} else if (previewRotation == CREAFTING_VERTICAL && isPlaceable == false){
					batch.draw(wFenceVertiNegative, previewPositionX, previewPositionY);
				}
			} else if(previewType == CRAFTING_MFENCE_TYPE ){
				if (previewRotation == CREAFTING_HORIZONTAL && isPlaceable == true){
					batch.draw(mFenceHoriPositive, previewPositionX, previewPositionY);
				} else if (previewRotation == CREAFTING_HORIZONTAL && isPlaceable == false){
					batch.draw(mFenceHoriNegative, previewPositionX, previewPositionY);
				} else if (previewRotation == CREAFTING_VERTICAL && isPlaceable == true){
					batch.draw(mFenceVertiPositive, previewPositionX, previewPositionY);
				} else if (previewRotation == CREAFTING_VERTICAL && isPlaceable == false){
					batch.draw(mFenceVertiNegative, previewPositionX, previewPositionY);
				}
			}
		}
	}
	
	public boolean createFence(int type, float x, float y, int rotation){
		if (type == CRAFTING_WFENCE_TYPE && currentWood >= CRAFTING_WFENCE_WOOD 
				&& currentMetal >= CRAFTING_WFENCE_METAL){
			map.createFence(x, y, type, rotation);
			currentWood -= CRAFTING_WFENCE_WOOD;
			currentMetal -= CRAFTING_WFENCE_METAL;
			
			return true;
		} else if (type == CRAFTING_MFENCE_TYPE && currentWood >= CRAFTING_MFENCE_WOOD 
				&& currentMetal >= CRAFTING_MFENCE_METAL){
			map.createFence(x, y, type, rotation);
			currentWood -= CRAFTING_MFENCE_WOOD;
			currentMetal -= CRAFTING_MFENCE_METAL;
					
			return true;
		}
		return false;
	}
	
	public int getCurrentWood(){
		return currentWood;
	}
	
	public int getCurrentMetal(){
		return currentMetal;
	}
	
	private void setUpTexture(){
		wFenceHoriPositive = new Texture("Fence/WoodenFenceHorizontalPreviewPositive.png");
		wFenceHoriNegative = new Texture("Fence/WoodenFenceHorizontalPreviewNegative.png");
		wFenceVertiPositive = new Texture("Fence/WoodenFenceVerticalPreviewPositive.png");
		wFenceVertiNegative = new Texture("Fence/WoodenFenceVerticalPreviewNegative.png");
		mFenceHoriPositive = new Texture("Fence/MetalFenceHorizontalPreviewPositive.png");
		mFenceHoriNegative = new Texture("Fence/MetalFenceHorizontalPreviewNegative.png");
		mFenceVertiPositive = new Texture("Fence/MetalFenceVerticalPreviewPositive.png");
		mFenceVertiNegative = new Texture("Fence/MetalFenceVerticalPreviewNegative.png");
	}
	
	private boolean previewFenceHori(Vector2 position){
		boolean ans = true;
		int xPosition = map.xPosition(position.x);
		int yPosition = map.yPosition(position.y);
		
		previewRotation = CREAFTING_HORIZONTAL;
		previewPositionX = map.xGamePosition(xPosition - 2);
		previewPositionY = map.yGamePosition(yPosition);
		
		if(xPosition < 0 || xPosition >= Map.MAP_XNUM || yPosition < 0 || yPosition >= Map.MAP_YNUM ){
			ans = false;
		} else if(map.getWorldMap()[yPosition][xPosition] != Map.MAP_FREESPACE){
			ans = false;
		}
		if((xPosition + 1) < Map.MAP_XNUM){
			ans = ans && (map.getWorldMap()[yPosition][xPosition + 1] ==  Map.MAP_FREESPACE);
		}
		if((xPosition + 2) < Map.MAP_XNUM){
			ans = ans && (map.getWorldMap()[yPosition][xPosition + 2] ==  Map.MAP_FREESPACE);
		}
		if((xPosition - 1) >= 0){
			ans = ans && (map.getWorldMap()[yPosition][xPosition - 1] ==  Map.MAP_FREESPACE);
		}
		if((xPosition - 2) >= 0){
			ans = ans && (map.getWorldMap()[yPosition][xPosition - 2] ==  Map.MAP_FREESPACE);
		}
		
		return ans;
	}
	
	private boolean previewFenceVerti(Vector2 position){
		boolean ans = true;
		int xPosition = map.xPosition(position.x);
		int yPosition = map.yPosition(position.y);
		
		previewRotation = CREAFTING_VERTICAL;
		previewPositionX = map.xGamePosition(xPosition);
		previewPositionY = map.yGamePosition(yPosition + 2);
		
		if(xPosition < 0 || xPosition >= Map.MAP_XNUM || yPosition < 0 || yPosition >= Map.MAP_YNUM ){
			ans = false;
		} else if(map.getWorldMap()[yPosition][xPosition] != Map.MAP_FREESPACE){
			ans = false;
		}
		if((yPosition + 1) < Map.MAP_YNUM){
			ans = ans && (map.getWorldMap()[yPosition + 1][xPosition] ==  Map.MAP_FREESPACE);
		}
		if((yPosition + 2) < Map.MAP_YNUM){
			ans = ans && (map.getWorldMap()[yPosition + 2][xPosition] ==  Map.MAP_FREESPACE);
		}
		if((yPosition - 1) >= 0){
			ans = ans && (map.getWorldMap()[yPosition - 1][xPosition] ==  Map.MAP_FREESPACE);
		}
		if((yPosition - 2) >= 0){
			ans = ans && (map.getWorldMap()[yPosition - 2][xPosition] ==  Map.MAP_FREESPACE);
		}
		
		return ans;
	}
	
}
