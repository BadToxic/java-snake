
public class SnakePart {
	private int x, y;	// Koordinaten
	private SnakePart next;	// Weitere Koerperteile
	
	public SnakePart getNext() {
		return next;
	}

	public void setNext(SnakePart next) {
		this.next = next;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public SnakePart(int x, int y){
		this.x = x;
		this.y = y;
		this.next = null;
	}
	
}
