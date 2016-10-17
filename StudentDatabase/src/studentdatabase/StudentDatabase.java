/*
 * Copyright 2016 bbroadstone
 */
package studentdatabase;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 *
 * @author bbroadstone
 */
public class StudentDatabase extends JFrame {
	static final int WINDOWWIDTH = 300, WINDOWHEIGHT = 200;
	private static final String[] LETTER_GRADES = 
        { "A", "B", "C", "D", "F"};
	private static Integer[] CREDS = { 1, 3, 4, 6 };

	// Hashmap for data and fields for program aesthetics.
	private static HashMap<String, Student> studentMajorGradeMap = 
                new HashMap<String, Student>();
        private static JTextField fieldForID = new JTextField();
	private static JTextField fieldForName = new JTextField();
	private static JTextField fieldForMajor = new JTextField();
	private static JComboBox fieldForChooseSelection = new JComboBox();
	private static JButton buttonForProcessRequest = new JButton(
			"Process Request");
	private static JOptionPane frame = new JOptionPane();
	private static JLabel labelForID = new JLabel("Id: ");
	private static JLabel labelForName = new JLabel("Name: ");
	private static JLabel labelForMajor = new JLabel("Major: ");
	private static JLabel labelForChooseSelection = new JLabel(
			"Choose Selection: ");
	
	private void setFrame(int width, int height) {
		setSize(width, height);
		setLocationRelativeTo(null);
	}

	public void display() {
		setVisible(true);
	}

	public StudentDatabase() {
		super("Project 4");
		
		// Extracted related tasks to methods
		JPanel mainPanel = createMainFrame();
                addPanels(mainPanel);
                createFieldForChooseSelection(mainPanel);
                createProcessRequestButton(mainPanel);
	}

	// Adds the panels to the main panel
	private void addPanels(JPanel mainPanel) {
		JComponent[] components = { labelForID, fieldForID,
				labelForName, fieldForName, labelForMajor,
                                fieldForMajor,
				labelForChooseSelection };

		addToPanel(mainPanel, components);
	}

	private void addToPanel(JPanel p, JComponent[] c) {
		// For each component in components, add the component to panel
		for (JComponent component : c)
			p.add(component);
	}

	private void addItemsToField(JComboBox field, String[] items) {
		for (String item : items)
			field.addItem(item);
	}

	// Create the choose selection
	private void createFieldForChooseSelection(JPanel mainPanel) {
		mainPanel.add(fieldForChooseSelection);

		String[] items = { "Insert", "Delete", "Find", "Update" };
		addItemsToField(fieldForChooseSelection, items);
	}

	private void createProcessRequestButton(JPanel mainPanel) {
		// Action Listener for process button.
		mainPanel.add(buttonForProcessRequest);
		buttonForProcessRequest
				.addActionListener(new 
        buttonForProcessRequestListener());
	}

	private JPanel createMainFrame() {
		setFrame(WINDOWWIDTH, WINDOWHEIGHT);
		setResizable(false);
		JPanel mainPanel = new JPanel();
		add(mainPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel.setLayout(new GridLayout(5, 5, 10, 10));
		return mainPanel;
	}

	class buttonForProcessRequestListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object selection = 
                                fieldForChooseSelection.getSelectedItem();

			if (selection.equals("Insert"))
				insertPerformed();

			if (selection.equals("Delete"))
				deletePerformed();

			if (selection.equals("Find"))
				findPerformed();

			if (selection.equals("Update"))
				updatePerformed();
		}
                
		private void preventBlankFields(JTextField[] requiredFields) {
			for (JTextField field : requiredFields)
				preventBlankField(field);
		}
		
		private void updatePerformed() {
			// Always prevent these fields from being blank
			preventBlankFields(new JTextField[] 
                        { fieldForID, fieldForName,
					fieldForMajor });
			String id = fieldForID.getText();
			Student student = studentMajorGradeMap.get(id);
			if (student == null) {
				showMessage("Entry not found in database!");
				return;
			}

			if (!studentInfoMatchesInput(student)) {
				JOptionPane.showMessageDialog(frame, 
                                        "ID/information mismatch!");
				return;
			}

			String grade = askForGrade(LETTER_GRADES);
			Integer cred = askForCred(CREDS);

			if (grade == null || cred == null) {
				showMessage("Cancelled.");
			} else {
				Integer gradeAsNumber = gradeStringToInt(grade);
                                student.courseCompleted(cred, gradeAsNumber);
				showMessage("Success!");
			}
		}

		// Check if the input from the name and major fields matches 
                //the student info
		private boolean studentInfoMatchesInput(Student student) {
			String name = fieldForName.getText();
			String major = fieldForMajor.getText();
			boolean nameMatches =  student.getName().equals(name);
			boolean majorMatches = student.getMajor().equals(major);
			boolean studentInfoMatchesInput = 
                                (nameMatches && majorMatches);
			return studentInfoMatchesInput;
		}

		private Integer askForCred(Integer[] creds) {
			return (Integer) JOptionPane.showInputDialog(null,
                                "Choose " + "Credit", "Credit", 
                                JOptionPane.QUESTION_MESSAGE, null,
					creds, creds[0]);
		}

		private String askForGrade(String[] letterGrade) {
			return (String) JOptionPane.showInputDialog(frame, 
                                "Choose Grade", "Grade", 
                                JOptionPane.QUESTION_MESSAGE, null, letterGrade,
					letterGrade[0]);
		}

		private Integer gradeStringToInt(String grade) {
			/**
                         * When grade is not in the list, it will then return 
                         * 0 since indexOf() returns -1 if the element is not in
                         * the list. If grade is "D",the index of "D" is 0 and 
                         * it will return 1. And so on. Arrays.asList is used 
                         * because it returns a List object which has the 
                         * indexOf method.
                         */
                        return Arrays.asList("F" + "D", "C", "B", "A")
                                .indexOf(grade) + 1;
                                
		}

		private void findPerformed() {
			// Always prevent these fields from being blank
			preventBlankFields(new JTextField[] 
                        { fieldForID, fieldForName,
					fieldForMajor });
                        String id = fieldForID.getText();
			Student student = studentMajorGradeMap.get(id);
                        if (student == null) {
                            showMessage("Entry not found in database!");
                        }
                        else if (!studentInfoMatchesInput(student)) {
				JOptionPane.showMessageDialog(frame, 
                                        "ID/information mismatch!");
				
			}
                        else if (studentMajorGradeMap.containsKey(id)) {
				JOptionPane.showMessageDialog(null, student, "Find", 
                                        JOptionPane.INFORMATION_MESSAGE);
			} 
                        else  {
				showMessage("Entry not found in database!");
			}
                    
		}

		private void deletePerformed() {
                        preventBlankFields(new JTextField[] 
                        { fieldForID, fieldForName,
					fieldForMajor });
                        String id = fieldForID.getText();
			Student student = studentMajorGradeMap.get(id);
                        if (student == null) {
                            showMessage("Entry not found in database!");
                        }
                        else if (!studentInfoMatchesInput(student)) {
				JOptionPane.showMessageDialog(frame, 
                                        "ID/information mismatch!");   
			} else if (studentMajorGradeMap.containsKey(fieldForID.getText())) {
				studentMajorGradeMap.remove(fieldForID.getText());
				showMessage("Success!");
			} else {
				showMessage("Entry not found in database!");
			}

		}

		private void insertPerformed() {
			String id = fieldForID.getText();
			
			if (studentMajorGradeMap.containsKey(id)) {
				showMessage("Please enter an ID that does "
                                        + "not already exist.");
			} else {
				String name = fieldForName.getText();
				String major = fieldForMajor.getText();
				Student student = new Student(name, major);
				
				//studentMajorGradeMap.get(id) == null since there is nothing with key id
				
				// Store student usign key id
				studentMajorGradeMap.put(id, student);
				
				//studentMajorGradeMap.get(id) now returns the student
				showMessage("Success!");
			}
		}

		private void showMessage(String msg) {
			JOptionPane.showMessageDialog(frame, msg);
		}
		
		// Functions dealing with blank fields
		private boolean isBlank(JTextField field) {
			return field.getText().equals("");
		}

		private void preventBlankField(JTextField requiredField) {
			if (isBlank(requiredField))
				showMessage("Entry cannot be blank. Try again.");
		}

	}

	public static void main(String[] args) {
		StudentDatabase db = new StudentDatabase();
		db.display();
	}
}