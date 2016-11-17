package com.thelastdaytosurvive.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameWorld {
	public static final float MAP_X = 3200;
	public static final float MAP_Y = 1800;
	public static final float CAMERA_X = 1600;
	public static final float CAMERA_Y = 900;
	
	private MainGameScreen mainGameScreen;
	private SpriteBatch batch;
	private Player player;
	private Bullet bullet;
	private Weapon weapon;
	private Enemy enemy;
	private Map map;
	private Crafting crafting;
	private MainGameTracker tracker;
	private ItemDropper itemDropper;
	
	public MainGameWorld(MainGameScreen mainGameScreen){
		this.mainGameScreen = mainGameScreen;
		batch = mainGameScreen.getSpriteBatch();
		
		map = new Map(this);
		tracker = new MainGameTracker(this);
		crafting = new Crafting(map);
		enemy = new Enemy();
		bullet = new Bullet(enemy);
		weapon = new Weapon(bullet);
		player = new Player(mainGameScreen, weapon, map, crafting, tracker);
		itemDropper = new ItemDropper(this);
		
		enemy.setUp(player, map, tracker, itemDropper);
		
	}
	
	public void update(float delta){
		player.update();
		bullet.update(delta);
		weapon.update(Player.WEAPON_PRIMARY);
		weapon.update(Player.WEAPON_SECONDARY);
		enemy.update(delta);
		map.update();
		tracker.update();
		itemDropper.update();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public SpriteBatch getSpriteBatch(){
		return batch;
	}
	
	public Bullet getBullet(){
		return bullet;
	}
	
	public Weapon getWeapon(){
		return weapon;
	}
	
	public Enemy getEnemy(){
		return enemy;
	}
	
	public Map getMap(){
		return map;
	}
	
	public Crafting getCrafting(){
		return crafting;
	}
	
	public MainGameTracker getTracker(){
		return tracker;
	}
	
	public ItemDropper getItemdropper(){
		return itemDropper;
	}
}
