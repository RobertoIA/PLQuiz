package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import es.ubu.inf.tfg.doc.Documento;
import es.ubu.inf.tfg.doc.DocumentoHTML;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllmanGenerador;

public class Main {

	private JFrame frame;
	private JPanel controlPanel;
	private JPanel previewPanel;
	private JScrollPane previewScroll;
	private JTextPane previewText;
	private JPanel addPanel;
	private JButton addButton;
	private JComboBox<String> addBox;
	private JPanel containerPanel;

	private List<JButton> genButtons;
	private List<JButton> resButtons;
	private List<JButton> delButtons;
	private List<JCheckBox> vacioCBs;
	private List<JTextField> expFields;
	private List<JSlider> simSliders;
	private List<JSlider> estSliders;

	private Documento preview;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialize();

		this.genButtons = new ArrayList<>();
		this.resButtons = new ArrayList<>();
		this.delButtons = new ArrayList<>();
		this.vacioCBs = new ArrayList<>();
		this.expFields = new ArrayList<>();
		this.simSliders = new ArrayList<>();
		this.estSliders = new ArrayList<>();

		this.preview = new DocumentoHTML();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 830, 679);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.controlPanel = new JPanel();
		this.frame.getContentPane().add(this.controlPanel, BorderLayout.WEST);
		this.controlPanel.setLayout(new BorderLayout(0, 0));

		this.containerPanel = new JPanel();
		this.controlPanel.add(this.containerPanel, BorderLayout.NORTH);
		this.containerPanel.setLayout(new BoxLayout(this.containerPanel,
				BoxLayout.Y_AXIS));

		this.addPanel = new JPanel();
		this.controlPanel.add(this.addPanel, BorderLayout.SOUTH);

		this.addButton = new JButton("+");
		this.addButton.addActionListener(new AddButtonActionListener());
		this.addPanel.add(this.addButton);

		this.addBox = new JComboBox<>();
		this.addBox.setModel(new DefaultComboBoxModel<>(new String[] {
				"Aho-Sethi-Ulman", "Thompson" }));
		this.addPanel.add(this.addBox);

		this.previewPanel = new JPanel();
		this.frame.getContentPane().add(this.previewPanel, BorderLayout.CENTER);
		this.previewPanel.setLayout(new BorderLayout(0, 0));

		this.previewScroll = new JScrollPane();
		this.previewScroll
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.previewPanel.add(this.previewScroll);

		this.previewText = new JTextPane();
		this.previewText.setEditable(false);
		this.previewText.setContentType("text/html");
		this.previewScroll.add(this.previewText);
		this.previewScroll.setViewportView(this.previewText);
	}

	private class AddButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JPanel asuPanel = new JPanel();
			containerPanel.add(asuPanel);
			GridBagLayout gbl_asuPanel = new GridBagLayout();
			gbl_asuPanel.columnWidths = new int[] { 100, 0 };
			gbl_asuPanel.rowHeights = new int[] { 30, 30, 30, 30, 0 };
			gbl_asuPanel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
			gbl_asuPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
					Double.MIN_VALUE };
			asuPanel.setLayout(gbl_asuPanel);

			JPanel asuExpPanel = new JPanel();
			GridBagConstraints gbc_asuExpPanel = new GridBagConstraints();
			gbc_asuExpPanel.anchor = GridBagConstraints.WEST;
			gbc_asuExpPanel.insets = new Insets(0, 0, 5, 0);
			gbc_asuExpPanel.gridx = 0;
			gbc_asuExpPanel.gridy = 0;
			asuPanel.add(asuExpPanel, gbc_asuExpPanel);

			JTextField expField = new JTextField();
			asuExpPanel.add(expField);
			expField.setColumns(30);

			JPanel asuButPanel = new JPanel();
			GridBagConstraints gbc_asuButPanel = new GridBagConstraints();
			gbc_asuButPanel.insets = new Insets(0, 0, 5, 0);
			gbc_asuButPanel.gridx = 0;
			gbc_asuButPanel.gridy = 1;
			asuPanel.add(asuButPanel, gbc_asuButPanel);
			asuButPanel.setLayout(new BoxLayout(asuButPanel, BoxLayout.X_AXIS));

			JButton asuGenButton = new JButton("Generar");
			asuGenButton.addActionListener(new AsuGenButtonActionListener());
			asuButPanel.add(asuGenButton);

			JButton asuResButton = new JButton("Resolver");
			asuResButton.addActionListener(new AsuResButtonActionListener());
			asuButPanel.add(asuResButton);

			JButton asuDelButton = new JButton("Borrar");
			asuButPanel.add(asuDelButton);

			JPanel asuVacioPanel = new JPanel();
			GridBagConstraints gbc_asuVacioPanel = new GridBagConstraints();
			gbc_asuVacioPanel.anchor = GridBagConstraints.WEST;
			gbc_asuVacioPanel.insets = new Insets(0, 0, 5, 0);
			gbc_asuVacioPanel.gridx = 0;
			gbc_asuVacioPanel.gridy = 2;
			asuPanel.add(asuVacioPanel, gbc_asuVacioPanel);
			asuVacioPanel.setLayout(new BorderLayout(0, 0));

			JCheckBox asuVacioCB = new JCheckBox("Vacio?");
			asuVacioCB.setSelected(true);
			asuVacioCB.setHorizontalAlignment(SwingConstants.CENTER);
			asuVacioPanel.add(asuVacioCB);

			JPanel asuSimPanel = new JPanel();
			GridBagConstraints gbc_asuSimPanel = new GridBagConstraints();
			gbc_asuSimPanel.anchor = GridBagConstraints.WEST;
			gbc_asuSimPanel.insets = new Insets(0, 0, 5, 0);
			gbc_asuSimPanel.gridx = 0;
			gbc_asuSimPanel.gridy = 3;
			asuPanel.add(asuSimPanel, gbc_asuSimPanel);

			JLabel asuSimLabel = new JLabel("Simbolos");
			asuSimPanel.add(asuSimLabel);

			JSlider asuSimSlider = new JSlider();
			asuSimSlider.setMaximum(10);
			asuSimSlider.setMinimum(1);
			asuSimSlider.setValue(3);
			asuSimSlider.setSnapToTicks(true);
			asuSimPanel.add(asuSimSlider);

			JPanel asuEstPanel = new JPanel();
			GridBagConstraints gbc_asuEstPanel = new GridBagConstraints();
			gbc_asuEstPanel.anchor = GridBagConstraints.WEST;
			gbc_asuEstPanel.gridx = 0;
			gbc_asuEstPanel.gridy = 4;
			asuPanel.add(asuEstPanel, gbc_asuEstPanel);

			JLabel asuEstLabel = new JLabel("Estados");
			asuEstPanel.add(asuEstLabel);

			JSlider asuEstSlider = new JSlider();
			asuEstSlider.setMaximum(21);
			asuEstSlider.setMinimum(3);
			asuEstSlider.setValue(5);
			asuEstSlider.setSnapToTicks(true);
			asuEstPanel.add(asuEstSlider);

			genButtons.add(asuGenButton);
			resButtons.add(asuResButton);
			delButtons.add(asuDelButton);
			vacioCBs.add(asuVacioCB);
			expFields.add(expField);
			simSliders.add(asuSimSlider);
			estSliders.add(asuEstSlider);

			containerPanel.revalidate();
		}
	}

	private class AsuGenButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JButton button = (JButton) arg0.getSource();
			int index = genButtons.indexOf(button);

			JSlider simSlider = simSliders.get(index);
			JSlider estSlider = estSliders.get(index);
			JCheckBox vacioCB = vacioCBs.get(index);

			AhoSethiUllman problema = AhoSethiUllmanGenerador.getInstance()
					.nuevo(simSlider.getValue(), estSlider.getValue(),
							vacioCB.isSelected());
			expFields.get(index).setText(problema.problema());
		}
	}

	private class AsuResButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JButton button = (JButton) arg0.getSource();
			int index = resButtons.indexOf(button);

			if (expFields.get(index).getText().length() > 0) {
				String expresion = expFields.get(index).getText();

				AhoSethiUllman problema = new AhoSethiUllman(expresion);
				preview.añadirProblema(problema);

				previewText.setText(preview.toString());
			}
		}
	}
}
