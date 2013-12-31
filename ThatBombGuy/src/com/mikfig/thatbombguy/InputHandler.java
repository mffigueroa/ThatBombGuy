package com.mikfig.thatbombguy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;

public class InputHandler implements InputProcessor {
	
	private 	Camera			mCamera;
	private 	Agent			mHandlingAgent;
	private		Vector2			mScreenOffset;
	private		float			mBlockScale;
	private		float			mScreenWidth;
	private		float			mScreenHeight;
	private		Timer			mDoubleTap_TouchTimer;
	private		float			mDoubleTap_MaxTimeBetweenTouches;
	private		boolean			mDoupleTap_HaveFirstTapInPair;
	private		GridPosition	mPositionLastTouched;
	
	private class DoubleTap_MaxTimeExceededWatcher extends Timer.Task {
		public void run() {
			// after a certain amount of time,
			// the first tap in a double tap sequence
			// is invalidated as too much time has passed
			// since the first tap
			mDoupleTap_HaveFirstTapInPair = false;
			
			// can only know it was a single tap and not a double tap
			// once the max time between the first and second tap goes
			// over the max
			mHandlingAgent.ReactToSingleTap(mPositionLastTouched);
		}
	};
	
	private		Timer.Task	mDoubleTap_MaxTimeExceededWatcherTask;
	
	public InputHandler(Camera cam, Agent handlingAgent, Vector2 screenOffset, float blockScale,
						float screenWidth, float screenHeight) {
		super();
		
		mCamera = cam;
		mHandlingAgent = handlingAgent;
		mScreenOffset = screenOffset;
		mBlockScale = blockScale;
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;
		mDoubleTap_TouchTimer = new Timer();
		mDoubleTap_MaxTimeExceededWatcherTask = new DoubleTap_MaxTimeExceededWatcher();
		mDoubleTap_MaxTimeBetweenTouches = 0.4f;
		mDoupleTap_HaveFirstTapInPair = false;
		mPositionLastTouched = null;
	}
	
	@Override
	public boolean keyDown (int keycode) {
		return false;
	}
	
	@Override
	public boolean keyUp (int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		return false;
	}

	@Override
	public boolean touchDown (int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		if(mDoupleTap_HaveFirstTapInPair)
			mDoubleTap_MaxTimeExceededWatcherTask.cancel();
		
		Vector3 touchPos = new Vector3();
		touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		mCamera.unproject(touchPos);
		touchPos.x -= mScreenOffset.x;
		touchPos.y -= mScreenOffset.y;
		
		// if its outside our "touchable" area, then ignore it
		if(	0 > touchPos.x || touchPos.x > mScreenWidth ||
			0 > touchPos.y || touchPos.y > mScreenHeight) {
			
			return false;
		}
		
		touchPos.x /= mBlockScale;
		touchPos.y /= mBlockScale;
		
		GridPosition positionTouched = new GridPosition(
				(int) Math.floor(touchPos.x),
				(int) Math.floor(touchPos.y));
		
		if(!mDoupleTap_HaveFirstTapInPair) {
			mPositionLastTouched = positionTouched;
			mDoupleTap_HaveFirstTapInPair = true;
			mDoubleTap_TouchTimer.scheduleTask(mDoubleTap_MaxTimeExceededWatcherTask, mDoubleTap_MaxTimeBetweenTouches);
		} else if(positionTouched.Equals(mPositionLastTouched)) {
			// we have two taps in the same grid position
			// following each other in time less than the max time
			// between taps 
			mDoupleTap_HaveFirstTapInPair = false;
			mHandlingAgent.ReactToDoubleTap(positionTouched);
		} else {
			// if the second tap isn't in the same location as
			// the first then we know it wasn't a double tap
			mHandlingAgent.ReactToSingleTap(mPositionLastTouched);
			
			// however, we don't yet know if the second tap
			// is a double tap
			mDoupleTap_HaveFirstTapInPair = false;
			mPositionLastTouched = positionTouched;
			mDoubleTap_TouchTimer.scheduleTask(mDoubleTap_MaxTimeExceededWatcherTask, mDoubleTap_MaxTimeBetweenTouches);
		}
		
		return true;
	}

	@Override
	public boolean touchDragged (int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		return false;
	}
}
