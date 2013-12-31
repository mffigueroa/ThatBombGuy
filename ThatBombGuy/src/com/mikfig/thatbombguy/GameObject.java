package com.mikfig.thatbombguy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
	protected	GridPosition	mGridPosition;
	protected	Color			mDebugBoxColor;
	
	private		Vector2			mScreenOffset;
	private		float			mBlockScale;
	
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
		mCurrentSprite.setPosition(mGridPosition.x * mBlockScale + mScreenOffset.x, mGridPosition.y * mBlockScale + mScreenOffset.y);
		mCurrentSprite.draw(batch);
	}
	
	public abstract void Update(float deltaTime);
	
	public Color GetDebugBoxColor() {
		return mDebugBoxColor;
	}
	
	public GridPosition GetGridPosition() {
		return mGridPosition;
	}
	
	public void	SetOffset(Vector2 screenOffset) {
		mScreenOffset = screenOffset;
	}
	
	public void	SetScale(float blockScale) {
		mBlockScale = blockScale;
	}
	
	public void dispose() {
		;
	}
}
