import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;

public class GradeGeneratorFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    GridBagConstraints constraints;
    private MenuBar menuBar;
    private Menu helpMenu;
    private MenuItem helpSystem;
    private MenuItem helpProcess;
    private JPanel gradePanel;
    private JPanel setupPanel;
    private JLabel subjectLabel;
    private JComboBox<String> subjectBox;
    private JPanel buttonPanel;
    private JButton sourceButton;
    private JButton examButton;
    private JButton setupButton;
    private JLabel wrWorkLabel;
    private JSpinner wrWorkSpinner;
    private JLabel perfTaskLabel;
    private JSpinner perfTaskSpinner;
    private JPanel statusPanel;
    private JLabel sourceLabel;
    private JTextField sourceField;
    private JLabel statusLabel;
    private JTextField statusField;
    private JButton generateButton;

    private ArrayList<Integer> srcGrades;
    private ArrayList<Integer[]> grades;
    private int[] wrWorkHPS;
    private int[] perfTaskHPS;
    private int examHPS;
    private int[] examScores;

    private static final String[] SUBJECTS = {"MTB","Filipino","English",
                                              "Math","AP", "ESP","Music",
                                              "Arts","PE","Health"};

    private static final HashMap<String,Float[]> WEIGHTS;

    static {
        WEIGHTS = new HashMap<String, Float[]>();

        for (int i = 0; i < SUBJECTS.length; ++i){
            if (SUBJECTS[i].equalsIgnoreCase("MTB") || 
                SUBJECTS[i].equalsIgnoreCase("FILIPINO") || 
                SUBJECTS[i].equalsIgnoreCase("ENGLISH") || 
                SUBJECTS[i].equalsIgnoreCase("AP") || 
                SUBJECTS[i].equalsIgnoreCase("ESP")){
                WEIGHTS.put(SUBJECTS[i], new Float[]{0.3F,0.5F,0.2F});
            }
            else if (SUBJECTS[i].equalsIgnoreCase("SCIENCE") || 
                     SUBJECTS[i].equalsIgnoreCase("MATH")){
                WEIGHTS.put(SUBJECTS[i], new Float[]{0.4F,0.4F,0.2F});
            }
            else if (SUBJECTS[i].equalsIgnoreCase("MUSIC") || 
                     SUBJECTS[i].equalsIgnoreCase("ARTS") || 
                     SUBJECTS[i].equalsIgnoreCase("PE") || 
                     SUBJECTS[i].equalsIgnoreCase("HEALTH")){
                WEIGHTS.put(SUBJECTS[i], new Float[]{0.2F,0.6F,0.2F});
            }
        }
    }

    public GradeGeneratorFrame(){
        setTitle("Grade Generator");

        menuBar = new MenuBar();

        helpMenu = new Menu("Help");

        helpSystem = new MenuItem("System");
        helpProcess = new MenuItem("Process");

        helpSystem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){

            }
        });

        helpProcess.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                
            }
        });

        helpMenu.add(helpSystem);
        helpMenu.add(helpProcess);

        menuBar.add(helpMenu);

        setMenuBar(menuBar);

        subjectLabel = new JLabel("Subject:");
        subjectBox = new JComboBox<String>(SUBJECTS);
        subjectBox.setSelectedIndex(0);
        subjectBox.setFocusable(false);

        SpinnerModel wrWorkModel = new SpinnerNumberModel(1, 1, 10, 1);
        wrWorkLabel = new JLabel("WW Item:");
        wrWorkSpinner = new JSpinner(wrWorkModel);
        ((DefaultEditor)wrWorkSpinner.getEditor()).getTextField().setEditable(false);
        wrWorkLabel.setToolTipText("Written Works");
        
        SpinnerModel perfTaskModel = new SpinnerNumberModel(1, 1, 10, 1);
        perfTaskLabel = new JLabel("PT Item:");
        perfTaskSpinner = new JSpinner(perfTaskModel);
        ((DefaultEditor)perfTaskSpinner.getEditor()).getTextField().setEditable(false);
        perfTaskLabel.setToolTipText("Performance Tasks");

        constraints = new GridBagConstraints();
        
        setupPanel = new JPanel(new GridBagLayout());
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        setupPanel.add(subjectLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        setupPanel.add(subjectBox, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        setupPanel.add(wrWorkLabel, constraints);

        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        setupPanel.add(wrWorkSpinner, constraints);

        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        setupPanel.add(perfTaskLabel, constraints);

        constraints.gridx = 5;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        setupPanel.add(perfTaskSpinner, constraints);

        sourceButton = new JButton("Grade Source");
        sourceButton.setToolTipText("Select a file for Target Grades Input");
        sourceButton.setFocusable(false);

        sourceButton.addActionListener(new SourceListener());

        examButton = new JButton("Exam Scores");
        examButton.setToolTipText("Select a file for Exam Scores Input");
        examButton.setFocusable(false);

        examButton.addActionListener(new ExamListener());

        setupButton = new JButton("Set HPS");
        setupButton.setToolTipText("Set High Possible Scores");
        setupButton.setFocusable(false);

        setupButton.addActionListener(new SetupListener());

        buttonPanel = new JPanel(new GridBagLayout());
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(sourceButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(setupButton, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(examButton, constraints);

        sourceLabel = new JLabel("Source:");
        sourceField = new JTextField(30);
        sourceField.setEditable(false);

        statusLabel = new JLabel("Status:");
        statusField = new JTextField(30);
        statusField.setText("Please select a source file first.");
        statusField.setEditable(false);

        statusPanel = new JPanel(new GridBagLayout());
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        statusPanel.add(sourceLabel, constraints);
        
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.WEST;
        statusPanel.add(sourceField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.EAST;
        statusPanel.add(statusLabel, constraints);
        
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        statusPanel.add(statusField, constraints);

        generateButton = new JButton("Generate");
        generateButton.setFocusable(false);

        generateButton.addActionListener(new GenerateListener());

        gradePanel = new JPanel(new GridBagLayout());
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTH;
        gradePanel.add(setupPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        gradePanel.add(buttonPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        gradePanel.add(statusPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.SOUTH;
        gradePanel.add(generateButton, constraints);

        gradePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        add(gradePanel);

        pack();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent event){
                int response = JOptionPane.showConfirmDialog(rootPane, "Do you want to exit", 
                "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION){
                    dispose();
                }
            }
        });
    }

    private class SourceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                JFileChooser sourceFile = new JFileChooser();
                sourceFile.showOpenDialog(rootPane);

                FileReader fReader = new FileReader(sourceFile.getSelectedFile());
                BufferedReader bReader = new BufferedReader(fReader);

                ArrayList<Integer> tempGrades = new ArrayList<Integer>();

                String gradeStr = null;
                while ((gradeStr = bReader.readLine()) != null){

                    if (gradeStr.isEmpty()){
                        bReader.close();
                        throw new IOException("Empty String Detected!");
                    }

                    Integer grade = Integer.valueOf(gradeStr);

                    tempGrades.add(grade);
                }

                String sourceStr = sourceFile.getSelectedFile().toString();
                statusField.setText("The source file has been retrieved successfully.");
                sourceField.setText(sourceStr);

                srcGrades = tempGrades;

                bReader.close();
            }
            catch (IOException e){
                JOptionPane.showMessageDialog(rootPane, "IOException:\n" 
                + e.getMessage(), "I/O Exception", JOptionPane.ERROR_MESSAGE);
                statusField.setText("The source file has not been retrieved successfully.");
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
                + e.getMessage(), "Number Format Exception", JOptionPane.ERROR_MESSAGE);
                statusField.setText("The source file has not been retrieved successfully.");
            }
            catch (NullPointerException e){
                // Expected Exception
            }
            catch (Exception e){
                e.printStackTrace();
				JOptionPane.showMessageDialog(rootPane, "Unimplemented Exception:\n" 
				+ e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            }
            finally {
                if (wrWorkHPS != null && perfTaskHPS != null && !(sourceField.getText().isEmpty())){
                    statusField.setText("The Grade Generator is ready for use.");
                }
            }
        }
    }

    private class SetupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (sourceField.getText().isEmpty()){
                JOptionPane.showMessageDialog(rootPane, "You must set the source file first.", 
                "Null Source", JOptionPane.WARNING_MESSAGE);
            }
            else {
                setupHPS();
            }
        }
    }

    private void setupHPS(){
        int workItem = (int)wrWorkSpinner.getValue();
        JLabel[] workLabels = new JLabel[workItem];
        JTextField[] workFields = new JTextField[workItem];

        int taskItem = (int)perfTaskSpinner.getValue();
        JLabel[] taskLabels = new JLabel[taskItem];
        JTextField[] taskFields = new JTextField[taskItem];

        JLabel examLabel = new JLabel("Exam HPS :");
        JTextField examField = new JTextField();

        Object[] prompt = new Object[(2 * (workItem + taskItem + 1))];
        int pIndex = 0;

        Object[] options = {"Save","Cancel"};

        for (int i = 0; i < workItem; ++i){
            workLabels[i] = new JLabel("Written Work # "+(i+1));
            prompt[pIndex++] = workLabels[i].getText();
            workFields[i] = new JTextField();
            prompt[pIndex++] = workFields[i];
        }

        for (int i = 0; i < taskItem; ++i){
            taskLabels[i] = new JLabel("Performance Task # "+(i+1));
            prompt[pIndex++] = taskLabels[i].getText();
            taskFields[i] = new JTextField();
            prompt[pIndex++] = taskFields[i];
        }

        prompt[pIndex++] = examLabel.getText();
        prompt[pIndex++] = examField;

        JOptionPane optionPane = new JOptionPane(prompt, JOptionPane.PLAIN_MESSAGE, 
        JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);

        String subject = String.valueOf(subjectBox.getSelectedItem());

        JDialog dialog = new JDialog(this, subject, true);
        dialog.setContentPane(optionPane);

        optionPane.addPropertyChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent event){
                if (JOptionPane.VALUE_PROPERTY.equals(event.getPropertyName())){
                    if (optionPane.getValue().equals(options[0])){
                        optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
                        try {
                            String[] workStrings = new String[workFields.length];
                            for (int i = 0; i < workStrings.length; ++i){
                                workStrings[i] = workFields[i].getText();
                            }

                            String[] taskStrings = new String[taskFields.length];
                            for (int i = 0; i < taskStrings.length; ++i){
                                taskStrings[i] = taskFields[i].getText();
                            }

                            for (int i = 0; i < workStrings.length; ++i){
                                if (workStrings[i].isEmpty()){
                                    JOptionPane.showMessageDialog(rootPane, "You must fill all the required fields.", 
                                    "Empty Field", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            }

                            for (int i = 0; i < taskStrings.length; ++i){
                                if (taskStrings[i].isEmpty()){
                                    JOptionPane.showMessageDialog(rootPane, "You must fill all the required fields.", 
                                    "Empty Field", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            }

                            String examStr = examField.getText();
                            if (examStr.isEmpty()){
                                JOptionPane.showMessageDialog(rootPane, "You must fill all the required fields.", 
                                "Empty Field", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            int[] workHPS = new int[workStrings.length];
                            for (int i = 0; i < workHPS.length; ++i){
                                workHPS[i] = Integer.parseInt(workStrings[i]);
                                if (workHPS[i] <= 0){
                                    JOptionPane.showMessageDialog(rootPane, "HPS must be a positive integer.", 
                                    "HPS Value", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            }

                            int[] taskHPS = new int[taskStrings.length];
                            for (int i = 0; i < taskHPS.length; ++i){
                                taskHPS[i] = Integer.parseInt(taskStrings[i]);
                                if (taskHPS[i] <= 0){
                                    JOptionPane.showMessageDialog(rootPane, "HPS must be a positive integer.", 
                                    "HPS Value", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            }

                            int examTemp = Integer.parseInt(examStr);
                            if (examTemp <= 0){
                                JOptionPane.showMessageDialog(rootPane, "HPS must be a positive integer.", 
                                "HPS Value", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            wrWorkHPS = workHPS;
                            perfTaskHPS = taskHPS;
                            examHPS = examTemp;
                            dialog.dispose();
                        }
                        catch (NumberFormatException e){
                            JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
                            + e.getMessage(), "Number Format Exception", JOptionPane.ERROR_MESSAGE);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(rootPane, "Unimplemented Exception:\n" 
                            + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                        }
                        finally {
                            if (wrWorkHPS != null && perfTaskHPS != null && !(sourceField.getText().isEmpty())){
                                statusField.setText("The Grade Generator is ready for use.");
                            }
                        }
                    }
                    else if (optionPane.getValue().equals(options[1])){
                        optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
                        dialog.dispose();
                    }
                }
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(rootPane);
        dialog.setVisible(true);
    }

    private class ExamListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (examHPS == 0){
                JOptionPane.showMessageDialog(rootPane, "You must set the Exam HPS first.", 
                "Null Exam HPS", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                JFileChooser sourceFile = new JFileChooser();
                sourceFile.showOpenDialog(rootPane);

                FileReader fReader = new FileReader(sourceFile.getSelectedFile());
                BufferedReader bReader = new BufferedReader(fReader);

                ArrayList<Integer> tempScores = new ArrayList<Integer>();
                
                String scoreStr = null;
                while ((scoreStr = bReader.readLine()) != null){

                    if (scoreStr.isEmpty()){
                        bReader.close();
                        throw new IOException("Empty String Detected!");
                    }

                    Integer score = Integer.valueOf(scoreStr);

                    if (score > examHPS){
                        bReader.close();
                        throw new IOException("Scores higher than HPS Detected!");
                    }

                    tempScores.add(score);
                }

                if (tempScores.size() != srcGrades.size()){
                    bReader.close();
                    throw new IOException("Exam Scores and Target Grades does not match.");
                }

                examScores = new int[srcGrades.size()];
                for (int i = 0; i < examScores.length; ++i){
                    examScores[i] = tempScores.get(i);
                }

                bReader.close();
            }
            catch (IOException e){
                JOptionPane.showMessageDialog(rootPane, "IOException:\n" 
                + e.getMessage(), "I/O Exception", JOptionPane.ERROR_MESSAGE);
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
                + e.getMessage(), "Number Format Exception", JOptionPane.ERROR_MESSAGE);
            }
            catch (NullPointerException e){
                // Expected Exception
            }
            catch (Exception e){
                e.printStackTrace();
				JOptionPane.showMessageDialog(rootPane, "Unimplemented Exception:\n" 
				+ e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class GenerateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (srcGrades == null){
                JOptionPane.showMessageDialog(rootPane, "You must set the source file first.", 
                "Null Source", JOptionPane.WARNING_MESSAGE);
            }
            else if (wrWorkHPS == null && perfTaskHPS == null){
                JOptionPane.showMessageDialog(rootPane, "You must set the HPS before generating.", 
                "Null HPS", JOptionPane.WARNING_MESSAGE);
            }
            else {
                grades = generateGrades(srcGrades, wrWorkHPS, perfTaskHPS);
            }
        }
    }

    private ArrayList<Integer[]> generateGrades(ArrayList<Integer> srcGrades, int[] wrWorkHPS, int[] perfTaskHPS){
        ArrayList<Integer[]> grades = null;

        String subject = String.valueOf(subjectBox.getSelectedItem());
        Float[] weights = WEIGHTS.get(subject);

        return grades;
    }
    
    public static void main(String[] args){
        GradeGeneratorFrame gradeFrame = new GradeGeneratorFrame();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            gradeFrame.setSize(450,300);
            gradeFrame.setResizable(false);
            gradeFrame.setLocationRelativeTo(null);
            gradeFrame.setVisible(true);
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(gradeFrame, "Unimplemented Exception:\n" 
            + e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }
}