package com.thelastdaytosurvive.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class EnemyDumb {
	private Player player;
	private Map map;
	private Texture texture;
	private Texture healthBarTexture;
	private Texture healthTexture;
	private BitmapFont font;
	private MainGameTracker tracker;
	private ItemDropper itemDropper;
	
	private Array<EnemyDumbInfo> enemyDumbInfoArray;
	private Array<Rectangle> enemyDumbRectangleArray;
	private Array<Rectangle> enemyDumbMovementRectangleArray;
	
	private int maxHealth = 100;
	private int damage = 5;
	private long attackDelay = 1000;
	private float speed = 100f;
	private float picSize = 76;
	private float hitBoxSize = 40;
	private float movementSize = 36;
	
	private boolean isShowHealth = true;
	
	public EnemyDumb(Player player, Map map,MainGameTracker tracker, ItemDropper itemDropper){
		this.player = player;
		this.map = map;
		this.tracker = tracker;
		this.itemDropper = itemDropper;
		texture = new Texture("Enemy/DumbReal.png");
		healthBarTexture = new Texture("FenceHealthBar/Bar.png");
		healthTexture = new Texture("FenceHealthBar/EnemyHealth.png");
		font = new BitmapFont(Gdx.files.internal("Font/Cloud32.fnt"));
		
		enemyDumbInfoArray = new Array<EnemyDumbInfo>();
		enemyDumbRectangleArray = new Array<Rectangle>();
		enemyDumbMovementRectangleArray = new Array<Rectangle>();
	}
	
	public void newEnemyDumb(float xPosition, float yPosition){
		setupInfo(xPosition, yPosition);
		setupRectangle(xPosition, yPosition);
		System.out.println("Spawn Dumb at " + xPosition + " / " + yPosition);
	}
	
	public void update(float delta){
		checkAttack();
		updateMovement(delta);
		checkDead();
	}
	
	public void draw(SpriteBatch batch){
		Iterator<EnemyDumbInfo> iterInfo = enemyDumbInfoArray.iterator();
		
		while (iterInfo.hasNext()){
			EnemyDumbInfo info = iterInfo.next();
			batch.draw(texture, info.xPosition, info.yPosition, texture.getWidth() / 2
					, texture.getHeight() / 2, texture.getWidth(), texture.getHeight()
					, 1, 1, info.rotation + 90, 0, 0, texture.getWidth(), texture.getHeight()
					, false, false);
			
			if (isShowHealth){
				batch.draw(healthBarTexture, info.xPosition, info.yPosition - 10);
				batch.draw(healthTexture, info.xPosition + 2, info.yPosition - 8
						, (float) info.health / (float) maxHealth * 50f, 4);
			}
		}
	}
	
	public void isBulletHit(BulletInfo bulletInfo, Rectangle bulletRectangle){
		Iterator<EnemyDumbInfo> iterInfo = enemyDumbInfoArray.iterator();
		Iterator<Rectangle> iterRectangle = enemyDumbRectangleArray.iterator();
		
		while (iterInfo.hasNext() && iterRectangle.hasNext() && (bulletInfo.hitCount > 0)){
			EnemyDumbInfo info = iterInfo.next();
			Rectangle rectangle = iterRectangle.next();
			
			if (rectangle.overlaps(bulletRectangle)){
				info.health -= Bullet.typeDamage(bulletInfo.type);
				bulletInfo.hitCount--;
			}
		}
	}
	
	private void setupInfo(float xPosition, float yPosition){
		EnemyDumbInfo newInfo = new EnemyDumbInfo();
		newInfo.health = maxHealth;
		newInfo.xPosition = xPosition;
		newInfo.yPosition = yPosition;
		newInfo.rotation = 0;
		newInfo.lastAttackTime = 0;
		newInfo.isJustHitPlayer = false;
		
		enemyDumbInfoArray.add(newInfo);
	}
	
	private void setupRectangle(float xPosition, float yPosition){
		Rectangle newRectangle = new Rectangle();
		newRectangle.x = xPosition + (picSize / 2) - (hitBoxSize / 2);
		newRectangle.y = yPosition + (picSize / 2) - (hitBoxSize / 2);
		newRectangle.width = hitBoxSize;
		newRectangle.height = hitBoxSize;
		enemyDumbRectangleArray.add(newRectangle);
		
		Rectangle newMovementRectangle = new Rectangle();
		newMovementRectangle.x = xPosition + (picSize / 2) - (movementSize / 2);
		newMovementRectangle.y = yPosition + (picSize / 2) - (movementSize / 2);
		newMovementRectangle.width = movementSize;
		newMovementRectangle.height = movementSize;
		enemyDumbMovementRectangleArray.add(newMovementRectangle);
	}
	
	private void updateMovement(float deltaTime){
		Iterator<EnemyDumbInfo> iterInfo = enemyDumbInfoArray.iterator();
		Iterator<Rectangle> iterRectangle = enemyDumbRectangleArray.iterator();
		Iterator<Rectangle> iterMovementRectangle = enemyDumbMovementRectangleArray.iterator();
		
		while (iterInfo.hasNext()){
			EnemyDumbInfo info = iterInfo.next();
			Rectangle rectangle = iterRectangle.next();
			Rectangle movementRectangle = iterMovementRectangle.next();
			//Vector3 movementInfo = calculateMovement(deltaTime, info.xPosition, info.yPosition);
			Vector3 movementInfo = calculateMovement(deltaTime, info.xPosition, info.yPosition, info.isJustHitPlayer);
			Vector3 fenceMovementInfo;
			if ((TimeUtils.millis() - info.lastAttackTime) > attackDelay){
				fenceMovementInfo = map.enemyDumbMapMovement(movementRectangle, movementInfo, damage);
			} else {
				fenceMovementInfo = map.enemyDumbMapMovement(movementRectangle, movementInfo, 0);
			}
			
			info.xPosition += fenceMovementInfo.x;
			info.yPosition += fenceMovementInfo.y;
			info.rotation = movementInfo.z;
			rectangle.x += fenceMovementInfo.x;
			rectangle.y += fenceMovementInfo.y;
			movementRectangle.x += fenceMovementInfo.x;
			movementRectangle.y += fenceMovementInfo.y;
			info.isJustHitPlayer = false;
			
			if(fenceMovementInfo.z > 0){
				info.lastAttackTime = TimeUtils.millis();
			}
		}
	}
	
	private Vector3 calculateMovement(float deltaTime, float x, float y, boolean isJustAttacked){
		float deltaDistanceX;
		float deltaDistanceY;
		float deltaDistance;
		float deltaX;
		float deltaY;
		float rotateTarget;
		
		deltaDistanceX = player.getSprite().getX() - x;
		deltaDistanceY = player.getSprite().getY() - y;
		deltaDistance = (float)Math.sqrt( (double) ((deltaDistanceX * deltaDistanceX) 
				+ (deltaDistanceY * deltaDistanceY)));
		if(isJustAttacked == false){
			deltaX = speed * deltaTime * (deltaDistanceX / deltaDistance);
			deltaY = speed * deltaTime * (deltaDistanceY / deltaDistance);
		} else {
			deltaX = 0;
			deltaY = 0;
		}
		
		rotateTarget =  (float) (Math.atan2((double)(-deltaDistanceY) 
				,(double) (-deltaDistanceX)) * 180.0d / Math.PI);
		
		return new Vector3(deltaX, deltaY, rotateTarget);
	}
	
	private void checkDead(){
		Iterator<EnemyDumbInfo> iterInfo = enemyDumbInfoArray.iterator();
		Iterator<Rectangle> iterRectangle = enemyDumbRectangleArray.iterator();
		Iterator<Rectangle> iterMovementRectangle = enemyDumbMovementRectangleArray.iterator();
		
		while (iterInfo.hasNext() && iterRectangle.hasNext() && iterMovementRectangle.hasNext()){
			EnemyDumbInfo info = iterInfo.next();
			Rectangle rectangle = iterRectangle.next();
			Rectangle movementRectangle = iterMovementRectangle.next();
			
			if (info.health <= 0){
				itemDropper.dropItem(Enemy.ENEMY_DUMB, info.xPosition, info.yPosition);
				tracker.enemyDown(Enemy.ENEMY_DUMB);
				iterInfo.remove();
				iterRectangle.remove();
				iterMovementRectangle.remove();
				
			}
		}
	}
	
	private void checkAttack(){
		Iterator<EnemyDumbInfo> iterInfo = enemyDumbInfoArray.iterator();
		Iterator<Rectangle> iterRectangle = enemyDumbRectangleArray.iterator();
		
		while (iterInfo.hasNext() && iterRectangle.hasNext()){
			EnemyDumbInfo info = iterInfo.next();
			Rectangle rectangle = iterRectangle.next();
			
			if(rectangle.overlaps(player.getRectangle())){
				if (TimeUtils.millis() - info.lastAttackTime > attackDelay){
					player.getHit(damage);
					info.lastAttackTime = TimeUtils.millis();
				}
				
				info.isJustHitPlayer = true;
			}
		}
	}
}
