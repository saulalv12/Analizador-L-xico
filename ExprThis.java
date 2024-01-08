package interprete;

public class ExprThis extends Expression{
    // final Token keyword;

    ExprThis() {
        // this.keyword = keyword;
    }

    @Override
    public void print(String indentation) {
         System.out.println(indentation + "ExprThis");
    
    }

    @Override
    public Object evaluate(TablaSimbolos tablita) {
        return null;
    }
}
