package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LastDayGame extends Game {
	public SpriteBatch batch;
	 
    @Override
    public void create () {
        batch = new SpriteBatch();
        setScreen(new MainGameScreen(this));
    }
 
    @Override
    public void render () {
        super.render();
    }
 
    @Override
    public void dispose () {
        batch.dispose();
    }
}
