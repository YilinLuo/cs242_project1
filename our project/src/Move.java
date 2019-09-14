import java.awt.Point;

public class Move {

	private Point start;
	private Point end;

	public Move(int sRow, int sCol, int eRow, int eCol) {
		start = new Point(sRow, sCol);
		end = new Point(eRow, eCol);
	}

	public Move(Point start, Point end) {
		this.start = start;
		this.end = end;
	}

	public Point getStart() {
		return start;
	}

	public Point getEnd() {
		return end;
	}

	public String toString() {
		return "Start: " + start.x + ", " + start.y + " End: " + end.x + ", " + end.y;
	}

//	public boolean equals(Object m) {
//		if (!(m instanceof Move))
//			return false;
//		Move x = (Move) m;
//		if (this.getStart().equals(x.getStart()) && this.getEnd().equals(x.getEnd()))
//			return true;
//
//		return false;
//	}
}
