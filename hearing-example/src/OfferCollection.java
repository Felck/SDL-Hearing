import java.util.ArrayList;
import java.util.Collections;

public class OfferCollection {
	public ArrayList<Offer> offers = new ArrayList<Offer>();
	public int size;
	public double price;
	
	public OfferCollection(Offer o) {
		offers.add(o);
		size = o.usedStops.size();
		price = o.price;
	}
	
	@SuppressWarnings("unchecked")
	public OfferCollection(OfferCollection oc) {
		offers = (ArrayList<Offer>) oc.offers.clone();
		size = oc.size;
		price = oc.price;
	}
	
	public OfferCollection addOffer(Offer offer) {
		for(Offer o : this.offers) {
			if(o.id == offer.id || !Collections.disjoint(o.usedStops.stops, offer.usedStops.stops))
				return null;
		}
		OfferCollection oc = new OfferCollection(this);
		oc.offers.add(offer);
		oc.size += offer.usedStops.size();
		oc.price += offer.price;
		return oc;
	}
}
