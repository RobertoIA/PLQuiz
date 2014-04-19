package es.ubu.inf.tfg.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;

public class BloquePreguntas {

	private JFrame frame;
	private JPanel asuPanel;
	private JPanel csPanel;
	private JLabel asuNumLabel;
	private JLabel asuSimbolosLabel;
	private JLabel asuEstadosLabel;
	private JPanel asuNumPanel;
	private JPanel asuSimbolosPanel;
	private JPanel asuEstadosPanel;
	private JSpinner asuNumSpinner;
	private JSpinner asuSimbolosSpinner;
	private JSpinner asuEstadosSpinner;
	private JPanel csNumPanel;
	private JLabel csNumLabel;
	private JSpinner csNumSpinner;
	private JPanel csSimbolosPanel;
	private JLabel csSimbolosLabel;
	private JSpinner csSimbolosSpinner;
	private JPanel csEstadosPanel;
	private JLabel csEstadosLabel;
	private JSpinner csEstadosSpinner;

	public BloquePreguntas() {
		initialize();
		this.frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 450, 300);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(new BoxLayout(this.frame.getContentPane(), BoxLayout.Y_AXIS));
		
		this.asuPanel = new JPanel();
		this.frame.getContentPane().add(this.asuPanel);
		this.asuPanel.setLayout(new BoxLayout(this.asuPanel, BoxLayout.Y_AXIS));
		
		this.asuNumPanel = new JPanel();
		this.asuPanel.add(this.asuNumPanel);
		
		this.asuNumLabel = new JLabel("N\u00FAmero de problemas");
		this.asuNumPanel.add(this.asuNumLabel);
		
		this.asuNumSpinner = new JSpinner();
		this.asuNumPanel.add(this.asuNumSpinner);
		
		this.asuSimbolosPanel = new JPanel();
		this.asuPanel.add(this.asuSimbolosPanel);
		
		this.asuSimbolosLabel = new JLabel("S\u00EDmbolos");
		this.asuSimbolosPanel.add(this.asuSimbolosLabel);
		
		this.asuSimbolosSpinner = new JSpinner();
		this.asuSimbolosPanel.add(this.asuSimbolosSpinner);
		
		this.asuEstadosPanel = new JPanel();
		this.asuPanel.add(this.asuEstadosPanel);
		
		this.asuEstadosLabel = new JLabel("Estados");
		this.asuEstadosPanel.add(this.asuEstadosLabel);
		
		this.asuEstadosSpinner = new JSpinner();
		this.asuEstadosPanel.add(this.asuEstadosSpinner);
		
		this.csPanel = new JPanel();
		this.frame.getContentPane().add(this.csPanel);
		this.csPanel.setLayout(new BoxLayout(this.csPanel, BoxLayout.Y_AXIS));
		
		this.csNumPanel = new JPanel();
		this.csPanel.add(this.csNumPanel);
		
		this.csNumLabel = new JLabel("N\u00FAmero de problemas");
		this.csNumPanel.add(this.csNumLabel);
		
		this.csNumSpinner = new JSpinner();
		this.csNumPanel.add(this.csNumSpinner);
		
		this.csSimbolosPanel = new JPanel();
		this.csPanel.add(this.csSimbolosPanel);
		
		this.csSimbolosLabel = new JLabel("Simbolos");
		this.csSimbolosPanel.add(this.csSimbolosLabel);
		
		this.csSimbolosSpinner = new JSpinner();
		this.csSimbolosPanel.add(this.csSimbolosSpinner);
		
		this.csEstadosPanel = new JPanel();
		this.csPanel.add(this.csEstadosPanel);
		
		this.csEstadosLabel = new JLabel("Estados");
		this.csEstadosPanel.add(this.csEstadosLabel);
		
		this.csEstadosSpinner = new JSpinner();
		this.csEstadosPanel.add(this.csEstadosSpinner);
	}

}
