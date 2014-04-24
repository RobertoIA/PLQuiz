package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

@SuppressWarnings("serial")
public class GraphFrame extends JFrame {

	private JPanel contentPane;
	private JLabel image;

	public GraphFrame(ConstruccionSubconjuntos problema) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);
		
		Icon icon = new ImageIcon(problema.automata());
		image = new JLabel(icon);
		this.contentPane.add(this.image, BorderLayout.CENTER);
		
		setVisible(true);
	}
}
