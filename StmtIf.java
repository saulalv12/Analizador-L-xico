package interprete;
public class StmtIf extends Statement {
    final Expression condition;
    final Statement thenBranch;
    final Statement elseBranch;

    StmtIf(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("(if ").append(condition).append("\n");
        builder.append("(then ").append(thenBranch).append(")\n");

        if (elseBranch != null) {
            builder.append("(else ").append(elseBranch).append(")\n");
        }
        
        builder.append(")");
        return builder.toString();
    }

    @Override
    public void print(String indentation) {
         System.out.println(indentation + "StmtIf");
        System.out.println(indentation + "\tCondition:");
        condition.print(indentation + "\t\t");

        System.out.println(indentation + "\tThen Branch:");
        thenBranch.print(indentation + "\t\t");

        if (elseBranch != null) {
            System.out.println(indentation + "\tElse Branch:");
            elseBranch.print(indentation + "\t\t");
        }
    }
}
