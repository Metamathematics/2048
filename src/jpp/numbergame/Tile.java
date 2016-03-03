package jpp.numbergame;

public class Tile {

	final private Coordinate2D coord;
	final private int value;
	private boolean merged = false;

	@Override
	public String toString() {
		return "Tile [" + coord + ", value=" + value + "]";
	}

	public Tile(Coordinate2D coord, int value) {
		if (value < 1)
			throw new IllegalArgumentException("The value c'ant be < 1");
		
		this.coord = coord;
		this.value = value;
	}
	
	public void doMerged() {
		merged = true;
	}
	
	public void unmarkMerged() {
		merged = false;
	}
	
	public boolean getMerged(){
		return merged;
	}
	
	public Coordinate2D getCoordinate() {
		return coord;
	}

	public int getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coord == null) ? 0 : coord.hashCode());
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (coord == null) {
			if (other.coord != null)
				return false;
		} else if (!coord.equals(other.coord))
			return false;
		if (value != other.value)
			return false;
		return true;
	}

}
