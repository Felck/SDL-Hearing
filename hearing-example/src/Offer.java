
public class Offer {
	String id;
	double price;
	Route route;
	WaypointSet usedStops;
	
	public Offer(String id, double price, Route route, WaypointSet usedStops) {
		this.id = id;
		this.price = price;
		this.route = route;
		this.usedStops = usedStops;
	}
}
