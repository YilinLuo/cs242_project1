import java.awt.Point;

public class Move {

	private Point start;
	private Point end;

	public Move(int startRow, int startCol, int endRow, int endCol) {
		start = new Point(startRow, startCol);
		end = new Point(endRow, endCol);
	}

	public Move(Point start, Point end) {
		this.start = start;
		this.end = end;
	}

	public Point getStartPoint() {
		return start;
	}

	public Point getEndPoint() {
		return end;
	}

	public String toString() {
		return "Start location: " + start.x + ", " + start.y + " -> " + "End loaction: " + end.x + ", " + end.y;
	}

	public boolean equals(Object m) {
		if (!(m instanceof Move))
			return false;
		Move x = (Move) m;
		if (this.getStartPoint().equals(x.getStartPoint()) && this.getEndPoint().equals(x.getEndPoint()))
			return true;
		return false;
	}

}
