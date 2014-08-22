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
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntosGenerador;

public class ConstruccionSubconjuntosPanel extends
		ProblemaPanel<ConstruccionSubconjuntos> {

	private static final Logger log = LoggerFactory
			.getLogger(ConstruccionSubconjuntosPanel.class);
	private static final long serialVersionUID = -1805230103073818602L;

	private boolean generando = false;
	private SwingWorker<ConstruccionSubconjuntos, Void> worker;

	private JPanel expresionPanel;
	private JButton borrarButton;
	private JTextField expresionText;
	private JPanel botonesPanel;
	private JButton resolverButton;
	private JButton generarButton;
	private JPanel opcionesPanel;
	private JPanel vacioPanel;
	private JPanel simbolosPanel;
	private JPanel estadosPanel;
	private JCheckBox vacioCheck;
	private JLabel simbolosLabel;
	private JLabel estadosLabel;
	private JPanel progresoPanel;
	private JProgressBar progresoBar;
	private JPanel modoPanelA;
	private final ButtonGroup modoGroup = new ButtonGroup();
	private JPanel modoPanelB;
	public ConstruccionSubconjuntosPanel(Main main, JPanel contenedor) {

		this.main = main;
		this.contenedorPanel = contenedor;

		inicializaPanel("McNaughton-Yamada-Thompson");

		this.modoA = new JRadioButton("Construir aut\u00F3mata");
		this.modoA.setSelected(true);
		modoGroup.add(this.modoA);
		this.modoA.addActionListener(new ModoButtonChangeListener());

		this.modoB = new JRadioButton("Resolver expresi\u00F3n");
		modoGroup.add(this.modoB);
		this.modoB.addActionListener(new ModoButtonChangeListener());

		this.modoC = new JRadioButton("Resolver aut\u00F3mata");
		modoGroup.add(this.modoC);
		this.modoC.addActionListener(new ModoButtonChangeListener());

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
		this.modoPanelA.add(this.modoB);

		this.modoPanelB = new JPanel();
		this.mainPanel.add(this.modoPanelB);
		this.modoPanelB.add(this.modoC);

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
		this.progresoPanel.add(this.progresoBar);
	}

	void problema(Problema<ConstruccionSubconjuntos> problema) {
		if (problemaActual != null) {
			if (!problema.getProblema().equals(problemaActual)) {
				main.eliminaImagen(problemaActual.getProblema().automata());
			}
		}

		switch (problema.getTipo()) {
		case "ConstruccionSubconjuntosConstruccion":
			modoA.setSelected(true);
			break;
		case "ConstruccionSubconjuntosExpresion":
			modoB.setSelected(true);
			break;
		case "ConstruccionSubconjuntosAutomata":
			main.añadeImagen(problema.getProblema().automata());
			modoC.setSelected(true);
			break;
		default:
			log.error(
					"Error identificando tipo de problema construcción de subconjuntos, definido como {}",
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
							ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
									expresion);
							Problema<ConstruccionSubconjuntos> csProblema;
							if (modoA.isSelected())
								csProblema = Problema.CSConstruccion(problema);
							else if (modoC.isSelected())
								csProblema = Problema.CSAutomata(problema);
							else
								csProblema = Problema.CSExpresion(problema);
							for (BufferedImage imagen : problemaActual
									.getProblema().alternativas())
								main.eliminaImagen(imagen);
							main.eliminaImagen(problemaActual.getProblema()
									.automata());
							for (BufferedImage imagen : problema.alternativas())
								main.añadeImagen(imagen);
							main.añadeImagen(problema.automata());
							problemaActual = csProblema;
						}
					} else {
						ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
								expresion);
						Problema<ConstruccionSubconjuntos> csProblema;
						if (modoA.isSelected())
							csProblema = Problema.CSConstruccion(problema);
						else if (modoC.isSelected())
							csProblema = Problema.CSAutomata(problema);
						else
							csProblema = Problema.CSExpresion(problema);
						main.añadeImagen(problema.automata());
						for (BufferedImage imagen : problema.alternativas())
							main.añadeImagen(imagen);
						problemaActual = csProblema;
					}
					mostrarVista();
				}
			} catch (UnsupportedOperationException e) {
				JOptionPane
						.showMessageDialog(
								actualPanel,
								"Expresión regular no valida, introduzca una más larga",
								"Error", JOptionPane.ERROR_MESSAGE);
				expresionText.setText("");
			}

		}
	}

	/**
	 * Implementa un SwingWorker cancelable encargado de generar problemas de
	 * tipo ConstruccionSubconjuntos de manera concurrente, y de actualizar la
	 * interfaz en consecuencia.
	 * 
	 * @author Roberto Izquierdo Amo.
	 *
	 */
	private class Worker extends SwingWorker<ConstruccionSubconjuntos, Void> {

		private ConstruccionSubconjuntosGenerador generador;

		@Override
		protected ConstruccionSubconjuntos doInBackground() throws Exception {
			generando = true;
			generarButton.setText("Cancelar");
			generador = new ConstruccionSubconjuntosGenerador();
			int nSimbolos = simbolosSlider.getValue();
			int nEstados = estadosSlider.getValue();
			boolean usaVacio = vacioCheck.isSelected();
			progresoBar.setVisible(true);

			ConstruccionSubconjuntos problema = generador.nuevo(nSimbolos,
					nEstados, usaVacio);
			return problema;
		}

		@Override
		public void done() {
			ConstruccionSubconjuntos problema = null;
			Problema<ConstruccionSubconjuntos> csProblema = null;
			try {
				problema = get();
				if (modoA.isSelected())
					csProblema = Problema.CSConstruccion(problema);
				else if (modoC.isSelected())
					csProblema = Problema.CSAutomata(problema);
				else
					csProblema = Problema.CSExpresion(problema);

				if (problemaActual != null) {
					main.eliminaImagen(problemaActual.getProblema().automata());
					for (BufferedImage imagen : problemaActual.getProblema()
							.alternativas())
						main.eliminaImagen(imagen);
				}
				main.añadeImagen(problema.automata());
				for (BufferedImage imagen : problema.alternativas())
					main.añadeImagen(imagen);

				problemaActual = csProblema;
				expresionText.setText(problema.problema());
				mostrarVista();
			} catch (InterruptedException | ExecutionException
					| CancellationException e) {
				log.error(
						"Error generando problema de tipo construcción de subconjuntos",
						e);
			} finally {
				generando = false;
				generarButton.setText("Generar");
				progresoBar.setVisible(false);
			}
		}

		public void cancel() {
			log.info("Cancelando generación de problema ConstruccionSubconjuntos.");
			generador.cancelar();
		}
	}

	private class ModoButtonChangeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton modoButton = (JRadioButton) e.getSource();
			ConstruccionSubconjuntos problema = problemaActual.getProblema();
			Problema<ConstruccionSubconjuntos> csProblema = null;

			if (modoA == modoButton) {
				log.info("Seleccionado modo construcción de autómata en problema de construcción de subconjuntos.");
				if (problemaActual != null) {
					csProblema = Problema.CSConstruccion(problema);
				}
			} else if (modoC == modoButton) {
				log.info("Seleccionado modo autómata en problema de construcción de subconjuntos.");
				if (problemaActual != null) {
					csProblema = Problema.CSAutomata(problema);
				}
			} else if (modoB == modoButton) {
				log.info("Seleccionado modo expresión en problema de construcción de subconjuntos.");
				if (problemaActual != null) {
					csProblema = Problema.CSExpresion(problema);
				}
			}
			problemaActual = csProblema;
			mostrarVista();
		}
	}
}
