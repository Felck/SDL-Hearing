
public class Requestor {
	private WaypointSet stops;
	
	public Requestor(int home_x, int home_y) {
		stops = new WaypointSet();
		stops.addPoint(home_x, home_y);
	}

	public WaypointSet getStops() {
		return stops;
	}
}
