import java.util.ArrayList;
import java.util.ListIterator;

import org.eclipse.swt.graphics.Point;

public class Provider {
	private Route route;
	private double reach;
	
	public Provider(int home_x, int home_y, int reach) {
		route = new Route();
		route.addPoint(home_x, home_y);
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
		
		for(Point p : stops) {
			for(ListIterator<Offer> it = offers.listIterator(); it.hasNext();) {
				Offer o = it.next();
				tmpOffer = o.route.insertPoint(p);
				if(tmpOffer.length() <= reach)
					it.add(new Offer(tmpOffer.length()-ownLength, tmpOffer));				
			}
			tmpOffer = route.insertPoint(p);
			if(tmpOffer.length() <= reach)
				offers.add(new Offer(tmpOffer.length()-ownLength, tmpOffer));
		}
		
		return offers;
	}
}
