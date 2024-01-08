package interprete;


import java.util.List;

public class StmtFunction extends Statement {
    final Token name;
    final List<Token> params;
    final StmtBlock body;

    StmtFunction(Token name, List<Token> params, StmtBlock body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public void print(String indentation) {
         System.out.println(indentation + "StmtFunction: " + name.lexema);

        // Imprimir los parámetros
        System.out.println(indentation + "\tParameters:");
        for (Token param : params) {
            System.out.println(indentation + "\t\t" + param.lexema);
        }

        System.out.println(indentation + "\tBody:");
        body.print(indentation + "\t\t");
    }
}
