package MatrixApplet.ExtendedComponents;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LimitedTextField extends TextField {
    int maxLength;

    public LimitedTextField(String initial, int maximumLength, int columns) {
        super(initial, columns);
        maxLength = maximumLength;
        createListener();
    }

    public LimitedTextField(int maxLength, int columns) {
        this("", maxLength, columns);
    }

    private void createListener() {
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent event) {
                String currentText = getText();
                if(currentText.length() >= maxLength)
                    event.consume();
            }
        });
    }

}
