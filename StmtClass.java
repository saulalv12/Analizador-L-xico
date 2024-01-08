package interprete;
import java.util.List;

public class StmtClass extends Statement {
    final Token name;
    final ExprVariable superclass;
    final List<StmtFunction> methods;

    StmtClass(Token name, ExprVariable superclass, List<StmtFunction> methods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }    

    @Override
    public void print(String indentation) {
        System.out.println(indentation + "StmtClass: " + name.lexema);
        if (superclass != null) {
            System.out.println(indentation + "\tSuperclass: " + superclass.toString());
        }
        System.out.println(indentation + "\tMethods:");
        for (StmtFunction method : methods) {
            method.print(indentation + "\t\t");
        }
    }

    @Override
    public Object evaluate(TablaSimbolos tablita) {
        miClase clase = new miClase(name.lexema,methods);
        if (tablita.existeClase(name.lexema)) {
            throw new RuntimeException("Clase ya definida: " + name.lexema);
        }
            tablita.registrarClase(name.lexema, clase);
        
        return clase;
    }
}
