//Refactored

package MatrixApplet;

public class MatrixNameValidator {

    private String name;
    private String errorMessage;

    public MatrixNameValidator(String name) {
        this.name = name;
        errorMessage = "";
    }

    public boolean isValid() {
        if(name.isEmpty()) {
            errorMessage = "Matrix name cannot be empty";
            return false;
        }

        for(int i=0; i<name.length(); ++i) {
            if(!Character.isLetter(name.charAt(i))) {
                errorMessage = "Matrix contains illegal character: " + name.charAt(i);
                return false;
            }
        }

        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
