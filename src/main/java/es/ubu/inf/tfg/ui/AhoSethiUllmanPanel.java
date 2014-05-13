package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllmanGenerador;

public class AhoSethiUllmanPanel extends JPanel {

	private static final Logger log = LoggerFactory
			.getLogger(AhoSethiUllmanPanel.class);
	private static final long serialVersionUID = -8899275410326830826L;

	private final Main main;
	private final JPanel contenedorPanel;
	private final JPanel actualPanel = this;
	private final Documento documento;
	private Problema<AhoSethiUllman> problemaActual = null;
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
	private JSlider simbolosSlider;
	private JLabel simbolosEstadoLabel;
	private JPanel estadosPanel;
	private JLabel estadosLabel;
	private JSlider estadosSlider;
	private JLabel estadosEstadoLabel;
	private JPanel progresoPanel;
	private JProgressBar progresoBar;
	private JPanel modoPanel;
	private JRadioButton modoCompletoButton;
	private JRadioButton modoArbolButton;
	private final ButtonGroup modoGroup = new ButtonGroup();

	public AhoSethiUllmanPanel(Main main, JPanel contenedor, Documento documento) {

		this.main = main;
		this.contenedorPanel = contenedor;
		this.documento = documento;

		setBorder(new CompoundBorder(new EmptyBorder(5, 5, 15, 5),
				new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),
						"Aho-Sethi-Ullman", TitledBorder.LEADING,
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
		this.expresionText.setColumns(40);

		this.modoPanel = new JPanel();
		add(this.modoPanel);

		this.modoCompletoButton = new JRadioButton("Resolver completo");
		this.modoCompletoButton.setSelected(true);
		modoGroup.add(this.modoCompletoButton);
		this.modoPanel.add(this.modoCompletoButton);

		this.modoArbolButton = new JRadioButton("Completar \u00E1rbol");
		this.modoArbolButton.addItemListener(new ModoButtonChangeListener());
		modoGroup.add(this.modoArbolButton);
		this.modoPanel.add(this.modoArbolButton);

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

		this.vacioCheck = new JCheckBox("Incluir s\u00EDmbolo vac\u00EDo");
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
		this.progresoPanel.add(this.progresoBar, BorderLayout.CENTER);
	}

	void problema(AhoSethiUllman problema) {
		Problema<AhoSethiUllman> asuProblema = Problema.ASUCompleto(problema);
		if (problemaActual != null) {
			if (!problema.equals(problemaActual))
				documento.sustituirProblema(problemaActual, asuProblema);
		} else {
			documento.añadirProblema(asuProblema);
		}

		problemaActual = asuProblema;
		expresionText.setText(problema.problema());
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
						AhoSethiUllman problema = new AhoSethiUllman(expresion);
						Problema<AhoSethiUllman> asuProblema = modoCompletoButton
								.isSelected() ? Problema.ASUCompleto(problema)
								: Problema.ASUArbol(problema);
						documento
								.sustituirProblema(problemaActual, asuProblema);
						main.eliminaImagen(problemaActual.getProblema()
								.arbolVacio());
						main.añadeImagen(problema.arbolVacio());
						problemaActual = asuProblema;
					}
				} else {
					AhoSethiUllman problema = new AhoSethiUllman(expresion);
					Problema<AhoSethiUllman> asuProblema = modoCompletoButton
							.isSelected() ? Problema.ASUCompleto(problema)
							: Problema.ASUArbol(problema);
					documento.añadirProblema(asuProblema);
					main.añadeImagen(problema.arbolVacio());
					problemaActual = asuProblema;
				}
				main.actualizaVistaPrevia();
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
				asuProblema = modoCompletoButton.isSelected() ? Problema
						.ASUCompleto(problema) : Problema.ASUArbol(problema);

				if (problemaActual != null) {
					main.eliminaImagen(problemaActual.getProblema()
							.arbolVacio());
					main.añadeImagen(problema.arbolVacio());
					documento.sustituirProblema(problemaActual, asuProblema);
				} else {
					main.añadeImagen(problema.arbolVacio());
					documento.añadirProblema(asuProblema);
				}

				problemaActual = asuProblema;
				expresionText.setText(problema.problema());
				main.actualizaVistaPrevia();
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

	private class ModoButtonChangeListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				log.info("Seleccionado modo árbol en problema de Aho-Sethi-Ullman.");
				if (problemaActual != null) {
					AhoSethiUllman problema = problemaActual.getProblema();
					Problema<AhoSethiUllman> asuProblema = Problema
							.ASUArbol(problema);
					documento.sustituirProblema(problemaActual, asuProblema);
					problemaActual = asuProblema;
					main.actualizaVistaPrevia();
				}
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				log.info("Seleccionado modo completo en problema de Aho-Sethi-Ullman.");
				if (problemaActual != null) {
					AhoSethiUllman problema = problemaActual.getProblema();
					Problema<AhoSethiUllman> asuProblema = Problema
							.ASUCompleto(problema);
					documento.sustituirProblema(problemaActual, asuProblema);
					problemaActual = asuProblema;
					main.actualizaVistaPrevia();
				}
			}
		}
	}
}
