package interprete;


public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;

    ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public void print(String indentation) {
         System.out.println(indentation + "ExprUnary");
        System.out.println(indentation + "\tOperator: " + operator.lexema);
        System.out.println(indentation + "\tRight:");
        right.print(indentation + "\t\t");
    }

    @Override
    public Object evaluate(TablaSimbolos tablita) {
        Object expr = right.evaluate(tablita);
        switch(operator.tipo){
            case MINUS:
                if (!(expr instanceof Double)) {
                throw new RuntimeException("El operador '-' no se puede aplicar ya que no es un numero.");
            }
                if (expr instanceof Double) {
                    return -(double)expr;
                }
            case BANG:
                return !isTruthy(expr);
            
            
            default:
                 throw new RuntimeException("Operador unario no soportado: " + operator.tipo);
        }
        
        
    }
    private boolean isTruthy(Object object) {
    if (object == null) return false;
    if (object instanceof Boolean) return (Boolean) object;
    return true;
