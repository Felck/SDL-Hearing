import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Point;

/**
 * unordered set of waypoints
 */
public class WaypointSet implements Iterable<Point> {
	// first element should be interpreted as home/warehouse
	protected ArrayList<Point> stops;

	public WaypointSet() {
		stops = new ArrayList<Point>();
	}
	
	public void addPoint(int x, int y) {
		stops.add(new Point(x, y));
	}

	public void addPoint(Point point) {
		stops.add(point);
	}

	public void removePoint(Point p) {
		if(stops.size() > 1)
			stops.remove(p);
	}
	
	public int size() {
		return stops.size();
	}

	@Override
	public Iterator<Point> iterator() {
		return stops.iterator();
	}
}
