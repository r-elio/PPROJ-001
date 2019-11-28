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
    private JPanel bottomPanel;
    private JButton generateButton;
    private JButton resetButton;

    private ArrayList<Integer> srcGrades;
    private ArrayList<Integer[]> grades;
    private int[] wrWorkHPS;
    private int[] perfTaskHPS;
    private int examHPS;
    private int[] examScores;
    private String examFile;

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

        resetButton = new JButton("Reset");
        resetButton.setFocusable(false);

        resetButton.addActionListener(new ResetListener());

        generateButton = new JButton("Generate");
        generateButton.setFocusable(false);

        generateButton.addActionListener(new GenerateListener());

        bottomPanel = new JPanel(new GridBagLayout());
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        bottomPanel.add(resetButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        bottomPanel.add(generateButton, constraints);

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
        constraints.anchor = GridBagConstraints.CENTER;
        gradePanel.add(bottomPanel, constraints);

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
            if (srcGrades != null){
                int response = JOptionPane.showConfirmDialog(rootPane, "Do you want to change the grade source?", 
                "Grade Source", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION){
                    setGradeSource();
                }
            }
            else {
                setGradeSource();
            }
        }
    }

    private void setGradeSource(){
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

                if (grade > 100){
                    bReader.close();
                    throw new IOException("Grades must not exceed to 100.");
                }
                else if (grade <= 0){
                    bReader.close();
                    throw new IOException("Grades must be positive integer.");
                }

                tempGrades.add(grade);
            }

            String sourceStr = sourceFile.getSelectedFile().toString();
            statusField.setText("The source file has been retrieved successfully.");
            sourceField.setText(sourceStr);

            srcGrades = tempGrades;

            System.out.println("srcGrades:");
            for (int i = 0; i < srcGrades.size(); ++i){
                System.out.print(srcGrades.get(i) + " ");
            }
            System.out.println();

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

        Object[] options = {"Save","Reset","Cancel"};

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

        if (wrWorkHPS != null && perfTaskHPS != null){
            for (int i = 0; i < workItem; ++i){
                String workStr = String.valueOf(wrWorkHPS[i]);
                workFields[i].setText(workStr);
            }

            for (int i = 0; i < taskItem; ++i){
                String taskStr = String.valueOf(perfTaskHPS[i]);
                taskFields[i].setText(taskStr);
            }

            String examStr = String.valueOf(examHPS);
            examField.setText(examStr);
        }

        JOptionPane optionPane = new JOptionPane(prompt, JOptionPane.PLAIN_MESSAGE, 
        JOptionPane.OK_CANCEL_OPTION, null, options);

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

                            wrWorkSpinner.setEnabled(false);
                            perfTaskSpinner.setEnabled(false);

                            wrWorkHPS = workHPS;
                            perfTaskHPS = taskHPS;
                            examHPS = examTemp;
                            dialog.dispose();

                            System.out.println("workHPS:");
                            for (int i = 0; i < wrWorkHPS.length; ++i){
                                System.out.print(wrWorkHPS[i] + " ");
                            }
                            System.out.println();
                            System.out.println("taskHPS:");
                            for (int i = 0; i < perfTaskHPS.length; ++i){
                                System.out.print(perfTaskHPS[i] + " ");
                            }
                            System.out.println();
                            System.out.println("examHPS: " + examHPS);
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
                        
                        wrWorkSpinner.setEnabled(true);
                        perfTaskSpinner.setEnabled(true);
                        wrWorkHPS = null;
                        perfTaskHPS = null;
                        examHPS = 0;
                        
                        dialog.dispose();
                    }
                    else if (optionPane.getValue().equals(options[2])){
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
            else if (examScores != null){
                int response = JOptionPane.showConfirmDialog(rootPane, "Do you want to change the exam scores?", 
                "Exam Scores", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION){
                    setExamScores();
                }
            }
            else {
                setExamScores();
            }
        }
    }

    private void setExamScores(){
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
                else if (score <= 0){
                    bReader.close();
                    throw new IOException("Scores must be positive integer.");
                }

                tempScores.add(score);
            }

            if (tempScores.size() != srcGrades.size()){
                bReader.close();
                throw new IOException("Exam Scores and Target Grades does not match.");
            }

            examFile = sourceFile.getSelectedFile().toString();

            examScores = new int[srcGrades.size()];
            for (int i = 0; i < examScores.length; ++i){
                examScores[i] = tempScores.get(i);
            }

            System.out.println("Exam Scores:");
            for (int i = 0; i < examScores.length; ++i){
                System.out.print(examScores[i] + " ");
            }
            System.out.println();

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
                int response = JOptionPane.showConfirmDialog(rootPane, "Exam Score Source:\n" + examFile + 
                "\nDo you want to continue?", "Generate", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION){
                    grades = generateGrades();
                    //writeGrades();
                }
            }
        }
    }

    private ArrayList<Integer[]> generateGrades(){
        ArrayList<Integer[]> grades = new ArrayList<Integer[]>();

        String subject = String.valueOf(subjectBox.getSelectedItem());
        Float[] weights = WEIGHTS.get(subject);

        for (int i = 0; i < srcGrades.size(); ++i){
            int grade = srcGrades.get(i);
            double initialGrade = randomInitialGrade(grade);

            double workWeight = initialGrade * weights[0];
            double taskWeight = initialGrade * weights[1];
            double examWeight = initialGrade * weights[2];

            int examScore;
            int[] workScores = new int[wrWorkHPS.length];
            int[] taskScores = new int[perfTaskHPS.length];

            int sumHPS = 0;
            for (int j = 0; j < wrWorkHPS.length; ++j){
                sumHPS += wrWorkHPS[j];
            }
            int workSum = Math.round((float)((workWeight * sumHPS) / (100 * weights[0])));

            sumHPS = 0;
            for (int j = 0; j < perfTaskHPS.length; ++j){
                sumHPS += perfTaskHPS[j];
            }
            int taskSum = Math.round((float)((taskWeight * sumHPS) / (100 * weights[1])));

            if (examScores != null){
                examScore = examScores[i];
            }
            else {
                examScore = Math.round((float)((examWeight * examHPS) / (100 * weights[2])));
            }

            System.out.println("Initial Grade: " + initialGrade);
            System.out.println("Work Weight: " + workWeight);
            System.out.println("Task Weight: " + taskWeight);
            System.out.println("Exam Weight: " + examWeight);
            System.out.println("Work Sum: " + workSum);
            System.out.println("Task Sum: " + taskSum);
            System.out.println("Exam Score: " + examScore);

            Integer[] gradeRow = new Integer[2 + workScores.length + taskScores.length];

            for (int j = 0; j < workScores.length; ++j){
                gradeRow[j] = workScores[j];
            }

            for (int j = 0; j < taskScores.length; ++j){
                gradeRow[workScores.length + j] = taskScores[j];
            }

            gradeRow[workScores.length + taskScores.length] = examScore;
            gradeRow[workScores.length + taskScores.length + 1] = grade;

            grades.add(gradeRow);
        }

        return grades;
    }

    private double randomInitialGrade(int grade){
        double initialGrade = 0.0;
        if (grade < 75){
            initialGrade = 4 * (grade - 60);
            initialGrade += 4 * Math.random();
        }
        else {
            initialGrade = 1.6 * (grade - 37.5);
            initialGrade += 1.6 * Math.random();
        }

        return initialGrade;
    }

    private void writeGrades(){
        try {
            File sourceFile = new File(sourceField.getText());
            FileWriter fWriter = new FileWriter(sourceFile);
            BufferedWriter bWriter = new BufferedWriter(fWriter);

            for (int i = 0; i < grades.size(); ++i){
                for (int j = 0; j < grades.get(i).length; ++j){
                    String valueStr = String.valueOf(grades.get(i)[j]);
                    bWriter.write(valueStr);
                    if (j < (grades.get(i).length - 1)){
                        bWriter.write("\t");
                    }
                }
                bWriter.write("\n");
            }

            bWriter.close();
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(rootPane, "IOException:\n" 
            + e.getMessage(), "I/O Exception", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, "Unimplemented Exception:\n" 
            + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class ResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int response = JOptionPane.showConfirmDialog(rootPane, "Do you want to reset?", 
            "Reset", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION){
                srcGrades = null;
                wrWorkHPS = null;
                perfTaskHPS = null;
                examHPS = 0;
                examScores = null;
                examFile = null;
                sourceField.setText("");
                statusField.setText("Please select a source file first.");
            }
        }
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