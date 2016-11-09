package com.thelastdaytosurvive.game;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Bullet {
	public Array<BulletInfo> bulletInfoArray;
	public Array<Rectangle> bulletRectangleArray;
	
	private Texture bulletTexture[];
	
	private float speedType[] = {1000, 1000};
	private float picSizeType[] = {12, 12};
	private float hitBoxSizeType[] = {12, 12};
	private int hitCount[] = {1, 1};
	
	public Bullet(){
		bulletInfoArray = new Array<BulletInfo>();
		bulletRectangleArray = new Array<Rectangle>();
		
		bulletTexture = new Texture[2];
		bulletTexture[0] = new Texture("Bullet/laserBlue03.png");
		bulletTexture[1] = new Texture("Bullet/laserRed03.png");
		
	}
	
	public void newBullet(int type, float xPosition, float yPosition, float rotation){
		setUpInfo(type, xPosition, yPosition, rotation);
		setUpRectangle(type, xPosition, yPosition);
	}
	
	public void update(float delta){
		updatePosition(delta);
		checkCondition();
	}
	
	public void draw(SpriteBatch batch){
		drawBullet(batch);
	}
	
	private void setUpInfo(int type, float xPosition, float yPosition, float rotation){
		BulletInfo newInfo = new BulletInfo();
		newInfo.type = type;
		newInfo.xPostion = xPosition;
		newInfo.yPostion = yPosition;
		newInfo.rotation = rotation;
		newInfo.xSpeed = (float) (speedType[type] * Math.cos(rotation / 180 * Math.PI));
		newInfo.ySpeed = (float) (speedType[type] * Math.sin(rotation / 180 * Math.PI));
		newInfo.hitCount = hitCount[type];
		
		bulletInfoArray.add(newInfo);
	}
	
	private void setUpRectangle(int type, float xPosition, float yPosition){
		Rectangle newRectangle = new Rectangle();
		newRectangle.x = xPosition + (picSizeType[type] / 2) - (hitBoxSizeType[type] / 2);
		newRectangle.y = yPosition + (picSizeType[type] / 2) - (hitBoxSizeType[type] / 2);
		newRectangle.width = hitBoxSizeType[type];
		newRectangle.height = hitBoxSizeType[type];
		
		bulletRectangleArray.add(newRectangle);
	}
	
	private void updatePosition(float delta){
		//System.out.println("" + bulletInfoArray.size);
		Iterator<BulletInfo> iterInfo = bulletInfoArray.iterator();
		Iterator<Rectangle> iterRectangle = bulletRectangleArray.iterator();
		while (iterInfo.hasNext() && iterRectangle.hasNext()){
			
			BulletInfo info = iterInfo.next();
			float deltaX = info.xSpeed * delta;
			float deltaY = info.ySpeed * delta;
			
			info.xPostion += deltaX;
			info.yPostion += deltaY;
			
			Rectangle rectangle = iterRectangle.next();
			rectangle.x += deltaX;
			rectangle.y += deltaY;
		}
	}
	
	private void checkCondition(){
		Iterator<BulletInfo> iterInfo = bulletInfoArray.iterator();
		Iterator<Rectangle> iterRectangle = bulletRectangleArray.iterator();
		while (iterInfo.hasNext() && iterRectangle.hasNext()){
			BulletInfo info = iterInfo.next();
			Rectangle rectangle = iterRectangle.next();
			
			boolean isRemove = isOutfBound(info);
			
			if (isRemove){
				iterInfo.remove();
				iterRectangle.remove();
			}
		}
	}
	
	private boolean isOutfBound(BulletInfo info){
		return (info.xPostion < 0 || info.xPostion > MainGameWorld.MAP_X 
				|| info.yPostion < 0 || info.yPostion > MainGameWorld.MAP_Y);
	}
	
	private void drawBullet(SpriteBatch batch){
		Iterator<BulletInfo> iterInfo = bulletInfoArray.iterator();
		while (iterInfo.hasNext()){
			BulletInfo info = iterInfo.next();
			batch.draw(bulletTexture[info.type], info.xPostion, info.yPostion
					, bulletTexture[info.type].getWidth()/2, bulletTexture[info.type].getHeight()/2
					, bulletTexture[info.type].getWidth(), bulletTexture[info.type].getHeight()
					, 1, 1, info.rotation, 0, 0, bulletTexture[info.type].getWidth()
					, bulletTexture[info.type].getHeight(), false, false);
		}
	}
	
	private int typeDamage(int type){
		switch (type){
			case Weapon.ASSAULT_RIFLE : return AssaultRifle.DAMAGE;
			case Weapon.NINEMM_PISTOL : return NineMmPistol.DAMAGE;
			default : return 0;
		}
	}
}