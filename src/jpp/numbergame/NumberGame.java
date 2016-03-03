package jpp.numbergame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberGame {

	private int score;
	private int width;
	private int height;
	private Tile[][] field;

	public NumberGame(int width, int height) {

		if (width < 1 || height < 1)
			throw new IllegalArgumentException("Heigth and width of the field schould be greater than zero");

		this.score = 0;
		this.width = width;
		this.height = height;
		this.field = new Tile[width][height];
	}

	public NumberGame(int width, int height, int initialTiles) {

		if (width < 1 || height < 1)
			throw new IllegalArgumentException("Heigth and width of the field schould be greater than zero");
		if (initialTiles < 0)
			throw new IllegalArgumentException("Initial tiles can't be < 0");
		if (initialTiles > height * width)
			throw new IllegalArgumentException("To much Initial tiles. Max(height * width) = " + height * width);

		this.score = 0;
		this.width = width;
		this.height = height;
		this.field = new Tile[width][height];

		while (initialTiles > 0) {
			addRandomTile();
			--initialTiles;
		}
	}

	public int get(Coordinate2D coord) {
		return get(coord.getX(), coord.getY());
	}

	public int get(int x, int y) {
		if (field[x][y] == null)
			return 0;
		return field[x][y].getValue();
	}

	public int getPoints() {
		return score;
	}

	public boolean isFull() {
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (field[x][y] == null) {
					return false;
				}
			}
		}
		return true;
	}

	public Tile addRandomTile() {

		Random rand = new Random();
		int value = random2or4();
		int x = rand.nextInt(width);
		int y = rand.nextInt(height);

		try {
			return addTile(x, y, value);
		} catch (TileExistsException e) {
			return addRandomTile();
		}
	}

	public int random2or4() {
		if (new Random().nextInt(10) == 9) {
			return 4;
		} else
			return 2;
	}

	public Tile addTile(int x, int y, int value) {
		if (field[x][y] != null)
			throw new TileExistsException("The field (" + x + "," + y + ") contain allready a tile");
		return field[x][y] = new Tile(new Coordinate2D(x, y), value);
	}

	public List<Move> move(Direction dir) {
		List<Move> moves = new ArrayList<Move>();
		switch (dir) {
		case RIGHT:
			for (int x = width - 2; x >= 0; --x) {
				for (int y = 0; y < height; ++y) {
					move(x, y, dir, moves);
				}
			}
			break;
		case LEFT:
			for (int x = 1; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					move(x, y, dir, moves);
				}
			}
			break;
		case DOWN:
			for (int y = height - 2; y >= 0; --y) {
				for (int x = 0; x < width; ++x) {
					move(x, y, dir, moves);
				}
			}
			break;
		case UP:
			for (int y = 1; y < height; ++y) {
				for (int x = 0; x < width; ++x) {
					move(x, y, dir, moves);
				}
			}
			break;

		}
		unMarkTiles();
		return moves;
	}

	public void move(int x, int y, Direction dir, List<Move> moves) {
		if (field[x][y] != null) {
			if (tileCanMove(field[x][y], dir) || canMerge(field[x][y], dir)) {
				Coordinate2D from = field[x][y].getCoordinate();
				int oldValue = field[x][y].getValue();
				int newValue = field[x][y].getValue();

				while (tileCanMove(field[x][y], dir)) {
					moveTileOneStep(field[x][y], dir);
					if (dir == Direction.RIGHT)
						++x;
					if (dir == Direction.LEFT)
						--x;
					if (dir == Direction.DOWN)
						++y;
					if (dir == Direction.UP)
						--y;
				}
				if (canMerge(field[x][y], dir)) {
					mergeTiles(field[x][y], dir);
					if (dir == Direction.RIGHT)
						++x;
					if (dir == Direction.LEFT)
						--x;
					if (dir == Direction.DOWN)
						++y;
					if (dir == Direction.UP)
						--y;
					newValue = field[x][y].getValue();
				}

				Coordinate2D to = field[x][y].getCoordinate();
				Move m = new Move(from, to, oldValue, newValue);
				moves.add(m);
			}
		}
	}

	public void mergeTiles(Tile tile, Direction dir) {
		int x = tile.getCoordinate().getX();
		int y = tile.getCoordinate().getY();
		int newValue = tile.getValue() * 2;
		switch (dir) {
		case RIGHT:
			field[x + 1][y] = new Tile(new Coordinate2D(x + 1, y), newValue);
			field[x + 1][y].doMerged();
			field[x][y] = null;
			break;
		case LEFT:
			field[x - 1][y] = new Tile(new Coordinate2D(x - 1, y), newValue);
			field[x - 1][y].doMerged();
			field[x][y] = null;
			break;
		case DOWN:
			field[x][y + 1] = new Tile(new Coordinate2D(x, y + 1), newValue);
			field[x][y + 1].doMerged();
			field[x][y] = null;
			break;
		case UP:
			field[x][y - 1] = new Tile(new Coordinate2D(x, y - 1), newValue);
			field[x][y - 1].doMerged();
			field[x][y] = null;
			break;
		default:
			throw new IllegalArgumentException("Such direction does'not exist");
		}
		score += newValue;
	}

	public void moveTileOneStep(Tile tile, Direction dir) {
		int x = tile.getCoordinate().getX();
		int y = tile.getCoordinate().getY();
		int value = tile.getValue();
		switch (dir) {
		case RIGHT:
			field[x + 1][y] = new Tile(new Coordinate2D(x + 1, y), value);
			field[x][y] = null;
			break;
		case LEFT:
			field[x - 1][y] = new Tile(new Coordinate2D(x - 1, y), value);
			field[x][y] = null;
			break;
		case DOWN:
			field[x][y + 1] = new Tile(new Coordinate2D(x, y + 1), value);
			field[x][y] = null;
			break;
		case UP:
			field[x][y - 1] = new Tile(new Coordinate2D(x, y - 1), value);
			field[x][y] = null;
			break;
		default:
			throw new IllegalArgumentException("Such direction does'not exist");
		}
	}

	public List<Tile> tilesList() {
		List<Tile> allTiles = new ArrayList<Tile>();
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (field[x][y] != null) {
					allTiles.add(field[x][y]);
				}
			}
		}
		return allTiles;
	}

	public boolean canMove() {
		if (canMove(Direction.RIGHT)||canMerge(Direction.RIGHT)) {
			return true;
		}
		if (canMove(Direction.LEFT)||canMerge(Direction.LEFT)) {
			return true;
		}
		if (canMove(Direction.DOWN)||canMerge(Direction.DOWN)) {
			return true;
		}
		if (canMove(Direction.UP)||canMerge(Direction.UP)) {
			return true;
		}
		return false;
	}

	public boolean canMove(Direction dir) {
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (field[x][y] != null) {
					if (tileCanMove(field[x][y], dir)||canMerge(field[x][y],dir)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean tileCanMove(Tile tile, Direction dir) {

		int x = tile.getCoordinate().getX();
		int y = tile.getCoordinate().getY();
		switch (dir) {
		case RIGHT:
			if (x == width - 1) {
				return false;
			}
			if (field[x + 1][y] == null) {
				return true;
			} else {
				return false;
			}
		case LEFT:
			if (x == 0) {
				return false;
			}
			if (field[x - 1][y] == null) {
				return true;
			} else {
				return false;
			}
		case DOWN:
			if (y == height - 1) {
				return false;
			}
			if (field[x][y + 1] == null) {
				return true;
			} else {
				return false;
			}
		case UP:
			if (y == 0) {
				return false;
			}
			if (field[x][y - 1] == null) {
				return true;
			} else {
				return false;
			}
		default:
			throw new IllegalArgumentException("Such direction does'not exist");
		}
	}

	public boolean canMerge(Direction dir) {
		boolean canMerge = false;
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (field[x][y] != null) {
					canMerge |= canMerge(field[x][y],dir);
				}
			}
		}
		return canMerge;
	}
	public boolean canMerge(Tile tile, Direction dir) {

		int x = tile.getCoordinate().getX();
		int y = tile.getCoordinate().getY();
		int value = tile.getValue();
		switch (dir) {
		case RIGHT:
			if (x == width - 1) {
				return false;
			}
			if (!field[x + 1][y].getMerged() && value == field[x + 1][y].getValue()) {
				return true;
			} else {
				return false;
			}
		case LEFT:
			if (x == 0) {
				return false;
			}
			if (!field[x - 1][y].getMerged() && value == field[x - 1][y].getValue()) {
				return true;
			} else {
				return false;
			}
		case DOWN:
			if (y == height - 1) {
				return false;
			}
			if (!field[x][y + 1].getMerged() && value == field[x][y + 1].getValue()) {
				return true;
			} else {
				return false;
			}
		case UP:
			if (y == 0) {
				return false;
			}
			if (!field[x][y - 1].getMerged() && value == field[x][y - 1].getValue()) {
				return true;
			} else {
				return false;
			}
		default:
			throw new IllegalArgumentException("Such direction does'not exist");
		}
	}

	@Override
	public String toString() {
		return "NumberGame [score=" + score + ", width=" + width + ", height=" + height + "]\n" + fieldPrint();
	}
	
	public void unMarkTiles() {
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				if (field[x][y] != null) {
					field[x][y].unmarkMerged();
				}
			}
		}
	}

	public String fieldPrint() {
		String fieldString = "";

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				if (field[x][y] == null) {
					fieldString += ". ";
				} else {
					fieldString += field[x][y].getValue() + " ";
				}
			}
			fieldString += "\n";
		}

		return fieldString;
	}
}
