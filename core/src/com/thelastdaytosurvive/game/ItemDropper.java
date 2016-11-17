package com.thelastdaytosurvive.game;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class ItemDropper {
	public static final int FULLMAG_TYPE = 0;
	public static final int HEALTHPACK_TYPE = 1;
	public static final int ONEMAG_TYPE = 2;
	public static final int METAL_TYPE = 3;
	public static final int WOOD_TYPE = 4;
	
	
	private int dropRate[][];
	private int healthPackValue = 50;
	private int metalValue = 1;
	private int woodValue = 1;
	private float size[] = new float[]{40f, 40f, 25f, 40f, 40f};
	private MainGameWorld mainGameWorld;
	private Texture healthPackTexture;
	private Texture oneMagTexture;
	private Texture fullMagTexture;
	private Texture woodTexture;
	private Texture metalTexture;
	
	private Array<Rectangle> itemRectangle;
	private Array<ItemDropperInfo> itemInfo;
	
	public ItemDropper(MainGameWorld mainGameWorld){
		this.mainGameWorld = mainGameWorld;
		
		setUpTexture();
		setUpArray();
		setUpDropRate();
	}
	
	public void update(){
		Iterator<ItemDropperInfo> iterInfo = itemInfo.iterator();
		Iterator<Rectangle> iterRectangle = itemRectangle.iterator();
		while(iterInfo.hasNext() && iterRectangle.hasNext()){
			ItemDropperInfo info = iterInfo.next();
			Rectangle rectangle = iterRectangle.next();
			
			if(rectangle.overlaps(mainGameWorld.getPlayer().getRectangle())){
				if(itemAction(info.type)){
					iterInfo.remove();
					iterRectangle.remove();
				}
			}
		}
	}
	
	public void draw(SpriteBatch batch){
		Iterator<ItemDropperInfo> iterInfo = itemInfo.iterator();
		while(iterInfo.hasNext()){
			ItemDropperInfo info = iterInfo.next();
			switch (info.type){
			case FULLMAG_TYPE : batch.draw(fullMagTexture, info.xPosition, info.yPosition); break;
			case HEALTHPACK_TYPE : batch.draw(healthPackTexture, info.xPosition, info.yPosition); break;
			case ONEMAG_TYPE : batch.draw(oneMagTexture, info.xPosition, info.yPosition); break;
			case METAL_TYPE : batch.draw(metalTexture, info.xPosition, info.yPosition); break;
			case WOOD_TYPE : batch.draw(woodTexture, info.xPosition, info.yPosition); break;
			default : break;
			}
		}
	}
	
	public void dropItem(int type, float x, float y){
		int chosenType = 4;
		int randomValue = MathUtils.random(0, 99);
		
		for(int i = 0 ; i <= 4 ; i++){
			if(randomValue < dropRate[type][i]){
				chosenType = i;
				break;
			}
		}
		
		createItem(chosenType, x, y);
	}
	
	private void setUpTexture(){
		healthPackTexture = new Texture("ItemDrop/HealthPack.png");
		oneMagTexture = new Texture("ItemDrop/OneMag.png");
		fullMagTexture = new Texture("ItemDrop/FullMag.png");
		woodTexture = new Texture("ItemDrop/Wood.png");
		metalTexture = new Texture("ItemDrop/Metal.png");
	}
	
	private void setUpArray(){
		itemRectangle = new Array<Rectangle>();
		itemInfo = new Array<ItemDropperInfo>();
	}
	
	private void setUpDropRate(){
		dropRate = new int[2][5];
		
		dropRate[Enemy.ENEMY_DUMB][FULLMAG_TYPE] = 1; 		// 1
		dropRate[Enemy.ENEMY_DUMB][HEALTHPACK_TYPE] = 8; 	// 7
		dropRate[Enemy.ENEMY_DUMB][ONEMAG_TYPE] = 23; 		// 15
		dropRate[Enemy.ENEMY_DUMB][METAL_TYPE] = 53; 		// 30
		dropRate[Enemy.ENEMY_DUMB][WOOD_TYPE] = 100; 		// 47
		
		dropRate[Enemy.ENEMY_SMART][FULLMAG_TYPE] = 1; 		// 1
		dropRate[Enemy.ENEMY_SMART][HEALTHPACK_TYPE] = 20;	// 19
		dropRate[Enemy.ENEMY_SMART][ONEMAG_TYPE] = 35;		// 20
		dropRate[Enemy.ENEMY_SMART][METAL_TYPE] = 75;		// 40
		dropRate[Enemy.ENEMY_SMART][WOOD_TYPE] = 100;		// 25
	}
	
	private void createItem(int type, float x, float y){
		Rectangle rectangle = new Rectangle();
		rectangle.x = x;
		rectangle.y = y;
		rectangle.width = size[type];
		rectangle.height = size[type];
		itemRectangle.add(rectangle);
		
		ItemDropperInfo info = new ItemDropperInfo();
		info.type = type;
		info.xPosition = x;
		info.yPosition = y;
		itemInfo.add(info);
	}
	
	private boolean itemAction(int type){
		switch(type){
			case FULLMAG_TYPE : return mainGameWorld.getWeapon().grabFullMag();
			case HEALTHPACK_TYPE : return mainGameWorld.getPlayer().heal(healthPackValue);
			case ONEMAG_TYPE : return mainGameWorld.getWeapon().grabOneMag();
			case METAL_TYPE : return mainGameWorld.getCrafting().addMetal(metalValue);
			case WOOD_TYPE : return mainGameWorld.getCrafting().addWood(woodValue);
			default : return false;
		}
	}
}
