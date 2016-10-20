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
	
	private float speedType[] = {10, 5};
	private float sizeType[] = {12, 12};
	
	public Bullet(){
		bulletTexture = new Texture[2];
		bulletTexture[0] = new Texture("Bullet/laserBlue03.png");
		bulletTexture[1] = new Texture("Bullet/laserRed03.png");
	}
	
	public void newBullet(int type, float xPosition, float yPosition, float rotation){
		setUpInfo(type, xPosition, yPosition, rotation);
		setUpRectangle(type, xPosition, yPosition);
	}
	
	public void draw(float delta, SpriteBatch batch){
		updatePosition(delta);
		checkCondition();
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
		
		bulletInfoArray.add(newInfo);
	}
	
	private void setUpRectangle(int type, float xPosition, float yPosition){
		Rectangle newRactangle = new Rectangle();
		newRactangle.x = xPosition;
		newRactangle.y = yPosition;
		newRactangle.width = sizeType[type];
		newRactangle.height = sizeType[type];
		
		bulletRectangleArray.add(newRactangle);
	}
	
	private void updatePosition(float delta){
		Iterator<BulletInfo> iterInfo = bulletInfoArray.iterator();
		Iterator<Rectangle> iterRectangle = bulletRectangleArray.iterator();
		while(iterInfo.hasNext() && iterRectangle.hasNext()){
			BulletInfo info = iterInfo.next();
			info.xPostion += info.xSpeed * delta;
			info.yPostion += info.ySpeed * delta;
			
			Rectangle rectangle = iterRectangle.next();
			rectangle.x = info.xPostion;
			rectangle.y = info.yPostion;
		}
	}
	
	private void checkCondition(){
		Iterator<BulletInfo> iterInfo = bulletInfoArray.iterator();
		Iterator<Rectangle> iterRectangle = bulletRectangleArray.iterator();
		while(iterInfo.hasNext() && iterRectangle.hasNext()){
			BulletInfo info = iterInfo.next();
			Rectangle rectangle = iterRectangle.next();
			
			boolean isRemove = isOutfBound(info);
			
			if(isRemove){
				iterInfo.remove();
				iterRectangle.remove();
			}
		}
	}
	
	private boolean isOutfBound(BulletInfo info){
		return (info.xPostion < 0 || info.xPostion > 1600 || info.yPostion < 0 || info.xPostion > 900);
	}
	
	private void drawBullet(SpriteBatch batch){
		Iterator<BulletInfo> iterInfo = bulletInfoArray.iterator();
		while(iterInfo.hasNext()){
			BulletInfo info = iterInfo.next();
			batch.draw(bulletTexture[info.type], info.xPostion, info.yPostion
					, bulletTexture[info.type].getWidth()/2, bulletTexture[info.type].getHeight()/2
					, bulletTexture[info.type].getWidth(), bulletTexture[info.type].getHeight()
					, 1, 1, info.rotation, 0, 0, bulletTexture[info.type].getWidth()
					, bulletTexture[info.type].getHeight(), false, false);
		}
	}
}