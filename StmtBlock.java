package interprete;

import java.util.List;

public class StmtBlock extends Statement {
    final List<Statement> statements;

    StmtBlock(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Statement statement : statements) {
            sb.append(statement.toString());
        }
        return sb.toString();
    }

    @Override
    public void print(String indentation) {
        System.out.println(indentation + "StmtBlock");
        for (Statement stmt : statements) {
            stmt.print(indentation + "\t");
        }
    }

    public Object evaluate(List<Statement> statements) {
        return null;
    }

 @Override
public Object evaluate(TablaSimbolos tablita) {
    tablita.entrarAlcance();
    Object returnValue = null;
    try {
        for (Statement stmt : statements) {
            returnValue = stmt.evaluate(tablita);
            if (returnValue instanceof StmtReturn) {
                break;
            }
        }
    } finally {
        tablita.salirAlcance();
    }
    return returnValue;
}


}

