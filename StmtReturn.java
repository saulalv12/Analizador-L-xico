package interprete;

public class StmtReturn extends Statement {
    final Expression value;

    StmtReturn(Expression value) {
        this.value = value;
    }

    @Override
    public void print(String indentation) {
        System.out.println(indentation + "StmtReturn");
        if (value != null) {
            value.print(indentation + "\t");
        }
    }
}
