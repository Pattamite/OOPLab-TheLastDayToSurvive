package com.thelastdaytosurvive.game;

import java.util.Iterator;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class EnemyDumb {
	private Player player;
	
	public Array<EnemyDumbInfo> enemyDumbInfoArray;
	public Array<Rectangle> enemyDumbRectangleArray;
	
	private int maxHealth = 100;
	private float speed = 5f;
	private float picSize = 64;
	private float hitBoxSize = 30;
	
	public EnemyDumb(Player player){
		this.player = player;
		
		enemyDumbInfoArray = new Array<EnemyDumbInfo>();
		enemyDumbRectangleArray = new Array<Rectangle>();
	}
	
	public void newEnemyDumb(float xPosition, float yPosition){
		setupInfo(xPosition, yPosition);
		setupRectangle(xPosition, yPosition);
	}
	
	public void update(float delta){
		//updateMovement();
		checkDead();
	}
	
	private void setupInfo(float xPosition, float yPosition){
		EnemyDumbInfo newInfo = new EnemyDumbInfo();
		newInfo.health = maxHealth;
		newInfo.xPosition = xPosition;
		newInfo.yPosition = yPosition;
		newInfo.rotation = 0;
		
		enemyDumbInfoArray.add(newInfo);
	}
	
	private void setupRectangle(float xPosition, float yPosition){
		Rectangle newRectangle = new Rectangle();
		newRectangle.x = xPosition + (picSize / 2) - (hitBoxSize / 2);
		newRectangle.y = yPosition + (picSize / 2) - (hitBoxSize / 2);
		newRectangle.width = hitBoxSize;
		newRectangle.height = hitBoxSize;
		
		enemyDumbRectangleArray.add(newRectangle);
	}
	
	private void checkDead(){
		Iterator<EnemyDumbInfo> iterInfo = enemyDumbInfoArray.iterator();
		Iterator<Rectangle> iterRectangle = enemyDumbRectangleArray.iterator();
		
		while (iterInfo.hasNext() && iterRectangle.hasNext()){
			EnemyDumbInfo info = iterInfo.next();
			Rectangle rectangle = iterRectangle.next();
			
			if (info.health <= 0){
				iterInfo.remove();
				iterRectangle.remove();
			}
		}
	}
}
