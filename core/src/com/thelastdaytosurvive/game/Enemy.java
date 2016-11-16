package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {
	public static final int ENEMY_DUMB = 0;
	public static final int ENEMY_SMART = 1;
	
	private Player player;
	private Map map;
	private MainGameTracker tracker;
	
	private EnemyDumb enemyDumb;
	private EnemySmart enemySmart;
	private boolean isTestMode = false;
	
	public void setUp(Player player, Map map, MainGameTracker tracker){
		this.player = player;
		this.map = map;
		this.tracker = tracker;
		setUpEnemy();
		if (isTestMode){
			testCase();
		}
	}
	
	public void newEnemy(int type, float xPosition,float yPosition){
		switch (type){
			case ENEMY_DUMB : enemyDumb.newEnemyDumb(xPosition, yPosition); break;
			case ENEMY_SMART : enemySmart.newEnemySmart(xPosition, yPosition); break;
			default : System.out.println("Error at Enemy in newEnemy method.");
		}
	}
	public void update(float delta){
		enemyDumb.update(delta);
		enemySmart.update(delta);
		
		if(Gdx.input.isKeyPressed(Input.Keys.L) && isTestMode){
			newEnemy(ENEMY_DUMB, MathUtils.random(0, MainGameWorld.MAP_X)
					, MathUtils.random(0, MainGameWorld.MAP_Y));
		}
	}
	
	public void draw(SpriteBatch batch){
		enemyDumb.draw(batch);
		enemySmart.draw(batch);
	}
	
	public void isBulletHit(BulletInfo bulletInfo, Rectangle rectangle){
		enemyDumb.isBulletHit(bulletInfo, rectangle);
		enemySmart.isBulletHit(bulletInfo, rectangle);
	}
	
	private void setUpEnemy(){
		enemyDumb = new EnemyDumb(player, map, tracker);
		enemySmart = new EnemySmart(player, map, tracker);
	}
	
	private void testCase(){
		newEnemy(ENEMY_DUMB, 4000, 2000);
		newEnemy(ENEMY_DUMB, 2200, 800);
		newEnemy(ENEMY_SMART, 4000, 2000);
	}
}
