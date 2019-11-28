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

    private static final String[] SUBJECTS = {"MTB","Filipino","English",
                                              "Math","AP", "ESP","Music",
                                              "Arts","PE","Health"};

    private ArrayList<Integer[]> srcGrades;
    private int[] wrWorkHPS;
    private int[] perfTaskHPS;

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

        sourceButton = new JButton("Source Text");
        sourceButton.setFocusable(false);

        sourceButton.addActionListener(new SourceListener());

        setupButton = new JButton("Setup HPS");
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

        sourceLabel = new JLabel("Source:");
        sourceField = new JTextField(30);
        sourceField.setEditable(false);

        statusLabel = new JLabel("Status:");
        statusField = new JTextField(30);
        statusField.setText("Please select a source text file and setup the HPS.");
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

                ArrayList<Integer[]> tempGrades = new ArrayList<Integer[]>();

                String line = null;
                while ((line = bReader.readLine()) != null){
                    String[] gradeStr = line.split("[ \t]");

                    for (int i = 0; i < gradeStr.length; ++i){
                        if (gradeStr[i].isEmpty()){
                            bReader.close();
                            throw new IOException("Empty String Detected");
                        }
                    }

                    Integer[] gradeInt = new Integer[gradeStr.length];
                    for (int i = 0; i < gradeInt.length; ++i){
                        gradeInt[i] = Integer.valueOf(gradeStr[i]);
                    }

                    tempGrades.add(gradeInt);
                }

                String sourceStr = sourceFile.getSelectedFile().toString();
                statusField.setText("The Source Text has been retrieved successfully.");
                sourceField.setText(sourceStr);

                srcGrades = tempGrades;

                bReader.close();
            }
            catch (IOException e){
                JOptionPane.showMessageDialog(rootPane, "IOException:\n" 
                + e.getMessage(), "I/O Exception", JOptionPane.ERROR_MESSAGE);
                statusField.setText("The Source Text has not been retrieved successfully.");
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
                + e.getMessage(), "Number Format Exception", JOptionPane.ERROR_MESSAGE);
                statusField.setText("The Source Text has not been retrieved successfully.");
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

    private class SetupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            setupHPS();
        }
    }

    private void setupHPS(){
        int workItem = (int)wrWorkSpinner.getValue();
        JLabel[] workLabels = new JLabel[workItem];
        JTextField[] workFields = new JTextField[workItem];

        int taskItem = (int)perfTaskSpinner.getValue();
        JLabel[] taskLabels = new JLabel[taskItem];
        JTextField[] taskFields = new JTextField[taskItem];

        Object[] prompt = new Object[(2 * (workItem + taskItem))];
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

                            int[] workHPS = new int[workStrings.length];
                            for (int i = 0; i < workHPS.length; ++i){
                                workHPS[i] = Integer.parseInt(workStrings[i]);
                            }

                            int[] taskHPS = new int[taskStrings.length];
                            for (int i = 0; i < taskHPS.length; ++i){
                                taskHPS[i] = Integer.parseInt(taskStrings[i]);
                            }

                            wrWorkHPS = workHPS;
                            perfTaskHPS = taskHPS;
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
                            if (wrWorkHPS != null && perfTaskHPS != null){
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

    private class GenerateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
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