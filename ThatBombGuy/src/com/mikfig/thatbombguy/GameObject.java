package com.mikfig.thatbombguy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
	public static Vector2 	ScreenOffset;
	public static float 	BlockScale;
	
	protected	GridPosition	mGridPosition;
	protected	Color			mDebugBoxColor;
	
	protected	Sprite			mCurrentSprite;
	protected	World			mWorld;
	
	public GameObject(World aWorld, GridPosition gridPosition) {
		this(aWorld, new Color(0.0f, 0.0f, 0.0f, 1.0f), gridPosition);
	}
	
	public GameObject(World aWorld, Color debugBoxColor, GridPosition gridPosition) {
		Gdx.app.log(ThatBombGuy.LogTag, "GameObject constructor");
		mDebugBoxColor = new Color(debugBoxColor);
		mGridPosition = new GridPosition(gridPosition);
		mWorld = aWorld;
	}
	
	public void Draw (SpriteBatch batch) {
		mCurrentSprite.setPosition(mGridPosition.x * BlockScale + ScreenOffset.x, mGridPosition.y * BlockScale + ScreenOffset.y);
		mCurrentSprite.draw(batch);
	}
	
	public abstract void Update(float deltaTime);
	
	public Color GetDebugBoxColor() {
		return mDebugBoxColor;
	}
	
	public GridPosition GetGridPosition() {
		return mGridPosition;
	}
	
	public void dispose() {
		;
	}
}
