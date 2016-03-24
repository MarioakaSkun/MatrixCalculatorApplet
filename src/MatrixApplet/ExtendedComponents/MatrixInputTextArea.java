package MatrixApplet.ExtendedComponents;

import ExpressionParser.Parser;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MatrixInputTextArea extends TextArea {

    public MatrixInputTextArea(String text, int rows, int columns) {
        super(text, rows, columns);
        createListener();
    }

    private void createListener() {
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent event) {
                char c = event.getKeyChar();

                if(!((c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_ENTER) || (c == KeyEvent.VK_TAB) || (c == KeyEvent.VK_COMMA) || (c == KeyEvent.VK_DECIMAL) || (c == KeyEvent.VK_PERIOD) || (c == KeyEvent.VK_SPACE) || (c == KeyEvent.VK_MINUS) || (Character.isDigit(c))))
                    event.consume();
            }
        });
    }

    public double[][] getArrayFromText(int rows, int columns) throws InvalidMatrixInputException {
        double[][] array = new double[rows][columns];

        String currentText = getText();
        String text = Parser.removeAllMultipleWhitespaces(currentText);
        text = text.replaceAll(",", ".");

        String[] strNumbers = text.split(" ");

        int fields = rows*columns;
        if(strNumbers.length != fields) {
            throw new InvalidMatrixInputException(strNumbers.length, fields);
        }

        int row = 0, column = 0;
        for(String number : strNumbers) {
            array[row][column] = getDoubleValue(number);
            if (++column == columns) {
                ++row;
                column = 0;
            }
        }

        return array;
    }

    public static double getDoubleValue(String text) {
        double number;

        try {
            number = Double.parseDouble(text);
        }
        catch(NumberFormatException e) {
            number = 0;
        }

        return number;
    }

}

