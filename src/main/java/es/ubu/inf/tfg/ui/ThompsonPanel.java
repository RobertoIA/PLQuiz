package es.ubu.inf.tfg.ui;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import es.ubu.inf.tfg.doc.Documento;
import es.ubu.inf.tfg.regex.thompson.Thompson;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ThompsonPanel extends JPanel {

	private static final long serialVersionUID = -1805230103073818602L;

	private final JPanel contenedorPanel;
	private final JPanel actualPanel = this;
	private final Documento documento;
	private final JTextPane vistaPrevia;
	private Thompson problemaActual = null;
	private JPanel expresionPanel;
	private JButton borrarButton;
	private JTextField expresionText;
	private JPanel botonesPanel;
	private JButton resolverButton;

	public ThompsonPanel(JPanel contenedor, Documento documento,
			JTextPane vistaPrevia) {

		this.contenedorPanel = contenedor;
		this.documento = documento;
		this.vistaPrevia = vistaPrevia;

		setBorder(new CompoundBorder(new EmptyBorder(5, 5, 15, 5),
				new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),
						"Thompson - Construcción de subconjuntos",
						TitledBorder.LEADING, TitledBorder.TOP, null, null)));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.expresionPanel = new JPanel();
		add(this.expresionPanel);

		this.borrarButton = new JButton("-");
		this.borrarButton.addActionListener(new BorrarButtonActionListener());
		this.expresionPanel.add(this.borrarButton);

		this.expresionText = new JTextField();
		this.expresionPanel.add(this.expresionText);
		this.expresionText.setColumns(40);

		this.botonesPanel = new JPanel();
		add(this.botonesPanel);

		this.resolverButton = new JButton("Resolver");
		this.resolverButton
				.addActionListener(new ResolverButtonActionListener());
		this.botonesPanel.add(this.resolverButton);
	}

	private class BorrarButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (problemaActual != null) {
				// documento.eliminarProblema(problemaActual); //TODO
				vistaPrevia.setText(documento.vistaPrevia());
			}

			contenedorPanel.remove(actualPanel);
			contenedorPanel.revalidate();
		}
	}

	private class ResolverButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String expresion = expresionText.getText();

			if (expresion.length() > 0) {
				if (problemaActual != null
						&& expresion.equals(problemaActual.problema())) {
					documento.eliminarProblema(problemaActual);
					documento.añadirProblema(problemaActual);
				} else {
					Thompson problema = new Thompson(expresion);
					if (problemaActual != null)
						documento.sustituirProblema(problemaActual, problema);
					problemaActual = problema;
					documento.añadirProblema(problema);
				}
				vistaPrevia.setText(documento.vistaPrevia());
			}
		}
	}
}
