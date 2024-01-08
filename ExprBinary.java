package interprete;

public class ExprBinary extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    ExprBinary(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
      @Override
    public String toString() {
        return "(" + left.toString() + " " + operator.lexema + " " + right.toString() + ")";
    }

    @Override
    public void print(String indentation) {
        System.out.println(indentation + "ExprBinary");
        System.out.println(indentation + "\tLeft:");
        left.print(indentation + "\t\t");
        System.out.println(indentation + "\tOperator: " + operator.lexema);
        System.out.println(indentation + "\tRight:");
        right.print(indentation + "\t\t");
    }
}
