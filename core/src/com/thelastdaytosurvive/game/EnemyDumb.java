package com.thelastdaytosurvive.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class EnemyDumb {
	private Player player;
	private Texture texture;
	private BitmapFont font;
	
	public Array<EnemyDumbInfo> enemyDumbInfoArray;
	public Array<Rectangle> enemyDumbRectangleArray;
	
	private int maxHealth = 100;
	private float speed = 5f;
	private float picSize = 64;
	private float hitBoxSize = 40;
	
	private boolean isShowHealth = true;
	
	public EnemyDumb(Player player){
		this.player = player;
		texture = new Texture("Enemy/Dumb.png");
		font = new BitmapFont(Gdx.files.internal("Font/Cloud32.fnt"));
		
		enemyDumbInfoArray = new Array<EnemyDumbInfo>();
		enemyDumbRectangleArray = new Array<Rectangle>();
	}
	
	public void newEnemyDumb(float xPosition, float yPosition){
		setupInfo(xPosition, yPosition);
		setupRectangle(xPosition, yPosition);
	}
	
	public void update(float delta){
		//updateMovement(delta);
		checkDead();
	}
	
	public void draw(SpriteBatch batch){
		Iterator<EnemyDumbInfo> iterInfo = enemyDumbInfoArray.iterator();
		
		while (iterInfo.hasNext()){
			EnemyDumbInfo info = iterInfo.next();
			batch.draw(texture, info.xPosition, info.yPosition, texture.getWidth() / 2
					, texture.getHeight() / 2, texture.getWidth(), texture.getHeight()
					, 1, 1, info.rotation, 0, 0, texture.getWidth(), texture.getHeight()
					, false, false);
			
			if (isShowHealth){
				font.draw(batch, "" + info.health, info.xPosition, info.yPosition - 10);
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
