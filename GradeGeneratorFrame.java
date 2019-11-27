import java.awt.*;
import java.awt.event.*;
//import java.beans.*;
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

    private static final String[] SUBJECTS = {"MTB","Filipino","English",
                                              "Math","AP", "ESP","Music",
                                              "Arts","PE","Health"};

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
        constraints.anchor = GridBagConstraints.SOUTH;
        gradePanel.add(statusPanel, constraints);

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
        public void actionPerformed(ActionEvent e) {
            
        }
    }

    private class SetupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
    }
    
    public static void main(String[] args){
        GradeGeneratorFrame gradeFrame = new GradeGeneratorFrame();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            gradeFrame.setSize(450,275);
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