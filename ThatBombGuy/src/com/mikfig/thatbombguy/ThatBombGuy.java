package com.mikfig.thatbombguy;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;

public class ThatBombGuy implements ApplicationListener {
//	private OrthographicCamera camera;
//	private SpriteBatch batch;
//	private Texture texture;
//	private Sprite sprite;
	
	public static	String			LogTag = "GAMEAPP";
	
	private	World			mWorld;
	private WorldRenderer	mRenderer;
	
	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		
		mWorld = new World(4);
		mRenderer = new WorldRenderer(mWorld, 72.0f);
		
		Gdx.input.setInputProcessor(new InputHandler(mRenderer.GetCamera(),
													mWorld.GetPlayerAgent(),
													mRenderer.GetScreenOffset(),
													mRenderer.GetBlockScale(),
													mRenderer.GetScreenWidth(),
													mRenderer.GetScreenHeight()));
//		batch = new SpriteBatch();
		
//		texture = new Texture(Gdx.files.internal("data/iconTex.png"));
//		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		
//		TextureRegion region = new TextureRegion(texture, 0, 0, 256, 256);
//		
//		sprite = new Sprite(region);
//		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
//		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
//		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
	}

	@Override
	public void dispose() {
		mWorld.dispose();
		mRenderer.dispose();
	}

	@Override
	public void render() {
		mWorld.Update(Gdx.graphics.getDeltaTime());
		mRenderer.RenderWorld();
		
//		Gdx.gl.glClearColor(1, 0, 1, 1);
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		sprite.draw(batch);
//		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
