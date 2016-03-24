package MatrixApplet.Dialogs;

import ExpressionParser.MatrixContainer;
import MatrixApplet.ExtendedComponents.LimitedTextField;
import MatrixApplet.Main;
import MatrixApplet.MatrixNameValidator;

import java.awt.*;
import java.awt.event.ActionEvent;

public class NewMatrixNameDialog extends Dialog {
    private Main parentWindow;

    private Button buttonOk;
    private LimitedTextField nameLine;

    private MatrixContainer calculatedMatrix;

    public NewMatrixNameDialog(Main parent, MatrixContainer matrix) {
        super((Frame)null, "Name your matrix", false);
        parentWindow = parent;
        calculatedMatrix = matrix;

        setSize(parent.getWidth(), parent.getHeight()/2);

        addComponents();
        addWindowListener(new DialogClosingAdapter(this));
    }

    private void addComponents() {
        GridBagLayout layout = new GridBagLayout();

        Panel panel = new Panel();
        panel.setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 75, 10, 75);
        nameLine = new LimitedTextField(24, 24);
        panel.add(nameLine, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 150, 10, 150);
        buttonOk = new Button("Ok");
        panel.add(buttonOk, gbc);

        setLayout(new GridBagLayout());
        add(panel);

        buttonOk.addActionListener( (ActionEvent event) -> {
            if(event.getSource() == buttonOk) {
                String name = nameLine.getText();

                MatrixNameValidator validator = new MatrixNameValidator(name);
                if(!validator.isValid()) {
                    parentWindow.setErrorMessage(validator.getErrorMessage());
                    return;
                }

                calculatedMatrix.setName(name);

                parentWindow.addToList(calculatedMatrix);
                dispose();
            }
        } );
    }
}
