package com.mikfig.thatbombguy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mikfig.thatbombguy.Block.BlockType;

public class World {
	public static final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("data/pack.atlas"));
	
	public static final int PermanentBlocksWidth = 6;
	public static final int PermanentBlocksHeight = 11;
	public static final int GridWidth = PermanentBlocksWidth * 2 + 1;
	public static final int GridHeight = PermanentBlocksHeight * 2 + 1;
	
	private ArrayList<GameObject>	mGameObjects;
	private ArrayList<GameObject>	mObjectsToAddNextFrame;
	private Agent					mPlayerAgent; 
	
	public World(int numberOfPlayers) {
		mGameObjects = new ArrayList<GameObject>();
		mObjectsToAddNextFrame = new ArrayList<GameObject>();
		
		for(int i = 0; i < PermanentBlocksWidth; ++i) {
			for(int j = 0; j < PermanentBlocksHeight; ++j) {
				mGameObjects.add(new Block(this, new GridPosition(1 + j * 2, 1 + i * 2), BlockType.Permanent));
			}
		}
		
		mPlayerAgent = new Agent(this, new GridPosition(0, GridWidth - 1), Agent.CardinalDirection.RIGHT, 1);
		mGameObjects.add(mPlayerAgent);
		
		for(int i = 2; i <= numberOfPlayers; ++i) {
			GridPosition pos = new GridPosition(i > 2 ? GridHeight - 1 : 0, (i % 2) * (GridWidth - 1));
			Agent agent = new Agent(this, pos, i > 2 ? Agent.CardinalDirection.LEFT : Agent.CardinalDirection.RIGHT, i);
			mGameObjects.add(agent);
		}
	}
	
	public Agent GetPlayerAgent() {
		return mPlayerAgent; 
	}
	
	public void Update(float deltaTime) {
		for( GameObject objectToAdd : mObjectsToAddNextFrame ) {
			mGameObjects.add(objectToAdd);
		}
		mObjectsToAddNextFrame.clear();
		
		for( GameObject object : mGameObjects ) {
			object.Update(deltaTime);
		}
	}
	
	public void AddObject(GameObject objectToAdd) {
		Gdx.app.log(ThatBombGuy.LogTag, "Adding object");
		mObjectsToAddNextFrame.add(objectToAdd);
	}
	
	public void RemoveObject(GameObject objectToRemove) {
		Gdx.app.log(ThatBombGuy.LogTag, "Removing object");
		mGameObjects.remove(objectToRemove);
	}
	
	public List<GameObject> getObjects() {
		return Collections.unmodifiableList(mGameObjects);
	}
	
	public void dispose() {
		for( GameObject object : mGameObjects ) {
			object.dispose();
		}
	}
}
