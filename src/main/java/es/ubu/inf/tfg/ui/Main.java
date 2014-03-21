package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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

import es.ubu.inf.tfg.doc.Documento;

public class Main {

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
	private JMenuItem menuExportarHTMLButton;
	private JMenu menuArchivo;
	private JMenuItem menuExportarMoodleXMLButton;

	private JFileChooser fileChooser;
	private Documento documento;
	private JMenuItem menuNuevo;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmPlquiz.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
		this.documento = new Documento();
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

		this.menuExportarHTMLButton = new JMenuItem("Exportar como HTML");
		this.menuExportarHTMLButton
				.addActionListener(new MenuExportarButtonActionListener());

		this.menuNuevo = new JMenuItem("Nuevo");
		this.menuNuevo.addActionListener(new MenuNuevoActionListener());
		this.menuArchivo.add(this.menuNuevo);
		this.menuArchivo.add(this.menuExportarHTMLButton);

		this.menuExportarMoodleXMLButton = new JMenuItem(
				"Exportar como Moodle XML");
		this.menuExportarMoodleXMLButton
				.addActionListener(new MenuExportarButtonActionListener());
		this.menuArchivo.add(this.menuExportarMoodleXMLButton);

		this.controlPanel = new JPanel();
		this.frmPlquiz.getContentPane().add(this.controlPanel,
				BorderLayout.WEST);
		this.controlPanel.setLayout(new BorderLayout(0, 0));

		this.contenedorPanel = new JPanel();
		this.controlPanel.add(this.contenedorPanel, BorderLayout.NORTH);
		this.contenedorPanel.setLayout(new BoxLayout(this.contenedorPanel,
				BoxLayout.Y_AXIS));

		this.añadirPanel = new JPanel();
		this.controlPanel.add(this.añadirPanel, BorderLayout.SOUTH);

		this.añadirButton = new JButton("+");
		this.añadirButton.addActionListener(new AddButtonActionListener());

		this.añadirIzquierdoStrut = Box.createHorizontalStrut(110);
		this.añadirPanel.add(this.añadirIzquierdoStrut);
		this.añadirPanel.add(this.añadirButton);

		this.añadirBox = new JComboBox<>();
		this.añadirBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Aho-Sethi-Ullman", "Construcción de subconjuntos" }));
		this.añadirPanel.add(this.añadirBox);

		this.añadirDerechoStrut = Box.createHorizontalStrut(110);
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
		this.vistaPreviaText.setContentType("text/html");
		this.vistaPreviaScroll.add(this.vistaPreviaText);
		this.vistaPreviaScroll.setViewportView(this.vistaPreviaText);
	}

	private class AddButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JPanel nuevoPanel = null;

			if (añadirBox.getSelectedItem().equals("Aho-Sethi-Ullman"))
				nuevoPanel = new AhoSethiUllmanPanel(contenedorPanel,
						documento, vistaPreviaText);
			else if (añadirBox.getSelectedItem().equals("Construcción de subconjuntos"))
				nuevoPanel = new ConstruccionSubconjuntosPanel(contenedorPanel, documento,
						vistaPreviaText);

			if (nuevoPanel != null) {
				contenedorPanel.add(nuevoPanel);
				contenedorPanel.revalidate();
			}
		}
	}

	private class MenuExportarButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JMenuItem source = (JMenuItem) event.getSource();

			if (source == menuExportarHTMLButton)
				fileChooser.setFileFilter(new HTMLFilter());
			else if (source == menuExportarMoodleXMLButton)
				fileChooser.setFileFilter(new XMLFilter());

			int valorRetorno = fileChooser.showSaveDialog(frmPlquiz);
			if (valorRetorno == JFileChooser.APPROVE_OPTION) {
				File fichero = fileChooser.getSelectedFile();
				try {
					if (source == menuExportarHTMLButton)
						documento.exportaHTML(fichero);
					else if (source == menuExportarMoodleXMLButton)
						documento.exportaXML(fichero);
				} catch (IOException e) {
					// TODO Avisar de error / reintento
					e.printStackTrace();
				}
			}
		}
	}

	private class MenuNuevoActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			documento = new Documento();
			vistaPreviaText.setText(documento.vistaPrevia());
			contenedorPanel.removeAll();
		}
	}

	private class HTMLFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.getName().toLowerCase().endsWith(".html");
		}

		@Override
		public String getDescription() {
			return "Ficheros HTML (*.html)";
		}
	}

	private class XMLFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.getName().toLowerCase().endsWith(".xml");
		}

		@Override
		public String getDescription() {
			return "Ficheros Moodle XML (*.xml)";
		}
	}
}
