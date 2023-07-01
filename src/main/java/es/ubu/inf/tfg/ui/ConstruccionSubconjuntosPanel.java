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
	
	public ConstruccionSubconjuntosPanel(Main main, JPanel contenedor, int numero) {

		this.main = main;
		this.contenedorPanel = contenedor;
		this.numero = numero;

		inicializaPanel("McNaughton-Yamada-Thompson"); //$NON-NLS-1$

		this.modoA = new JRadioButton(Messages.getString("ConstruccionSubconjuntosPanel.RE2NFA")); //$NON-NLS-1$
		this.modoA.setSelected(true);
		modoGroup.add(this.modoA);
		this.modoA.addActionListener(new ModoButtonChangeListener());

		this.modoB = new JRadioButton(Messages.getString("ConstruccionSubconjuntosPanel.RE2DFA")); //$NON-NLS-1$
		modoGroup.add(this.modoB);
		this.modoB.addActionListener(new ModoButtonChangeListener());

		this.modoC = new JRadioButton(Messages.getString("ConstruccionSubconjuntosPanel.NFA2DFA")); //$NON-NLS-1$
		modoGroup.add(this.modoC);
		this.modoC.addActionListener(new ModoButtonChangeListener());

		this.simbolosSlider = new JSlider();
		this.simbolosSlider.setValue(3);
		this.simbolosSlider.setMaximum(6);
		this.simbolosSlider.setMinimum(2);
		this.simbolosSlider.addChangeListener(new SliderChangeListener());

		this.simbolosEstadoLabel = new JLabel("3"); //$NON-NLS-1$

		this.estadosSlider = new JSlider();
		this.estadosSlider.setValue(5);
		this.estadosSlider.setMinimum(3);
		this.estadosSlider.setMaximum(15);
		this.estadosSlider.addChangeListener(new SliderChangeListener());

		this.estadosEstadoLabel = new JLabel("5"); //$NON-NLS-1$

		this.expresionPanel = new JPanel();
		this.mainPanel.add(this.expresionPanel);
		this.expresionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		this.borrarButton = new JButton("➖");  // 🗑⌫␡☠× //$NON-NLS-1$
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

		this.generarButton = new JButton(Messages.getString("ConstruccionSubconjuntosPanel.generate")); //$NON-NLS-1$
		this.generarButton.addActionListener(new BotonGenerarActionListener());
		this.botonesPanel.add(this.generarButton);

		this.resolverButton = new JButton(Messages.getString("ConstruccionSubconjuntosPanel.solve")); //$NON-NLS-1$
		this.resolverButton
				.addActionListener(new BotonResolverActionListener());
		this.botonesPanel.add(this.resolverButton);

		this.opcionesPanel = new JPanel();
		this.mainPanel.add(this.opcionesPanel);
		this.opcionesPanel.setLayout(new BoxLayout(this.opcionesPanel,
				BoxLayout.Y_AXIS));

		this.vacioPanel = new JPanel();
		this.opcionesPanel.add(this.vacioPanel);

		this.vacioCheck = new JCheckBox(Messages.getString("ConstruccionSubconjuntosPanel.includeEpsilon")); //$NON-NLS-1$
		this.vacioPanel.add(this.vacioCheck);

		this.simbolosPanel = new JPanel();
		this.opcionesPanel.add(this.simbolosPanel);

		this.simbolosLabel = new JLabel(Messages.getString("ConstruccionSubconjuntosPanel.symbols")); //$NON-NLS-1$
		this.simbolosPanel.add(this.simbolosLabel);
		this.simbolosPanel.add(this.simbolosSlider);
		this.simbolosPanel.add(this.simbolosEstadoLabel);

		this.estadosPanel = new JPanel();
		this.opcionesPanel.add(this.estadosPanel);

		this.estadosLabel = new JLabel(Messages.getString("ConstruccionSubconjuntosPanel.state")); //$NON-NLS-1$
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

	@SuppressWarnings("unlikely-arg-type")
	void problema(Problema<ConstruccionSubconjuntos> problema) {
		if (problemaActual != null) {
			if (!problema.getProblema().equals(problemaActual)) {
				main.eliminaImagen(problemaActual.getProblema().automata());
			}
		}

		switch (problema.getTipo()) {
		case "ConstruccionSubconjuntosConstruccion": //$NON-NLS-1$
			modoA.setSelected(true);
			for (BufferedImage imagen : problema.getProblema().alternativas())
				main.añadeImagen(imagen);
			break;
		case "ConstruccionSubconjuntosExpresion": //$NON-NLS-1$
			modoB.setSelected(true);
			break;
		case "ConstruccionSubconjuntosAutomata": //$NON-NLS-1$
			main.añadeImagen(problema.getProblema().automata());
			modoC.setSelected(true);
			break;
		default:
			log.error(
					"Error identificando tipo de problema construcción de subconjuntos, definido como {}", //$NON-NLS-1$
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
								csProblema = Problema.CSConstruccion(problema, numero);
							else if (modoC.isSelected())
								csProblema = Problema.CSAutomata(problema, numero);
							else
								csProblema = Problema.CSExpresion(problema, numero);
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
							csProblema = Problema.CSConstruccion(problema, numero);
						else if (modoC.isSelected())
							csProblema = Problema.CSAutomata(problema, numero);
						else
							csProblema = Problema.CSExpresion(problema, numero);
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
								Messages.getString("ConstruccionSubconjuntosPanel.wrongRE"), //$NON-NLS-1$
								"Error", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
//				expresionText.setText("");
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
			generarButton.setText(Messages.getString("ConstruccionSubconjuntosPanel.cancel")); //$NON-NLS-1$
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
					csProblema = Problema.CSConstruccion(problema, numero);
				else if (modoC.isSelected())
					csProblema = Problema.CSAutomata(problema, numero);
				else
					csProblema = Problema.CSExpresion(problema, numero);

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
						"Error generando problema de tipo construcción de subconjuntos", //$NON-NLS-1$
						e);
			} finally {
				generando = false;
				generarButton.setText("Generar"); //$NON-NLS-1$
				progresoBar.setVisible(false);
			}
		}

		public void cancel() {
			log.info("Cancelando generación de problema ConstruccionSubconjuntos."); //$NON-NLS-1$
			generador.cancelar();
		}
	}

	private class ModoButtonChangeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton modoButton = (JRadioButton) e.getSource();
			Problema<ConstruccionSubconjuntos> csProblema = null;
			if(problemaActual != null) {
				ConstruccionSubconjuntos problema = problemaActual.getProblema();
	
				if (modoA == modoButton) {
					log.info("Seleccionado modo construcción de autómata en problema de construcción de subconjuntos numero {}.", numero); //$NON-NLS-1$
					csProblema = Problema.CSConstruccion(problema, numero);
				} else if (modoC == modoButton) {
					log.info("Seleccionado modo autómata en problema de construcción de subconjuntos numero {}.", numero); //$NON-NLS-1$
					csProblema = Problema.CSAutomata(problema, numero);
				} else if (modoB == modoButton) {
					log.info("Seleccionado modo expresión en problema de construcción de subconjuntos numero {}.", numero); //$NON-NLS-1$
					if (problemaActual != null) {
						csProblema = Problema.CSExpresion(problema, numero);
					}
				}
				problemaActual = csProblema;
			}
			mostrarVista();
		}
	}
}
