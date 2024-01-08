package interprete;

public class ExprLogical extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    ExprLogical(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public void print(String indentation) {
       System.out.println(indentation + "ExprLogical");
        System.out.println(indentation + "\tLeft:");
        left.print(indentation + "\t\t");
        System.out.println(indentation + "\tOperator: " + operator.lexema);
        System.out.println(indentation + "\tRight:");
        right.print(indentation + "\t\t");
    }

    
    @Override
    public Object evaluate(TablaSimbolos tablita) {
        Object leftValue = left.evaluate(tablita);
        Object rightValue = right.evaluate(tablita);
         if (!(leftValue instanceof Boolean && rightValue instanceof Boolean)) {
        throw new RuntimeException("Error, las operaciones lógicas solo pueden realizarse con valores booleanos");
    }
        
            switch (operator.tipo)
            {
                case AND:
                    return ((Boolean) leftValue && (Boolean) rightValue);
                case OR:
                    return ((Boolean) leftValue || (Boolean) rightValue);
                default:
                    throw new RuntimeException("Operador lógico no soportado: " + operator.lexema);
            }
        
        
        
       
    }
}
