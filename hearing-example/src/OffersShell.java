import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class OffersShell extends Shell {
	private MainWindow mainWindow;
	private Requestor requestor;
	private Table table;

	/**
	 * Create the shell.
	 * @param display
	 */
	public OffersShell(Display display, MainWindow mw) {
		super(Display.getDefault(), SWT.TITLE|SWT.RESIZE|SWT.MIN);
		mainWindow = mw;
		requestor = mainWindow.getRequestor();
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Angebote");
		setSize(450, 300);
		setLocation(0, 0);
		setLayout(new GridLayout(1, false));
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!requestor.getOffers().isEmpty())
					mainWindow.setSelectedOffer(requestor.getOffers().get(table.getSelectionIndex()));
			}
		});
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnId = new TableColumn(table, SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("Anbieter");
		
		TableColumn tblclmnPrice = new TableColumn(table, SWT.NONE);
		tblclmnPrice.setWidth(100);
		tblclmnPrice.setText("Preis");
		
		TableColumn tblclmnRoute = new TableColumn(table, SWT.NONE);
		tblclmnRoute.setWidth(100);
		tblclmnRoute.setText("Benutzte Stops");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void updateOffers() {
		table.removeAll();
		OfferCollection bestOffers = requestor.getBestOffers();
		for(Offer offer : requestor.getOffers()) {
			TableItem t = new TableItem(table, SWT.NULL);
			t.setText(0, ""+offer.id);
			t.setText(1, ""+offer.price);
			t.setText(2, ""+offer.usedStops.stops);
			if(bestOffers.offers.contains(offer))
				t.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));;
		}
	}
}
