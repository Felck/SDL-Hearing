import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class MainWindow {

	private final class CanvasPaintListener implements PaintListener {
		public void paintControl(PaintEvent e) {
			// providers
			e.gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			Point p1, p2;
			for (Provider party : providers) {
				p1 = party.getRoute().getPoint(0);
				p2 = party.getRoute().getPoint(party.getRoute().size() - 1);
				e.gc.drawOval(p1.x - 5, p1.y - 5, 10, 10);
				e.gc.drawLine(p1.x, p1.y, p2.x, p2.y);
				for (int i = 1; i < party.getRoute().size(); i++) {
					p1 = party.getRoute().getPoint(i - 1);
					p2 = party.getRoute().getPoint(i);
					e.gc.drawLine(p1.x, p1.y, p2.x, p2.y);
					e.gc.drawOval(p2.x - 3, p2.y - 3, 6, 6);
				}
			}
			// requestor
			e.gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			for(Point p : requestor.getStops()) {
				e.gc.drawOval(p.x - 3, p.y - 3, 6, 6);
			}
			// merged getRoute()s
			/*
			 * Provider party; if(btnRadioButton1.getSelection()) party = providers.get(0);
			 * else party = providers.get(1);
			 * e.gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
			 * e.gc.setLineStyle(SWT.LINE_DOT); getRoute() getRoute() =
			 * party.getRoute().merge(requestor.getStops()); p1 = getRoute().getPoint(0); p2 =
			 * getRoute().getPoint(getRoute().size()-1); e.gc.drawLine(p1.x, p1.y, p2.x, p2.y);
			 * for(int i=1; i<getRoute().size(); i++) { p1 = getRoute().getPoint(i-1); p2 =
			 * getRoute().getPoint(i); e.gc.drawLine(p1.x, p1.y, p2.x, p2.y); }
			 * e.gc.setLineStyle(SWT.LINE_SOLID);
			 */
		}
	}

	private final class CanvasMouseAdapter extends MouseAdapter {
		private final Canvas canvas;

		private CanvasMouseAdapter(Canvas canvas) {
			this.canvas = canvas;
		}

		public void mouseDown(MouseEvent e) {
			for (Provider party : providers) {
				for (Point point : party.getRoute()) {
					if (Utils.distance(point.x, point.y, e.x, e.y) < 10) {
						switch (e.stateMask) {
						case SWT.SHIFT:
							Point newPoint = new Point(0, 0);
							party.getRoute().addPoint(point, newPoint);
							selectionPoint = newPoint;
							break;
						case SWT.CTRL:
							party.getRoute().removePoint(point);
							break;
						default:
							selectionPoint = point;
							break;
						}
						return;
					}
				}
			}
			for (Point point : requestor.getStops()) {
				if (Utils.distance(point.x, point.y, e.x, e.y) < 10) {
					switch (e.stateMask) {
					case SWT.SHIFT:
						Point newPoint = new Point(0, 0);
						requestor.getStops().addPoint(newPoint);
						selectionPoint = newPoint;
						break;
					case SWT.CTRL:
						requestor.getStops().removePoint(point);
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
	private ArrayList<Provider> providers;
	private Requestor requestor;
	private Point selectionPoint = null;

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
		computeOffers();
		shell.open();
		shell.layout();
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
		shell.setSize(450, 300);
		shell.setText("SDL Hearing");
		shell.setLayout(new GridLayout(1, false));

		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.addMouseListener(new CanvasMouseAdapter(canvas));
		canvas.addPaintListener(new CanvasPaintListener());
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));

	}

	private void initParties() {
		providers = new ArrayList<Provider>();
		providers.add(new Provider(15, 30, 700));
		providers.get(0).getRoute().addPoint(150, 15);
		providers.get(0).getRoute().addPoint(200, 150);
		providers.get(0).getRoute().addPoint(70, 200);
		providers.get(0).getRoute().addPoint(10, 120);

		providers.add(new Provider(300, 20, 700));
		providers.get(1).getRoute().addPoint(330, 130);
		providers.get(1).getRoute().addPoint(230, 160);
		providers.get(1).getRoute().addPoint(180, 30);

		requestor = new Requestor(150, 140);
		requestor.getStops().addPoint(215, 145);
		requestor.getStops().addPoint(190, 190);
	}

	private void computeOffers() {
		providers.get(0).getOffer(requestor.getStops());
		providers.get(1).getOffer(requestor.getStops());
	}
}
