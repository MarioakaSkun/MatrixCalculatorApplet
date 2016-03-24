package ExpressionParser;

public class MatrixContainer extends Matrix {
    private String name;
    private boolean isNumber = false;

    public MatrixContainer(String name, double[][] tab) {
        super(tab);

        this.name = name;
    }

    public MatrixContainer(String name, Matrix matrix) {
        super(matrix.getArray());

        this.name = name;
    }

    public MatrixContainer getDeterminantMatrix(String name) throws NonSquareMatrixException {
        double array[][] = new double[1][1];
        array[0][0] = getDeterminant();

        MatrixContainer result = new MatrixContainer(name, array);
        result.setIsNumberOnly(true);

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setIsNumberOnly(boolean set) {
        isNumber = set;
    }

    public boolean isNumberOnly() {
        return isNumber;
    }
}
