package ExpressionParser;

public class Matrix {
    double tab[][];

    static final double PRECISION = 1_000_000_000.0;

    public Matrix(double mTab[][]) {
        tab = mTab;

        if(tab.length < 1 || tab[0].length < 1) {
            tab = new double[1][1];
            tab[0][0] = 0;
        }
    }

    public Matrix add(Matrix matrix) throws InvalidMatrixExpressionException {
        int sizeX = getColumns();
        int sizeY = getRows();

        if(sizeY != matrix.getRows() || sizeX != matrix.getColumns())
            throw new InvalidMatrixExpressionException("addition (sizes of both matrices are not equal)");

        double[][] newArray = new double[sizeY][sizeX];
        for(int row=0; row<sizeY; ++row) {
            for(int column=0; column<sizeX; ++column) {
                newArray[row][column] = getValue(column, row) + matrix.getValue(column, row);
            }
        }

        return new Matrix(newArray);
    }

    public Matrix subtract(Matrix matrix) throws InvalidMatrixExpressionException {
        double[][] array = new double[matrix.getRows()][matrix.getColumns()];

        for(int y=0; y<matrix.getRows(); ++y) {
            for(int x=0; x<matrix.getColumns(); ++x) {
                array[y][x] = matrix.tab[y][x] * -1;
            }
        }

        return add(new Matrix(array));
    }

    public Matrix multiplyBy(double multiplier) {
        double[][] newArray = new double[getRows()][getColumns()];

        for(int y=0; y<getRows(); ++y) {
            for(int x=0; x<getColumns(); ++x) {
                newArray[y][x] = getValue(x, y) * multiplier;
            }
        }

        return new Matrix(newArray);
    }

    public Matrix multiplyBy(Matrix matrix) throws InvalidMatrixExpressionException {
        int size = getColumns();

        if(size != matrix.getRows())
            throw new InvalidMatrixExpressionException("multiply (columns of first matrix are not equal to rows of the second one)");

        double[][] newArray = new double[getRows()][matrix.getColumns()];

        for(int row=0; row<getRows(); ++row) {
            for(int column=0; column<matrix.getColumns(); ++column) {
                double sum = 0;

                for(int position=0; position<matrix.getRows(); ++position) {   // numer elementu
                    sum += getValue(position, row) * matrix.getValue(column, position);
                }

                newArray[row][column] = sum;
            }
        }

        return new Matrix(newArray);
    }

    public Matrix transposition() {
        double[][] newArray = new double[getColumns()][getRows()];

        for(int y=0; y<getRows(); ++y) {
            for(int x=0; x<getColumns(); ++x) {
                newArray[x][y] = getValue(x, y);
            }
        }

        return new Matrix(newArray);
    }

    public Matrix getOppositeMatrix() throws NonSquareMatrixException, InvalidMatrixExpressionException {
        int sizeX = getRows();
        int sizeY = getColumns();

        if(sizeX != sizeY)
            throw new NonSquareMatrixException(sizeX, sizeY);

        double determinant = getDeterminant();

        if(determinant == 0)
            throw new InvalidMatrixExpressionException("Cannot create an opposite matrix to matrix with 0 determinant.");

        double[][] newArray = new double[sizeX][sizeX];
        double[][] smallerArray = new double[sizeX-1][sizeX-1];

        for(int row=0; row<sizeX; ++row) {
            for(int column=0; column<sizeX; ++column) {

                int XtoFill = 0;
                int YtoFill = 0;

                for(int y=0; y<sizeX; ++y) {
                    if(y == row)
                        continue;

                    for(int x=0; x<sizeX; ++x) {
                        if(x == column)
                            continue;

                        smallerArray[YtoFill][XtoFill] = getValue(x, y);
                        ++XtoFill;
                    }

                    ++YtoFill;
                    XtoFill = 0;
                }

                Matrix smallerMatrix = new Matrix(smallerArray);
                newArray[row][column] =  smallerMatrix.getDeterminant();

                if((row + column) % 2 != 0)
                    newArray[row][column] *= -1;
            }
        }

        Matrix created = new Matrix(newArray);
        created = created.multiplyBy(1/determinant);
        created = created.transposition();
        return created;
    }

    public double getDeterminant() throws NonSquareMatrixException {
        int Size = getRows();
        int sizeY = getColumns();

        if(Size != sizeY)
            throw new NonSquareMatrixException(Size, sizeY);

        double[][] copiedTab = new double[Size][Size];
        for(int y=0; y<Size; ++y) {
            for(int x=0; x<Size; ++x) {
                copiedTab[y][x] = getValue(x, y);
            }
        }
        return doubleValue(getDeterminantFromArray(copiedTab, Size));
    }

    private double getDeterminantFromArray(double[][] mTab, int size) {
        switch(size) {
            case 1:
                return getValue(mTab, 0,0);
            case 2:
                return getValue(mTab, 0, 0) * getValue(mTab, 1, 1) - getValue(mTab, 0, 1) * getValue(mTab, 1, 0);
            case 3: {
                double determinantPlus = getValue(mTab, 0, 0) * getValue(mTab, 1, 1) * getValue(mTab, 2, 2) +
                        getValue(mTab, 1, 0) * getValue(mTab, 2, 1) * getValue(mTab, 0, 2) +
                        getValue(mTab, 2, 0) * getValue(mTab, 0, 1) * getValue(mTab, 1, 2);
                double determinantMinus = getValue(mTab, 0, 2) * getValue(mTab, 1, 1) * getValue(mTab, 2, 0) +
                        getValue(mTab, 1, 2) * getValue(mTab, 2, 1) * getValue(mTab, 0, 0) +
                        getValue(mTab, 2, 2) * getValue(mTab, 0, 1) * getValue(mTab, 1, 0);

                return determinantPlus - determinantMinus;
            }
            default: {
                int chosenX = size - 1;
                int chosenY = size - 1;

                double ChosenValue = getValue(mTab, chosenX, chosenY);

                while(ChosenValue == 0.0) {
                    --chosenX;
                    if(chosenX < 0) {
                        --chosenY;
                        if(chosenY < 0)
                            return 0.0; // Totally empty matrix

                        chosenX = size - 1;
                    }

                    ChosenValue = getValue(mTab, chosenX, chosenY);
                }

                for(int row=0; row<size; ++row) {
                    if(row == chosenY)
                        continue;

                    double FieldValue = getValue(mTab, chosenX, row);

                    if(FieldValue == 0.0)
                        continue;

                    Fraction NumberToDivideBy = new Fraction(-FieldValue, ChosenValue);

                    for(int column=0; column<size; ++column) { // Mnozenie dla kazdego elementu wiersza
                        double CalculatedFieldValue = NumberToDivideBy.doubleFromMultiplication(getValue(mTab, column, chosenY));
                        mTab[row][column] += CalculatedFieldValue;
                    }
                }

                for(int x=chosenX; x < size - 1; ++x) {
                    for(int y=0; y < size; ++y) {
                        mTab[y][x] = mTab[y][x + 1];
                    }
                }
                for(int y=chosenY; y < size - 1; ++y) {
                    System.arraycopy(mTab[y + 1], 0, mTab[y], 0, size);
                }

                int PowMinusOne = -1;
                if((chosenX + chosenY) % 2 == 0)
                    PowMinusOne = 1;

                return ChosenValue * PowMinusOne * getDeterminantFromArray(mTab, size - 1);
            }
        }
    }

    public double getValue(int x, int y) {
        return doubleValue(tab[y][x]);
    }

    private double doubleValue(double value) {
        value = Math.round(value * PRECISION) / PRECISION;
        return value;
    }

    public int getRows() {
        return tab.length;
    }

    public int getColumns() {
        return tab[0].length;
    }

    private double getValue(double array[][], int x, int y) {
        return array[y][x];
    }

    public double[][] getArray() {
        return tab;
    }
}

class NonSquareMatrixException extends Exception {
    private final int sizeX;
    private final int sizeY;

    NonSquareMatrixException(int x, int y) {
        sizeX = x;
        sizeY = y;
    }

    public String toString() {
        return "Matrix is non-square [ " + sizeX + "x" + sizeY + " ]";
    }
}

class InvalidMatrixExpressionException extends Exception {
    private final String message;

    InvalidMatrixExpressionException(String text) {
        message = text;
    }

    public String toString() {
        return "Invalid matrix expression: " + message;
    }
}