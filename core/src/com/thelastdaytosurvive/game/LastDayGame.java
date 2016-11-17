package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LastDayGame extends Game {
	private int highScore = 0;
	
	public SpriteBatch batch;
	 
    @Override
    public void create () {
        batch = new SpriteBatch();
        //setScreen(new MainMenu(this));
        setScreen(new MainMenu(this));
    }
 
    @Override
    public void render () {
        super.render();
    }
 
    @Override
    public void dispose () {
        batch.dispose();
    }
    
    public void goToHowToPlay(){
    	setScreen(new HowToPlay(this));
    }
    
    public void goToMainGame(){
    	setScreen(new MainGameScreen(this));
    }
    
    public void goToMainMenu(){
    	setScreen(new MainMenu(this));
    }
    
    public void gameOver(int score){
    	if(highScore < score){
    		highScore = score;
    		setScreen(new GameOverScreen(this, score, true));
    	} else {
    		setScreen(new GameOverScreen(this, score, false));
    	}
    }
    
    public int getHghScore(){
    	return highScore;
    }
}
