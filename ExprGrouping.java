package interprete;

public class ExprGrouping extends Expression {
    final Expression expression;

    ExprGrouping(Expression expression) {
        this.expression = expression;
    }
    @Override
    public void print(String indentation) {
        System.out.println(indentation + "ExprGrouping");
        expression.print(indentation + "\t");
    }
}
