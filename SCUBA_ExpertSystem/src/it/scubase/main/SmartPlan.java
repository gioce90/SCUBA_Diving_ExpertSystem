package it.scubase.main;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import CLIPSJNI.Environment;
import CLIPSJNI.PrimitiveValue;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.toedter.calendar.JCalendar;
 
public class SmartPlan implements ActionListener {
	private JFrame jfrm;
	private JLabel displayLabel;
	private JPanel choicesPanel;
	private JButton prevButton;
	private JButton nextButton;
	private JButton confirmButton;
	private ButtonGroup choicesButtons;
	private ArrayList<JCheckBox> choicesCheckBoxes;
	ResourceBundle scubaResources;
	private JCalendar calendar;
	private Thread executionThread;
	private boolean isExecuting;
	private Environment clips;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//SmartPlan frame = 
					new SmartPlan();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	/*************/
	/* SmartPlan */
	/*************/
	SmartPlan(){
		
		/* ================================== */
		/* Load the right language resources  */
		/* ================================== */
		
		try {
			scubaResources = ResourceBundle.getBundle(
					"resources.ScubaResources", Locale.getDefault());
		} catch (MissingResourceException mre) {
			mre.printStackTrace();
			return;
		}
		
		/* =============================================================== */
		/* Create a new JFrame container.
		 * Specify Layout and give the frame an initial size. 
		 * Terminate the program when the user closes the application.     */
		/* =============================================================== */

		jfrm = new JFrame(scubaResources.getString("Plan"));
		jfrm.setSize(450, 300);
		jfrm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		/* =========================== */
		/* Create the display panel. */
		/* =========================== */

		JPanel displayPanel = new JPanel();
		displayLabel = new JLabel();
		displayPanel.add(displayLabel);

		/* =========================== */
		/* Create the choices panel. */
		/* =========================== */

		choicesPanel = new JPanel();
		choicesButtons = new ButtonGroup();

		/* =========================== */
		/* Create the buttons panel. */
		/* =========================== */

		JPanel buttonPanel = new JPanel();

		prevButton = new JButton(scubaResources.getString("Prev"));
		prevButton.setActionCommand("Prev");
		buttonPanel.add(prevButton);
		prevButton.addActionListener(this);

		nextButton = new JButton(scubaResources.getString("Next"));
		nextButton.setActionCommand("Next");
		buttonPanel.add(nextButton);
		nextButton.addActionListener(this);

		confirmButton = new JButton(scubaResources.getString("Confirm"));
		confirmButton.setActionCommand("Confirm");
		buttonPanel.add(confirmButton);
		confirmButton.addActionListener(this);
		
		/* ===================================== */
		/* Add the panels to the content pane.   */
		/* ===================================== */
		jfrm.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("434px"),},
			new RowSpec[] {
				RowSpec.decode("45px"),
				FormFactory.GLUE_ROWSPEC,
				FormFactory.MIN_ROWSPEC,}));

		jfrm.getContentPane().add(displayPanel, "1, 1, fill, fill");
		jfrm.getContentPane().add(choicesPanel, "1, 2, fill, fill");
		jfrm.getContentPane().add(buttonPanel, "1, 3, fill, fill");

		/* ========================== */
		/* Load the program.		  */
		/* ========================== */

		clips = new Environment();
		clips.load("scuba.clp");
		clips.reset();
		runPlan();
		/* ==================== */
		/* Display the frame. */
		/* ==================== */

		jfrm.setVisible(true);
	}
	
	
	/*************/
	/* runPlan   */
	/*************/
	public void runPlan() {
		Runnable runThread = new Runnable() {
			public void run() {
				clips.run();
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							nextUIState();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		};
		isExecuting = true;
		executionThread = new Thread(runThread);
		executionThread.start();
	}
	
	
	/****************/
	/* nextUIState: */
	/****************/
	private void nextUIState() throws Exception {
		/* ===================== */
		/* Get the state-list. */
		/* ===================== */

		String evalStr = "(find-all-facts ((?f state-list)) TRUE)";
		String currentID = clips.eval(evalStr).get(0).getFactSlot("current").toString();
		
		/* =========================== */
		/* Get the current UI state. */
		/* =========================== */
		
		evalStr = "(find-all-facts ((?f UI-state)) " + "(eq ?f:id " + currentID + "))";
		
		PrimitiveValue currentUI = clips.eval(evalStr).get(0);
		
		/* ======================================== */
		/* Determine the Next/Prev button states. */
		/* ======================================== */

		if (currentUI.getFactSlot("state").toString().equals("final")) {
			nextButton.setActionCommand("Restart");
			nextButton.setText(scubaResources.getString("Restart"));
			prevButton.setVisible(true);
			confirmButton.setVisible(false);
		} else if (currentUI.getFactSlot("state").toString().equals("initial")) {
			nextButton.setActionCommand("Next");
			nextButton.setText(scubaResources.getString("Next"));
			prevButton.setVisible(false);
			confirmButton.setVisible(false);
		} else if (currentUI.getFactSlot("state").toString().equals("diver-definition")) {
			confirmButton.setActionCommand("Confirm");
			confirmButton.setText(scubaResources.getString("Confirm"));
			confirmButton.setVisible(true);
			nextButton.setVisible(false);
			prevButton.setVisible(true);
		} else {
			nextButton.setActionCommand("Next");
			nextButton.setText(scubaResources.getString("Next"));
			prevButton.setVisible(true);
			nextButton.setVisible(true);
			confirmButton.setVisible(false);
		}

		/* ===================== */
		/* Set up the choices. */
		/* ===================== */
		
		choicesPanel.setLayout(new FlowLayout());
		choicesPanel.removeAll();
		
		choicesButtons = new ButtonGroup();
		choicesCheckBoxes = new ArrayList<JCheckBox>();
		calendar = new JCalendar();
		
		PrimitiveValue validAnswersPV = currentUI.getFactSlot("valid-answers");
		PrimitiveValue typeAnswerPV = currentUI.getFactSlot("type-answer");
		
		/* Ho diversi casi di risposta:
		 * - radioButton (di default)
		 * - checkButton
		 * - message
		 * - birthday
		 */
		
		switch(typeAnswerPV.getValue().toString()){
			case "radio": {
				String selected = currentUI.getFactSlot("response").get(0).toString();
				
				for (int i = 0; i < validAnswersPV.size(); i++) {
					PrimitiveValue chooseRadioBtnPV = validAnswersPV.get(i);
					JRadioButton rButton;
		
					if (chooseRadioBtnPV.toString().equals(selected)) {
						rButton = new JRadioButton(scubaResources.getString(
								chooseRadioBtnPV.toString()), true);
					} else {
						rButton = new JRadioButton(scubaResources.getString(
								chooseRadioBtnPV.toString()), false);
					}
		
					rButton.setActionCommand(chooseRadioBtnPV.toString());
					choicesPanel.add(rButton);
					choicesButtons.add(rButton);
				}
				
				if (validAnswersPV.size()>5){
					choicesPanel.setLayout(new GridLayout(0, 2, 1, 1));
				} else if (validAnswersPV.size()>2){
					choicesPanel.setLayout(new GridLayout(0, 1, 0, 0));
				} else {
					choicesPanel.setLayout(new FlowLayout());
				}
				
			} break;
				
			case "check": {
				PrimitiveValue selectedPV = currentUI.getFactSlot("response");
				
				choicesPanel.setLayout(new GridLayout(0, 2, 0, 0));
				
				for (int i=0; i<validAnswersPV.size(); i++) {
					String answer=validAnswersPV.get(i).toString();
					
					JCheckBox checkBox = new JCheckBox(scubaResources.getString(answer));
					
					for(int j=0;j<selectedPV.size();j++){
						if(answer.equals(selectedPV.get(j).toString())){
							checkBox.setSelected(true);
						}
					}
					choicesCheckBoxes.add(checkBox);
					checkBox.setActionCommand(answer);
					choicesPanel.add(checkBox);
				}
				
			} break;
				
			case "message": {
				
				choicesPanel.setLayout(new FlowLayout());
				
				String subMessage = validAnswersPV.get(0).stringValue();
				PrimitiveValue sub = clips.eval("(find-all-facts ((?f subacqueo)) TRUE)").get(0);
				stampDiver(sub, subMessage);
				
			} break;
			
			case "birthday": {
				PrimitiveValue selectedPV = currentUI.getFactSlot("response");
				
				String date = selectedPV.toString();
				if (!date.equals("(none)")){
					date = selectedPV.get(0).toString();
					java.util.Date rightFormatDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
					calendar.setDate(rightFormatDate);
				}
				
				choicesPanel.setLayout(new FlowLayout());
				calendar.setDoubleBuffered(false);
				choicesPanel.add(calendar);
				
			} break;
			
		}
		
		choicesPanel.repaint();
		
		/* ==================================== */
		/* Controllo i dati dal clp			    */
		/* ==================================== */
		
		System.out.println("");
		System.out.println(currentUI+": "+scubaResources.getString(currentUI.getFactSlot("display").toString()));	
		System.out.println("Fatti: ");
		clips.eval("(facts)");
		
		/* ==================================== */
		/* Set the label to the display text.   */
		/* ==================================== */
		
		String theText = scubaResources.getString(currentUI.getFactSlot("display").symbolValue());

		wrapLabelText(displayLabel, theText);

		executionThread = null;

		isExecuting = false;
	}
	
	
	
	private void stampDiver(PrimitiveValue sub, String subMessage) throws Exception {

		String name=getNameDiver(sub);
		if (!name.isEmpty())
			name="<p>  > Nome: "+name;
		
		int età=getAgeDiver(sub);
		String age;
		if (età!=0)
			age = "<p>  > Età: "+età+" anni";
		else age="<p>  > Età: non pervenuta";
		
		switch (subMessage){
		
			case "Esordiente": {
				choicesPanel.add(new JLabel("<html>"+subMessage+
						"<p>Subacqueo: "+name+age+"</html>"));
			} break;
			
			case "Sub senza brevetto": {
				String deep = "<p>  > Profondità massima raggiunta: "+getDeepDiver(sub)+"  metri";
				String numberDives = "<p>  > Numero di immersioni: "+getNumberOfDives(sub);
				
				choicesPanel.add(new JLabel("<html>"+subMessage+"<p>Subacqueo: "+
						name+age+deep+numberDives+"</html>"));
			} break;
			
			case "Sub con brevetto": {
				String deep = "<p>  > Profondità massima raggiunta: "+getDeepDiver(sub)+"  metri";
				String numberDives = "<p>  > Numero di immersioni: "+getNumberOfDives(sub);
				String licence = "<p>  > Livello brevetto: "+getLicenceDiver(sub);
				String specialty = "<p>  > Specialità acquisite: "+getSpecialtyDiver(sub);
				
				choicesPanel.add(new JLabel("<html>"+subMessage+"<p>Subacqueo: "+
						name+age+numberDives+deep+licence+specialty+"</html>"));
			} break;
			
			case "Non sono riuscito a definire la tua esperienza": {
				choicesPanel.add(new JLabel("<html>È occorso un errore.<p>"+subMessage+"<p>Riprova</html>"));
				
				nextButton.setActionCommand("Restart");
				nextButton.setText(scubaResources.getString("Restart"));
				prevButton.setVisible(false);
				confirmButton.setVisible(false);
				nextButton.setVisible(true);
			} break;
		}
	}

	private String getSpecialtyDiver(PrimitiveValue sub) throws Exception {
		int size = sub.getFactSlot("specialty").size();
		String specialty="";
		for (int i=0;i<size;i++){
			specialty += "<p>"+scubaResources.getString(
					sub.getFactSlot("specialty").get(i).toString());
		}
		if (specialty.equals(""))
			specialty=scubaResources.getString("nothing");
		
		return specialty;
	}

	private String getLicenceDiver(PrimitiveValue sub) throws Exception {
		return scubaResources.getString(sub.getFactSlot("brevetto").toString());
	}

	private String getNumberOfDives(PrimitiveValue sub) throws Exception {
		return scubaResources.getString(sub.getFactSlot("numero-immersioni").toString());
	}

	private String getDeepDiver(PrimitiveValue sub) throws Exception {
		return scubaResources.getString(sub.getFactSlot("max-deep").toString());
	}

	private int getAgeDiver(PrimitiveValue sub) throws Exception {
		String ageString = sub.getFactSlot("data-nascita").toString();
		int age = 0;
		if(!ageString.equals("nil")){
			Date dateOfBirth=new SimpleDateFormat("dd-MM-yyyy").parse(ageString);
			Calendar dob = Calendar.getInstance();  
			dob.setTime(dateOfBirth);  
			Calendar today = Calendar.getInstance();  
			age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);  
			if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
			  age--;
			} else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
			    && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
			  age--;  
			}
		}
		return age;
	}

	private String getNameDiver(PrimitiveValue sub) throws Exception {
		PrimitiveValue subName=sub.getFactSlot("nome");
		int size = sub.getFactSlot("nome").size();
		String name="";
		for (int i=0; i<size;i++){
			name += subName.get(i).toString();
		}
		return name;
	}

	
	/*********************/
	/* actionPerformed   */
	/*********************/
	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			onActionPerformed(ae);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*********************/
	/* onActionPerformed */
	/*********************/
	public void onActionPerformed(ActionEvent ae) throws Exception {
		if (isExecuting)
			return;

		/* ===================== */
		/* Get the state-list. */
		/* ===================== */

		String evalStr = "(find-all-facts ((?f state-list)) TRUE)";

		String currentID = clips.eval(evalStr).get(0).getFactSlot("current").toString();

		/* ========================= */
		/* Handle the Next button. */
		/* ========================= */
		
		switch (ae.getActionCommand()){
		
			case "Next": {
				
				// TODO sistemare le if!
				
				if (choicesButtons.getButtonCount()==0
						&& choicesCheckBoxes.isEmpty()&&!calendar.isShowing()) {
					
					clips.assertString("(next " + currentID + ")");
					System.out.println("(next " + currentID + ") --> PRIMO IF ");
					
				} else {
					
					if (choicesButtons.getButtonCount()!=0){
						
						clips.assertString("(next " + currentID + " "
								+ choicesButtons.getSelection().getActionCommand() + ")");
						System.out.println("(next " + currentID + " "
								+ choicesButtons.getSelection().getActionCommand() + ") --> SECONDO IF ");
						
					} else if (!choicesCheckBoxes.isEmpty()){
						
						String checkboxes="";
						Iterator<JCheckBox> I = choicesCheckBoxes.iterator();
						while (I.hasNext()){
							JCheckBox jcb = I.next();
							if (jcb.isSelected()){
								checkboxes+=jcb.getActionCommand()+" ";
							}
						}
						clips.assertString("(next " + currentID + " " + checkboxes + ")");
						System.out.println("(next " + currentID + " " + checkboxes + ") --> TERZO IF");
						
					} else {
						
						String date = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getDate());
						clips.assertString("(next " + currentID + " " + date + ")");
						System.out.println("(next " + currentID + " " + date + ") --> QUARTO IF");
					}
				}
				
				runPlan();
			} break;
			
			case "Restart": {
				clips.reset();
				runPlan();
			} break;
			
			case "Prev": {
				clips.assertString("(prev " + currentID + ")");
				System.out.println("(prev " + currentID + ")");
				
				runPlan();
			} break;
			
			case "Confirm": {
				
				int result = JOptionPane.showConfirmDialog(jfrm, "Vuoi salvare il profilo prima di continuare?");
				
				if (result==JOptionPane.OK_OPTION){
					//Profilo.
				}
					
				// ATTENZIONE
				//System.out.println("CONFIRM->(prev " + currentID + ")");
				
				//clips.assertString("(prev " + currentID + ")");
				
				//runPlan();
			} break;
			
		}
		
	}
	
	
	
	/*****************/
	/* wrapLabelText */
	/*****************/
	private void wrapLabelText(JLabel label, String text) {
		FontMetrics fm = label.getFontMetrics(label.getFont());
		Container container = label.getParent();
		int containerWidth = container.getWidth();
		int textWidth = SwingUtilities.computeStringWidth(fm, text);
		int desiredWidth;

		if (textWidth <= containerWidth) {
			desiredWidth = containerWidth;
		} else {
			int lines = (int) ((textWidth + containerWidth) / containerWidth);
			desiredWidth = (int) (textWidth / lines);
		} 

		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);

		StringBuffer trial = new StringBuffer();
		StringBuffer real = new StringBuffer("<html><center>");

		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE;
				start = end, end = boundary.next()) {
			String word = text.substring(start, end);
			trial.append(word);
			int trialWidth = SwingUtilities.computeStringWidth(fm,
					trial.toString());
			if (trialWidth > containerWidth) {
				trial = new StringBuffer(word);
				real.append("<br>");
				real.append(word);
			} else if (trialWidth > desiredWidth) {
				trial = new StringBuffer("");
				real.append(word);
				real.append("<br>");
			} else {
				real.append(word);
			}
		}

		real.append("</html>");

		label.setText(real.toString());
	}
	
}
