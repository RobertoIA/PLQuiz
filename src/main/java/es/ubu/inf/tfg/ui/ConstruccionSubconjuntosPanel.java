package es.ubu.inf.tfg.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import es.ubu.inf.tfg.doc.Documento;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class ConstruccionSubconjuntosPanel extends JPanel {

	private static final long serialVersionUID = -1805230103073818602L;

	private final JPanel contenedorPanel;
	private final JPanel actualPanel = this;
	private final Documento documento;
	private final JTextPane vistaPrevia;
	private ConstruccionSubconjuntos problemaActual = null;
	private JPanel expresionPanel;
	private JButton borrarButton;
	private JTextField expresionText;
	private JPanel botonesPanel;
	private JButton resolverButton;

	public ConstruccionSubconjuntosPanel(JPanel contenedor, Documento documento,
			JTextPane vistaPrevia) {

		this.contenedorPanel = contenedor;
		this.documento = documento;
		this.vistaPrevia = vistaPrevia;

		setBorder(new CompoundBorder(new EmptyBorder(5, 5, 15, 5),
				new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),
						"Construcción de subconjuntos",
						TitledBorder.LEADING, TitledBorder.TOP, null, null)));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.expresionPanel = new JPanel();
		add(this.expresionPanel);

		this.borrarButton = new JButton("-");
		this.borrarButton.addActionListener(new BotonBorrarActionListener());
		this.expresionPanel.add(this.borrarButton);

		this.expresionText = new JTextField();
		this.expresionText.addActionListener(new BotonResolverActionListener());
		this.expresionPanel.add(this.expresionText);
		this.expresionText.setColumns(40);

		this.botonesPanel = new JPanel();
		add(this.botonesPanel);

		this.resolverButton = new JButton("Resolver");
		this.resolverButton
				.addActionListener(new BotonResolverActionListener());
		this.botonesPanel.add(this.resolverButton);
	}

	private class BotonBorrarActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (problemaActual != null) {
				documento.eliminarProblema(problemaActual);
				vistaPrevia.setText(documento.vistaPrevia());
			}

			contenedorPanel.remove(actualPanel);
			contenedorPanel.revalidate();
		}
	}

	private class BotonResolverActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String expresion = expresionText.getText();

			if (expresion.length() > 0) {
				if(problemaActual != null) {
					if (!expresion.equals(problemaActual.problema())) {
						ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(expresion);
						documento.sustituirProblema(problemaActual, problema);
						problemaActual = problema;
					}
				}else{
					ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(expresion);
					documento.añadirProblema(problema);
					problemaActual = problema;
				}
				vistaPrevia.setText(documento.vistaPrevia());
			}
		}
	}
}
