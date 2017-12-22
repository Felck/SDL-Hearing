import java.util.Comparator;
import java.util.stream.IntStream;

import org.eclipse.swt.graphics.Point;

/**
 * ordered list of waypoints
 */
public class Route extends WaypointSet {

	public Route() {
		super();
	}
	
	public Route(Route route) {
		this();
		for(Point p : route) {
			addPoint(p.x, p.y);
		}
	}
	
	public void addPoint(int index, int x, int y) {
		stops.add(index, new Point(x, y));
	}
	
	public void addPoint(Point pos, Point newPoint) {
		stops.add(stops.indexOf(pos)+1, newPoint);
	}
	
	public Route insertPoint(Point p) {
		Route newRoute = new Route(this);
		int x = p.x, y = p.y;
		if(newRoute.size() < 3)
			newRoute.addPoint(x, y);
		else {
			int nearestIndex = getNearest(x, y);
			int size = newRoute.size();
			int prevIndex = (nearestIndex - 1 + size) % size;
			int postIndex = (nearestIndex + 1 + size) % size;
			Point prev = newRoute.getPoint(prevIndex);
			Point post = newRoute.getPoint(postIndex);
			
			int insertIndex;
			if(Utils.distance(prev.x, prev.y, x, y) < Utils.distance(post.x, post.y, x, y))
				insertIndex = nearestIndex;
			else
				insertIndex = nearestIndex+1;
			
			newRoute.addPoint(insertIndex, x, y);
		}
		return newRoute;
	}
	
	/*public Route merge(Route route) {
		Route merged = new Route(this);
		for(Point p : route) {
			int x = p.x, y = p.y;
			if(merged.size() < 3)
				merged.addPoint(x, y);
			else {
				int nearestIndex = getNearest(x, y);
				int size = merged.size();
				int prevIndex = (nearestIndex - 1 + size) % size;
				int postIndex = (nearestIndex + 1 + size) % size;
				Point prev = merged.getPoint(prevIndex);
				Point post = merged.getPoint(postIndex);
				
				int insertIndex;
				if(Utils.distance(prev.x, prev.y, x, y) < Utils.distance(post.x, post.y, x, y))
					insertIndex = nearestIndex;
				else
					insertIndex = nearestIndex+1;
				
				merged.addPoint(insertIndex, x, y);
			}
		}
		return merged;
	}*/
	
	public int getNearest(int x, int y) {
		return IntStream.range(0,stops.size()).boxed()
	            .min(Comparator.comparing(i -> {Point p = stops.get(i);
	    				return Utils.distance(p.x, p.y, x, y);}))
	            .get();
	}
	
	public Point getPoint(int index) {
		return stops.get(index);
	}
	
	public double length() {
		double length = 0;
		for(int i=0; i<stops.size()-1; i++) {
			length += Utils.distance(stops.get(i), stops.get(i+1));
		}
		length += Utils.distance(stops.get(stops.size()-1), stops.get(0));
		
		return length;
	}
}
