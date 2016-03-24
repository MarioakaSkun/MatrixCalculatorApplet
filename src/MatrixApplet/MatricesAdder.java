package MatrixApplet;

import ExpressionParser.MatrixContainer;

public class MatricesAdder {

    Main parent;

    public MatricesAdder(Main main) {
        parent = main;

        addSomeMatrices();
    }

    private void addSomeMatrices() {

        double[][] arrA = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        double[][] arrB = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };

        parent.addToList(new MatrixContainer("example", arrA));
        parent.addToList(new MatrixContainer("diagonal", arrB));
    }


}
