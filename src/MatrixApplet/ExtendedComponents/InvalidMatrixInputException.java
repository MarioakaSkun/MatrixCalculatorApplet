package MatrixApplet.ExtendedComponents;

public class InvalidMatrixInputException extends Exception {
    private int received, needed;

    public InvalidMatrixInputException(int fieldsReceived, int fieldsNeeded) {
        received = fieldsReceived;
        needed = fieldsNeeded;
    }

    public String toString() {
        if(needed > received)
            return "Too few numbers: need " + needed + ", received " + received + ".";
        else
            return "ExpressionParser.Matrix numbers are not clear: need " + needed + ", received " + received + ".";
    }

}
