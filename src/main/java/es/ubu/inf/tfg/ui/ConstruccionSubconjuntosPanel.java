package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.doc.Documento;
import es.ubu.inf.tfg.doc.Problema;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntosGenerador;

public class ConstruccionSubconjuntosPanel extends JPanel {

	private static final Logger log = LoggerFactory
			.getLogger(ConstruccionSubconjuntosPanel.class);
	private static final long serialVersionUID = -1805230103073818602L;

	private final Main main;
	private final JPanel contenedorPanel;
	private final JPanel actualPanel = this;
	private final Documento documento;
	private Problema<ConstruccionSubconjuntos> problemaActual = null;
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
	private JSlider simbolosSlider;
	private JLabel simbolosEstadoLabel;
	private JLabel estadosLabel;
	private JSlider estadosSlider;
	private JLabel estadosEstadoLabel;
	private JPanel progresoPanel;
	private JProgressBar progresoBar;
	private JPanel modoPanelA;
	private JRadioButton modoConstruccionButton;
	private JRadioButton modoExpresionButton;
	private JRadioButton modoAutomataButton;
	private final ButtonGroup modoGroup = new ButtonGroup();
	private JPanel modoPanelB;

	public ConstruccionSubconjuntosPanel(Main main, JPanel contenedor,
			Documento documento) {

		this.main = main;
		this.contenedorPanel = contenedor;
		this.documento = documento;

		setBorder(new CompoundBorder(new EmptyBorder(5, 5, 15, 5),
				new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),
						"McNaughton-Yamada-Thompson", TitledBorder.LEADING,
						TitledBorder.TOP, null, null)));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.expresionPanel = new JPanel();
		add(this.expresionPanel);
		this.expresionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		this.borrarButton = new JButton("-");
		this.borrarButton.addActionListener(new BotonBorrarActionListener());
		this.expresionPanel.add(this.borrarButton);

		this.expresionText = new JTextField();
		this.expresionText.addActionListener(new BotonResolverActionListener());
		this.expresionPanel.add(this.expresionText);
		this.expresionText.setColumns(25);

		this.modoPanelA = new JPanel();
		add(this.modoPanelA);

		this.modoConstruccionButton = new JRadioButton(
				"Construir aut\u00F3mata");
		this.modoConstruccionButton.setSelected(true);
		modoGroup.add(this.modoConstruccionButton);
		this.modoConstruccionButton
				.addActionListener(new ModoButtonChangeListener());
		this.modoPanelA.add(this.modoConstruccionButton);

		this.modoExpresionButton = new JRadioButton("Resolver expresi\u00F3n");
		modoGroup.add(this.modoExpresionButton);
		this.modoExpresionButton
				.addActionListener(new ModoButtonChangeListener());
		this.modoPanelA.add(this.modoExpresionButton);
		
		this.modoPanelB = new JPanel();
		add(this.modoPanelB);
		
				this.modoAutomataButton = new JRadioButton("Resolver aut\u00F3mata");
				this.modoPanelB.add(this.modoAutomataButton);
				modoGroup.add(this.modoAutomataButton);
				this.modoAutomataButton
						.addActionListener(new ModoButtonChangeListener());

		this.botonesPanel = new JPanel();
		add(this.botonesPanel);

		this.generarButton = new JButton("Generar");
		this.generarButton.addActionListener(new BotonGenerarActionListener());
		this.botonesPanel.add(this.generarButton);

		this.resolverButton = new JButton("Resolver");
		this.resolverButton
				.addActionListener(new BotonResolverActionListener());
		this.botonesPanel.add(this.resolverButton);

		this.opcionesPanel = new JPanel();
		add(this.opcionesPanel);
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

		this.simbolosSlider = new JSlider();
		this.simbolosSlider.setValue(3);
		this.simbolosSlider.setMaximum(6);
		this.simbolosSlider.setMinimum(2);
		this.simbolosSlider.addChangeListener(new SliderChangeListener());
		this.simbolosPanel.add(this.simbolosSlider);

		this.simbolosEstadoLabel = new JLabel("3");
		this.simbolosPanel.add(this.simbolosEstadoLabel);

		this.estadosPanel = new JPanel();
		this.opcionesPanel.add(this.estadosPanel);

		this.estadosLabel = new JLabel("Estados");
		this.estadosPanel.add(this.estadosLabel);

		this.estadosSlider = new JSlider();
		this.estadosSlider.setValue(5);
		this.estadosSlider.setMinimum(3);
		this.estadosSlider.setMaximum(15);
		this.estadosSlider.addChangeListener(new SliderChangeListener());
		this.estadosPanel.add(this.estadosSlider);

		this.estadosEstadoLabel = new JLabel("5");
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
				documento.sustituirProblema(problemaActual, problema);
			}
		} else {
			documento.añadirProblema(problema);
		}

		switch (problema.getTipo()) {
		case "ConstruccionSubconjuntosConstruccion":
			modoConstruccionButton.setSelected(true);
			break;
		case "ConstruccionSubconjuntosExpresion":
			modoExpresionButton.setSelected(true);
			break;
		case "ConstruccionSubconjuntosAutomata":
			main.añadeImagen(problema.getProblema().automata());
			modoAutomataButton.setSelected(true);
			break;
		default:
			log.error(
					"Error identificando tipo de problema construcción de subconjuntos, definido como {}",
					problema.getTipo());
		}

		problemaActual = problema;
		expresionText.setText(problema.getProblema().problema());
	}

	private class SliderChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			JSlider source = (JSlider) event.getSource();
			if (source == simbolosSlider)
				simbolosEstadoLabel.setText("" + simbolosSlider.getValue());
			else if (source == estadosSlider)
				estadosEstadoLabel.setText("" + estadosSlider.getValue());
		}
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

	private class BotonBorrarActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (problemaActual != null) {
				documento.eliminarProblema(problemaActual);
				main.actualizaVistaPrevia();
			}

			contenedorPanel.remove(actualPanel);
			contenedorPanel.revalidate();
		}
	}

	private class BotonResolverActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String expresion = expresionText.getText();

			if (expresion.length() > 0) {
				if (problemaActual != null) {
					if (!expresion.equals(problemaActual.getProblema()
							.problema())) {
						ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
								expresion);
						Problema<ConstruccionSubconjuntos> csProblema;
						if (modoConstruccionButton.isSelected())
							csProblema = Problema.CSConstruccion(problema);
						else if (modoAutomataButton.isSelected())
							csProblema = Problema.CSAutomata(problema);
						else
							csProblema = Problema.CSExpresion(problema);
						documento.sustituirProblema(problemaActual, csProblema);
						for(BufferedImage imagen : problemaActual.getProblema().alternativas())
							main.eliminaImagen(imagen);
						main.eliminaImagen(problemaActual.getProblema().automata());
						for(BufferedImage imagen : problema.alternativas())
							main.añadeImagen(imagen);
						main.añadeImagen(problema.automata());
						problemaActual = csProblema;
					}
				} else {
					ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
							expresion);
					Problema<ConstruccionSubconjuntos> csProblema;
					if (modoConstruccionButton.isSelected())
						csProblema = Problema.CSConstruccion(problema);
					else if (modoAutomataButton.isSelected())
						csProblema = Problema.CSAutomata(problema);
					else
						csProblema = Problema.CSExpresion(problema);
					documento.añadirProblema(csProblema);
					main.añadeImagen(problema.automata());
					for(BufferedImage imagen : problema.alternativas())
						main.añadeImagen(imagen);
					problemaActual = csProblema;
				}
				main.actualizaVistaPrevia();
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
				if (modoConstruccionButton.isSelected())
					csProblema = Problema.CSConstruccion(problema);
				else if (modoAutomataButton.isSelected())
					csProblema = Problema.CSAutomata(problema);
				else
					csProblema = Problema.CSExpresion(problema);

				if (problemaActual != null) {
					main.eliminaImagen(problemaActual.getProblema().automata());
					for(BufferedImage imagen : problemaActual.getProblema().alternativas())
						main.eliminaImagen(imagen);
					main.añadeImagen(problema.automata());
					for(BufferedImage imagen : problema.alternativas())
						main.añadeImagen(imagen);
					documento.sustituirProblema(problemaActual, csProblema);
				} else {
					main.añadeImagen(problema.automata());
					for(BufferedImage imagen : problema.alternativas())
						main.añadeImagen(imagen);
					documento.añadirProblema(csProblema);
				}

				problemaActual = csProblema;
				expresionText.setText(problema.problema());
				main.actualizaVistaPrevia();
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

			if (modoConstruccionButton == modoButton) {
				log.info("Seleccionado modo construcción de autómata en problema de construcción de subconjuntos.");
				if (problemaActual != null) {
					ConstruccionSubconjuntos problema = problemaActual
							.getProblema();
					Problema<ConstruccionSubconjuntos> csProblema = Problema
							.CSConstruccion(problema);
					documento.sustituirProblema(problemaActual, csProblema);
					problemaActual = csProblema;
					main.actualizaVistaPrevia();
				}
			} else if (modoAutomataButton == modoButton) {
				log.info("Seleccionado modo autómata en problema de construcción de subconjuntos.");
				if (problemaActual != null) {
					ConstruccionSubconjuntos problema = problemaActual
							.getProblema();
					Problema<ConstruccionSubconjuntos> csProblema = Problema
							.CSAutomata(problema);
					documento.sustituirProblema(problemaActual, csProblema);
					problemaActual = csProblema;
					main.actualizaVistaPrevia();
				}
			} else if (modoExpresionButton == modoButton) {
				log.info("Seleccionado modo expresión en problema de construcción de subconjuntos.");
				if (problemaActual != null) {
					ConstruccionSubconjuntos problema = problemaActual
							.getProblema();
					Problema<ConstruccionSubconjuntos> csProblema = Problema
							.CSExpresion(problema);
					documento.sustituirProblema(problemaActual, csProblema);
					problemaActual = csProblema;
					main.actualizaVistaPrevia();
				}
			}
		}
	}
}
