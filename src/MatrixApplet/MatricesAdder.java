package MatrixApplet;

public class MatricesAdder {

    Main parent;

    public MatricesAdder(Main main) {
        parent = main;

        addSomeMatrices();
    }

    private void addSomeMatrices() {
        double[][] arrA = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };

        double[][] arrB = {
                {1, 2},
                {3, 4}
        };

        double[][] arrC = {
                {1, 5, -1, 3, 2},
                {1, 5, 8, 1, -2},
                {1, -4, 6, 8, 2}
        };

        double[][] arrD = {
                {1},
                {2},
                {3},
                {4},
                {5}
        };

        double[][] arrE = {
                {0, 1, 1},
                {1, 0, 1},
                {1, 1, 0}
        };

        double[][] arrF = {
                {2, 1},
                {4, 3}
        };

        double[][] arrG = {
                {1, 2, 3, 4, 5}
        };

        double[][] arrH = {
                {1, 2, 4, 3, 1, 2, 1},
                {1, -1, -12, 4, 5, 1, 0},
                {0, 1, 0, 1, 0, 1, 0}
        };

        parent.addToList("A", arrA);
        parent.addToList("B", arrB);
        parent.addToList("C", arrC);
        parent.addToList("D", arrD);
        parent.addToList("HDG", arrE);
        parent.addToList("DEED", arrF);
        parent.addToList("G", arrG);
        parent.addToList("Z", arrH);
    }


}
