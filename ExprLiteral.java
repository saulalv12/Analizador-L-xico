package interprete;
class ExprLiteral extends Expression {
    final Object value;

    ExprLiteral(Object value) {
        this.value = value;
    }
     @Override
    public String toString() {
        if (value instanceof String) {
            // Asegúrate de que el valor no contenga comillas
            return (String) value;
        } else {
            // Usa String.valueOf para convertir otros objetos a una cadena
            return String.valueOf(value);
        }
    }

    @Override
    public void print(String indentation) {
         System.out.println(indentation + "ExprLiteral: " + value);
    }
}
