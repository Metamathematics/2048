package jpp.numbergame;

public class Coordinate2D {


	final private int x;
	final private int y;

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	public Coordinate2D(int x, int y) {
		// Erstellt eine neue Koordinate mit den Komponenten x und y. Ist einer
		// der Werte kleiner als 0, wird eine IllegalArgumentException geworfen.
		if (x<0 || y<0) throw new IllegalArgumentException("The coordinates c'ant be < 0");
		
		this.x = x;
		this.y = y;
	}

	public int getX() {
		// Gibt die X-Komponente der Koordinate zurück.
		return x;
	}

	public int getY() {
		// Gibt die Y-Komponente der Koordinate zurück.
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Coordinate2D other = (Coordinate2D) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
