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
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.doc.Documento;
import es.ubu.inf.tfg.doc.Problema;
import es.ubu.inf.tfg.doc.datos.Plantilla;
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
	private JMenuItem menuASU;
	private JMenuItem menuCS;
	private JMenuItem menuExportarMoodleXMLButton;
	private JMenuItem menuExportarLatexButton;
	private JMenuItem menuExportarGraphvizLatexButton;
	private JMenuItem menuExportarSVGLatexButton; //JBA
	private JMenuItem menuExportarPDFLatexButton; //JBA
	private JMenuItem menuExportarTikZLatexButton; //JBA
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

	public static void printAll(ResourceBundle Messages) {
		String[] keys = {"Main.about", "Main.authorRobertoIzquierdoAmo", "Main.blankDocument",
				"Main.computingScienceFinalProject", "Main.defendedOnJuly2014", "Main.export", "Main.exportLaTeX",
				"Main.exportLaTeXandGraphviz", "Main.exportLaTeXandPDF", "Main.exportLaTeXandSVG", "Main.exportLaTeXandTikZ",
				"Main.exportMoodleXML", "Main.file", "Main.genQuestionsBlock", "Main.help", "Main.LaTeXFiles",
				"Main.moveProblemDown", "Main.moveProblemUp", "Main.newQuestionASU", "Main.newQuestionThompson",
				"Main.supervisorDrCegarIgnacioGarciaOsorio", "Main.webPage", "Main.XMLMoodleFiles", };
		System.out.println("\n\n---------------------------------------------------");
		System.out.println("Default locale: " + Locale.getDefault());
		
		for (String k: keys) {
			System.out.println(k + ": " + Messages.getString(k));		
		}		
		System.out.println("DONE!"); //$NON-NLS-1$
	}
	
	public static void test_I18N() {
		// This for Java 9
		// String bundle_name = Main.class.getPackageName() + ".messages"; //$NON-NLS-1$
		// This for Java 8
		String bundle_name = Main.class.getPackage().getName() + ".messages"; //$NON-NLS-1$
		ResourceBundle Messages;
		
		Locale.setDefault(new Locale("en", "EN"));
		Messages = ResourceBundle.getBundle(bundle_name);
		printAll(Messages);
		
		Locale.setDefault(new Locale("es", "ES"));
		Messages = ResourceBundle.getBundle(bundle_name);
		printAll(Messages);
	}
	
	public static void main(String[] args) {
		//test_I18N();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmPlquiz.setVisible(true);
				} catch (Exception e) {
					log.error("Error al iniciar la aplicación", e); //$NON-NLS-1$
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
			log.error("Error estableciendo el look and feel", e); //$NON-NLS-1$
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
		this.frmPlquiz.setTitle("PLQuiz"); //$NON-NLS-1$
		this.frmPlquiz.setBounds(100, 100, 1150, 900);
		this.frmPlquiz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frmPlquiz.setMinimumSize(new Dimension(480, 360));

		initMenuBar();
		initControlPanel();
		initVistaPreviaPanel();
	}

	private void initMenuBar() {
		this.menuBar = new JMenuBar();
		this.frmPlquiz.getContentPane().add(this.menuBar, BorderLayout.NORTH);

		this.menuArchivo = new JMenu(Messages.getString("Main.file")); //$NON-NLS-1$
		this.menuBar.add(this.menuArchivo);

		this.menuExportar = new JMenu(Messages.getString("Main.export")); //$NON-NLS-1$
		this.menuBar.add(this.menuExportar);

		this.menuAyuda = new JMenu(Messages.getString("Main.help")); //$NON-NLS-1$
		this.menuBar.add(this.menuAyuda);

		this.menuWeb = new JMenuItem(Messages.getString("Main.webPage")); //$NON-NLS-1$
		this.menuWeb.addActionListener(new MenuWebActionListener());
		this.menuAyuda.add(this.menuWeb);

		this.menuAcercaDe = new JMenuItem(Messages.getString("Main.about")); //$NON-NLS-1$
		this.menuAcercaDe.addActionListener(new MenuAcercaDeActionListener());
		this.menuAyuda.add(this.menuAcercaDe);

		this.menuNuevo = new JMenuItem(Messages.getString("Main.blankDocument")); //$NON-NLS-1$
		this.menuNuevo.addActionListener(new MenuNuevoActionListener());
		this.menuNuevo.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_N, java.awt.Event.CTRL_MASK));
		this.menuArchivo.add(this.menuNuevo);

		this.menuBloque = new JMenuItem(Messages.getString("Main.genQuestionsBlock")); //$NON-NLS-1$
		this.menuBloque.addActionListener(new MenuBloqueActionListener());
		this.menuBloque.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_B, java.awt.Event.CTRL_MASK));
		this.menuArchivo.add(this.menuBloque);

		this.menuArchivo.addSeparator();

		this.menuASU = new JMenuItem(Messages.getString("Main.newQuestionASU")); //$NON-NLS-1$
		this.menuASU.addActionListener(new MenuASUActionListener());
		this.menuASU.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_A, java.awt.Event.CTRL_MASK));
		this.menuArchivo.add(this.menuASU);

		this.menuCS = new JMenuItem(
				Messages.getString("Main.newQuestionThompson")); //$NON-NLS-1$
		this.menuCS.addActionListener(new MenuCSActionListener());
		this.menuCS.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_T, java.awt.Event.CTRL_MASK));
		this.menuArchivo.add(this.menuCS);

		this.menuExportarMoodleXMLButton = new JMenuItem(
				Messages.getString("Main.exportMoodleXML")); //$NON-NLS-1$
		this.menuExportarMoodleXMLButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuExportarMoodleXMLButton.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_M, java.awt.Event.CTRL_MASK));
		this.menuExportar.add(this.menuExportarMoodleXMLButton);

		this.menuExportarLatexButton = new JMenuItem(Messages.getString("Main.exportLaTeX")); //$NON-NLS-1$
		this.menuExportarLatexButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuExportarLatexButton.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_L, java.awt.Event.CTRL_MASK));
		this.menuExportar.add(this.menuExportarLatexButton);

		this.menuExportarGraphvizLatexButton = new JMenuItem(
				Messages.getString("Main.exportLaTeXandGraphviz")); //$NON-NLS-1$
		this.menuExportarGraphvizLatexButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuExportarGraphvizLatexButton.setAccelerator(KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_G,
						java.awt.Event.CTRL_MASK));
		this.menuExportar.add(this.menuExportarGraphvizLatexButton);
		
		// JBA >>
		this.menuExportarSVGLatexButton = new JMenuItem(
				Messages.getString("Main.exportLaTeXandSVG")); //$NON-NLS-1$
		this.menuExportarSVGLatexButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuExportarSVGLatexButton.setAccelerator(KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_S,		// Maybe this should be another key, but which one?
						java.awt.Event.CTRL_MASK));
		this.menuExportar.add(this.menuExportarSVGLatexButton);
		
		
		this.menuExportarPDFLatexButton = new JMenuItem(
				Messages.getString("Main.exportLaTeXandPDF")); //$NON-NLS-1$
		this.menuExportarPDFLatexButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuExportarPDFLatexButton.setAccelerator(KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_P,
						java.awt.Event.CTRL_MASK));
		this.menuExportar.add(this.menuExportarPDFLatexButton);
		
		
		this.menuExportarTikZLatexButton = new JMenuItem(
				Messages.getString("Main.exportLaTeXandTikZ")); //$NON-NLS-1$
		this.menuExportarTikZLatexButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuExportarTikZLatexButton.setAccelerator(KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_I,
						java.awt.Event.CTRL_MASK));
		this.menuExportar.add(this.menuExportarTikZLatexButton);
		// << JBA
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

		this.añadirButton = new JButton("➕"); //$NON-NLS-1$
		this.añadirButton.addActionListener(new AddButtonActionListener());

		this.añadirIzquierdoStrut = Box.createHorizontalStrut(70);
		this.añadirPanel.add(this.añadirIzquierdoStrut);
		this.añadirPanel.add(this.añadirButton);

		this.añadirBox = new JComboBox<>();
		this.añadirBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Aho-Sethi-Ullman", "McNaughton-Yamada-Thompson" })); //$NON-NLS-1$ //$NON-NLS-2$
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
		this.vistaPreviaText.setContentType("text/html;charset=UTF-8"); //$NON-NLS-1$
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
			log.info(Messages.getString("Main.moveProblemUp"), index); //$NON-NLS-1$
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
			log.info(Messages.getString("Main.moveProblemDown"), index); //$NON-NLS-1$
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
			String url = "http:\\" + Math.abs(imagen.hashCode()) + ".jpg"; //$NON-NLS-1$ //$NON-NLS-2$
			log.debug("Añadiendo imagen {}.", url); //$NON-NLS-1$
			Dictionary cache = (Dictionary) vistaPreviaText.getDocument()
					.getProperty("imageCache"); //$NON-NLS-1$
			if (cache == null) {
				cache = new Hashtable();
				vistaPreviaText.getDocument().putProperty("imageCache", cache); //$NON-NLS-1$
			}
			cache.put(new URL(url), imagen);
		} catch (Exception e) {
			log.error("Error al añadir imagen.", e); //$NON-NLS-1$
		}
	}

	@SuppressWarnings("rawtypes")
	void eliminaImagen(BufferedImage imagen) {
		try {
			String url = "http:\\" + Math.abs(imagen.hashCode()) + ".jpg"; //$NON-NLS-1$ //$NON-NLS-2$
			Dictionary cache = (Dictionary) vistaPreviaText.getDocument()
					.getProperty("imageCache"); //$NON-NLS-1$
			if (cache != null) {
				log.debug("Eliminando imagen {}.", url); //$NON-NLS-1$
				cache.remove(new URL(url));
			}
		} catch (Exception e) {
			log.error("Error al eliminar imagen.", e); //$NON-NLS-1$
		}
	}

	void eliminaMarca() {
		for (ProblemaPanel<?> panel : this.panelesProblema)
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
			if (añadirBox.getSelectedItem().equals("Aho-Sethi-Ullman")) { //$NON-NLS-1$
				log.info("Añadiendo problema tipo Aho-Sethi-Ullman"); //$NON-NLS-1$
				añadeAhoSethiUllman(null);
			} else if (añadirBox.getSelectedItem().equals(
					"McNaughton-Yamada-Thompson")) { //$NON-NLS-1$
				log.info("Añadiendo problema tipo construcción de subconjuntos"); //$NON-NLS-1$
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
			else if (source == menuExportarSVGLatexButton) 		// JBA
				fileChooser.setFileFilter(new LatexFilter());
			else if (source == menuExportarPDFLatexButton) 		// JBA
				fileChooser.setFileFilter(new LatexFilter());
			else if (source == menuExportarTikZLatexButton) 		// JBA
				fileChooser.setFileFilter(new LatexFilter());

			int valorRetorno = fileChooser.showSaveDialog(frmPlquiz);
			if (valorRetorno == JFileChooser.APPROVE_OPTION) {
				File fichero = fileChooser.getSelectedFile();
				try {
					if (source == menuExportarMoodleXMLButton) {
						log.info("Exportando fichero XML a {}.", fichero); //$NON-NLS-1$
						documento().exportaXML(fichero);
					} else if (source == menuExportarLatexButton) {
						log.info("Exportando fichero Latex a {}.", fichero); //$NON-NLS-1$
						documento().exportaLatex(fichero);
					} else if (source == menuExportarGraphvizLatexButton) {
						log.info(
								"Exportando fichero Latex con imágenes en graphviz a {}.", //$NON-NLS-1$
								fichero);
						documento().exportaGraphvizLatex(fichero);
					} else if (source == menuExportarSVGLatexButton) {	// JBA
						log.info(
								"Exportando fichero Latex con imágenes en SVG a {}.", //$NON-NLS-1$
								fichero);
						documento().exportaSVGLatex(fichero);
					} else if (source == menuExportarPDFLatexButton) {	// JBA
						log.info(
								"Exportando fichero Latex con imágenes en SVG a {}.", //$NON-NLS-1$
								fichero);
						documento().exportaPDFLatex(fichero);
					} else if (source == menuExportarTikZLatexButton) {	// JBA
						log.info(
								"Exportando fichero Latex con imágenes en TikZ a {}.", //$NON-NLS-1$
								fichero);
						documento().exportaTikZLatex(fichero);
					}
				} catch (IOException e) {
					log.error("Fallo al exportar fichero", e); //$NON-NLS-1$
				}
			}
		}
	}

	private class MenuNuevoActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			log.info("Generando un documento nuevo."); //$NON-NLS-1$
			actualizaVistaPrevia(null);
			contenedorPanel.removeAll();
			panelesProblema.clear();
			contenedorPanel.revalidate();
		}
	}

	private class MenuWebActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			log.info("Mostrando página web."); //$NON-NLS-1$
			try {
				Desktop.getDesktop().browse(
						new URI("http://robertoia.github.io/PLQuiz/")); //$NON-NLS-1$
			} catch (IOException | URISyntaxException e) {
				log.error("Error abriendo página web de la aplicación", e); //$NON-NLS-1$
			}
		}
	}

	private class MenuAcercaDeActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			log.info("Mostrando acerca de."); //$NON-NLS-1$
			JOptionPane.showMessageDialog(frmPlquiz, "PLQuiz\n" //$NON-NLS-1$
					+ Messages.getString("Main.computingScienceFinalProject") //$NON-NLS-1$
					+ "Escuela Politécnica Superior, Universidad de Burgos\n" //$NON-NLS-1$
					+ Messages.getString("Main.defendedOnJuly2014") //$NON-NLS-1$
					+ Messages.getString("Main.authorRobertoIzquierdoAmo") //$NON-NLS-1$
					+ Messages.getString("Main.supervisorDrCegarIgnacioGarciaOsorio"), Messages.getString("Main.about"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	private class MenuBloqueActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			log.info("Generando un bloque de problemas."); //$NON-NLS-1$
			new BloquePreguntas(main);
		}
	}

	private class MenuASUActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			añadeAhoSethiUllman(null);
		}
	}

	private class MenuCSActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			añadeConstruccionSubconjuntos(null);
		}
	}

	private class XMLFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.getName().toLowerCase().endsWith(".xml") //$NON-NLS-1$
					|| f.isDirectory();
		}

		@Override
		public String getDescription() {
			return Messages.getString("Main.XMLMoodleFiles"); //$NON-NLS-1$
		}
	}

	private class LatexFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.getName().toLowerCase().endsWith(".tex") //$NON-NLS-1$
					|| f.isDirectory();
		}

		@Override
		public String getDescription() {
			return Messages.getString("Main.LaTeXFiles"); //$NON-NLS-1$
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
