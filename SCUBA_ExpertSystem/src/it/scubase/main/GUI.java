package it.scubase.main;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.FlowLayout;
 

/**
 * @author Cecco
 * 
 */
public class GUI {

	private JFrame frmScubaDivingExpert;
	private JLabel suggestLabel;
	private JButton btnNuovoProfilo;
	private JButton btnCaricaProfilo;
	private JButton btnPianificazione;
	private JButton btnInfo;
	private JButton btnCrediti;
	private JButton btnEsci;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GUI window = new GUI();
					window.frmScubaDivingExpert.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
		frmScubaDivingExpert.pack();
	}
	
	
	/**
	 * Procedura per il cambio dei suggerimenti
	 */
	protected void changeText(String text) {
		suggestLabel.setText(text);
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmScubaDivingExpert = new JFrame();
		frmScubaDivingExpert.setTitle("SCUBA Diving Expert System");
		frmScubaDivingExpert.setBounds(100, 100, 500, 450);
		frmScubaDivingExpert.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// GridBagLayout
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 333, 0 };
		gridBagLayout.rowHeights = new int[] { 57, 114, 80, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		frmScubaDivingExpert.getContentPane().setLayout(gridBagLayout);

		// headerLabel
		JLabel headerLabel = new JLabel("SCUBA Diving");
		headerLabel.setFont(new Font("DejaVu Sans", Font.BOLD | Font.ITALIC, 26));
		headerLabel.setForeground(SystemColor.menu);
		headerLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		headerLabel.setToolTipText("SCUBA Diving Expert System");
		headerLabel.setIcon(new ImageIcon(
				"..\\SCUBA_ExpertSystem\\image\\intro.jpg"));
		GridBagConstraints gbc_headerLabel = new GridBagConstraints();
		gbc_headerLabel.insets = new Insets(0, 0, 5, 0);
		gbc_headerLabel.gridx = 0;
		gbc_headerLabel.gridy = 0;
		frmScubaDivingExpert.getContentPane().add(headerLabel, gbc_headerLabel);

		// chooicePanel
		JPanel chooicePanel = new JPanel();
		GridBagConstraints gbc_chooicePanel = new GridBagConstraints();
		gbc_chooicePanel.insets = new Insets(0, 0, 5, 0);
		gbc_chooicePanel.gridx = 0;
		gbc_chooicePanel.gridy = 1;
		frmScubaDivingExpert.getContentPane().add(chooicePanel, gbc_chooicePanel);

		// chooicePanelLayout
		GridBagLayout chooicePanelLayout = new GridBagLayout();
		chooicePanelLayout.columnWidths = new int[] { 150, 150 };
		chooicePanelLayout.rowHeights = new int[] { 23, 23 };
		chooicePanelLayout.columnWeights = new double[] { 0.0, 0.0 };
		chooicePanelLayout.rowWeights = new double[] { 0.0, 0.0 };
		chooicePanel.setLayout(chooicePanelLayout);

		// btnNuovoProfilo
		btnNuovoProfilo = new JButton("Nuovo Profilo");
		GridBagConstraints gbc_btnNuovoProfilo = new GridBagConstraints();
		gbc_btnNuovoProfilo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNuovoProfilo.anchor = GridBagConstraints.NORTH;
		gbc_btnNuovoProfilo.insets = new Insets(0, 0, 5, 5);
		gbc_btnNuovoProfilo.gridx = 0;
		gbc_btnNuovoProfilo.gridy = 0;
		chooicePanel.add(btnNuovoProfilo, gbc_btnNuovoProfilo);

		// btnCaricaProfilo
		btnCaricaProfilo = new JButton("Carica Profilo");
		GridBagConstraints gbc_btnCaricaProfilo = new GridBagConstraints();
		gbc_btnCaricaProfilo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCaricaProfilo.anchor = GridBagConstraints.NORTH;
		gbc_btnCaricaProfilo.insets = new Insets(0, 0, 5, 0);
		gbc_btnCaricaProfilo.gridx = 1;
		gbc_btnCaricaProfilo.gridy = 0;
		chooicePanel.add(btnCaricaProfilo, gbc_btnCaricaProfilo);

		// btnPianificazione
		btnPianificazione = new JButton("Pianificazione veloce");
		GridBagConstraints gbc_btnPianificazione = new GridBagConstraints();
		gbc_btnPianificazione.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPianificazione.anchor = GridBagConstraints.NORTH;
		gbc_btnPianificazione.insets = new Insets(0, 0, 5, 5);
		gbc_btnPianificazione.gridx = 0;
		gbc_btnPianificazione.gridy = 1;
		chooicePanel.add(btnPianificazione, gbc_btnPianificazione);

		// btnInfo
		btnInfo = new JButton("Info");
		GridBagConstraints gbc_btnInfo = new GridBagConstraints();
		gbc_btnInfo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnInfo.anchor = GridBagConstraints.NORTH;
		gbc_btnInfo.insets = new Insets(0, 0, 5, 0);
		gbc_btnInfo.gridx = 1;
		gbc_btnInfo.gridy = 1;
		chooicePanel.add(btnInfo, gbc_btnInfo);

		// btnCrediti
		btnCrediti = new JButton("Crediti");
		GridBagConstraints gbc_btnCrediti = new GridBagConstraints();
		gbc_btnCrediti.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCrediti.anchor = GridBagConstraints.NORTH;
		gbc_btnCrediti.insets = new Insets(0, 0, 0, 5);
		gbc_btnCrediti.gridx = 0;
		gbc_btnCrediti.gridy = 2;
		chooicePanel.add(btnCrediti, gbc_btnCrediti);

		// btnEsci
		btnEsci = new JButton("Esci");
		GridBagConstraints gbc_btnEsci = new GridBagConstraints();
		gbc_btnEsci.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEsci.anchor = GridBagConstraints.NORTH;
		gbc_btnEsci.gridx = 1;
		gbc_btnEsci.gridy = 2;
		chooicePanel.add(btnEsci, gbc_btnEsci);

		// Listener per i button
		createButtonListener();

		// suggestPanel
		JPanel suggestPanel = new JPanel();
		GridBagConstraints gbc_commandPanel = new GridBagConstraints();
		gbc_commandPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_commandPanel.gridx = 0;
		gbc_commandPanel.gridy = 2;
		frmScubaDivingExpert.getContentPane().add(suggestPanel, gbc_commandPanel);
		suggestPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		suggestLabel = new JLabel();
		suggestLabel.setText(". . .");
		suggestLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		suggestLabel.setHorizontalAlignment(SwingConstants.CENTER);
		suggestPanel.add(suggestLabel);

		// Menu
		JMenuBar menuBar = new JMenuBar();
		frmScubaDivingExpert.setJMenuBar(menuBar);
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		JMenuItem mntmChiudi = new JMenuItem("Chiudi");
		mnFile.add(mntmChiudi);
	}

	
	/**
	 * createButtonListener() crea i listener per i button
	 */
	private void createButtonListener() {
		btnNuovoProfilo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				changeText("Crea un nuovo profilo");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				new Profilo().newProfile();
			}
		});

		btnCaricaProfilo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				changeText("Carica un profilo salvato in memoria");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				new Profilo().loadProfile();
			}

		});

		btnPianificazione.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				changeText("Avvia una pianificazione veloce");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				new SmartPlan();
			}
		});

		btnInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				changeText("Controlla le tabelle e le altre info");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btnCrediti.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				changeText("Guarda i crediti");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				JDialog creditJDialog = new Crediti();
				creditJDialog.setLocationRelativeTo(frmScubaDivingExpert);
				creditJDialog.setVisible(true);
			}
		});

		btnEsci.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				changeText("Chiudi SCUBA Expert System");
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		});
	}
}




/*
 * class JButton extends javax.swing.JButton{ private String description;
 * 
 * JButton(){ super(); }
 * 
 * JButton(String s){ super(s); }
 * 
 * public JButton(Action a) { super(a); }
 * 
 * public JButton(Icon icon) { super(icon); }
 * 
 * public JButton(String text, Icon icon) { super(text, icon); }
 * 
 * public String getDescription() { return description; }
 * 
 * public void setDescription(String description) { this.description =
 * description; } }
 */

// Descrizioni per i button
/*
 * btnNuovoProfilo.setDescription("Crea un nuovo profilo");
 * btnCaricaProfilo.setDescription("Carica un profilo salvato in memoria");
 * btnPianificazione.setDescription("Avvia una pianificazione veloce");
 * btnInfo.setDescription("Controlla le tabelle e le altre info");
 * btnCrediti.setDescription("Guarda i crediti");
 * btnEsci.setDescription("Chiudi SCUBA Expert System");
 */

// btnNuovoProfilo.getLabel()

/*
 * // è buono: MouseAdapter MA1 = new MouseAdapter() { public void
 * mouseEntered(MouseEvent e) {
 * changeText(((JButton)e.getComponent()).getDescription()); } };
 */