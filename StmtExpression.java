package interprete;

public class StmtExpression extends Statement {
    final Expression expression;

    StmtExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void print(String indentation) {
        System.out.println(indentation + "StmtExpression");
        expression.print(indentation + "\t");
    }
}
