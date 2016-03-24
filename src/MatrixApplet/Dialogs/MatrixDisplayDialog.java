package MatrixApplet.Dialogs;

import ExpressionParser.MatrixContainer;

import java.awt.*;

public class MatrixDisplayDialog extends Dialog {
    private static final int PADDING = 50;
    private static final int LINES_LENGTH = 16;
    private static final int FONT_SIZE = 16;
    private static final int SPACE_WIDTH = 24;
    private static final Font FONT = new Font("Serif", Font.BOLD, FONT_SIZE);

    private MatrixContainer matrix;

    private int totalWidth;
    private int totalHeight;

    public MatrixDisplayDialog(MatrixContainer matrix) {
        super((Frame)null, matrix.getName(), false);
        this.matrix = matrix;

        setFont(FONT);

        prepareSize();

        addWindowListener(new DialogClosingAdapter(this));
    }

    public void paint(Graphics g) {
        drawLines(g);
        drawNumbers(g);
    }

    private void drawLines(Graphics g) {
        g.drawLine(PADDING, PADDING, PADDING, totalHeight - PADDING);
        g.drawLine(totalWidth - PADDING, PADDING, totalWidth - PADDING, totalHeight - PADDING);

        g.drawLine(PADDING, PADDING, PADDING + LINES_LENGTH, PADDING);
        g.drawLine(totalWidth - PADDING, PADDING, totalWidth - PADDING - LINES_LENGTH, PADDING);

        g.drawLine(PADDING, totalHeight - PADDING, PADDING + LINES_LENGTH, totalHeight - PADDING);
        g.drawLine(totalWidth - PADDING, totalHeight - PADDING, totalWidth - PADDING - LINES_LENGTH, totalHeight - PADDING);
    }

    private void drawNumbers(Graphics g) {
        double[][] array = matrix.getArray();
        int[] columnsWidth = getBiggestColumnsWidth();

        FontMetrics metrics = getFontMetrics(FONT);

        int y = PADDING + metrics.getAscent();
        for(int row = 0; row < matrix.getRows(); ++row) {
            int x = PADDING + SPACE_WIDTH;

            for(int column = 0; column < matrix.getColumns(); ++column) {
                String value = stringRepresentation(array[row][column]);
                g.drawString(value, x, y);

                x += columnsWidth[column] + SPACE_WIDTH;
            }

            y += metrics.getHeight();
        }
    }

    private void prepareSize() {
        totalWidth = 2*PADDING + SPACE_WIDTH;
        totalHeight = getFontMetrics(FONT).getHeight()*matrix.getRows() + 2*PADDING;

        for(int width : getBiggestColumnsWidth()) {
            totalWidth += width + SPACE_WIDTH;
        }

        setSize(totalWidth, totalHeight);
    }

    private int[] getBiggestColumnsWidth() {
        double[][] array = matrix.getArray();

        int[] columnsWidth = new int[matrix.getColumns()];

        for(int column = 0; column < matrix.getColumns(); ++column) {
            int widestInColumn = 0;

            for(int row = 0; row < matrix.getRows(); ++row) {
                String value = stringRepresentation(array[row][column]);
                int width = getFontMetrics(FONT).stringWidth(value);

                if(width > widestInColumn)
                    widestInColumn = width;
            }

            columnsWidth[column] = widestInColumn;
        }

        return columnsWidth;
    }

    private String stringRepresentation(double value) {
        String representation = String.valueOf(value);

        if(value - (int)value == 0)
            representation = representation.substring(0, representation.indexOf('.'));

        return representation;
    }
}