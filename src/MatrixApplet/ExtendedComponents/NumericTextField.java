package MatrixApplet.ExtendedComponents;

import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NumericTextField extends TextField {
    static final int MAX_NUM = 25;

    public NumericTextField(String initial, int columns) {
        super(initial, columns);
        createListener();
    }

    public NumericTextField(int columns) {
        this("", columns);
    }

    private void createListener() {
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent event) {
                char c = event.getKeyChar();

                if(!((c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_ENTER) || (c == KeyEvent.VK_TAB))) {
                    if(Character.isDigit(c)){
                        int number = getNumericValue(getText());
                        int nextNumber = getNumericValue(getText() + String.valueOf(c));

                        if(number == MAX_NUM) {
                            event.consume();
                        }
                        else if(nextNumber >= MAX_NUM) {
                            setText(String.valueOf(MAX_NUM));
                            event.consume();
                        }
                    }
                    else
                        event.consume();
                }
            }
        });
    }

    public static int getNumericValue(String text) {
        int number;

        try {
            number = Integer.parseInt(text);
        }
        catch(NumberFormatException e) {
            number = 0;
        }

        return number;
    }

}
