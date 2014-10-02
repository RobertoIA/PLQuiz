package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

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
import javax.swing.JOptionPane;
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
	private JMenuItem menuExportarGraphvizLatexButton;
	private JMenu menuArchivo;
	private JMenu menuExportar;
	private JMenu menuAyuda;
	private JMenuItem menuAcercaDe;
	private JMenuItem menuWeb;

	private Main main = this;
	private JFileChooser fileChooser;
	private JScrollPane contenedorScroll;
	private JPanel problemasPanel;
	private boolean scrollContenedor = true;
	private boolean scrollVistaPrevia = true;

	private List<ProblemaPanel<?>> panelesProblema = new ArrayList<>();

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
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			log.error("Error estableciendo el look and feel", e);
		}
		initialize();
		actualizaVistaPrevia(null);
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frmPlquiz = new JFrame();
		this.frmPlquiz.setTitle("PLQuiz");
		this.frmPlquiz.setBounds(100, 100, 1150, 900);
		this.frmPlquiz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initMenuBar();
		initControlPanel();
		initVistaPreviaPanel();
	}

	private void initMenuBar() {
		this.menuBar = new JMenuBar();
		this.frmPlquiz.getContentPane().add(this.menuBar, BorderLayout.NORTH);

		this.menuArchivo = new JMenu("Archivo");
		this.menuBar.add(this.menuArchivo);

		this.menuExportar = new JMenu("Exportar");
		this.menuBar.add(this.menuExportar);

		this.menuAyuda = new JMenu("Ayuda");
		this.menuBar.add(this.menuAyuda);

		this.menuWeb = new JMenuItem("Página web");
		this.menuWeb.addActionListener(new MenuWebActionListener());
		this.menuAyuda.add(this.menuWeb);

		this.menuAcercaDe = new JMenuItem("Acerca de");
		this.menuAcercaDe.addActionListener(new MenuAcercaDeActionListener());
		this.menuAyuda.add(this.menuAcercaDe);

		this.menuNuevo = new JMenuItem("Documento en blanco");
		this.menuNuevo.addActionListener(new MenuNuevoActionListener());
		this.menuArchivo.add(this.menuNuevo);

		this.menuBloque = new JMenuItem("Generar bloque de preguntas");
		this.menuBloque.addActionListener(new MenuBloqueActionListener());
		this.menuArchivo.add(this.menuBloque);

		this.menuExportarMoodleXMLButton = new JMenuItem(
				"Exportar como Moodle XML");
		this.menuExportarMoodleXMLButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuExportar.add(this.menuExportarMoodleXMLButton);

		this.menuExportarLatexButton = new JMenuItem("Exportar como LaTeX");
		this.menuExportarLatexButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuExportar.add(this.menuExportarLatexButton);

		this.menuExportarGraphvizLatexButton = new JMenuItem(
				"Exportar como LaTeX + Graphviz");
		this.menuExportarGraphvizLatexButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuExportar.add(this.menuExportarGraphvizLatexButton);
	}

	private void initControlPanel() {
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

		this.añadirIzquierdoStrut = Box.createHorizontalStrut(70);
		this.añadirPanel.add(this.añadirIzquierdoStrut);
		this.añadirPanel.add(this.añadirButton);

		this.añadirBox = new JComboBox<>();
		this.añadirBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Aho-Sethi-Ullman", "McNaughton-Yamada-Thompson" }));
		this.añadirPanel.add(this.añadirBox);

		this.añadirDerechoStrut = Box.createHorizontalStrut(70);
		this.añadirPanel.add(this.añadirDerechoStrut);
	}

	private void initVistaPreviaPanel() {
		this.vistaPreviaPanel = new JPanel();
		this.frmPlquiz.getContentPane().add(this.vistaPreviaPanel,
				BorderLayout.CENTER);
		this.vistaPreviaPanel.setLayout(new BorderLayout(0, 0));

		this.vistaPreviaScroll = new JScrollPane();
		this.vistaPreviaPanel.add(this.vistaPreviaScroll);

		this.vistaPreviaText = new JTextPane();
		this.vistaPreviaText.setEditable(false);
		this.vistaPreviaText.setContentType("text/html;charset=UTF-8");
		this.vistaPreviaScroll.add(this.vistaPreviaText);
		this.vistaPreviaScroll.setViewportView(this.vistaPreviaText);

		contenedorScroll.getVerticalScrollBar().addAdjustmentListener(
				new ScrollbarContenedorListener());
		vistaPreviaScroll.getVerticalScrollBar().addAdjustmentListener(
				new ScrollbarVistaPreviaListener());
	}

	void añadeAhoSethiUllman(Problema<AhoSethiUllman> problema) {
		AhoSethiUllmanPanel panel = new AhoSethiUllmanPanel(this,
				contenedorPanel, this.panelesProblema.size() + 1);

		scrollContenedor = false;
		if (problema != null)
			panel.problema(problema);

		contenedorPanel.add(panel);
		panelesProblema.add(panel);
		contenedorPanel.revalidate();
	}

	void añadeConstruccionSubconjuntos(
			Problema<ConstruccionSubconjuntos> problema) {
		ConstruccionSubconjuntosPanel panel = new ConstruccionSubconjuntosPanel(
				this, contenedorPanel, this.panelesProblema.size() + 1);

		scrollContenedor = false;
		if (problema != null)
			panel.problema(problema);

		contenedorPanel.add(panel);
		panelesProblema.add(panel);
		contenedorPanel.revalidate();
	}

	void actualizaVistaPrevia(Problema<?> problema) {
		scrollVistaPrevia = false;
		vistaPreviaText.setText(Documento.vistaPrevia(problema));
	}

	void moverProblemaArriba(ProblemaPanel<?> problema) {
		int index = this.panelesProblema.indexOf(problema);
		if (index > 0) {
			log.info("Moviendo problema {} hacia arriba", index);
			this.panelesProblema.remove(problema);
			this.panelesProblema.add(index - 1, problema);

			contenedorPanel.removeAll();
			int num = 1;
			for (ProblemaPanel<?> panel : this.panelesProblema) {
				contenedorPanel.add(panel);
				if (panel.problemaActual != null)
					panel.problemaActual.setNumero(num++);
			}
			contenedorPanel.revalidate();
			actualizaVistaPrevia(problema.problemaActual);
		}
	}

	void moverProblemaAbajo(ProblemaPanel<?> problema) {
		int index = this.panelesProblema.indexOf(problema);
		if (index < this.panelesProblema.size() - 1) {
			log.info("Moviendo problema {} hacia abajo", index);
			this.panelesProblema.remove(problema);
			this.panelesProblema.add(index + 1, problema);
			problema.problemaActual.setNumero(index + 2);

			contenedorPanel.removeAll();
			int num = 1;
			for (ProblemaPanel<?> panel : this.panelesProblema) {
				contenedorPanel.add(panel);
				if (panel.problemaActual != null)
					panel.problemaActual.setNumero(num++);
			}
			contenedorPanel.revalidate();
			actualizaVistaPrevia(problema.problemaActual);
		}
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

	@SuppressWarnings("rawtypes")
	void eliminaImagen(BufferedImage imagen) {
		try {
			String url = "http:\\" + imagen.hashCode() + ".jpg";
			Dictionary cache = (Dictionary) vistaPreviaText.getDocument()
					.getProperty("imageCache");
			if (cache != null) {
				log.debug("Eliminando imagen {}.", url);
				cache.remove(new URL(url));
			}
		} catch (Exception e) {
			log.error("Error al eliminar imagen.", e);
		}
	}
	
	void eliminaMarca() {
		for(ProblemaPanel<?> panel : this.panelesProblema)
			panel.eliminarVista();
	}

	private Documento documento() {
		Documento documento = new Documento();
		for (ProblemaPanel<?> panel : this.panelesProblema)
			documento.añadirProblema(panel.problemaActual);
		return documento;
	}

	private class AddButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (añadirBox.getSelectedItem().equals("Aho-Sethi-Ullman")) {
				log.info("Añadiendo problema tipo Aho-Sethi-Ullman");
				añadeAhoSethiUllman(null);
			} else if (añadirBox.getSelectedItem().equals(
					"McNaughton-Yamada-Thompson")) {
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
			else if (source == menuExportarGraphvizLatexButton)
				fileChooser.setFileFilter(new LatexFilter());

			int valorRetorno = fileChooser.showSaveDialog(frmPlquiz);
			if (valorRetorno == JFileChooser.APPROVE_OPTION) {
				File fichero = fileChooser.getSelectedFile();
				try {
					if (source == menuExportarMoodleXMLButton) {
						log.info("Exportando fichero XML a {}.", fichero);
						documento().exportaXML(fichero);
					} else if (source == menuExportarLatexButton) {
						log.info("Exportando fichero Latex a {}.", fichero);
						documento().exportaLatex(fichero);
					} else if (source == menuExportarGraphvizLatexButton) {
						log.info(
								"Exportando fichero Latex con imágenes en graphviz a {}.",
								fichero);
						documento().exportaGraphvizLatex(fichero);
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
			actualizaVistaPrevia(null);
			contenedorPanel.removeAll();
			panelesProblema.clear();
			contenedorPanel.revalidate();
		}
	}

	private class MenuWebActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			log.info("Mostrando página web.");
			try {
				Desktop.getDesktop().browse(
						new URI("http://robertoia.github.com/PLQuiz"));
			} catch (IOException | URISyntaxException e) {
				log.error("Error abriendo página web de la aplicación", e);
			}
		}
	}

	private class MenuAcercaDeActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			log.info("Mostrando acerca de.");
			JOptionPane.showMessageDialog(frmPlquiz, "PLQuiz\n"
					+ "TFG del Grado en Ingeniería Informática\n"
					+ "Escuela Politécnica Superior, Universidad de Burgos\n"
					+ "Presentado en Julio de 2014\n\n"
					+ "Autor: Roberto Izquierdo Amo\n"
					+ "Tutor: Dr. Cesar Ignacio García Osorio", "Acerca de",
					JOptionPane.PLAIN_MESSAGE);
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

	private class ScrollbarContenedorListener implements AdjustmentListener {
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!scrollContenedor)
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			scrollContenedor = true;
		}
	}

	private class ScrollbarVistaPreviaListener implements AdjustmentListener {
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!scrollVistaPrevia)
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			scrollVistaPrevia = true;
		}
	}
}
