package MatrixApplet;

import ExpressionParser.MatrixCalculator;
import ExpressionParser.MatrixContainer;
import ExpressionParser.Parser;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import ExpressionParser.InvalidMatrixCommandException;
import MatrixApplet.Dialogs.MatrixCreationDialog;
import MatrixApplet.Dialogs.MatrixDisplayDialog;
import MatrixApplet.Dialogs.NewMatrixNameDialog;

/*
<applet code="Matrix Solver" width=400 height=300> </applet>
*/

public class Main extends Applet {
    private final static int PADDING = 20;

    private String errorMessage = "";

    private List listOfMatrices;
    private TextField commandLine;
    private Button functionButtons[] = new Button[4];

    private GridBagConstraints gbc;

    private ArrayList<MatrixContainer> matricesContainer = new ArrayList<>();

    public void init() {
        addMainLayout();
        addFunctionalButtons();
        addCommandLine();
        addListOfMatrices();

        new MatricesAdder(this);
    }

    public void paint(Graphics g) {
        int y = Integer.valueOf(getParameter("height")) - g.getFontMetrics().getHeight();
        int x = g.getFontMetrics().getHeight();

        g.setColor(Color.RED);
        g.drawString(errorMessage, x, y);
    }

    private void addFunctionalButtons() {
        functionButtons[0] = new Button("New");
        functionButtons[1] = new Button("Remove");
        functionButtons[2] = new Button("Calculate");
        functionButtons[3] = new Button("Display");

        GridLayout layout = new GridLayout(2, 2);
        layout.setVgap(PADDING);
        layout.setHgap(PADDING);

        Panel panel = new Panel();
        panel.setLayout(layout);

        for(Button button : functionButtons) {
            panel.add(button);
            button.addActionListener( (ActionEvent event) -> {
                Component source = (Component)event.getSource();

                if(source == button)
                    buttonAction(button);
                } );
        }

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panel, gbc);
    }

    private void addMainLayout() {
        GridBagLayout layoutMain = new GridBagLayout();
        setLayout(layoutMain);

        gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
    }

    private void addCommandLine() {
        commandLine = new TextField();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, PADDING*3, 0, PADDING*3);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(commandLine, gbc);
    }

    private void addListOfMatrices() {
        listOfMatrices = new List();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(PADDING, 0, PADDING, 0);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(listOfMatrices, gbc);
    }

    private void buttonAction(Button button) {
        if(button == functionButtons[0])
            createNewMatrixDialog();
        else if(button == functionButtons[1])
            removeMatrix();
        else if(button == functionButtons[2])
            calculateExpression();
        else if(button == functionButtons[3])
            displayCurrentMatrix();
    }

    private void createNewMatrixDialog() {
        MatrixCreationDialog dialog = new MatrixCreationDialog(this, "Create new matrix");
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    private void removeMatrix() {
        if(matricesContainer.isEmpty())
            return;

        if(listOfMatrices.getSelectedIndex() == -1)
            return;

        removeCurrentFromList();
    }

    private void calculateExpression() {
        String expression = commandLine.getText();

        if(expression.isEmpty()) {
            setErrorMessage("The expression is empty!");
            return;
        }

        String rpnExpression = Parser.parse(expression);

        try {
            MatrixCalculator calculator = new MatrixCalculator(this);
            MatrixContainer result = calculator.calculate(rpnExpression);

            NewMatrixNameDialog dialog = new NewMatrixNameDialog(this, result);
            dialog.setModal(true);
            dialog.setVisible(true);

            setErrorMessage("");
        }
        catch (InvalidMatrixCommandException ex) {
            setErrorMessage(ex.toString());
        }
    }

    public void setErrorMessage(String message) {
        errorMessage = message;
        repaint();
    }

    private void displayCurrentMatrix() {
        int index = listOfMatrices.getSelectedIndex();
        if(index == -1)
            return;

        MatrixContainer matrix = matricesContainer.get(index);

        if(matrix.isNumberOnly())
            return;

        MatrixDisplayDialog dialog = new MatrixDisplayDialog(matrix);
        dialog.setVisible(true);
    }

    public void addToList(MatrixContainer matrix) {
        matricesContainer.add(matrix);

        if(matrix.isNumberOnly())
            listOfMatrices.add(matrix.getName() + " = " + (matrix.getArray())[0][0]);
        else
            listOfMatrices.add(matrix.getName() + " (" + matrix.getRows() + "x" + matrix.getColumns() + ")");
    }

    public void removeCurrentFromList() {
        int index = listOfMatrices.getSelectedIndex();

        if(index == -1)
            return;

        matricesContainer.remove(index);
        listOfMatrices.remove(index);
    }

    public boolean isAlreadyOnList(String name) {
        for(MatrixContainer matrix : matricesContainer) {
            if (matrix.getName().equals(name))
                return true;
        }

        return false;
    }

    public MatrixContainer getMatrixByName(String name) {
        for(MatrixContainer matrix : matricesContainer) {
            if (matrix.getName().equals(name))
                return matrix;
        }

        return null;
    }

    public Insets getInsets() {
        return new Insets(PADDING, PADDING, PADDING, PADDING);
    }
}

