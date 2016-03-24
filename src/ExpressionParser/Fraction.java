package ExpressionParser;

public class Fraction {
    private double nominator;
    private double denominator;

    public Fraction(double nom, double denom) {
        nominator = nom;
        denominator = denom;
    }

    public double getNominator() {
        return nominator;
    }

    public double getDenominator() {
        return denominator;
    }

    public double getDoubleValue() {
        return nominator / denominator;
    }

    public double doubleFromMultiplication(double x) {
        return nominator * x / denominator;
    }

    public void multipleBy(double x) {
        nominator *= x;
    }

    public void multipleBy(Fraction f) {
        nominator *= f.nominator;
        denominator *= f.denominator;
    }

    public void setValue(double nom, double denom) {
        nominator = nom;
        denominator = denom;
    }

    public void reverse() {
        double temp = nominator;
        nominator = denominator;
        denominator = temp;
    }

    public void shorten() {
        double nwd = NWD(nominator, denominator);
        nominator /= nwd;
        denominator /= nwd;
    }


    private double NWD(double a, double b) {
        while(a != b) {
            if(a>b)
                a -= b;
            else
                b -= a;

            if(a < 1.0 || b < 1.0)
                return 1.0;
        }

        return a;
    }
}
