package com.mikfig.thatbombguy;

import java.util.ArrayDeque;
import java.util.Queue;
import java.lang.Math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;

public class Agent extends GameObject {

	private static final Sprite[][] Players_stationary = { 
		{ World.textureAtlas.createSprite("player1_up_stationary"), World.textureAtlas.createSprite("player1_down_stationary"), 
			World.textureAtlas.createSprite("player1_left_stationary"), World.textureAtlas.createSprite("player1_right_stationary") },
			
		{ World.textureAtlas.createSprite("player2_up_stationary"), World.textureAtlas.createSprite("player2_down_stationary"), 
			World.textureAtlas.createSprite("player2_left_stationary"), World.textureAtlas.createSprite("player2_right_stationary") },
			
		{ World.textureAtlas.createSprite("player3_up_stationary"), World.textureAtlas.createSprite("player3_down_stationary"), 
			World.textureAtlas.createSprite("player3_left_stationary"), World.textureAtlas.createSprite("player3_right_stationary") },
			
		{ World.textureAtlas.createSprite("player4_up_stationary"), World.textureAtlas.createSprite("player4_down_stationary"), 
			World.textureAtlas.createSprite("player4_left_stationary"), World.textureAtlas.createSprite("player4_right_stationary") }
	};
	
	private static final Sprite[][][] Players_moving = { 
		{
			{ World.textureAtlas.createSprite("player1_up_moving", 1), World.textureAtlas.createSprite("player1_up_moving", 2) },
			{ World.textureAtlas.createSprite("player1_down_moving", 1), World.textureAtlas.createSprite("player1_down_moving", 2) },
			{ World.textureAtlas.createSprite("player1_left_moving", 1), World.textureAtlas.createSprite("player1_left_moving", 2),
				World.textureAtlas.createSprite("player1_left_moving", 3) },
			{ World.textureAtlas.createSprite("player1_right_moving", 1), World.textureAtlas.createSprite("player1_right_moving", 2),
				World.textureAtlas.createSprite("player1_right_moving", 3) }
		}, {
			{ World.textureAtlas.createSprite("player2_up_moving", 1), World.textureAtlas.createSprite("player2_up_moving", 2) },
			{ World.textureAtlas.createSprite("player2_down_moving", 1), World.textureAtlas.createSprite("player2_down_moving", 2) },
			{ World.textureAtlas.createSprite("player2_left_moving", 1), World.textureAtlas.createSprite("player2_left_moving", 2),
				World.textureAtlas.createSprite("player2_left_moving", 3) },
			{ World.textureAtlas.createSprite("player2_right_moving", 1), World.textureAtlas.createSprite("player2_right_moving", 2),
				World.textureAtlas.createSprite("player2_right_moving", 3) }
		}, {
			{ World.textureAtlas.createSprite("player3_up_moving", 1), World.textureAtlas.createSprite("player3_up_moving", 2) },
			{ World.textureAtlas.createSprite("player3_down_moving", 1), World.textureAtlas.createSprite("player3_down_moving", 2) },
			{ World.textureAtlas.createSprite("player3_left_moving", 1), World.textureAtlas.createSprite("player3_left_moving", 2),
				World.textureAtlas.createSprite("player3_left_moving", 3) },
			{ World.textureAtlas.createSprite("player3_right_moving", 1), World.textureAtlas.createSprite("player3_right_moving", 2),
				World.textureAtlas.createSprite("player3_right_moving", 3) }
		}, {
			{ World.textureAtlas.createSprite("player4_up_moving", 1), World.textureAtlas.createSprite("player4_up_moving", 2) },
			{ World.textureAtlas.createSprite("player4_down_moving", 1), World.textureAtlas.createSprite("player4_down_moving", 2) },
			{ World.textureAtlas.createSprite("player4_left_moving", 1), World.textureAtlas.createSprite("player4_left_moving", 2),
				World.textureAtlas.createSprite("player4_left_moving", 3) },
			{ World.textureAtlas.createSprite("player4_right_moving", 1), World.textureAtlas.createSprite("player4_right_moving", 2),
				World.textureAtlas.createSprite("player4_right_moving", 3) }
		}
	};
	
	public enum CardinalDirection {
		UP(0), DOWN(1), LEFT(2), RIGHT(3);

		private final int value;

        private CardinalDirection(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
    
	}
	
	private static final Color[] playerColors =
	{	
		new Color(	96.0f		/255.0f,		151.0f	/255.0f,	68.0f	/255.0f, 1.0f),
		new Color(	64.0f		/255.0f,		78.0f	/255.0f,	89.0f	/255.0f, 1.0f),
		new Color(	80.0f		/255.0f,		50.0f	/255.0f,	143.0f	/255.0f, 1.0f),
		new Color(	206.0f		/255.0f,		128.0f	/255.0f,	37.0f	/255.0f, 1.0f)
	};

	private CardinalDirection	mCurrentDirection;
	private int					mPlayerNumber;
	private int					mCurrentFrame;
	private float				mTimeInCurrentFrame;
	
	private static final float	TimePerAnimationCycle = 1.0f;
	private static final float	TimePerHorizontalMovingFrame = TimePerAnimationCycle / 4.0f;
	private static final float	TimePerVerticalMovingFrame = TimePerAnimationCycle / 2.0f;
	
	private class Goal {
		public CardinalDirection	movementDirection;
		public float				distanceToTravel;
		public boolean				dropBombAtGoal;
	};
	
	// each goal is a unidirectional vector, so
	// we move either vertically or horizontally at
	// any point in time but not both. so to get
	// to some point we travel there one direction
	// at a time.
	private Queue<Goal>		mGoalQueue;
	private Goal			mCurrentGoal;
	private GridPosition	mDestinationOfCurrentGoal;
	
	// grid units per second. travel from the top to bottom in 4 seconds
	// may change if we have power ups.
	private float		mVelocity;
	
	// the velocity won't be in integer increments, therefore, we
	// will need to keep track of the movement "leftovers" of
	// each update to add them on to the next
	private float		mPartialGridMovements;
	
	public Agent(World aWorld, GridPosition position, CardinalDirection direction, int playerNumber) {
		super(aWorld, position);
		mCurrentDirection = direction;
		mPlayerNumber = playerNumber;
		mCurrentFrame = 0;
		mTimeInCurrentFrame = 0.0f;
		mDestinationOfCurrentGoal = new GridPosition(mGridPosition);
		
		mGoalQueue = new ArrayDeque<Goal>();
		mCurrentGoal = null;
		mVelocity = World.GridHeight / 4.0f;
		mPartialGridMovements = 0.0f;
		
		if(1 <= mPlayerNumber && mPlayerNumber <= 4) {
			mDebugBoxColor = playerColors[mPlayerNumber - 1];
			mCurrentSprite = Players_moving[mPlayerNumber - 1][mCurrentDirection.getValue()][0];
		} else {
			throw new IllegalArgumentException("App - Invalid player number: " + Integer.toString(mPlayerNumber));
		}
	}
	
	public void PlanMovementToPosition(GridPosition touchPosition, boolean plantBombOnArrival) {
		int gridDistanceX = touchPosition.x - mDestinationOfCurrentGoal.x;
		int gridDistanceY = touchPosition.y - mDestinationOfCurrentGoal.y;
		
		mDestinationOfCurrentGoal.x = touchPosition.x;
		mDestinationOfCurrentGoal.y = touchPosition.y;
		
		// need to keep track of the last facing direction
		// before the bomb drop so that the direction
		// that will be faced when the bomb is dropped is
		// the same as before its dropped
		CardinalDirection finalFacingDirection = mCurrentDirection;
		
		if(gridDistanceX < 0) {
			Goal xGoal = new Goal();
			xGoal.movementDirection = CardinalDirection.LEFT;
			xGoal.distanceToTravel = -gridDistanceX;
			xGoal.dropBombAtGoal = false;
			mGoalQueue.add(xGoal);
			finalFacingDirection = xGoal.movementDirection;
		} else if(gridDistanceX > 0) {
			Goal xGoal = new Goal();
			xGoal.movementDirection = CardinalDirection.RIGHT;
			xGoal.distanceToTravel = gridDistanceX;
			xGoal.dropBombAtGoal = false;
			mGoalQueue.add(xGoal);
			finalFacingDirection = xGoal.movementDirection;
		}
		
		if(gridDistanceY < 0) {
			Goal yGoal = new Goal();
			yGoal.movementDirection = CardinalDirection.DOWN;
			yGoal.distanceToTravel = -gridDistanceY;
			yGoal.dropBombAtGoal = false;
			mGoalQueue.add(yGoal);
			finalFacingDirection = yGoal.movementDirection;
		} else if(gridDistanceY > 0) {
			Goal yGoal = new Goal();
			yGoal.movementDirection = CardinalDirection.UP;
			yGoal.distanceToTravel = gridDistanceY;
			yGoal.dropBombAtGoal = false;
			mGoalQueue.add(yGoal);
			finalFacingDirection = yGoal.movementDirection;
		}
		
		if(plantBombOnArrival) {
			Goal bombGoal = new Goal();
			bombGoal.distanceToTravel = 0;
			bombGoal.movementDirection = finalFacingDirection;
			bombGoal.dropBombAtGoal = true;
			mGoalQueue.add(bombGoal);
		}
	}
	
	public void ReactToSingleTap(GridPosition touchPosition) {
		PlanMovementToPosition(touchPosition, false);
	}
	
	public void ReactToDoubleTap(GridPosition touchPosition) {
		PlanMovementToPosition(touchPosition, true);
	}
	
	public void Update(float deltaTime) {
		// if we don't have a current goal,
		// check if there's any goals waiting
		// on the queue
		if(mCurrentGoal == null) {
			mCurrentGoal = mGoalQueue.poll();
			
			// if we're not going anywhere, just stand still
			// facing the same direction we're currently facing
			if(mCurrentGoal == null) {
				// exit the function as we only animate
				// if we're currently moving
				mCurrentSprite = Players_stationary[mPlayerNumber - 1][mCurrentDirection.getValue()];
				return;
			} else {
				// we just got a new goal, so we have to update
				// our current cardinal direction we're facing
				// in order that we move in the direction of the
				// new goal.
				mCurrentDirection = mCurrentGoal.movementDirection;
			}
		}
		
		moveTowardsGoal(deltaTime);
		updateAnimation(deltaTime);
	}
	
	public void moveTowardsGoal(float deltaTime) {
		// move towards the goal at a velocity given
		// by mVelocity
		float totalMovement = Math.min(mVelocity * deltaTime + mPartialGridMovements, mCurrentGoal.distanceToTravel);
		
		if(totalMovement < 1.0f && totalMovement != mCurrentGoal.distanceToTravel) {
			mPartialGridMovements = totalMovement;
			return;
		} else {
			mPartialGridMovements = totalMovement - (float)Math.floor(totalMovement);
			totalMovement -= mPartialGridMovements;
		}
		
		mCurrentGoal.distanceToTravel -= totalMovement;
		
		if(mCurrentDirection == CardinalDirection.UP) {	
			mGridPosition.y += totalMovement;
		} else if(mCurrentDirection == CardinalDirection.DOWN) {
			mGridPosition.y -= totalMovement;
		} else if(mCurrentDirection == CardinalDirection.LEFT) {
			mGridPosition.x -= totalMovement;
		} else if(mCurrentDirection == CardinalDirection.RIGHT) {
			mGridPosition.x += totalMovement;
		}
		
		// we reached the goal destination
		// so get rid of the current goal
		// in preparation for future goals
		// and drop a bomb 
		if(mCurrentGoal.distanceToTravel <= 0.0f) {
			Gdx.app.log(ThatBombGuy.LogTag, "Agent reached goal");
			if(mCurrentGoal.dropBombAtGoal) {
				Gdx.app.log(ThatBombGuy.LogTag, "Dropping bomb");
				mWorld.AddObject(new Bomb(mWorld, mGridPosition));
			}
			
			mCurrentGoal = null;
			mPartialGridMovements = 0.0f;
		}
	}
	
	public void updateAnimation(float deltaTime) {
		// update the animation frame,
		// only happens if we're currently moving
		mTimeInCurrentFrame += deltaTime;
		
		boolean verticalMovement =	mCurrentDirection == CardinalDirection.UP ||
									mCurrentDirection == CardinalDirection.DOWN; 
		
		float timePerFrame = verticalMovement ? TimePerVerticalMovingFrame : TimePerHorizontalMovingFrame;
		int numberOfFrames = verticalMovement ? 2 : 4;
		
		if(mTimeInCurrentFrame >= timePerFrame) {
			mTimeInCurrentFrame -= timePerFrame;
			mCurrentFrame = (mCurrentFrame + 1) % numberOfFrames;
		
			if(verticalMovement || mCurrentFrame % 2 == 0)
				mCurrentSprite = Players_moving[mPlayerNumber - 1][mCurrentDirection.getValue()][mCurrentFrame];
			else
				mCurrentSprite = Players_moving[mPlayerNumber - 1][mCurrentDirection.getValue()][1];
		}
	}
}
