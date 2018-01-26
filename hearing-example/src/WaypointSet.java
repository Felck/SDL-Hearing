import java.util.ArrayList;
import java.util.Iterator;

/**
 * unordered set of waypoints
 */
public class WaypointSet implements Iterable<Waypoint> {
	// first element should be interpreted as home/warehouse
	protected ArrayList<Waypoint> stops;

	public WaypointSet() {
		stops = new ArrayList<Waypoint>();
	}
	
	public WaypointSet(Waypoint p) {
		this();
		stops.add(p);
	}
	
	public WaypointSet(WaypointSet wps) {
		stops = (ArrayList<Waypoint>) wps.stops.clone();
	}
	
	public void addWaypoint(int x, int y) {
		stops.add(new Waypoint(x, y));
	}
	
	public void addWaypoint(int x, int y, int priority) {
		stops.add(new Waypoint(x, y, priority));
	}

	public void addWaypoint(Waypoint p) {
		stops.add(p);
	}
	
	public WaypointSet addWaypointAndClone(Waypoint p) {
		WaypointSet newWPS = new WaypointSet(this);
		newWPS.addWaypoint(p);
		return newWPS;
	}

	public void removeWaypoint(Waypoint p) {
		if(stops.size() > 1)
			stops.remove(p);
	}
	
	public int size() {
		return stops.size();
	}

	@Override
	public Iterator<Waypoint> iterator() {
		return stops.iterator();
	}
}
