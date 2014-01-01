package com.mikfig.thatbombguy;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WorldRenderer {
	private float				mBlockSize;
	private float				mScreenWidth;
	private float				mScreenHeight;
	private Vector2				mScreenOffset;
	private OrthographicCamera	mCamera;
	
	private Texture 		field;
	private Sprite			fieldSprite;
	private World			mWorld;
	private SpriteBatch 	batch;
	
	private Rectangle		currentDebugBox = new Rectangle();	
	private ShapeRenderer	debugBoxRenderer = new ShapeRenderer();
	
	public WorldRenderer(float blockSize) {
		mBlockSize = blockSize;
		mScreenWidth = Gdx.graphics.getWidth();
		mScreenHeight = Gdx.graphics.getHeight();
		
		mScreenOffset = new Vector2();
		mScreenOffset.x = -(mScreenWidth / 2.0f) + mBlockSize;
		mScreenOffset.y = -(mScreenHeight / 2.0f) + mBlockSize;
		
		mCamera = new OrthographicCamera(mScreenWidth, mScreenHeight);
		
		field = new Texture(Gdx.files.internal("data/field.png"));
		field.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(field, 0, 0, 1920, 1080);
		fieldSprite = new Sprite(region);
		
		fieldSprite.setPosition(-mScreenWidth / 2.0f, -mScreenHeight / 2.0f);
		fieldSprite.setSize(mScreenWidth, mScreenHeight);
		
		batch = new SpriteBatch();
	}
	
	public void SetWorld(World world) {
		mWorld = world;
	}
	
	public Camera GetCamera() {
		return mCamera;
	}
	
	public Vector2 GetScreenOffset() {
		return mScreenOffset;
	}
	
	public float GetBlockScale() {
		return mBlockSize;
	}
	
	public float GetScreenWidth() {
		return mScreenWidth;
	}
	
	public float GetScreenHeight() {
		return mScreenHeight;
	}
	
	public void RenderWorld() {		
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.setProjectionMatrix(mCamera.combined);
		fieldSprite.draw(batch);
		
		for (GameObject object : mWorld.getObjects()) {
			object.Draw(batch);
		}
		
		batch.end();
		
//		debugBoxRenderer.setProjectionMatrix(mCamera.combined);
//		debugBoxRenderer.begin(ShapeType.Filled);
//		
//		for( GameObject object : gameObjects ) {
//			GridPosition	position = object.GetGridPosition();
//			Sprite sprite = object.GetSprite();
			
//			
//			// libGDX origin is lower left of screen
//			currentDebugBox.width = mBlockSize;
//			currentDebugBox.height = mBlockSize;			
//			currentDebugBox.x = position.x * mBlockSize - (mScreenWidth / 2.0f) + mBlockSize;
//			currentDebugBox.y = position.y * mBlockSize - (mScreenHeight / 2.0f) + mBlockSize;
//			
////			debugBoxRenderer.setColor(object.GetDebugBoxColor());
//			debugBoxRenderer.setColor(Color.BLACK);
//			debugBoxRenderer.rect(currentDebugBox.x, currentDebugBox.y, currentDebugBox.height, currentDebugBox.width);
//		}
//		
//		debugBoxRenderer.end();
	}
	
	public void dispose() {
		field.dispose();
	}
}
