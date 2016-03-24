package MatrixApplet.Dialogs;

import ExpressionParser.MatrixContainer;
import MatrixApplet.ExtendedComponents.LimitedTextField;
import MatrixApplet.ExtendedComponents.NumericTextField;
import MatrixApplet.ExtendedComponents.MatrixInputTextArea;
import MatrixApplet.ExtendedComponents.InvalidMatrixInputException;

import MatrixApplet.Main;
import MatrixApplet.MatrixNameValidator;

import java.awt.*;
import java.awt.event.*;

public class MatrixCreationDialog extends Dialog implements ActionListener {

    private Main parentWindow;
    private Button buttonAccept;
    private Button buttonCancel;

    private LimitedTextField nameField;
    private NumericTextField rowsField;
    private NumericTextField columnsField;
    private MatrixInputTextArea matrixField;

    private String errorMessage = "";

    public MatrixCreationDialog(Main parent, String title) {
        super((Frame)null, title, false);
        parentWindow = parent;
        setSize(600, 300);

        addComponents();
        addWindowListener(new DialogClosingAdapter(this));
    }

    private void addComponents() {
        buttonAccept = new Button("Ok");
        buttonCancel = new Button("Cancel");
        nameField = new LimitedTextField(10, 5);
        rowsField = new NumericTextField(2);
        columnsField = new NumericTextField(2);
        matrixField = new MatrixInputTextArea("", 10, 40);

        Label textName = new Label("Name");
        Label textRows = new Label("Rows");
        Label textColumns = new Label("Columns");

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(textName, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(nameField, gbc);

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 5;
        gbc.fill = GridBagConstraints.BOTH;
        add(matrixField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(textRows, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(rowsField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(textColumns, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(columnsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(buttonAccept, gbc);
        buttonAccept.addActionListener(this);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(buttonCancel, gbc);
        buttonCancel.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Component source = (Component)event.getSource();

        if(source == buttonAccept)
            createNewMatrixRequest();
        else if(source == buttonCancel)
            dispose();
    }

    private void createNewMatrixRequest() {
        try {
            createNewMatrix();
        }
        catch(InvalidMatrixParameterException ex) {
            errorMessage = ex.toString();
            repaint();
        }
    }

    private void createNewMatrix() throws InvalidMatrixParameterException {

        int rows = NumericTextField.getNumericValue(rowsField.getText());
        int columns = NumericTextField.getNumericValue(columnsField.getText());
        String name = nameField.getText();

        if(rows == 0)
            throw new InvalidMatrixParameterException(InvalidMatrixParameterException.ERR_ROWS);
        if(columns == 0)
            throw new InvalidMatrixParameterException(InvalidMatrixParameterException.ERR_COLUMNS);
        if(name.length() == 0)
            throw new InvalidMatrixParameterException("Empty matrix name");

        MatrixNameValidator validator = new MatrixNameValidator(name);
        if(!validator.isValid())
            throw new InvalidMatrixParameterException(validator.getErrorMessage());

        if(parentWindow.isAlreadyOnList(name))
                throw new InvalidMatrixParameterException("Matrix named " + name + " already exists");

        double[][] array;
        try {
            array = matrixField.getArrayFromText(rows, columns);
        }
        catch(InvalidMatrixInputException ex) {
            errorMessage = ex.toString();
            repaint();
            return;
        }

        parentWindow.addToList(new MatrixContainer(name, array));
        dispose();
    }

    public void paint(Graphics g) {
        if(errorMessage.length() == 0)
            return;

        g.setColor(Color.red);
        g.drawString(errorMessage, g.getFontMetrics().getHeight(), getHeight() - g.getFontMetrics().getHeight());
    }
}

class DialogClosingAdapter extends WindowAdapter {
    Dialog dialog;

    DialogClosingAdapter(Dialog dialog) {
        this.dialog = dialog;
    }

    public void windowClosing(WindowEvent we) {
        dialog.dispose();
    }
}

class InvalidMatrixParameterException extends Exception {
    private int type;

    static final int ERR_NAME = 0;
    static final int ERR_ROWS = 1;
    static final int ERR_COLUMNS = 2;

    private String errorMessage = "";

    InvalidMatrixParameterException(int errorType) {
        type = errorType;
    }

    InvalidMatrixParameterException(String errorMsg) {
        type = ERR_NAME;
        errorMessage = errorMsg;
    }

    public String toString() {
        if(type == ERR_NAME)
            return errorMessage;
        else if(type == ERR_ROWS)
            return "Invalid rows number";
        else
            return "Invalid columns number";
    }
}

