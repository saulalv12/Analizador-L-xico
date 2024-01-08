package interprete;


public class StmtPrint extends Statement {
    final Expression expression;

    StmtPrint(Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public String toString() {
        return expression.toString(); 
    }

    @Override
    public void print(String indentation) {
         System.out.println(indentation + "StmtPrint");
        expression.print(indentation + "\t");
    }
}
