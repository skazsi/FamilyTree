package hu.skzs.familytree.renderer;

class GridPosition {

	private final int generation;
	private final int horizontalPosition;
	private final int horizontalSize;

	GridPosition(int generation, int horizontalPosition, int horizontalSize) {
		this.generation = generation;
		this.horizontalPosition = horizontalPosition;
		this.horizontalSize = horizontalSize;
	}

	public int getGeneration() {
		return generation;
	}

	public int getHorizontalPosition() {
		return horizontalPosition;
	}

	public int getHorizontalSize() {
		return horizontalSize;
	}
}
