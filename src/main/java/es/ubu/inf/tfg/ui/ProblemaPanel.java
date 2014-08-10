package es.ubu.inf.tfg.ui;

import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ProblemaPanel extends JPanel {

	protected JSlider simbolosSlider;
	protected JSlider estadosSlider;
	protected JLabel estadosEstadoLabel;
	protected JLabel simbolosEstadoLabel;

	public ProblemaPanel() {
		super();
	}

	public ProblemaPanel(LayoutManager layout) {
		super(layout);
	}

	public ProblemaPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public ProblemaPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	class SliderChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			JSlider source = (JSlider) event.getSource();
			if (source == simbolosSlider)
				simbolosEstadoLabel.setText("" + simbolosSlider.getValue());
			else if (source == estadosSlider)
				estadosEstadoLabel.setText("" + estadosSlider.getValue());
		}
	}
}