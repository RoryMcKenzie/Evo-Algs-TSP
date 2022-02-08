
public class Location {
	int idx;
	double x;
	double y;
	public Location(int idx, double x, double y) {
		super();
		this.idx = idx;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "" + idx + " " + x + " " + y;
	}

	//Overridden equals method so .indexOf() and .contains() work
	@Override
	public boolean equals(Object o){
		// If the object is compared with itself then return true
		if (o == this) {
			return true;
		}

		 /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
		if (!(o instanceof Location)) {
			return false;
		}

		Location l = (Location) o;

		// Compare the data members and return accordingly
		return idx == l.idx
				&& Double.compare(x, l.x) == 0
				&& Double.compare(y, l.y) == 0;
	}

	public Location copy() {
		Location location = new  Location(this.idx, this.x, this.y);
		return location;
	}
}
