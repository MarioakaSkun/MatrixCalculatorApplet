package ExpressionParser;

import java.util.Stack;
import MatrixApplet.Main;

public class MatrixCalculator {
    Main applet;

    public MatrixCalculator(Main applet) {
        this.applet = applet;
    }

    public MatrixContainer calculate(String expression) throws InvalidMatrixCommandException {
        Stack<MatrixContainer> matricesStack = new Stack<>();
        String[] tokens = expression.split(" ");

        for (String token : tokens) {
            if (Parser.isWord(token)) {
                if(applet.isAlreadyOnList(token)) {
                    MatrixContainer matrix = new MatrixContainer(token, applet.getArrayFromName(token));
                    matricesStack.push(matrix);
                }
                else
                    throw new InvalidMatrixCommandException("Invalid matrix name: " + token);
            }
            else if (Parser.isNumber(token)) {
                double[][] array = new double[1][1];
                array[0][0] = Double.valueOf(token);

                MatrixContainer element = new MatrixContainer(token, array);
                element.setIsNumberOnly(true);

                matricesStack.push(element);
            }
            else if (Parser.isOperator(token)) {
                if(matricesStack.size() < 2)
                    throw new InvalidMatrixCommandException("The amount of values and operators is invalid");

                MatrixContainer elementOne = matricesStack.pop();
                MatrixContainer elementTwo = matricesStack.pop();

                matricesStack.push(operation(token, elementTwo, elementOne));
            }
            else if (Parser.isFunction(token)) {
                MatrixContainer element = matricesStack.pop();

                matricesStack.push(function(token, element));
            }
        }
        if(matricesStack.size() != 1)
            throw new InvalidMatrixCommandException("The amount of values and operators is invalid");

        return matricesStack.pop();
    }

    private MatrixContainer function(String operator, MatrixContainer matrix) throws InvalidMatrixCommandException {
        if(matrix.isNumberOnly())
            throw new InvalidMatrixCommandException("Function '" + operator + "' performed on a number: " + matrix.getValue(0, 0));

        switch(operator) {
            case "!":
                return doGetDeterminant(matrix);
            case "@":
                return doTransposition(matrix);
            case "#":
                return doOpposite(matrix);
        }

        throw new InvalidMatrixCommandException("Invalid function: " + operator);
    }

    private MatrixContainer doGetDeterminant(MatrixContainer matrix) throws InvalidMatrixCommandException {
        String name = getFunctionName("D", matrix);
        try {
            return matrix.getDeterminantMatrix(name);
        }
        catch(NonSquareMatrixException ex) {
            throw new InvalidMatrixCommandException("Error during getting determinant of non-square matrix: " + matrix.getName());
        }
    }

    private MatrixContainer doTransposition(MatrixContainer matrix) {
        String name = getFunctionName("T", matrix);
        return new MatrixContainer(name, matrix.transposition());
    }

    private MatrixContainer doOpposite(MatrixContainer matrix) throws InvalidMatrixCommandException {
        String name = getFunctionName("#", matrix);

        try {
            return new MatrixContainer(name, matrix.getOppositeMatrix());
        } catch(NonSquareMatrixException ex) {
            throw new InvalidMatrixCommandException("Error during creating opposite matrix of non-square matrix: " + matrix.getName());
        } catch(InvalidMatrixExpressionException ex) {
            throw new InvalidMatrixCommandException("Error during creating opposite matrix: " + matrix.getName() + "[" + ex + "]");
        }
    }

    private String getFunctionName(String function, MatrixContainer matrix) {
        return function + "(" + matrix.getName() + ")";
    }



    private MatrixContainer operation(String operator, MatrixContainer leftMatrix, MatrixContainer rightMatrix) throws InvalidMatrixCommandException {

        if(operator.equals("*"))
            return doMultiply(leftMatrix, rightMatrix);

        if(leftMatrix.isNumberOnly())
            throw new InvalidMatrixCommandException("Operator " + operator + " performed on a number: " + leftMatrix.getValue(0, 0));
        if(rightMatrix.isNumberOnly())
            throw new InvalidMatrixCommandException("Operator " + operator + " performed on a number: " + rightMatrix.getValue(0, 0));

        if(operator.equals("+"))
            return doAdd(leftMatrix, rightMatrix);
        else if(operator.equals("-"))
            return doSubtract(leftMatrix, rightMatrix);

        throw new InvalidMatrixCommandException("Invalid operator: " + operator);
    }

    private MatrixContainer doAdd(MatrixContainer leftMatrix, MatrixContainer rightMatrix) throws InvalidMatrixCommandException {
        String name = getOperationName("+", leftMatrix, rightMatrix);

        try {
            return new MatrixContainer(name, leftMatrix.add(rightMatrix));
        }
        catch(InvalidMatrixExpressionException ex) {
            throw new InvalidMatrixCommandException("Error during matrix addition: " + name);
        }
    }

    private MatrixContainer doSubtract(MatrixContainer leftMatrix, MatrixContainer rightMatrix) throws InvalidMatrixCommandException {
        String name = getOperationName("-", leftMatrix, rightMatrix);

        try {
            return new MatrixContainer(name, leftMatrix.subtract(rightMatrix));
        }
        catch(InvalidMatrixExpressionException ex) {
            throw new InvalidMatrixCommandException("Error during matrix subtraction: " + name);
        }
    }

    private MatrixContainer doMultiply(MatrixContainer leftMatrix, MatrixContainer rightMatrix) throws InvalidMatrixCommandException {
        String name = getOperationName("*", leftMatrix, rightMatrix);

        try {
            MatrixContainer result;
            if (leftMatrix.isNumberOnly() && rightMatrix.isNumberOnly()) {
                double[][] array = new double[1][1];
                array[0][0] = leftMatrix.getValue(0, 0) * rightMatrix.getValue(0, 0);

                result = new MatrixContainer(name, array);
            }
            else if(leftMatrix.isNumberOnly())
                result = new MatrixContainer(name, rightMatrix.multiplyBy(leftMatrix.getValue(0, 0)));
            else if(rightMatrix.isNumberOnly())
                result = new MatrixContainer(name, leftMatrix.multiplyBy(rightMatrix.getValue(0, 0)));
            else {
                result = new MatrixContainer(name, leftMatrix.multiplyBy(rightMatrix));
            }

            return result;
        }
        catch(InvalidMatrixExpressionException ex) {
            throw new InvalidMatrixCommandException("Error during matrix multiplication: " + name);
        }
    }

    private String getOperationName(String operator, MatrixContainer leftMatrix, MatrixContainer rightMatrix) {
        return "( " + leftMatrix.getName() + " " + operator + " " + rightMatrix.getName() + " )";
    }
}


