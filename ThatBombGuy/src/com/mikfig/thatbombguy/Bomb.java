package com.mikfig.thatbombguy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Bomb extends GameObject {
	public Bomb(World aWorld, GridPosition gridPosition) {
		super(aWorld, gridPosition);
		
		Gdx.app.log(ThatBombGuy.LogTag, "Bomb constructor");
		
		mCurrentFrame = 0;
		mTimeInCurrentFrame = 0.0f;
		mCurrentSprite = Bomb_sprites[0];
	}

	private static final Sprite[] Bomb_sprites = {
		World.textureAtlas.createSprite("bomb",1), World.textureAtlas.createSprite("bomb",2),
		World.textureAtlas.createSprite("bomb",3), World.textureAtlas.createSprite("bomb",4),
		World.textureAtlas.createSprite("bomb",5), World.textureAtlas.createSprite("bomb",6) };
	
	private int					mCurrentFrame;
	private float				mTimeInCurrentFrame;
	
	private static final float	TimePerAnimationCycle = 1.0f;
	private static final int	NumberOfFrames = 6;
	private static final float	TimePerFrame = TimePerAnimationCycle / NumberOfFrames;
	
	@Override
	public void Update(float deltaTime) {
		Gdx.app.log(ThatBombGuy.LogTag, "Bomb.Update");
		// update the animation frame
		mTimeInCurrentFrame += deltaTime;
		
		if(mTimeInCurrentFrame >= TimePerFrame) {
			Gdx.app.log(ThatBombGuy.LogTag, "Bomb next frame");
			mTimeInCurrentFrame -= TimePerFrame;
			mCurrentFrame = mCurrentFrame + 1;
			
			// once the bomb has exploded,
			// remove it from the world
			if(mCurrentFrame >= NumberOfFrames) {
				Gdx.app.log(ThatBombGuy.LogTag, "Bomb is dead");
				mWorld.RemoveObject(this);
			}
			
			mCurrentSprite = Bomb_sprites[mCurrentFrame];
		}		
	}
}
