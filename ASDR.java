package interprete;
import java.util.List;

public class ASDR implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;


    public ASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        PROGRAM();

        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

    // 
    private void PROGRAM(){
        DECLARATION();
    }
    
    private void DECLARATION(){
        if(preanalisis.tipo == TipoToken.FUN){
            
        }
        if(preanalisis.tipo == TipoToken.VAR){
            
        }
        if (preanalisis.tipo == TipoToken.EQUAL || preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE  || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN || preanalisis.tipo == TipoToken.FOR || preanalisis.tipo == TipoToken.IF || preanalisis.tipo == TipoToken.PRINT || preanalisis.tipo == TipoToken.RETURN || preanalisis.tipo == TipoToken.WHILE || preanalisis.tipo == TipoToken.LEFT_BRACE){
            
        }
    }
    //DECLARACIONES
    private void FUN_DECL(){
        if(preanalisis.tipo == TipoToken.FUN){
            
        }
    }
    
    private void VAR_DECL(){
        switch (preanalisis.tipo) {
            case VAR:
                break;
            case IDENTIFIER:
                break;
            default:
                break;
        }
    }
    
    private void VAR_INIT(){
        if(preanalisis.tipo == TipoToken.EQUAL){
            
        }
    }
    //SENTENCIAS
    private void STATEMENT(){
        
    }
      //expresion set, get, super no se usan 
    
    private Statement EXPR_STMT(){
        //regresa un statment expression
        return null;
        //regresa un statment expression
    } 
    
    private void FOR_STMT(){
        
    } 
    
    private void FOR_STMT_1(){
        
    }
    
    private void FOR_STMT_2(){
        
    }
    
    private void FOR_STMT_3(){
        
    }
    
    private void IF_STMT(){
        
    }
    
    private void ELSE_STMT(){
        
    }
    
    private void PRINT_STMT(){
        
    }
    
    private void RETURN_STMT(){
        
    }
    
    private void RETURN_EXP_OPC(){
        
    }
    
    private void WHILE_STMT(){
        
    }
    
    private void BLOCK(){
        if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            
        }
    }
    //EXPRESIONES
    private Expression  expression(){
        ASSIGNMENT();
        return null;
    }
    
    private void ASSIGNMENT(){
        LOGIC_OR();
        ASSIGNMENT_OPC();
    }
    //Expresion
    private void ASSIGNMENT_OPC(){
        if(preanalisis.tipo == TipoToken.EQUAL){
            
        }
    }
    
    private void LOGIC_OR(){
        LOGIC_AND();
        LOGIC_OR_2();
    }
    //exprlogical
    private void LOGIC_OR_2(){
     if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            
        }   
    }
    
    private void LOGIC_AND(){
        EQUALITY();
        LOGIC_AND_2();
    }
    //exptrlogical
    private void LOGIC_AND_2(){
        if(preanalisis.tipo == TipoToken.AND){
            
        }
    }
    
    private void EQUALITY(){
        COMPARISON();
        EQUALITY_2();
    }
    //equialty es binatry
    private void EQUALITY_2(){
        switch (preanalisis.tipo) {
            case BANG:
                break;
            case EQUAL:
                break;
            default:
                break;
        }
    }
    
    private Expression COMPARISON(){
        Expression expr = TERM();
        expr = COMPARISON_2(expr);
        return expr;
    }
    //and or logica
    //binary comparison
    private Expression COMPARISON_2(Expression expr){
        switch (preanalisis.tipo) {
            case GREATER:
                break;
            case GREATER_EQUAL:
                break;
            case LESS:
                break;
            case LESS_EQUAL:
                break;
        }
        return expr;
    }
    
    private Expression TERM(){
        Expression expr = FACTOR();
        expr = TERM_2(expr);
        return expr;
    }
    
    private Expression TERM_2(Expression expr){
        switch (preanalisis.tipo) {
            case MINUS:
                match(TipoToken.MINUS);
                Token operador = previous();
                Expression expr2 = FACTOR();
                ExprBinary expb = new ExprBinary(expr, operador, expr2);
                return FACTOR_2(expb);
            case PLUS:
                match(TipoToken.PLUS);
                operador = previous();
                expr2 = FACTOR();
                expb = new ExprBinary(expr, operador, expr2);
                return FACTOR_2(expb);
        }
        return expr;
    }
    
    private Expression FACTOR(){
        Expression expr = UNARY();
        expr = FACTOR_2(expr);
        return expr;
    }
    
    private Expression FACTOR_2(Expression expr){
        switch (preanalisis.tipo) {
            case SLASH:
                match(TipoToken.SLASH);
                Token operador = previous();
                Expression expr2 = UNARY();
                ExprBinary expb = new ExprBinary(expr, operador, expr2);
                return FACTOR_2(expb);
            case STAR:
                match(TipoToken.STAR);
                operador = previous();
                expr2 = UNARY();
                expb = new ExprBinary(expr, operador, expr2);
                return FACTOR_2(expb);
        }
        return expr;
    }
    
    private Expression UNARY(){
        switch (preanalisis.tipo) {
            case BANG:
                match(TipoToken.BANG);
                Token operador = previous();
                Expression expr = UNARY();
                return new ExprUnary(operador, expr);
            case MINUS:
               match(TipoToken.MINUS);
                operador = previous();
                expr = UNARY();
                return new ExprUnary(operador, expr);
            default:
                return CALL();
        }
    }
    
    private Expression CALL(){
        Expression expr = PRIMARY();
        expr = CALL_2(expr);
        return expr;
    }
    
    private Expression CALL_2(Expression expr){
        switch (preanalisis.tipo){
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                List<Expression> lstArguments = ARGUMENTS_OPC();
                match(TipoToken.RIGHT_PAREN);
                ExprCallFunction ecf = new ExprCallFunction(expr, lstArguments);
                return CALL_2(ecf);
        }
        return expr;
    }
    
    private Expression PRIMARY(){
        switch(preanalisis.tipo){
            case TRUE:
                match(TipoToken.TRUE);
                return new ExprLiteral(true);
            case FALSE:
                match(TipoToken.FALSE);
                return new ExprLiteral(false);
            case NULL:
                match(TipoToken.NULL);
                return new ExprLiteral(null);
            case NUMBER:
                match(TipoToken.NUMBER);
                Token numero = previous();
                return new ExprLiteral(numero.literal);
            case STRING:
                match(TipoToken.STRING);
                Token cadena = previous();
                return new ExprLiteral(cadena.literal);
            case IDENTIFIER:
                match(TipoToken.IDENTIFIER);
                Token id = previous();
                return new ExprVariable(id);
            case LEFT_BRACE:
                match(TipoToken.LEFT_PAREN);
                Expression expr = expression();
                // Tiene que ser cachado aquello que retorna
                match(TipoToken.RIGHT_PAREN);
                return new ExprGrouping(expr);
        }
        return null;
    }
    //OTRAS
    private void FUNCTION(){
        if (hayErrores) return;
        match(TipoToken.IDENTIFIER);
        match(TipoToken.LEFT_PAREN);
        PARAMETERS_OPC();
        match(TipoToken.RIGHT_PAREN);
        BLOCK();
    }
    
    private void FUNCTIONS(){
        if (hayErrores) return;
        FUN_DECL();
        FUNCTIONS();
    }
    
    private void PARAMETERS_OPC(){
        if (hayErrores) return;
        PARAMETERS();
    }
    
    private void PARAMETERS(){
        if (hayErrores) return;
        match(TipoToken.IDENTIFIER);
        //PARAMETERS_2(preanalisis.tipo);
    }
    
    private void PARAMETERS_2(List<Token>parametros){
        if (preanalisis.tipo==TipoToken.COMMA) {
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            parametros.add(name);
            PARAMETERS_2(parametros);
        }else{
             System.out.println("Error en la posicion" 
                        + preanalisis.posicion
                        +" cerca de "+ preanalisis.lexema
                     +" se esperaba un identificador");
        }
    }
    
    private List<Expression> ARGUMENTS_OPC(){
        switch(preanalisis.tipo){
            case BANG:
            case MINUS:
            case TRUE:
            case FALSE:
            case NULL:
            case NUMBER:
            case STRING:
            case IDENTIFIER:
                ARGUMENTS();
           
        default:
                System.out.println("Error en la posicion" 
                        + preanalisis.posicion
                        +" cerca de "+ preanalisis.lexema);   
        }
            
        
        
        return null;
    }
    
    private void ARGUMENTS(){
        if (hayErrores) return;
        match(TipoToken.COMMA);
        expression();
        ARGUMENTS();
    }
    
    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error encontrado");
        }
    }
    
    private Token previous() {
        return this.tokens.get(i - 1);
    }
}
