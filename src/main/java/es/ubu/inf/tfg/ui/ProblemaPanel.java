package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.ubu.inf.tfg.doc.Documento;
import es.ubu.inf.tfg.doc.Problema;

@SuppressWarnings("serial")
public class ProblemaPanel<T> extends JPanel {

	protected JSlider simbolosSlider;
	protected JSlider estadosSlider;
	protected JLabel estadosEstadoLabel;
	protected JLabel simbolosEstadoLabel;
	protected Main main;
	protected JPanel contenedorPanel;
	protected JPanel actualPanel = this;
	protected Documento documento;
	protected Problema<T> problemaActual = null;
	protected JRadioButton modoA;
	protected JRadioButton modoB;
	protected JRadioButton modoC;
	protected JPanel mainPanel;
	protected JPanel ordenPanel;
	protected JPanel mostrarPanel;
	protected JButton arribaButton;
	protected JButton abajoButton;
	protected JButton mostrarButton;

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

	void inicializaPanel(String titulo) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));

		this.ordenPanel = new JPanel();
		this.ordenPanel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		this.ordenPanel.setLayout(new BoxLayout(this.ordenPanel,
				BoxLayout.Y_AXIS));
		add(this.ordenPanel);

		this.arribaButton = new JButton("<<");
		this.arribaButton.setMargin(new Insets(0, 1, 0, 1));
		this.arribaButton.addActionListener(new BotonArribaActionListener());
		this.ordenPanel.add(this.arribaButton);

		this.abajoButton = new JButton(">>");
		this.abajoButton.setMargin(new Insets(0, 1, 0, 1));
		this.abajoButton.addActionListener(new BotonAbajoActionListener());
		this.ordenPanel.add(this.abajoButton);

		this.mainPanel = new JPanel();
		this.mainPanel.setBorder(new CompoundBorder(
				new EmptyBorder(5, 2, 15, 2), new TitledBorder(new LineBorder(
						new Color(0, 0, 0), 1, true), titulo,
						TitledBorder.LEADING, TitledBorder.TOP, null,
						new Color(51, 51, 51))));
		this.mainPanel
				.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
		add(this.mainPanel);

		this.mostrarPanel = new JPanel();
		this.mostrarPanel.setBorder(new EmptyBorder(new Insets(13, 0, 16, 0)));
		this.mostrarPanel.setLayout(new BorderLayout());
		add(this.mostrarPanel);

		this.mostrarButton = new JButton(">>");
		this.mostrarButton.setMargin(new Insets(0, 1, 0, 1));
		this.mostrarButton.addActionListener(new BotonMostrarActionListener());
		this.mostrarPanel.add(this.mostrarButton, BorderLayout.CENTER);
	}

	private class BotonArribaActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			main.moverProblemaArriba(ProblemaPanel.this);
		}
	}

	private class BotonAbajoActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			main.moverProblemaAbajo(ProblemaPanel.this);
		}
	}

	private class BotonMostrarActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			main.actualizaVistaPrevia(problemaActual);
			mainPanel.setBackground(new Color(255, 0, 0));
		}
	}

	class BotonBorrarActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (problemaActual != null) {
				documento.eliminarProblema(problemaActual);
				main.actualizaVistaPrevia();
			}

			contenedorPanel.remove(actualPanel);
			contenedorPanel.revalidate();
		}
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