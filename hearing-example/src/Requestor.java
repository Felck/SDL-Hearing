import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

public class Requestor {
	private WaypointSet stops = new WaypointSet();
	private ArrayList<Offer> offers = new ArrayList<Offer>();
	private int priceLimit = 1000; //TODO changeable
	
	public Requestor() {
	}

	public WaypointSet getStops() {
		return stops;
	}
	
	public void addOffers(ArrayList<Offer> offers) {
		this.offers.addAll(offers);
	}
	
	public void clearOffers() {
		offers.clear();
	}

	public ArrayList<Offer> getOffers() {
		return offers;
	}
	
	public OfferCollection getBestOffers() {
		ArrayList<OfferCollection> offerCollections = new ArrayList<OfferCollection>();
		OfferCollection tmpCollection;
		
		for(Offer o : offers) {
			for(ListIterator<OfferCollection> it = offerCollections.listIterator(); it.hasNext();) {
				OfferCollection oc = it.next();
				tmpCollection = oc.addOffer(o);
				if(tmpCollection != null && tmpCollection.price <= priceLimit)
					it.add(tmpCollection);
			}
			tmpCollection = new OfferCollection(o);
			if(o.price <= priceLimit)
				offerCollections.add(tmpCollection);
		}
		
		// offerCollections enthält alle möglichen Angebotskombinationen im Preisrahmen
		
		return offerCollections.stream().min((o1, o2) -> o1.size > o2.size ? -1 :
			o1.size == o2.size ? Double.compare(o1.price, o2.price) : 1).get();
	}
}
