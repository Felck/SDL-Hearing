import org.eclipse.swt.graphics.Point;

public class Utils {
	public static double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}
	
	public static double distance(Point p1, Point p2) {
		return distance(p1.x, p1.y, p2.x, p2.y);
	}
}
