import java.util.ArrayList;
import java.util.ListIterator;

public class Provider {
	public String id;
	private Route route;
	private double reach;
	
	public Provider(String id, int home_x, int home_y, int reach) {
		this.id = id;
		route = new Route();
		route.addWaypoint(home_x, home_y);
		this.reach = reach;
	}
	
	public void setReach(double r) {
		reach = r;
	}
	
	public Route getRoute() {
		return route;
	}
	
	public ArrayList<Offer> getOffer(WaypointSet stops) {
		double ownLength = route.length();
		ArrayList<Offer> offers = new ArrayList<Offer>();
		Route tmpOffer;
		
		for(Waypoint p : stops) {
			for(ListIterator<Offer> it = offers.listIterator(); it.hasNext();) {
				Offer o = it.next();
				tmpOffer = o.route.insertWaypoint(p);
				if(tmpOffer.length() <= reach)
					it.add(new Offer(id, tmpOffer.length()-ownLength, tmpOffer, o.usedStops.addWaypointAndClone(p)));				
			}
			tmpOffer = route.insertWaypoint(p);
			if(tmpOffer.length() <= reach)
				offers.add(new Offer(id, tmpOffer.length()-ownLength, tmpOffer, new WaypointSet(p)));
		}
		
		return offers;
	}
}
