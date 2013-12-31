package com.mikfig.thatbombguy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

public class Block extends GameObject {
		
	private static final Texture crate = new Texture(Gdx.files.internal("data/crate.png"));
	private static final Texture permanentBlock = new Texture(Gdx.files.internal("data/permanentBlock.png"));
	
	private static Sprite crateSprite = new Sprite(new TextureRegion(crate, 0, 0, crate.getWidth(), crate.getHeight()));
	private static Sprite permanentBlockSprite = new Sprite(new TextureRegion(permanentBlock, 0, 0, permanentBlock.getWidth(), permanentBlock.getHeight()));
	
	private static final Color permanentBlockColor = 
		new Color(	96.0f		/255.0f,		151.0f	/255.0f,	68.0f	/255.0f, 1.0f);
		
		
	private static final Color crateBlockColor = 
		new Color(	96.0f		/255.0f,		151.0f	/255.0f,	68.0f	/255.0f, 1.0f);
		
	public enum BlockType {
		Permanent,
		Crate
	}
	
	private BlockType mBlockType;

	public Block(World aWorld, GridPosition position, BlockType blockType) {
		super(aWorld, position);
		mBlockType = blockType;
		
		if(mBlockType == BlockType.Permanent) {
			mDebugBoxColor = permanentBlockColor;
			mCurrentSprite = permanentBlockSprite;
		} else {
			mDebugBoxColor = crateBlockColor;
			mCurrentSprite = crateSprite;
		}
	}
	
	public void Update(float deltaTime) {
		;
	}
}
