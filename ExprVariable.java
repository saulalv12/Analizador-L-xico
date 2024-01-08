package interprete;

class ExprVariable extends Expression {
    final Token name;
   
    
    ExprVariable(Token name, TablaSimbolos tablita) {
        this.name = name;
        
    }
    @Override
    public String toString() {
        return name.lexema;
    }

    @Override
    public void print(String indentation) {
        System.out.println(indentation + "ExprVariable: " +  name.lexema);
    }

    @Override
public Object evaluate(TablaSimbolos tablita) {
    
    if (tablita.existeIdentificador(name.lexema)) {
        return tablita.obtener(name.lexema); 
    } else {
        throw new RuntimeException("La variable '" + name.lexema + "' no está definida.");
    }
}
public String getName(){
    return name.lexema;
}

}
