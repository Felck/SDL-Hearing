import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

public class MainWindow {

	private final class CanvasPaintListener implements PaintListener {
		public void paintControl(PaintEvent e) {
			// providers
			e.gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			Waypoint p1, p2;
			for (Provider party : providers) {
				p1 = party.getRoute().getWaypoint(0);
				p2 = party.getRoute().getWaypoint(party.getRoute().size() - 1);
				e.gc.drawOval(p1.x - 5, p1.y - 5, 10, 10);
				e.gc.drawLine(p1.x, p1.y, p2.x, p2.y);
				for (int i = 1; i < party.getRoute().size(); i++) {
					p1 = party.getRoute().getWaypoint(i - 1);
					p2 = party.getRoute().getWaypoint(i);
					e.gc.drawLine(p1.x, p1.y, p2.x, p2.y);
					e.gc.drawOval(p2.x - 3, p2.y - 3, 6, 6);
				}
			}
			// requestor
			e.gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			for(Waypoint p : requestor.getStops()) {
				e.gc.drawOval(p.x - 3, p.y - 3, 6, 6);
			}
			// offer
			if(selectedOffer != null) {
				e.gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
				e.gc.setLineStyle(SWT.LINE_DOT);
				Route route = selectedOffer.route;
				p1 = route.getWaypoint(0);
				p2 = route.getWaypoint(route.size()-1);
				e.gc.drawLine(p1.x, p1.y, p2.x, p2.y);
				for(int i=1; i<route.size(); i++) {
					p1 = route.getWaypoint(i-1);
					p2 = route.getWaypoint(i);
					e.gc.drawLine(p1.x, p1.y, p2.x, p2.y);
				}
				e.gc.setLineStyle(SWT.LINE_SOLID);
			}
		}
	}

	private final class CanvasMouseAdapter extends MouseAdapter {
		private final Canvas canvas;

		private CanvasMouseAdapter(Canvas canvas) {
			this.canvas = canvas;
		}

		public void mouseDown(MouseEvent e) {
			for (Provider party : providers) {
				for (Waypoint point : party.getRoute()) {
					if (Utils.distance(point.x, point.y, e.x, e.y) < 10) {
						switch (e.stateMask) {
						case SWT.SHIFT:
							Waypoint newPoint = new Waypoint(0, 0);
							party.getRoute().addWaypoint(point, newPoint);
							selectionPoint = newPoint;
							break;
						case SWT.CTRL:
							party.getRoute().removeWaypoint(point);
							break;
						default:
							selectionPoint = point;
							break;
						}
						return;
					}
				}
			}
			for (Waypoint point : requestor.getStops()) {
				if (Utils.distance(point.x, point.y, e.x, e.y) < 10) {
					switch (e.stateMask) {
					case SWT.SHIFT:
						Waypoint newPoint = new Waypoint(0, 0);
						requestor.getStops().addWaypoint(newPoint);
						selectionPoint = newPoint;
						break;
					case SWT.CTRL:
						requestor.getStops().removeWaypoint(point);
						break;
					default:
						selectionPoint = point;
						break;
					}
					return;
				}
			}
		}

		public void mouseUp(MouseEvent e) {
			if (selectionPoint != null) {
				selectionPoint.x = e.x;
				selectionPoint.y = e.y;
				selectionPoint = null;
			}
			canvas.redraw();
			computeOffers();
		}
	}

	protected Shell shell;
	private OffersShell offersShell;
	private ArrayList<Provider> providers;
	private Requestor requestor;
	private Waypoint selectionPoint = null;
	private Offer selectedOffer = null;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		initParties();
		shell.open();
		shell.layout();
		offersShell = new OffersShell(display, this);
		offersShell.setVisible(true);
		offersShell.layout();
		computeOffers();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				shell.dispose(); //nötig?
			}
		});
		shell.setSize(600, 400);
		shell.setText("SDL Hearing");
		shell.setLayout(new GridLayout(2, false));

		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.addMouseListener(new CanvasMouseAdapter(canvas));
		canvas.addPaintListener(new CanvasPaintListener());
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmRoute = new MenuItem(menu, SWT.CASCADE);
		mntmRoute.setText("Route");
		
		Menu menu_1 = new Menu(mntmRoute);
		mntmRoute.setMenu(menu_1);
		
		MenuItem mntmNewRoute = new MenuItem(menu_1, SWT.NONE);
		mntmNewRoute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RouteGeneratorDialog rgd = new RouteGeneratorDialog(MainWindow.this);
				providers.add(rgd.open());
				canvas.redraw();
				computeOffers();
			}
		});
		mntmNewRoute.setText("Neue Route");
		new Label(shell, SWT.NONE);
	}

	private void initParties() {
		providers = new ArrayList<Provider>();
		providers.add(new Provider("P1", 15, 30, 700));
		providers.get(0).getRoute().addWaypoint(150, 15);
		providers.get(0).getRoute().addWaypoint(200, 150);
		providers.get(0).getRoute().addWaypoint(70, 200);
		providers.get(0).getRoute().addWaypoint(10, 120);

		providers.add(new Provider("P2", 300, 20, 700));
		providers.get(1).getRoute().addWaypoint(330, 130);
		providers.get(1).getRoute().addWaypoint(230, 160);
		providers.get(1).getRoute().addWaypoint(180, 30);

		requestor = new Requestor();
		requestor.getStops().addWaypoint(150, 140, 3);
		requestor.getStops().addWaypoint(215, 145, 1);
		requestor.getStops().addWaypoint(190, 190, 2);
	}

	private void computeOffers() {
		selectedOffer = null;
		requestor.clearOffers();
		for(Provider provider : providers) {
			requestor.addOffers(provider.getOffer(requestor.getStops()));			
		}
		offersShell.updateOffers();
	}
	
	public void setSelectedOffer(Offer offer) {
		selectedOffer = offer;
		shell.redraw();
	}
	
	public Requestor getRequestor() {
		return requestor;
	}
}
