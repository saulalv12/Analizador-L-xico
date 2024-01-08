package interprete;

public class ExprSuper extends Expression {
    // final Token keyword;
    final Token method;

    ExprSuper(Token method) {
        // this.keyword = keyword;
        this.method = method;
    }

    @Override
    public void print(String indentation) {
       System.out.println(indentation + "ExprSuper");
        System.out.println(indentation + "\tMethod: " + method.lexema);
    }

    @Override
    public Object evaluate(TablaSimbolos tablita) {
        return null;
    }
}
