import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class RouteGeneratorDialog extends Dialog {
	private MainWindow mainWindow;
	private Provider provider;

	/**
	 * Create the shell.
	 * @param display
	 */
	public RouteGeneratorDialog(MainWindow mw) {
		super(mw.shell, SWT.DIALOG_TRIM);
		mainWindow = mw;
	}
	
	public Provider open() {
	    // Create the dialog window
	    Shell shell = new Shell(getParent(), getStyle());
	    createContents(shell);
	    shell.pack();
	    shell.open();
	    Display display = getParent().getDisplay();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch()) {
	        display.sleep();
	      }
	    }
	    return provider;
	  }

	/**
	 * Create contents of the shell.
	 */
	protected void createContents(final Shell shell) {
		shell.setText("Neue Route");
		shell.setLayout(new GridLayout(2, false));
		
		Label lblName = new Label(shell, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		Text txtName = new Text(shell, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblStopNumber = new Label(shell, SWT.NONE);
		lblStopNumber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStopNumber.setText("Anzahl an Stops:");
		
		Text txtStopNumber = new Text(shell, SWT.BORDER);
		txtStopNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		//TODO add input filter

		Button btnOK = new Button(shell, SWT.NONE);
		btnOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO
				provider = new Provider(txtName.getText(), 10, 10, 1000);
				int stopNumber = Integer.parseInt(txtStopNumber.getText());
				for(int i=1; i<stopNumber; i++) {
					provider.getRoute().addWaypoint(10+i*10, 10+i*i*10);
				}
				shell.close();
			}
		});
		btnOK.setText("OK");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
}
