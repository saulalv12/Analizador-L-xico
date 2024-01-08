package interprete;

import java.util.List;

public class ExprCallFunction extends Expression{
    final Expression callee;
    // final Token paren;
    final List<Expression> arguments;

    ExprCallFunction(Expression callee, /*Token paren,*/ List<Expression> arguments) {
        this.callee = callee;
        // this.paren = paren;
        this.arguments = arguments;
    }

      @Override
    public String toString() {
        // Manejar un posible valor nulo para callee
        String calleeStr = (callee != null) ? callee.toString() : "null";

        // Construir la representación en cadena de la lista de argumentos
        StringBuilder argsStr = new StringBuilder();
        if (arguments != null) {
            for (int i = 0; i < arguments.size(); i++) {
                if (arguments.get(i) != null) {
                    argsStr.append(arguments.get(i).toString());
                } else {
                    argsStr.append("null");
                }
                if (i < arguments.size() - 1) {
                    argsStr.append(", ");
                }
            }
        }

        // Devolver la representación completa de la llamada a función
        return calleeStr + "(" + argsStr.toString() + ")";
    }

    @Override
    public void print(String indentation) {
        System.out.println(indentation + "ExprCallFunction");
        System.out.println(indentation + "\tCallee:");
        callee.print(indentation + "\t\t");

        System.out.println(indentation + "\tArguments:");
        for (Expression arg : arguments) {
            arg.print(indentation + "\t\t");
        }
    }
}
