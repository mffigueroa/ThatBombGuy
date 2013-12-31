package com.mikfig.thatbombguy;

public class GridPosition {
	public int x, y;
	
	public GridPosition(int a_x, int a_y) {
		x = a_x;
		y = a_y;
	}

	public GridPosition(GridPosition gridPosition) {
		x = gridPosition.x;
		y = gridPosition.y;
	}
	
	public boolean Equals(GridPosition otherPosition) {
		return x == otherPosition.x && y == otherPosition.y;
	}
}
