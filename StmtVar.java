package interprete;

public class StmtVar extends Statement {
    final Token name;
    final Expression initializer;

    StmtVar(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }
    
    @Override
    public String toString() {
        if (initializer != null) {
            return "var " + name.lexema + " = " + initializer.toString() + ";";
        } else {
            return "var " + name.lexema + ";";
        }
    }

    @Override
    public void print(String indentation) {
       System.out.print(indentation + "StmtVar: " + name.lexema);
        if (initializer != null) {
            System.out.print(" = ");
            initializer.print(""); // Sin indentación adicional para el inicializador
        }
        System.out.println(";");
    }
}
