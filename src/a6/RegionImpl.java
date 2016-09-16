package a6;

public class RegionImpl implements Region {

	private Coordinate a;
	private Coordinate b;

	
	public RegionImpl(Coordinate a, Coordinate b) {
		this.a = a;
		this.b = b;
		
	}
	
	@Override
	public Coordinate getUpperLeft() {
		Coordinate upperLeft = new Coordinate (getLeft(), getTop()); 
		return upperLeft;
	}

	@Override
	public Coordinate getLowerRight() {
		Coordinate lowerRight = new Coordinate (getRight(), getBottom()); 
		return lowerRight;
	}

	@Override
	public int getTop() {
		return Math.min(a.getY(), b.getY());
	}

	@Override
	public int getBottom() {
		return Math.max(a.getY(), b.getY());
	}

	@Override
	public int getLeft() {
		return Math.min(a.getX(), b.getX());
	}

	@Override
	public int getRight() {
		return Math.max(a.getX(), b.getX());
	}

	@Override
	public Region intersect(Region other) throws NoIntersectionException {
		if (other == null) {throw new NoIntersectionException();}
		
		int newerRight = Math.min(this.getRight(), other.getRight());
		int newerBottom = Math.min(this.getBottom(), other.getBottom());
		int newerLeft = Math.max(this.getLeft(), other.getLeft());
		int newerTop = Math.max(this.getTop(), other.getTop());
		
		if (newerRight < newerLeft || newerBottom < newerTop) {
			throw new NoIntersectionException();
		}
	
	
		Coordinate newerUpperLeft = new Coordinate (newerLeft, newerTop);
		
		Coordinate newerBottomRight = new Coordinate (newerRight, newerBottom);
		
		return new RegionImpl(newerUpperLeft, newerBottomRight);
		
	}

	@Override
	public Region union(Region other) {
		
		if (other == null) {return this;}
		
		int newTop = Math.min(this.getTop(), other.getTop());
		int newBottom = Math.max(this.getBottom(), other.getBottom());
		int newLeft = Math.min(this.getLeft(), other.getLeft());
		int newRight = Math.max(this.getRight(), other.getRight());
		
		Coordinate newUpperLeft = new Coordinate (newLeft, newTop);
		Coordinate newBottomRight = new Coordinate (newRight, newBottom);
		
		return new RegionImpl(newUpperLeft, newBottomRight);
		
		
	}


}
