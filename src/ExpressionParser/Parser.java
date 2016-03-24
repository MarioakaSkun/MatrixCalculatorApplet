package ExpressionParser;

import java.util.Stack;

public abstract class Parser {
    private static final String OPERATORS[] = {"+", "-", "*"};
    private static final String FUNCTIONS[] = {"!", "@", "#"};
    private static final String PARENTHESIS[] = {"(", ")"};

    private static final char C_OPERATORS[] = { '+', '-', '*'};
    private static final char C_FUNCTIONS[] = { '!', '@', '#' };
    private static final char C_PARENTHESIS[] = { '(', ')' };

    private static final char READABLE_FUNCTIONS[] = { 'D', 'T', 'O' };

    public static String parse(String expression) {
        expression = filterExpression(expression);
        return convertToRPN(expression);
    }

    private static String filterExpression(String expression) {
        if(expression.length() == 0)
            return expression;

        StringBuilder output = new StringBuilder(expression.length() * 2);
        expression = removeAllWhitespaces(expression);

        for (int i = 0; i < expression.length(); ++i) {
            char token = expression.charAt(i);

            if (isOperator(token) || isFunction(token) || isParenthesis(token))
                output.append(token).append(' ');
            else if(isNumber(token)) {
                if(i + 1 != expression.length()) {
                    if(!isNumber(expression.charAt(i + 1)))
                        output.append(token).append(' ');
                    else
                        output.append(token);
                }
                else output.append(token);
            }
            else if(isLetter(token)) {
                if(i+1 != expression.length()) {
                    char nextToken = expression.charAt(i+1);

                    if(isReadableFunction(token) && nextToken == C_PARENTHESIS[0])
                        output.append(getFunctionLetterFromReadableSymbol(token)).append(' ');
                    else if(!isLetter(nextToken))
                        output.append(token).append(' ');
                    else
                        output.append(token);

                }
                else
                    output.append(token);
            }
        }

        int length = output.length() - 1;

        if (output.charAt(length) == ' ')
            output.deleteCharAt(length);

        String result = output.toString();
        return removeAllMultipleWhitespaces(result);
    }

    private static String convertToRPN(String expression) {
        StringBuilder output = new StringBuilder();
        Stack<String> operationsStack = new Stack<>();

        String[] tokens = expression.split(" ");

        for (String token : tokens) {
            if(isNumber(token) || isWord(token))
                output.append(token).append(" ");
            else if(isFunction(token))
                operationsStack.push(token);
            else if(isOperator(token)) {
                while(!operationsStack.isEmpty() && isOperator(operationsStack.peek())) {
                    if(getPrecedence(token) <= getPrecedence(operationsStack.peek()))
                        output.append(operationsStack.pop()).append(" ");
                    else break;
                }
                operationsStack.push(token);
            }
            else if(token.equals(PARENTHESIS[0]))
                operationsStack.push(token);
            else if(token.equals(PARENTHESIS[1])) {
                while(!operationsStack.peek().equals(PARENTHESIS[0])) {
                    output.append(operationsStack.pop()).append(" ");
                }
                operationsStack.pop();

                if(!operationsStack.isEmpty() && isFunction(operationsStack.peek()))
                    output.append(operationsStack.pop()).append(" ");
            }
        }

        while (!operationsStack.isEmpty())
            output.append(operationsStack.pop()).append(" ");

        return output.toString();
    }

    private static int getPrecedence(String token) {
        if(token.equals(OPERATORS[0]) || token.equals(OPERATORS[1]))
            return 1;
        else if(token.equals(OPERATORS[2]))
            return 2;

        return 0;
    }

    public static boolean isOperator(String token) {
        for (String operator : OPERATORS) {
            if (token.equals(operator))
                return true;
        }

        return false;
    }

    public static boolean isFunction(String token) {
        for (String function : FUNCTIONS) {
            if (token.equals(function))
                return true;
        }

        return false;
    }

    public static boolean isNumber(String token) {
        try {
            Double.valueOf(token);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

    public static boolean isWord(String token) {
        for(char symbol : token.toCharArray()) {
            if(!isLetter(symbol))
                return false;
        }

        return true;
    }

    private static boolean isOperator(char token) {
        for(char operator : C_OPERATORS) {
            if (token == operator)
                return true;
        }

        return false;
    }

    private static boolean isFunction(char token) {
        for(char function : C_FUNCTIONS) {
            if (token == function)
                return true;
        }

        return false;
    }

    private static boolean isParenthesis(char token) {
        for(char bracket : C_PARENTHESIS) {
            if(token == bracket)
                return true;
        }
        return false;
    }

    private static boolean isNumber(char token) {
        return (Character.isDigit(token) || token == '.');
    }

    private static boolean isLetter(char token) { // Yea, pointless
        return Character.isLetter(token);
    }

    private static boolean isReadableFunction(char token) {
        for(char function : READABLE_FUNCTIONS) {
            if(token == function)
                return true;
        }

        return false;
    }

    private static char getFunctionLetterFromReadableSymbol(char token) {
        for(int i=0; i<READABLE_FUNCTIONS.length; ++i) {
            if(token == READABLE_FUNCTIONS[i])
                return C_FUNCTIONS[i];
        }

        return ' ';
    }

    public static String removeAllMultipleWhitespaces(String expression) {
        return expression.replaceAll("\\s+", " ");
    }

    public static String removeAllWhitespaces(String expression) {
        return expression.replaceAll("\\s+", "");
    }
}