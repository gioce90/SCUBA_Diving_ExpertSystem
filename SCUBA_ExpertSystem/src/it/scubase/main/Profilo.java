package it.scubase.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.SwingConstants;

import CLIPSJNI.Environment;
import CLIPSJNI.PrimitiveValue;

import com.toedter.calendar.JCalendar;

public class Profilo extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JPanel brevettoPanel;
	private JPanel specialityPanel;
	private JPanel anagraficaPanel1;
	private JPanel anagraficaPanel2;
	private JPanel cmdPanel;
	private JPanel ExperiencePanel;
	private JPanel SuggestPanel;
	private JLabel suggestLabel;
	private JCheckBox chckbxImmersioneInQuota;
	private JCheckBox chckbxImmersioneDallaBarca;
	private JCheckBox chckbxImmersioneProfonda;
	private JCheckBox chckbxStressrescue;
	private JCheckBox chckbxImmersioneConMuta;
	private JCheckBox chckbxNitrox;
	private JCheckBox chckbxManutenzioneAttrezz;
	private JCheckBox chckbxNavigazione;
	private JCheckBox chckbxNotturnaEVisibilit;
	private JCheckBox chckbxAssettoPerfetto;
	private JCheckBox chckbxImmersioneNeiFiumi;
	private JCheckBox chckbxScienceOfDiving;
	private JCheckBox chckbxSidemount;
	private JCheckBox chckbxRicercaRecupero;
	private JCheckBox chckbxImmersioneConSquali;
	private JCheckBox chckbxOndeMareeCorrenti;
	private JCheckBox chckbxImmersioneSuRelitto;
	private JCheckBox chckbxFotografiaSubacquea;
	private JTextField txtNome;
	private JTextField txtCognome;
	private JRadioButton rdbtnS;
	private JRadioButton rdbtnNo;
	private JButton btnSalva;
	private JButton btnChiudi;
	private JComboBox<String> comboBox_brevetto;
	private JComboBox<String> comboBox_numImmersioni;
	private JComboBox<String> comboBox_deep;
	
	private Environment clips;
	private JTabbedPane tabbedPane;
	private JCalendar calendar;
	
	private ArrayList<JCheckBox> listaSpecialty;
	private String[] levels = { "Open Water Diver",
								"Advanced Open Water Diver",
								"Master Diver" };
	String[] profondità = new String[] { "Nessuna/Imprecisata",
			"-6 metri", "-7.5 metri", "-9.0 metri", "-10.5 metri",
			"-12.0 metri", "-15.0 metri", "-18.0 metri", "-21.0 metri",
			"-24.0 metri", "-27.0 metri", "-30.0 metri", "-33.0 metri",
			"-36.0 metri", "-39.0 metri", "Oltre i 40 metri" };
	String[] numeroImmersioni =	new String[] { "Nessuna/Imprecisata",
			"da 1 a 12", "pi\u00F9 di 12", "pi\u00F9 di 24", "pi\u00F9 di 50",
			"pi\u00F9 di 100", "pi\u00F9 di 200", "pi\u00F9 di 300",
			"pi\u00F9 di 400", "pi\u00F9 di 500", "pi\u00F9 di 1000" };
	
	
	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Profilo frame = new Profilo();
					frame.setVisible(true);
					
					// Provvisorio per il test
					//new Profilo().loadProfile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/

	
	/**
	 * Crea il Profilo del subacqueo
	 */
	public Profilo() {
		createBaseGUI();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	
	/**
	 * Restituisce un nuovo profilo
	 */
	public Profilo newProfile(){
		setTitle("Crea un nuovo profilo");
		this.setVisible(true);
		return this;
	}
	
	
	/**
	 * Restituisce un profilo caricato in memoria
	 */
	public Profilo loadProfile() {
		// Creo JFileChooser per selezionare il file
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Carica file");
		chooser.setFileFilter(new ClpFileFilter());
		chooser.setCurrentDirectory(new java.io.File("divers"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(true);
		
		chooser.setFileView(new FileView() {
			@Override
			public Boolean isTraversable(File f) {
				return (new java.io.File("divers").equals(f));
			}
		});
		
		int result = chooser.showOpenDialog(null);
		
		if (result==JFileChooser.APPROVE_OPTION){
			// operazioni di caricamento:
			if (chooser.getSelectedFile() != null) {
				try {
					loadFromCLP(chooser.getSelectedFile().getAbsolutePath());
					this.setVisible(true);
					
				} catch (NullPointerException e) {
					e.printStackTrace();
					System.out.println("Errore qui: loadProfile()");
				}
			}
		}
		return this;
	}
	
	
	/**
	 * Azioni Salva ecc..
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("Salva")) {
			String nome_file = "unknown";
			
			if (!txtCognome.getText().isEmpty() && !txtNome.getText().isEmpty())
				nome_file = txtCognome.getText()+"_"+txtNome.getText();
			
			if (!nome_file.equals("unknown")){
				if (saveOnCLP(nome_file))
					JOptionPane.showMessageDialog(this, "Salvataggio avvenuto con successo!");
			} else {
				JOptionPane.showMessageDialog(this, "Inserire cognome e nome prima di salvare");
			}
			
		} else if (e.getActionCommand().equals("Salva Modifiche")) {
			// TODO
			System.out.println("Salva modifiche gneeeeeee");
			//clips = new Environment();
			//clips.load("scuba.clp");
			//clips.reset();
			/*
			// LETTURA DA FILE
			try {
				BufferedReader buffReader;
				buffReader = new BufferedReader(new FileReader(nome_file+".clp"));
				String line = buffReader.readLine();
				while (line != null) {
					System.out.println(line);
					line = buffReader.readLine();
				}
				buffReader.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			*/
		}

	}
	
	
	///////////////////////////////////////
	// Metodi privati
	///////////////////////////////////////
	
	
	/**
	 * Crea la finestra grafica per il Profilo
	 */
	private void createBaseGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		
		contentPane = new JPanel();
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 434, 0 };
		gbl_contentPane.rowHeights = new int[] { 347, 82, 40, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		contentPane.add(tabbedPane, gbc_tabbedPane);
		
		/////////////////////
		// Tab Anagrafica:
		
		JPanel anagraficaTabPanel = new JPanel();
		tabbedPane.addTab("Anagrafica", null, anagraficaTabPanel, null);
		anagraficaTabPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		anagraficaPanel1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) anagraficaPanel1.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		
		JLabel lblCognome = new JLabel("Cognome");
		txtCognome = new JTextField();
		txtCognome.setColumns(10);
		JLabel lblNome = new JLabel("Nome");
		txtNome = new JTextField();
		txtNome.setColumns(10);
		
		anagraficaPanel2 = new JPanel();
		anagraficaPanel2.setLayout(new BoxLayout(anagraficaPanel2, BoxLayout.Y_AXIS));
		
		JLabel lblDataDiNascita = new JLabel("Data di nascita");
		lblDataDiNascita.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		calendar = new JCalendar();
		BorderLayout borderLayout = (BorderLayout) calendar.getLayout();
		borderLayout.setHgap(90);
		calendar.setDoubleBuffered(false);
		
		anagraficaPanel1.add(lblCognome);
		anagraficaPanel1.add(txtCognome);
		anagraficaPanel1.add(lblNome);
		anagraficaPanel1.add(txtNome);
		anagraficaPanel2.add(lblDataDiNascita);
		anagraficaPanel2.add(calendar);
		anagraficaTabPanel.add(anagraficaPanel1);
		anagraficaTabPanel.add(anagraficaPanel2);
		
		/////////////////////
		// Tab Licenze
		
		JPanel licenzeTabPanel = new JPanel();
		tabbedPane.addTab("Licenze", null, licenzeTabPanel, null);
		licenzeTabPanel.setLayout(new BoxLayout(licenzeTabPanel, BoxLayout.Y_AXIS));
		
		brevettoPanel = new JPanel();
		brevettoPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"Brevetto",	TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel lblBrevetto = new JLabel("Brevetto");
		rdbtnS = new JRadioButton("S\u00EC");
		rdbtnS.addMouseListener(brevettoYes);
		rdbtnNo = new JRadioButton("No");
		rdbtnNo.setSelected(true);
		rdbtnNo.addMouseListener(brevettoNo);
		ButtonGroup buttonGroup_Brevetto = new ButtonGroup();
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		
		comboBox_brevetto = new JComboBox<String>();
		comboBox_brevetto.setEnabled(false);
		comboBox_brevetto.setModel(new DefaultComboBoxModel<String>(levels));
		FlowLayout fl_BrevettoPanel = new FlowLayout(FlowLayout.CENTER, 2, 2);
		brevettoPanel.setLayout(fl_BrevettoPanel);
		
		specialityPanel = new JPanel();
		specialityPanel.setBorder(new TitledBorder(null, "Specialit\u00E0",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		specialityPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		chckbxImmersioneDallaBarca = new JCheckBox("Immersione dalla barca");
		chckbxImmersioneProfonda = new JCheckBox("Immersione profonda");
		chckbxFotografiaSubacquea = new JCheckBox("Fotografia subacquea");
		chckbxImmersioneSuRelitto = new JCheckBox("Immersione su relitto");
		chckbxNavigazione = new JCheckBox("Navigazione");
		chckbxAssettoPerfetto = new JCheckBox("Assetto perfetto");
		chckbxNitrox = new JCheckBox("Nitrox");
		chckbxImmersioneConMuta = new JCheckBox("Immersione con muta stagna");
		chckbxStressrescue = new JCheckBox("Stress&Rescue");
		chckbxManutenzioneAttrezz = new JCheckBox("Manutenzione Attrezzatura");
		chckbxNotturnaEVisibilit = new JCheckBox("Notturna e visibilit\u00E0 limitata");
		chckbxSidemount = new JCheckBox("Recreational Simemount Diving");
		chckbxImmersioneInQuota = new JCheckBox("Immersione in quota");
		chckbxImmersioneNeiFiumi = new JCheckBox("Immersione nei fiumi");
		chckbxScienceOfDiving = new JCheckBox("Science of Diving");
		chckbxRicercaRecupero = new JCheckBox("Ricerca&Recupero");
		chckbxImmersioneConSquali = new JCheckBox("Immersione con gli Squali");
		chckbxOndeMareeCorrenti = new JCheckBox("Onde, maree e correnti");
		
		listaSpecialty = new ArrayList<JCheckBox>();
		listaSpecialty.add(chckbxImmersioneDallaBarca);
		listaSpecialty.add(chckbxImmersioneProfonda);
		listaSpecialty.add(chckbxFotografiaSubacquea);
		listaSpecialty.add(chckbxImmersioneSuRelitto);
		listaSpecialty.add(chckbxNavigazione);
		listaSpecialty.add(chckbxAssettoPerfetto);
		listaSpecialty.add(chckbxNitrox);
		listaSpecialty.add(chckbxImmersioneConMuta);
		listaSpecialty.add(chckbxStressrescue);
		listaSpecialty.add(chckbxManutenzioneAttrezz);
		listaSpecialty.add(chckbxNotturnaEVisibilit);
		listaSpecialty.add(chckbxSidemount);
		listaSpecialty.add(chckbxImmersioneInQuota);
		listaSpecialty.add(chckbxImmersioneNeiFiumi);
		listaSpecialty.add(chckbxScienceOfDiving);
		listaSpecialty.add(chckbxRicercaRecupero);
		listaSpecialty.add(chckbxImmersioneConSquali);
		listaSpecialty.add(chckbxOndeMareeCorrenti);
		
		// Itera tutte le specialità
		Iterator<JCheckBox> i = listaSpecialty.iterator();
		while (i.hasNext()){
			JCheckBox jcb = i.next();
			jcb.setEnabled(false);
			specialityPanel.add(jcb);
		}
		
		buttonGroup_Brevetto.add(rdbtnS);
		buttonGroup_Brevetto.add(rdbtnNo);
		brevettoPanel.add(lblBrevetto);
		brevettoPanel.add(rdbtnS);
		brevettoPanel.add(rdbtnNo);
		brevettoPanel.add(horizontalStrut_3);
		brevettoPanel.add(comboBox_brevetto);
		licenzeTabPanel.add(brevettoPanel);
		licenzeTabPanel.add(specialityPanel);
		
		/////////////////////
		// Tab Esperienza:
		
		JPanel esperienzaTabPanel = new JPanel();
		tabbedPane.addTab("Esperienza", null, esperienzaTabPanel, null);
		esperienzaTabPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		ExperiencePanel = new JPanel();
		ExperiencePanel.setBorder(new TitledBorder(null, "Esperienza",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ExperiencePanel.setLayout(new GridLayout(3, 2, 20, 0));
		
		JLabel lblNumeroDiImmersioni = new JLabel("Numero di immersioni effettuate");
		comboBox_numImmersioni = new JComboBox<String>();
		comboBox_numImmersioni.setModel(new DefaultComboBoxModel<String>(numeroImmersioni));
		
		JLabel lblProfonditMassimaRaggiunta = new JLabel("Profondit\u00E0 massima raggiunta");
		comboBox_deep = new JComboBox<String>();
		comboBox_deep.setModel(new DefaultComboBoxModel<String>(profondità));

		esperienzaTabPanel.add(ExperiencePanel);
		ExperiencePanel.add(lblNumeroDiImmersioni);
		ExperiencePanel.add(comboBox_numImmersioni);
		ExperiencePanel.add(lblProfonditMassimaRaggiunta);
		ExperiencePanel.add(comboBox_deep);
		
		/////////////////////////////////////
		// TODO Panel dei suggerimenti:
		
		SuggestPanel = new JPanel();
		GridBagConstraints gbc_SuggestPanel = new GridBagConstraints();
		gbc_SuggestPanel.fill = GridBagConstraints.BOTH;
		gbc_SuggestPanel.insets = new Insets(0, 0, 5, 0);
		gbc_SuggestPanel.gridx = 0;
		gbc_SuggestPanel.gridy = 1;
		
		contentPane.add(SuggestPanel, gbc_SuggestPanel);
		SuggestPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		SuggestPanel.setLayout(new BorderLayout(0, 0));

		suggestLabel = new JLabel(
				"<html>Per un migliore utilizzo del sistema, immettere i dati nel modo pi\u00F9 preciso possibile.</html>");
		suggestLabel.setHorizontalAlignment(SwingConstants.CENTER);
		SuggestPanel.add(suggestLabel, BorderLayout.CENTER);
		
		/////////////////////////////////////
		// Command Panel:
		
		cmdPanel = new JPanel();
		GridBagConstraints gbc_cmdPanel = new GridBagConstraints();
		gbc_cmdPanel.gridx = 0;
		gbc_cmdPanel.gridy = 2;
		contentPane.add(cmdPanel, gbc_cmdPanel);
		cmdPanel.setLayout(new BoxLayout(cmdPanel, BoxLayout.X_AXIS));

		btnSalva = new JButton("Salva");
		btnSalva.setActionCommand("Salva");
		btnSalva.addActionListener(this);
		cmdPanel.add(btnSalva);

		btnChiudi = new JButton("Chiudi");
		btnChiudi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Profilo.this.dispose();
			}
		});
		cmdPanel.add(btnChiudi);
	}
	
	
	/**
	 * Salva i dati del profilo su un file .clp
	 */
	private boolean saveOnCLP(String nome_file) {
		clips = new Environment();
		clips.load("scuba.clp");
		
		String addressFile = new java.io.File("divers/"+nome_file+"_diver.clp").getAbsolutePath();
		String date = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getDate());
		
		// Parsing BREVETTO e Livello
		String brevetto = "no";
		if (rdbtnS.isSelected()){
			if (comboBox_brevetto.getSelectedItem().equals("Open Water Diver"))
				brevetto="open-water";
			else if (comboBox_brevetto.getSelectedItem().equals("Advanced Open Water Diver"))	
				brevetto="advanced";
			else if (comboBox_brevetto.getSelectedItem().equals("Master Diver"))
				brevetto="master";
		} 
		
		// Parsing SPECIALITA'
		String specialità = "nothing";
		String specList="";
		if (chckbxImmersioneDallaBarca.isSelected()) 	specList+="boat ";
		if (chckbxImmersioneProfonda.isSelected()) 		specList+="deep ";
		if (chckbxFotografiaSubacquea.isSelected()) 	specList+="photography ";
		if (chckbxImmersioneSuRelitto.isSelected()) 	specList+="wreck ";
		if (chckbxNavigazione.isSelected()) 			specList+="navigation ";
		if (chckbxAssettoPerfetto.isSelected()) 		specList+="perfect-buoyancy ";
		if (chckbxNitrox.isSelected()) 					specList+="nitrox ";
		if (chckbxImmersioneConMuta.isSelected()) 		specList+="dry-suit ";
		if (chckbxStressrescue.isSelected()) 			specList+="stress-rescue ";
		if (chckbxManutenzioneAttrezz.isSelected()) 	specList+="equipment ";
		if (chckbxNotturnaEVisibilit.isSelected()) 		specList+="night-limited-visibility ";
		if (chckbxSidemount.isSelected()) 				specList+="sidemount ";
		if (chckbxImmersioneInQuota.isSelected()) 		specList+="altitude ";
		if (chckbxImmersioneNeiFiumi.isSelected()) 		specList+="river ";
		if (chckbxScienceOfDiving.isSelected()) 		specList+="science ";
		if (chckbxRicercaRecupero.isSelected()) 		specList+="search-recovery ";
		if (chckbxImmersioneConSquali.isSelected()) 	specList+="shark ";
		if (chckbxOndeMareeCorrenti.isSelected()) 		specList+="currents ";
		
		if (!specList.equals(""))
			specialità = specList;
		
		// Parsing PROFONDITA' MASSIMA RAGGIUNTA
		double profondità = 0;
		switch (comboBox_deep.getSelectedIndex()){
			case 0:  profondità = 0; 	 break; // "Nessuna/Imprecisata"
			case 1:  profondità = 6; 	 break; // "-6 metri"
			case 2:  profondità = 7.5; 	 break; // "-7.5 metri"
			case 3:  profondità = 9; 	 break; // "-9.0 metri"
			case 4:  profondità = 10.5;  break; // "-10.5 metri"
			case 5:  profondità = 12; 	 break; // "-12.0 metri"
			case 6:  profondità = 15; 	 break; // "-15.0 metri"
			case 7:  profondità = 18; 	 break; // "-18.0 metri"
			case 8:  profondità = 21; 	 break; // "-21.0 metri"
			case 9:  profondità = 24;    break; // "-24.0 metri"
			case 10: profondità = 27; 	 break; // "-27.0 metri"
			case 11: profondità = 30; 	 break; // "-30.0 metri"
			case 12: profondità = 33; 	 break; // "-33.0 metri"
			case 13: profondità = 36; 	 break; // "-36.0 metri"
			case 14: profondità = 39; 	 break; // "-39.0 metri"
			case 15: profondità = 40; 	 break; // "Oltre i 40 metri"
		}
		
		// Parsing NUMERO DI IMMERSIONI
		int num = 0;
		switch (comboBox_numImmersioni.getSelectedIndex()){
			case 0:  num = 0; 	 	break; // "Nessuna/Imprecisata"
			case 1:  num = 1; 		break; // "da 1 a 12"
			case 2:  num = 12; 		break; // "più di 12"
			case 3:  num = 24; 	 	break; // "più di 24"
			case 4:  num = 50;  	break; // "più di 50"
			case 5:  num = 100; 	break; // "più di 100"
			case 6:  num = 200; 	break; // "più di 200"
			case 7:  num = 300; 	break; // "più di 300"
			case 8:  num = 400; 	break; // "più di 400"
			case 9:  num = 500;    	break; // "più di 500"
			case 10: num = 1000; 	break; // "più di 1000"
		}
		
		// SALVA il profilo su un file .CLP
		String facts = "(assert(subacqueo"
				+ "(nome " + txtCognome.getText()+" "+txtNome.getText()+")"
				+ "(data-nascita " + date + ")"
				+ "(max-deep " + profondità + ")"
				+ "(numero-immersioni " + num + ")"
				+ "(brevetto " + brevetto + ")"
				+ "(specialty "+ specialità + ")"
				+ "))";
		System.out.println(facts);
		clips.eval(facts);
		PrimitiveValue pv = clips.eval("(save-facts "+addressFile+")");
		
		boolean flag = false;
		if (pv.toString().equals("TRUE"))
			flag = true;
		
		clips.destroy();
		return flag;
	}


	/**
	 * Carica il profilo specificato da un file .clp
	 */
	private void loadFromCLP(String profilo){
		// TODO forse questo dovrebbe andare in PIANIFICAZIONE:
		clips = new Environment();
		clips.load("scuba.clp");
		clips.reset();
		clips.loadFacts(profilo);
		
		try {
			// maybe TODO ...
			 //rimuove il template subacqueo che si crea di default per dare spazio al nuovo:
			
			clips.loadFacts(profilo);
			
			String evalStr = "(find-all-facts ((?f subacqueo)) TRUE)";
			PrimitiveValue subs = clips.eval(evalStr);
			PrimitiveValue firstSub = subs.get(1); // TODO attenzione, prima era 0... più subacquei potrebbero essere un problwem
			
			// RE-PARSING dell'anagrafica
			String cognome = firstSub.getFactSlot("nome").get(0).toString();
			String nome = firstSub.getFactSlot("nome").get(1).toString();
			txtCognome.setText(cognome);
			txtNome.setText(nome);
			String data_nascita = firstSub.getFactSlot("data-nascita").toString();
			calendar.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(data_nascita));
			
			// RE-PARSING Brevetto
			String brevetto = firstSub.getFactSlot("brevetto").toString();
			if (brevetto.equals("no")){
				rdbtnNo.setSelected(true);
			} else {
				rdbtnS.setSelected(true);
				comboBox_brevetto.setEnabled(true);
				
				switch(brevetto){
					case "open-water":
						comboBox_brevetto.setSelectedIndex(0);
						break;
					case "advanced":
						comboBox_brevetto.setSelectedIndex(1);
						break;
					case "master":
						comboBox_brevetto.setSelectedIndex(2);
						break;
				}
				
				// RE-PARSING Specialità
				Iterator<JCheckBox> I = listaSpecialty.iterator();
				while (I.hasNext()){
					JCheckBox jcb = I.next();
					jcb.setEnabled(true);
				}
				
				int numSpec = firstSub.getFactSlot("specialty").size();
				for (int i=0;i<numSpec;i++){
					switch(firstSub.getFactSlot("specialty").get(i).toString()){
						case "boat": chckbxImmersioneDallaBarca.setSelected(true); break;
						case "deep": chckbxImmersioneProfonda.setSelected(true); break;
						case "photography": chckbxFotografiaSubacquea.setSelected(true); break;
						case "wreck": chckbxImmersioneSuRelitto.setSelected(true); break;
						case "navigation": chckbxNavigazione.setSelected(true); break;
						case "perfect-buoyancy": chckbxAssettoPerfetto.setSelected(true); break;
						case "nitrox": chckbxNitrox.setSelected(true); break;
						case "dry-suit": chckbxImmersioneConMuta.setSelected(true); break;
						case "stress-rescue": chckbxStressrescue.setSelected(true); break;
						case "equipment": chckbxManutenzioneAttrezz.setSelected(true); break;
						case "night-limited-visibility": chckbxNotturnaEVisibilit.setSelected(true); break;
						case "sidemount": chckbxSidemount.setSelected(true); break;
						case "altitude": chckbxImmersioneInQuota.setSelected(true); break;
						case "river": chckbxImmersioneNeiFiumi.setSelected(true); break;
						case "science": chckbxScienceOfDiving.setSelected(true); break;
						case "search-recovery": chckbxRicercaRecupero.setSelected(true); break;
						case "shark": chckbxImmersioneConSquali.setSelected(true); break;
						case "currents": chckbxOndeMareeCorrenti.setSelected(true); break;
					}
				}
			}
			
			// RE-PARSING Numero immersioni
			switch (firstSub.getFactSlot("numero-immersioni").intValue()){
				case 0:		comboBox_numImmersioni.setSelectedIndex(0);  break; // "Nessuna/Imprecisata"
				case 1:  	comboBox_numImmersioni.setSelectedIndex(1);  break; // "da 1 a 12"
				case 12:  	comboBox_numImmersioni.setSelectedIndex(2);  break; // "più di 12"
				case 24:  	comboBox_numImmersioni.setSelectedIndex(3);  break; // "più di 24"
				case 50:  	comboBox_numImmersioni.setSelectedIndex(4);  break; // "più di 50"
				case 100:  	comboBox_numImmersioni.setSelectedIndex(5);  break; // "più di 100"
				case 200:  	comboBox_numImmersioni.setSelectedIndex(6);  break; // "più di 200"
				case 300:  	comboBox_numImmersioni.setSelectedIndex(7);  break; // "più di 300"
				case 400:  	comboBox_numImmersioni.setSelectedIndex(8);  break; // "più di 400"
				case 500:  	comboBox_numImmersioni.setSelectedIndex(9);  break; // "più di 500"
				case 1000:  comboBox_numImmersioni.setSelectedIndex(10); break; // "più di 1000"
			}
			
			// RE-PARSING Profondità
			switch (firstSub.getFactSlot("max-deep").toString()){
				case "0.0":  comboBox_deep.setSelectedIndex(0);  break; // "Nessuna/Imprecisata"
				case "6.0":  comboBox_deep.setSelectedIndex(1);  break; // "-6 metri"
				case "7.5":  comboBox_deep.setSelectedIndex(2);  break; // "-7.5 metri"
				case "9.0":  comboBox_deep.setSelectedIndex(3);  break; // "-9.0 metri"
				case "10.5": comboBox_deep.setSelectedIndex(4);  break; // "-10.5 metri"
				case "12.0": comboBox_deep.setSelectedIndex(5);  break; // "-12.0 metri"
				case "15.0": comboBox_deep.setSelectedIndex(6);  break; // "-15.0 metri"
				case "18.0": comboBox_deep.setSelectedIndex(7);  break; // "-18.0 metri"
				case "21.0": comboBox_deep.setSelectedIndex(8);  break; // "-21.0 metri"
				case "24.0": comboBox_deep.setSelectedIndex(9);  break; // "-24.0 metri"
				case "27.0": comboBox_deep.setSelectedIndex(10); break; // "-27.0 metri"
				case "30.0": comboBox_deep.setSelectedIndex(11); break; // "-30.0 metri"
				case "33.0": comboBox_deep.setSelectedIndex(12); break; // "-33.0 metri"
				case "36.0": comboBox_deep.setSelectedIndex(13); break; // "-36.0 metri"
				case "39.0": comboBox_deep.setSelectedIndex(14); break; // "-39.0 metri"
				case "40.0": comboBox_deep.setSelectedIndex(15); break; // "Oltre i 40 metri"
			}
			
			setTitle(cognome+" "+nome);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		btnSalva.setText("Salva modifiche");
		//btnSalva.setActionCommand("Salva Modifiche");
	}
	
	
	///////////////////////////////////////
	// Metodi, eventi, oggetti, utility...
	///////////////////////////////////////
	
	
	/**
	 * Classe privata ClpFileFilter
	 */
	private class ClpFileFilter extends FileFilter {
		public boolean accept(File f) {
			if (f.isDirectory())
				return false; // Non accetta le directory!
			String name = f.getName().toLowerCase();
			return name.endsWith(".clp");
		}
		
		public String getDescription() {
			return "File di testo (*.clp)";
		}
	}
	
	
	/**
	 * Procedura per il cambio dei suggerimenti
	 */
	protected void changeText(String text) {
		suggestLabel.setText(text);
	}
	
	/**
	 * MouseAdapter: Brevetto YES or NO
	 */
	MouseAdapter brevettoYes = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			comboBox_brevetto.setEnabled(true);
			
			// Itera tutte le specialità
			Iterator<JCheckBox> i = listaSpecialty.iterator();
			while (i.hasNext()){
				i.next().setEnabled(true);
			}
		}
	};
	MouseAdapter brevettoNo = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			comboBox_brevetto.setEnabled(false);
			
			// Itera tutte le specialità
			Iterator<JCheckBox> i = listaSpecialty.iterator();
			while (i.hasNext()){
				i.next().setEnabled(false);
			}
		}
	};
	
	
}
