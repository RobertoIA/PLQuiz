package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.doc.Problema;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllmanGenerador;

public class AhoSethiUllmanPanel extends ProblemaPanel<AhoSethiUllman> {

	private static final Logger log = LoggerFactory
			.getLogger(AhoSethiUllmanPanel.class);
	private static final long serialVersionUID = -8899275410326830826L;

	private boolean generando = false;
	private SwingWorker<AhoSethiUllman, Void> worker;

	private JPanel expresionPanel;
	private JTextField expresionText;
	private JPanel botonesPanel;
	private JButton generarButton;
	private JButton resolverButton;
	private JButton borrarButton;
	private JPanel opcionesPanel;
	private JPanel vacioPanel;
	private JCheckBox vacioCheck;
	private JPanel simbolosPanel;
	private JLabel simbolosLabel;
	private JPanel estadosPanel;
	private JLabel estadosLabel;
	private JPanel progresoPanel;
	private JProgressBar progresoBar;
	private JPanel modoPanelA;
	private final ButtonGroup modoGroup = new ButtonGroup();
	private JPanel modoPanelB;
	
	public AhoSethiUllmanPanel(Main main, JPanel contenedor, int numero) {

		this.main = main;
		this.contenedorPanel = contenedor;
		this.numero = numero;

		inicializaPanel("Aho-Sethi-Ullman");

		this.modoA = new JRadioButton("Construcci\u00F3n de \u00E1rbol");
		this.modoA.addActionListener(new ModoButtonChangeListener());
		this.modoA.setSelected(true);
		modoGroup.add(this.modoA);

		this.modoC = new JRadioButton("Etiquetado de \u00E1rbol");
		this.modoC.addActionListener(new ModoButtonChangeListener());
		modoGroup.add(this.modoC);

		this.modoB = new JRadioButton("Construcci\u00F3n de tablas");
		this.modoB.addActionListener(new ModoButtonChangeListener());
		modoGroup.add(this.modoB);

		this.simbolosSlider = new JSlider();
		this.simbolosSlider.setValue(3);
		this.simbolosSlider.setMaximum(6);
		this.simbolosSlider.setMinimum(2);
		this.simbolosSlider.addChangeListener(new SliderChangeListener());

		this.simbolosEstadoLabel = new JLabel("3");

		this.estadosSlider = new JSlider();
		this.estadosSlider.setValue(5);
		this.estadosSlider.setMinimum(3);
		this.estadosSlider.setMaximum(15);
		this.estadosSlider.addChangeListener(new SliderChangeListener());

		this.estadosEstadoLabel = new JLabel("5");

		this.expresionPanel = new JPanel();
		this.mainPanel.add(this.expresionPanel);
		this.expresionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		this.borrarButton = new JButton("-");
		this.borrarButton.addActionListener(new BotonBorrarActionListener());
		this.expresionPanel.add(this.borrarButton);

		this.expresionText = new JTextField();
		this.expresionText.addActionListener(new BotonResolverActionListener());
		this.expresionPanel.add(this.expresionText);
		this.expresionText.setColumns(25);

		this.modoPanelA = new JPanel();
		this.mainPanel.add(this.modoPanelA);
		this.modoPanelA.add(this.modoA);
		this.modoPanelA.add(this.modoC);

		this.modoPanelB = new JPanel();
		this.mainPanel.add(this.modoPanelB);
		this.modoPanelB.add(this.modoB);

		this.botonesPanel = new JPanel();
		this.mainPanel.add(this.botonesPanel);

		this.generarButton = new JButton("Generar");
		this.generarButton.addActionListener(new BotonGenerarActionListener());
		this.botonesPanel.add(this.generarButton);

		this.resolverButton = new JButton("Resolver");
		this.resolverButton
				.addActionListener(new BotonResolverActionListener());
		this.botonesPanel.add(this.resolverButton);

		this.opcionesPanel = new JPanel();
		this.mainPanel.add(this.opcionesPanel);
		this.opcionesPanel.setLayout(new BoxLayout(this.opcionesPanel,
				BoxLayout.Y_AXIS));

		this.vacioPanel = new JPanel();
		this.opcionesPanel.add(this.vacioPanel);

		this.vacioCheck = new JCheckBox("Incluir \u03B5");
		this.vacioPanel.add(this.vacioCheck);

		this.simbolosPanel = new JPanel();
		this.opcionesPanel.add(this.simbolosPanel);

		this.simbolosLabel = new JLabel("S\u00EDmbolos");
		this.simbolosPanel.add(this.simbolosLabel);
		this.simbolosPanel.add(this.simbolosSlider);
		this.simbolosPanel.add(this.simbolosEstadoLabel);

		this.estadosPanel = new JPanel();
		this.opcionesPanel.add(this.estadosPanel);

		this.estadosLabel = new JLabel("Estados");
		this.estadosPanel.add(this.estadosLabel);
		this.estadosPanel.add(this.estadosSlider);
		this.estadosPanel.add(this.estadosEstadoLabel);

		this.progresoPanel = new JPanel();
		this.opcionesPanel.add(this.progresoPanel);
		this.progresoPanel.setLayout(new BorderLayout(0, 0));

		this.progresoBar = new JProgressBar();
		this.progresoBar.setVisible(false);
		this.progresoBar.setIndeterminate(true);
		this.progresoPanel.add(this.progresoBar, BorderLayout.CENTER);
	}

	void problema(Problema<AhoSethiUllman> problema) {
		if (problemaActual != null) {
			if (!problema.getProblema().equals(problemaActual))
				main.eliminaImagen(problemaActual.getProblema().arbolVacio());
		}

		switch (problema.getTipo()) {
		case "AhoSethiUllmanConstruccion":
			for (BufferedImage imagen : problema.getProblema().alternativas())
				main.añadeImagen(imagen);
			modoA.setSelected(true);
			break;
		case "AhoSethiUllmanTablas":
			modoB.setSelected(true);
			break;
		case "AhoSethiUllmanEtiquetado":
			main.añadeImagen(problema.getProblema().arbolVacio());
			modoC.setSelected(true);
			break;
		default:
			log.error(
					"Error identificando tipo de problema Aho-Sethi-Ullman, definido como {}",
					problema.getTipo());
		}

		problemaActual = problema;
		expresionText.setText(problema.getProblema().problema());
	}

	private class BotonGenerarActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (!generando) {
				worker = new Worker();
				worker.execute();
			} else {
				((Worker) worker).cancel();
			}
		}
	}

	private class BotonResolverActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String expresion = expresionText.getText();

			try {
				if (expresion.length() > 0) {
					if (problemaActual != null) {
						if (!expresion.equals(problemaActual.getProblema()
								.problema())) {
							AhoSethiUllman problema = new AhoSethiUllman(
									expresion);
							Problema<AhoSethiUllman> asuProblema;

							if (modoB.isSelected())
								asuProblema = Problema.asuTablas(problema, numero);
							else if (modoC.isSelected())
								asuProblema = Problema.asuEtiquetado(problema, numero);
							else
								asuProblema = Problema
										.asuConstruccion(problema, numero);

							main.eliminaImagen(problemaActual.getProblema()
									.arbolVacio());
							for (BufferedImage imagen : problemaActual
									.getProblema().alternativas())
								main.eliminaImagen(imagen);
							main.añadeImagen(problema.arbolVacio());
							for (BufferedImage imagen : problema.alternativas())
								main.añadeImagen(imagen);
							problemaActual = asuProblema;
						}
					} else {
						AhoSethiUllman problema = new AhoSethiUllman(expresion);
						Problema<AhoSethiUllman> asuProblema;

						if (modoB.isSelected())
							asuProblema = Problema.asuTablas(problema, numero);
						else if (modoC.isSelected())
							asuProblema = Problema.asuEtiquetado(problema, numero);
						else
							asuProblema = Problema.asuConstruccion(problema, numero);

						main.añadeImagen(problema.arbolVacio());
						for (BufferedImage imagen : problema.alternativas())
							main.añadeImagen(imagen);
						problemaActual = asuProblema;
					}
					mostrarVista();
				}
			} catch (UnsupportedOperationException e) {
				JOptionPane
						.showMessageDialog(
								actualPanel,
								"Expresión regular no valida.",
								"Error", JOptionPane.ERROR_MESSAGE);
//				expresionText.setText("");
			}
		}
	}

	/**
	 * Implementa un SwingWorker cancelable encargado de generar problemas de
	 * tipo AhoSethiUllman de manera concurrente, y de actualizar la interfaz en
	 * consecuencia.
	 * 
	 * @author Roberto Izquierdo Amo.
	 *
	 */
	private class Worker extends SwingWorker<AhoSethiUllman, Void> {

		private AhoSethiUllmanGenerador generador;

		@Override
		protected AhoSethiUllman doInBackground() throws Exception {
			generando = true;
			generarButton.setText("Cancelar");
			generador = new AhoSethiUllmanGenerador();
			int nSimbolos = simbolosSlider.getValue();
			int nEstados = estadosSlider.getValue();
			boolean usaVacio = vacioCheck.isSelected();
			progresoBar.setVisible(true);

			AhoSethiUllman problema = generador.nuevo(nSimbolos, nEstados,
					usaVacio);
			return problema;
		}

		@Override
		public void done() {
			AhoSethiUllman problema = null;
			Problema<AhoSethiUllman> asuProblema = null;
			try {
				problema = get();
				if (modoB.isSelected())
					asuProblema = Problema.asuTablas(problema, numero);
				else if (modoC.isSelected())
					asuProblema = Problema.asuEtiquetado(problema, numero);
				else
					asuProblema = Problema.asuConstruccion(problema, numero);

				if (problemaActual != null) {
					main.eliminaImagen(problemaActual.getProblema()
							.arbolVacio());
					for (BufferedImage imagen : problemaActual.getProblema()
							.alternativas())
						main.eliminaImagen(imagen);
				}
				main.añadeImagen(asuProblema.getProblema().arbolVacio());
				for (BufferedImage imagen : asuProblema.getProblema()
						.alternativas())
					main.añadeImagen(imagen);

				problemaActual = asuProblema;
				expresionText.setText(problema.problema());
				mostrarVista();
			} catch (InterruptedException | ExecutionException
					| CancellationException e) {
				log.error("Error generando problema de tipo AhoSethiUllman", e);
			} finally {
				generando = false;
				generarButton.setText("Generar");
				progresoBar.setVisible(false);
			}
		}

		public void cancel() {
			log.info("Cancelando generación de problema AhoSethiUllman.");
			generador.cancelar();
		}
	}

	private class ModoButtonChangeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton modoButton = (JRadioButton) e.getSource();
			Problema<AhoSethiUllman> asuProblema = null;
			if(problemaActual != null) {
				AhoSethiUllman problema = problemaActual.getProblema();
	
				if (modoC == modoButton) {
					log.info("Seleccionado modo etiquetado en problema de Aho-Sethi-Ullman numero {}.", numero);
					if (problemaActual != null) {
						asuProblema = Problema.asuEtiquetado(problema, numero);
					}
				} else if (modoB == modoButton) {
					log.info("Seleccionado modo tablas en problema de Aho-Sethi-Ullman numero {}.", numero);
					if (problemaActual != null) {
						asuProblema = Problema.asuTablas(problema, numero);
					}
				} else {
					log.info("Seleccionado modo construcción en problema de Aho-Sethi-Ullman numero {}.", numero);
					if (problemaActual != null) {
						asuProblema = Problema.asuConstruccion(problema, numero);
					}
				}
				problemaActual = asuProblema;
			}
			mostrarVista();
		}
	}
}
