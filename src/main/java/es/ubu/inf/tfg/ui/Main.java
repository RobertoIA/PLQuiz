package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.doc.Documento;
import es.ubu.inf.tfg.doc.Problema;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	private JFrame frmPlquiz;
	private JPanel controlPanel;
	private JPanel vistaPreviaPanel;
	private JScrollPane vistaPreviaScroll;
	private JTextPane vistaPreviaText;
	private JPanel añadirPanel;
	private JButton añadirButton;
	private JComboBox<String> añadirBox;
	private JPanel contenedorPanel;
	private Component añadirDerechoStrut;
	private Component añadirIzquierdoStrut;
	private JMenuBar menuBar;
	private JMenuItem menuNuevo;
	private JMenuItem menuBloque;
	private JMenuItem menuExportarMoodleXMLButton;
	private JMenuItem menuExportarLatexButton;
	private JMenu menuArchivo;

	private Main main = this;
	private JFileChooser fileChooser;
	private Documento documento;
	private JScrollPane contenedorScroll;
	private JPanel problemasPanel;

	public static void main(String[] args) {
		log.info("Aplicación iniciada");

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmPlquiz.setVisible(true);
				} catch (Exception e) {
					log.error("Error al iniciar la aplicación", e);
				}
			}
		});
	}

	public Main() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			log.error("Error estableciendo el look and feel", e);
		}
		initialize();
		this.documento = new Documento();
		this.vistaPreviaText.setText(documento.vistaPrevia());
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frmPlquiz = new JFrame();
		this.frmPlquiz.setTitle("PLQuiz");
		this.frmPlquiz.setBounds(100, 100, 1100, 900);
		this.frmPlquiz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.menuBar = new JMenuBar();
		this.frmPlquiz.getContentPane().add(this.menuBar, BorderLayout.NORTH);

		this.menuArchivo = new JMenu("Archivo");
		this.menuBar.add(this.menuArchivo);

		this.menuNuevo = new JMenuItem("Documento en blanco");
		this.menuNuevo.addActionListener(new MenuNuevoActionListener());
		this.menuArchivo.add(this.menuNuevo);

		this.menuBloque = new JMenuItem("Generar bloque de problemas");
		this.menuBloque.addActionListener(new MenuBloqueActionListener());
		this.menuArchivo.add(this.menuBloque);

		this.menuExportarMoodleXMLButton = new JMenuItem(
				"Exportar como Moodle XML");
		this.menuExportarMoodleXMLButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuArchivo.add(this.menuExportarMoodleXMLButton);

		this.menuExportarLatexButton = new JMenuItem("Exportar como LaTeX");
		this.menuExportarLatexButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuArchivo.add(this.menuExportarLatexButton);

		this.controlPanel = new JPanel();
		this.controlPanel.setBorder(null);
		this.frmPlquiz.getContentPane().add(this.controlPanel,
				BorderLayout.WEST);
		this.controlPanel.setLayout(new BorderLayout(0, 0));

		this.contenedorScroll = new JScrollPane();
		this.contenedorScroll
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.controlPanel.add(this.contenedorScroll, BorderLayout.CENTER);

		this.problemasPanel = new JPanel();
		this.problemasPanel.setBorder(null);
		this.contenedorScroll.add(this.problemasPanel);
		this.contenedorScroll.setViewportView(this.problemasPanel);

		this.contenedorPanel = new JPanel();
		this.contenedorPanel.setBorder(null);
		// this.controlPanel.add(this.contenedorPanel, BorderLayout.NORTH);
		this.contenedorPanel.setLayout(new BoxLayout(this.contenedorPanel,
				BoxLayout.Y_AXIS));
		this.problemasPanel.add(this.contenedorPanel);

		this.añadirPanel = new JPanel();
		this.controlPanel.add(this.añadirPanel, BorderLayout.SOUTH);

		this.añadirButton = new JButton("+");
		this.añadirButton.addActionListener(new AddButtonActionListener());

		this.añadirIzquierdoStrut = Box.createHorizontalStrut(100);
		this.añadirPanel.add(this.añadirIzquierdoStrut);
		this.añadirPanel.add(this.añadirButton);

		this.añadirBox = new JComboBox<>();
		this.añadirBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Aho-Sethi-Ullman", "Construcción de subconjuntos" }));
		this.añadirPanel.add(this.añadirBox);

		this.añadirDerechoStrut = Box.createHorizontalStrut(100);
		this.añadirPanel.add(this.añadirDerechoStrut);

		this.vistaPreviaPanel = new JPanel();
		this.frmPlquiz.getContentPane().add(this.vistaPreviaPanel,
				BorderLayout.CENTER);
		this.vistaPreviaPanel.setLayout(new BorderLayout(0, 0));

		this.vistaPreviaScroll = new JScrollPane();
		this.vistaPreviaScroll
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.vistaPreviaPanel.add(this.vistaPreviaScroll);

		this.vistaPreviaText = new JTextPane();
		this.vistaPreviaText.setEditable(false);
		this.vistaPreviaText.setContentType("text/html;charset=UTF-8");
		this.vistaPreviaScroll.add(this.vistaPreviaText);
		this.vistaPreviaScroll.setViewportView(this.vistaPreviaText);
	}

	void añadeAhoSethiUllman(Problema<AhoSethiUllman> problema) {
		AhoSethiUllmanPanel panel = new AhoSethiUllmanPanel(this,
				contenedorPanel, documento);

		if (problema != null)
			panel.problema(problema);

		contenedorPanel.add(panel);
		contenedorPanel.revalidate();
	}

	void añadeConstruccionSubconjuntos(Problema<ConstruccionSubconjuntos> problema) {
		ConstruccionSubconjuntosPanel panel = new ConstruccionSubconjuntosPanel(
				this, contenedorPanel, documento);

		if (problema != null) {
			panel.problema(problema);
		}

		contenedorPanel.add(panel);
		contenedorPanel.revalidate();
	}

	void actualizaVistaPrevia() {
		vistaPreviaText.setText(documento.vistaPrevia());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	void añadeImagen(BufferedImage imagen) {
		try {
			String url = "http:\\" + imagen.hashCode() + ".jpg";
			Dictionary cache = (Dictionary) vistaPreviaText.getDocument()
					.getProperty("imageCache");
			if (cache == null) {
				cache = new Hashtable();
				vistaPreviaText.getDocument().putProperty("imageCache", cache);
			}
			cache.put(new URL(url), imagen);
		} catch (Exception e) {
			log.error("Error al añadir imagen.", e);
		}
	}

	void eliminaImagen(BufferedImage imagen) {
		// TODO
	}

	private class AddButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (añadirBox.getSelectedItem().equals("Aho-Sethi-Ullman")) {
				log.info("Añadiendo problema tipo Aho-Sethi-Ullman");
				añadeAhoSethiUllman(null);
			} else if (añadirBox.getSelectedItem().equals(
					"Construcción de subconjuntos")) {
				log.info("Añadiendo problema tipo construcción de subconjuntos");
				añadeConstruccionSubconjuntos(null);
			}
		}
	}

	private class MenuExportarButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JMenuItem source = (JMenuItem) event.getSource();

			if (source == menuExportarMoodleXMLButton)
				fileChooser.setFileFilter(new XMLFilter());
			else if (source == menuExportarLatexButton)
				fileChooser.setFileFilter(new LatexFilter());

			int valorRetorno = fileChooser.showSaveDialog(frmPlquiz);
			if (valorRetorno == JFileChooser.APPROVE_OPTION) {
				File fichero = fileChooser.getSelectedFile();
				try {
					if (source == menuExportarMoodleXMLButton) {
						log.info("Exportando fichero XML a {}.", fichero);
						documento.exportaXML(fichero);
					} else if (source == menuExportarLatexButton) {
						log.info("Exportando fichero Latex a {}.", fichero);
						documento.exportaLatex(fichero);
					}
				} catch (IOException e) {
					log.error("Fallo al exportar fichero", e);
				}
			}
		}
	}

	private class MenuNuevoActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			log.info("Generando un documento nuevo.");
			documento = new Documento();
			actualizaVistaPrevia();
			contenedorPanel.removeAll();
		}
	}

	private class MenuBloqueActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			log.info("Generando un bloque de problemas.");
			new BloquePreguntas(main);
		}
	}

	private class XMLFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.getName().toLowerCase().endsWith(".xml")
					|| f.isDirectory();
		}

		@Override
		public String getDescription() {
			return "Ficheros Moodle XML (*.xml)";
		}
	}

	private class LatexFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.getName().toLowerCase().endsWith(".tex")
					|| f.isDirectory();
		}

		@Override
		public String getDescription() {
			return "Ficheros LaTeX (*.tex)";
		}
	}
}
