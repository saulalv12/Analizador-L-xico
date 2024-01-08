package interprete;

public class ExprSet extends Expression{
    final Expression object;
    final Token name;
    final Expression value;

    ExprSet(Expression object, Token name, Expression value) {
        this.object = object;
        this.name = name;
        this.value = value;
    }

    @Override
    public void print(String indentation) {
         System.out.println(indentation + "ExprSet");
        System.out.println(indentation + "\tObject:");
        object.print(indentation + "\t\t");
        System.out.println(indentation + "\tName: " + name.lexema);
        System.out.println(indentation + "\tValue:");
        value.print(indentation + "\t\t");
    }

    @Override
    public Object evaluate(TablaSimbolos tablita) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
