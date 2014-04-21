package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllmanGenerador;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntosGenerador;

@SuppressWarnings("serial")
public class BloquePreguntas extends JDialog {

	private static final Logger log = LoggerFactory
			.getLogger(BloquePreguntas.class);

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

	/**
	 * Create the dialog.
	 */
	public BloquePreguntas(Main main) {
		this.main = main;

		setTitle("Genera bloque de preguntas");
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 301, 345);
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
		this.asuNumPanel.add(this.asuNumSpinner);

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
		this.asuEstadosPanel.add(this.asuEstadosSpinner);

		this.asuEstadosVarLabel = new JLabel(" +/- ");
		this.asuEstadosPanel.add(this.asuEstadosVarLabel);

		this.asuEstadosVarSpinner = new JSpinner();
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
		this.asuSimbolosPanel.add(this.asuSimbolosSpinner);

		this.asuSimbolosVarLabel = new JLabel(" +/- ");
		this.asuSimbolosPanel.add(this.asuSimbolosVarLabel);

		this.asuSimbolosVarSpinner = new JSpinner();
		this.asuSimbolosPanel.add(this.asuSimbolosVarSpinner);

		this.csPanel = new JPanel();
		this.mainPanel.add(this.csPanel);
		this.csPanel.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 10, 5),
				new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),
						"Construcci\u00F3n de subconjuntos",
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
		this.csNumPanel.add(this.csNumSpinner);

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
		this.csEstadosPanel.add(this.csEstadosSpinner);

		this.csEstadosVarLabel = new JLabel(" +/- ");
		this.csEstadosPanel.add(this.csEstadosVarLabel);

		this.csEstadosVarSpinner = new JSpinner();
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
		this.csSimbolosPanel.add(this.csSimbolosSpinner);

		this.csSimbolosVarLabel = new JLabel(" +/- ");
		this.csSimbolosPanel.add(this.csSimbolosVarLabel);

		this.csSimbolosVarSpinner = new JSpinner();
		this.csSimbolosPanel.add(this.csSimbolosVarSpinner);

		this.controlesPanel = new JPanel();
		this.mainPanel.add(this.controlesPanel);
		this.controlesPanel.setLayout(new BoxLayout(this.controlesPanel,
				BoxLayout.Y_AXIS));

		this.añadeButton = new JButton("Genera y a\u00F1ade problemas");
		this.añadeButton.addActionListener(new AñadeButtonActionListener());
		this.añadeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.controlesPanel.add(this.añadeButton);

		this.progressBar = new JProgressBar();
		this.progressBar.setVisible(false);
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
			
			for (int i = 0; i < asuNum; i++) {
				asuProblema = asuGenerador.nuevo(asuSimbolos, asuEstados, false); //TODO nunca usa vacio
				problemas.add(asuProblema);
			}

			for (int i = 0; i < csNum; i++) {
				csProblema = csGenerador.nuevo(csSimbolos, csEstados, false); //TODO nunca usa vacio
				problemas.add(csProblema);
			}

			return problemas;
		}

		@Override
		public void done() {			
			try {
				List<Object> problemas = get();
				
				for(Object problema : problemas) {
					if(problema instanceof AhoSethiUllman)
						main.añadeAhoSethiUllman((AhoSethiUllman) problema);
					else if(problema instanceof ConstruccionSubconjuntos)
						main.añadeConstruccionSubconjuntos((ConstruccionSubconjuntos) problema);
					else
						log.error("Generado problema de tipo desconocido.");
				}
				
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
			asuGenerador.cancelar();
			csGenerador.cancelar();
		}
	}
}
