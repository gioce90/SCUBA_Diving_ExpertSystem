package it.scubase.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;

public class Crediti extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
	private JLabel lblSistemaEspertoCreato;
	private JLabel lblScubaExpertSystem;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Crediti dialog = new Crediti();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Create the dialog.
	 */
	public Crediti() {
		setModal(true);
		setUndecorated(true);
		setType(Type.POPUP);
		setResizable(false);
		setAlwaysOnTop(true);
		setBounds(100, 100, 400, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null,
				null, null, null));
		contentPanel.setBackground(SystemColor.inactiveCaption);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		lblSistemaEspertoCreato = new JLabel(
				"<html>Self Contained Underwater Breathing Apparatus Expert System<p><p>"
						+ "Sistema Esperto per la pianificazione di immersioni subacquee<p>"
						+ "Creato ed ideato da Gioacchino Piazzolla, "
						+ "per l'Universit\u00E0 degli Studi di Bari Aldo Moro.<p><p>"
						+ "Docente di riferimento: Esposito</html>");
		lblSistemaEspertoCreato.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblSistemaEspertoCreato);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(contentPanel.getBackground());
			contentPanel.add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

			okButton = new JButton("OK");
			buttonPane.add(okButton);

			/*
			 * okButton.addActionListener(new ActionListener() { public void
			 * actionPerformed(ActionEvent e) { dispose(); } });
			 */
			okButton.setActionCommand("OK");
			okButton.addActionListener(this);
			getRootPane().setDefaultButton(okButton);

		}

		lblScubaExpertSystem = new JLabel("SCUBA Expert System");
		lblScubaExpertSystem.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblScubaExpertSystem.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblScubaExpertSystem, BorderLayout.NORTH);
	}

	
	/**
	 * ActionListener.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK"))
			dispose();
	}
}
