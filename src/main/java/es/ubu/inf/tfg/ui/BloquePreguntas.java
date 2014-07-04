package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.doc.Problema;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllmanGenerador;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntosGenerador;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class BloquePreguntas extends JDialog {

	private static final Logger log = LoggerFactory
			.getLogger(BloquePreguntas.class);

	private static final Random random = new Random(new Date().getTime());

	private Main main;
	private boolean generando;
	private SwingWorker<List<Object>, Void> worker;

	private JPanel asuPanel;
	private JPanel csPanel;
	private JPanel controlesPanel;
	private JButton añadeButton;
	private JProgressBar progressBar;
	private JPanel asuNumPanel;
	private JLabel asuNumLabel;
	private JSpinner asuNumSpinner;
	private JPanel asuEstadosPanel;
	private JLabel asuEstadosLabel;
	private JSpinner asuEstadosSpinner;
	private JLabel asuEstadosVarLabel;
	private JSpinner asuEstadosVarSpinner;
	private JPanel asuSimbolosPanel;
	private JLabel asuSimbolosLabel;
	private JSpinner asuSimbolosSpinner;
	private JLabel asuSimbolosVarLabel;
	private JSpinner asuSimbolosVarSpinner;
	private JPanel csNumPanel;
	private JPanel csEstadosPanel;
	private JPanel csSimbolosPanel;
	private JLabel csNumLabel;
	private JSpinner csNumSpinner;
	private JLabel csEstadosLabel;
	private JLabel csSimbolosLabel;
	private JLabel csEstadosVarLabel;
	private JLabel csSimbolosVarLabel;
	private JSpinner csEstadosSpinner;
	private JSpinner csEstadosVarSpinner;
	private JSpinner csSimbolosSpinner;
	private JSpinner csSimbolosVarSpinner;
	private JPanel mainPanel;
	private Component asuNumStrut;
	private Component asuEstadosStrut;
	private Component asuSimbolosStrut;
	private Component csNumStrut;
	private Component csEstadosStrut;
	private Component csSimbolosStrut;
	private JPanel asuVacioPanel;
	private JCheckBox asuVacioCheck;
	private JPanel csVacioPanel;
	private JCheckBox csVacioCheck;
	private Component asuVacioGlue;
	private Component csVacioGlue;
	private Component controlesStrut;
	private JPanel asuModoPanelA;
	private JRadioButton csModoConstruccionButton;
	private JRadioButton csModoAutomataButton;
	private JRadioButton asuModoEtiquetadoButton;
	private JPanel csModoPanel;
	private JRadioButton csModoExpresionButton;
	private JRadioButton asuModoTablasButton;
	private final ButtonGroup asuModo = new ButtonGroup();
	private final ButtonGroup csModo = new ButtonGroup();
	private JRadioButton asuModoConstruccionButton;
	private JPanel asuModoPanelB;

	/**
	 * Create the dialog.
	 */
	public BloquePreguntas(Main main) {
		this.main = main;

		setTitle("Genera bloque de preguntas");
//		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 300, 472);
		getContentPane().setLayout(new BorderLayout(0, 0));

		this.mainPanel = new JPanel();
		this.mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.mainPanel);
		this.mainPanel
				.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));

		this.asuPanel = new JPanel();
		this.mainPanel.add(this.asuPanel);
		this.asuPanel.setBorder(new CompoundBorder(
				new EmptyBorder(5, 5, 15, 5), new TitledBorder(new LineBorder(
						new Color(0, 0, 0), 1, true), "Aho Sethi Ullman",
						TitledBorder.LEADING, TitledBorder.TOP, null,
						new Color(0, 0, 0))));
		this.asuPanel.setLayout(new BoxLayout(this.asuPanel, BoxLayout.Y_AXIS));

		this.asuNumPanel = new JPanel();
		this.asuNumPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.asuPanel.add(this.asuNumPanel);
		this.asuNumPanel.setLayout(new BoxLayout(this.asuNumPanel,
				BoxLayout.X_AXIS));

		this.asuNumLabel = new JLabel("Cantidad");
		this.asuNumLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.asuNumPanel.add(this.asuNumLabel);

		this.asuNumStrut = Box.createHorizontalStrut(25);
		this.asuNumPanel.add(this.asuNumStrut);

		this.asuNumSpinner = new JSpinner();
		this.asuNumSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		this.asuNumPanel.add(this.asuNumSpinner);

		this.asuModoPanelA = new JPanel();
		this.asuPanel.add(this.asuModoPanelA);
		this.asuModoPanelA.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		this.asuModoConstruccionButton = new JRadioButton(
				"Construcci\u00F3n de \u00E1rbol");
		asuModo.add(this.asuModoConstruccionButton);
		this.asuModoPanelA.add(this.asuModoConstruccionButton);

		this.asuModoEtiquetadoButton = new JRadioButton(
				"Etiquetado de \u00E1rbol");
		asuModo.add(this.asuModoEtiquetadoButton);
		this.asuModoPanelA.add(this.asuModoEtiquetadoButton);

		this.asuModoPanelB = new JPanel();
		this.asuPanel.add(this.asuModoPanelB);

		this.asuModoTablasButton = new JRadioButton(
				"Construcci\u00F3n de tablas");
		asuModo.add(this.asuModoTablasButton);
		this.asuModoPanelB.add(this.asuModoTablasButton);
		this.asuModoTablasButton.setSelected(true);

		this.asuVacioPanel = new JPanel();
		this.asuVacioPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
		this.asuPanel.add(this.asuVacioPanel);
		this.asuVacioPanel.setLayout(new BoxLayout(this.asuVacioPanel,
				BoxLayout.X_AXIS));

		this.asuVacioCheck = new JCheckBox("Incluir s\u00EDmbolo vac\u00EDo");
		this.asuVacioPanel.add(this.asuVacioCheck);

		this.asuVacioGlue = Box.createHorizontalGlue();
		this.asuVacioPanel.add(this.asuVacioGlue);

		this.asuEstadosPanel = new JPanel();
		this.asuEstadosPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
		this.asuPanel.add(this.asuEstadosPanel);
		this.asuEstadosPanel.setLayout(new BoxLayout(this.asuEstadosPanel,
				BoxLayout.X_AXIS));

		this.asuEstadosLabel = new JLabel("Estados");
		this.asuEstadosPanel.add(this.asuEstadosLabel);

		this.asuEstadosStrut = Box.createHorizontalStrut(30);
		this.asuEstadosPanel.add(this.asuEstadosStrut);

		this.asuEstadosSpinner = new JSpinner();
		this.asuEstadosSpinner.setModel(new SpinnerNumberModel(3, 3, 15, 1));
		this.asuEstadosPanel.add(this.asuEstadosSpinner);

		this.asuEstadosVarLabel = new JLabel(" +/- ");
		this.asuEstadosPanel.add(this.asuEstadosVarLabel);

		this.asuEstadosVarSpinner = new JSpinner();
		this.asuEstadosVarSpinner.setModel(new SpinnerNumberModel(0, 0, 4, 1));
		this.asuEstadosPanel.add(this.asuEstadosVarSpinner);

		this.asuSimbolosPanel = new JPanel();
		this.asuSimbolosPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
		this.asuPanel.add(this.asuSimbolosPanel);
		this.asuSimbolosPanel.setLayout(new BoxLayout(this.asuSimbolosPanel,
				BoxLayout.X_AXIS));

		this.asuSimbolosLabel = new JLabel("Simbolos");
		this.asuSimbolosPanel.add(this.asuSimbolosLabel);

		this.asuSimbolosStrut = Box.createHorizontalStrut(27);
		this.asuSimbolosPanel.add(this.asuSimbolosStrut);

		this.asuSimbolosSpinner = new JSpinner();
		this.asuSimbolosSpinner.setModel(new SpinnerNumberModel(5, 3, 6, 1));
		this.asuSimbolosPanel.add(this.asuSimbolosSpinner);

		this.asuSimbolosVarLabel = new JLabel(" +/- ");
		this.asuSimbolosPanel.add(this.asuSimbolosVarLabel);

		this.asuSimbolosVarSpinner = new JSpinner();
		this.asuSimbolosVarSpinner.setModel(new SpinnerNumberModel(0, 0, 4, 1));
		this.asuSimbolosPanel.add(this.asuSimbolosVarSpinner);

		this.csPanel = new JPanel();
		this.mainPanel.add(this.csPanel);
		this.csPanel.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 10, 5),
				new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),
						"McNaughton-Yamada-Thompson",
						TitledBorder.LEADING, TitledBorder.TOP, null,
						new Color(0, 0, 0))));
		this.csPanel.setLayout(new BoxLayout(this.csPanel, BoxLayout.Y_AXIS));

		this.csNumPanel = new JPanel();
		this.csNumPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.csPanel.add(this.csNumPanel);
		this.csNumPanel.setLayout(new BoxLayout(this.csNumPanel,
				BoxLayout.X_AXIS));

		this.csNumLabel = new JLabel("Cantidad");
		this.csNumPanel.add(this.csNumLabel);

		this.csNumStrut = Box.createHorizontalStrut(25);
		this.csNumPanel.add(this.csNumStrut);

		this.csNumSpinner = new JSpinner();
		this.csNumSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		this.csNumPanel.add(this.csNumSpinner);

		this.csModoPanel = new JPanel();
		this.csPanel.add(this.csModoPanel);

		this.csModoExpresionButton = new JRadioButton("Resolver expresi\u00F3n");
		this.csModoExpresionButton.setSelected(true);
		csModo.add(this.csModoExpresionButton);
		this.csModoPanel.add(this.csModoExpresionButton);

		this.csModoConstruccionButton = new JRadioButton(
				"Resolver aut\u00F3mata");
		csModo.add(this.csModoConstruccionButton);
		this.csModoPanel.add(this.csModoConstruccionButton);

		this.csModoAutomataButton = new JRadioButton("Resolver aut\u00F3mata");
		csModo.add(this.csModoAutomataButton);
		this.csModoPanel.add(this.csModoAutomataButton);

		this.csVacioPanel = new JPanel();
		this.csVacioPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
		this.csPanel.add(this.csVacioPanel);
		this.csVacioPanel.setLayout(new BoxLayout(this.csVacioPanel,
				BoxLayout.X_AXIS));

		this.csVacioCheck = new JCheckBox("Incluir s\u00EDmbolo vac\u00EDo");
		this.csVacioPanel.add(this.csVacioCheck);

		this.csVacioGlue = Box.createHorizontalGlue();
		this.csVacioPanel.add(this.csVacioGlue);

		this.csEstadosPanel = new JPanel();
		this.csEstadosPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
		this.csPanel.add(this.csEstadosPanel);
		this.csEstadosPanel.setLayout(new BoxLayout(this.csEstadosPanel,
				BoxLayout.X_AXIS));

		this.csEstadosLabel = new JLabel("Estados");
		this.csEstadosPanel.add(this.csEstadosLabel);

		this.csEstadosStrut = Box.createHorizontalStrut(30);
		this.csEstadosPanel.add(this.csEstadosStrut);

		this.csEstadosSpinner = new JSpinner();
		this.csEstadosSpinner.setModel(new SpinnerNumberModel(3, 3, 15, 1));
		this.csEstadosPanel.add(this.csEstadosSpinner);

		this.csEstadosVarLabel = new JLabel(" +/- ");
		this.csEstadosPanel.add(this.csEstadosVarLabel);

		this.csEstadosVarSpinner = new JSpinner();
		this.csEstadosVarSpinner.setModel(new SpinnerNumberModel(0, 0, 4, 1));
		this.csEstadosPanel.add(this.csEstadosVarSpinner);

		this.csSimbolosPanel = new JPanel();
		this.csSimbolosPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
		this.csPanel.add(this.csSimbolosPanel);
		this.csSimbolosPanel.setLayout(new BoxLayout(this.csSimbolosPanel,
				BoxLayout.X_AXIS));

		this.csSimbolosLabel = new JLabel("Simbolos");
		this.csSimbolosPanel.add(this.csSimbolosLabel);

		this.csSimbolosStrut = Box.createHorizontalStrut(27);
		this.csSimbolosPanel.add(this.csSimbolosStrut);

		this.csSimbolosSpinner = new JSpinner();
		this.csSimbolosSpinner.setModel(new SpinnerNumberModel(2, 2, 6, 1));
		this.csSimbolosPanel.add(this.csSimbolosSpinner);

		this.csSimbolosVarLabel = new JLabel(" +/- ");
		this.csSimbolosPanel.add(this.csSimbolosVarLabel);

		this.csSimbolosVarSpinner = new JSpinner();
		this.csSimbolosVarSpinner.setModel(new SpinnerNumberModel(0, 0, 4, 1));
		this.csSimbolosPanel.add(this.csSimbolosVarSpinner);

		this.controlesPanel = new JPanel();
		this.controlesPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
		this.mainPanel.add(this.controlesPanel);
		this.controlesPanel.setLayout(new BoxLayout(this.controlesPanel,
				BoxLayout.Y_AXIS));

		this.añadeButton = new JButton("Genera y a\u00F1ade problemas");
		this.añadeButton.addActionListener(new AñadeButtonActionListener());
		this.añadeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.controlesPanel.add(this.añadeButton);

		this.progressBar = new JProgressBar();
		this.progressBar.setVisible(false);

		this.controlesStrut = Box.createVerticalStrut(5);
		this.controlesPanel.add(this.controlesStrut);
		this.progressBar.setIndeterminate(true);
		this.controlesPanel.add(this.progressBar);

		setVisible(true);
	}

	private class AñadeButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!generando) {
				worker = new Worker();
				worker.execute();
			} else {
				((Worker) worker).cancel();
			}
		}
	}

	private class Worker extends SwingWorker<List<Object>, Void> {

		private AhoSethiUllmanGenerador asuGenerador;
		private ConstruccionSubconjuntosGenerador csGenerador;

		@Override
		protected List<Object> doInBackground() throws Exception {
			generando = true;
			añadeButton.setText("Cancelar");
			progressBar.setVisible(true);

			asuGenerador = new AhoSethiUllmanGenerador();
			csGenerador = new ConstruccionSubconjuntosGenerador();

			Integer asuNum = (Integer) asuNumSpinner.getValue();
			Integer asuEstados = (Integer) asuEstadosSpinner.getValue();
			Integer asuEstadosVar = (Integer) asuEstadosVarSpinner.getValue();
			Integer asuSimbolos = (Integer) asuSimbolosSpinner.getValue();
			Integer asuSimbolosVar = (Integer) asuSimbolosVarSpinner.getValue();

			Integer csNum = (Integer) csNumSpinner.getValue();
			Integer csEstados = (Integer) csEstadosSpinner.getValue();
			Integer csEstadosVar = (Integer) csEstadosVarSpinner.getValue();
			Integer csSimbolos = (Integer) csSimbolosSpinner.getValue();
			Integer csSimbolosVar = (Integer) csSimbolosVarSpinner.getValue();

			log.info(
					"Generando bloque de preguntas con {} de tipo Aho-Sethi-Ullman y {} de tipo Construcción de Subconjuntos",
					asuNum, csNum);
			log.info(
					"Problemas Aho-Sethi-Ullman con {} +/- {} estados y {} +/- {} simbolos",
					asuEstados, asuEstadosVar, asuSimbolos, asuSimbolosVar);
			log.info(
					"Problemas Construcción de subconjuntos con {} +/- {} estados y {} +/- {} simbolos",
					csEstados, csEstadosVar, csSimbolos, csSimbolosVar);

			List<Object> problemas = new ArrayList<>();
			AhoSethiUllman asuProblema;
			ConstruccionSubconjuntos csProblema;
			int simbolos, estados;
			boolean vacio = asuVacioCheck.isSelected();

			for (int i = 0; i < asuNum && generando; i++) {
				simbolos = asuSimbolos
						+ (random.nextInt((2 * asuSimbolosVar) + 1) - asuSimbolosVar);
				estados = asuEstados
						+ (random.nextInt((2 * asuEstadosVar) + 1) - asuEstadosVar);

				if (simbolos < 2)
					simbolos = 2;
				else if (simbolos > 6)
					simbolos = 6;
				if (estados < 3)
					estados = 3;
				else if (estados > 15)
					estados = 15;

				asuProblema = asuGenerador.nuevo(simbolos, estados, vacio);
				problemas.add(asuProblema);
			}

			vacio = csVacioCheck.isSelected();
			for (int i = 0; i < csNum && generando; i++) {
				simbolos = csSimbolos
						+ (random.nextInt((2 * csSimbolosVar) + 1) - csSimbolosVar);
				estados = csEstados
						+ (random.nextInt((2 * csEstadosVar) + 1) - csEstadosVar);

				if (simbolos < 2)
					simbolos = 2;
				else if (simbolos > 6)
					simbolos = 6;
				if (estados < 3)
					estados = 3;
				else if (estados > 15)
					estados = 15;

				csProblema = csGenerador.nuevo(simbolos, estados, vacio);
				problemas.add(csProblema);
			}

			return problemas;
		}

		@Override
		public void done() {
			try {
				List<Object> problemas = get();

				for (Object problema : problemas) {
					if (problema instanceof AhoSethiUllman) {
						Problema<AhoSethiUllman> p;
						if (asuModoTablasButton.isSelected())
							p = Problema.asuTablas((AhoSethiUllman) problema);
						else if (asuModoEtiquetadoButton.isSelected())
							p = Problema
									.asuEtiquetado((AhoSethiUllman) problema);
						else
							p = Problema
									.asuConstruccion((AhoSethiUllman) problema);
						main.añadeAhoSethiUllman(p);
					} else if (problema instanceof ConstruccionSubconjuntos) {
						Problema<ConstruccionSubconjuntos> p;
						if (csModoConstruccionButton.isSelected())
							p = Problema
									.CSConstruccion((ConstruccionSubconjuntos) problema);
						else if (csModoExpresionButton.isSelected())
							p = Problema
									.CSExpresion((ConstruccionSubconjuntos) problema);
						else
							p = Problema
									.CSAutomata((ConstruccionSubconjuntos) problema);
						main.añadeConstruccionSubconjuntos(p);
					} else
						log.error("Generado problema de tipo desconocido.");
				}

				main.actualizaVistaPrevia();

			} catch (InterruptedException | ExecutionException
					| CancellationException e) {
				log.error("Error generando bloque de problemas", e);
			} finally {
				generando = false;
				añadeButton.setText("Genera y añade problemas");
				progressBar.setVisible(false);
				dispose();
			}
		}

		public void cancel() {
			generando = false;
			asuGenerador.cancelar();
			csGenerador.cancelar();
		}
	}
}
