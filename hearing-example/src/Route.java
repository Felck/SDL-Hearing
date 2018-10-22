import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.IntStream;

/**
 * ordered list of waypoints
 */
public class Route extends WaypointSet {

	public Route() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public Route(Route route) {
		super();
		stops = (ArrayList<Waypoint>) route.stops.clone();
	}
	
	public void addWaypoint(int index, Waypoint p) {
		stops.add(index, p);
	}
	
	public void addWaypoint(Waypoint pos, Waypoint newWaypoint) {
		stops.add(stops.indexOf(pos)+1, newWaypoint);
	}
	
	public Route insertWaypoint(Waypoint p) {
		Route newRoute = new Route(this);
		if(p.priority == -1) {
			newRoute.addWaypoint(getNearest(p.x, p.y)+1, p);	
		} else {
			int nearestIndex;
			for(Iterator<Integer> it = getNearestIterator(p.x, p.y); it.hasNext();) {
				nearestIndex = it.next();
				int minPrio = -1, maxPrio = -1, time = 0;
				for(int i=0; i<=nearestIndex; i++) {
					if(minPrio < newRoute.stops.get(i).priority) {
						minPrio = newRoute.stops.get(i).priority;
					}
					time += Utils.distance(newRoute.stops.get(i), newRoute.stops.get((i+1)%stops.size()));
				}
				time += Utils.distance(newRoute.stops.get(nearestIndex), p);
				for(int i=nearestIndex+1; i<newRoute.stops.size(); i++) {
					if(newRoute.stops.get(i).priority != -1) {
						maxPrio = newRoute.stops.get(i).priority;
						break;
					}
				}
				if((minPrio == -1 || minPrio <= p.priority) && (maxPrio == -1 || maxPrio >= p.priority) &&
						(p.minTime == -1 || p.minTime < time) && (p.maxTime == -1 || p.maxTime > time)) {
					newRoute.addWaypoint(nearestIndex+1, p);
					break;
				}
			}
		}
		return newRoute;
	}
	
	public int getNearest(int x, int y) {
		return IntStream.range(0,stops.size()).boxed()
	            .min(Comparator.comparing(i -> {
					Waypoint p1 = stops.get(i), p2 = stops.get((i+1)%stops.size());
					return Utils.distance(p1.x, p1.y, x, y) + Utils.distance(p2.x, p2.y, x, y);}))
	            .get();
	}
	
	public  Iterator<Integer> getNearestIterator(int x, int y) {
		return IntStream.range(0,stops.size()).boxed().sorted(Comparator.comparing(i -> {
						Waypoint p1 = stops.get(i), p2 = stops.get((i+1)%stops.size());
						return Utils.distance(p1.x, p1.y, x, y) + Utils.distance(p2.x, p2.y, x, y);
					})).iterator();
	}
	
	public Waypoint getWaypoint(int index) {
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
