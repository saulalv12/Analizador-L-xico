package interprete;

public class StmtLoop extends Statement {
    final Expression condition;
    final Statement body;

    StmtLoop(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }
    
    @Override
    public void print(String indentation) {
         System.out.println(indentation + "StmtLoop");
        System.out.println(indentation + "\tCondition:");
        condition.print(indentation + "\t\t");

        System.out.println(indentation + "\tBody:");
        body.print(indentation + "\t\t");
    }
}
