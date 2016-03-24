package ExpressionParser;

public class InvalidMatrixCommandException extends Exception {
    String message;

    InvalidMatrixCommandException(String msg) {
        message = msg;
    }

    public String toString() {
        return message;
    }
}