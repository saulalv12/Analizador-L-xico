package interprete;
import static interprete.TipoToken.BANG;
import static interprete.TipoToken.FALSE;
import static interprete.TipoToken.IDENTIFIER;
import static interprete.TipoToken.MINUS;
import static interprete.TipoToken.NULL;
import static interprete.TipoToken.NUMBER;
import static interprete.TipoToken.SEMICOLON;
import static interprete.TipoToken.STRING;
import static interprete.TipoToken.TRUE;
import static interprete.TipoToken.VAR;

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
    public List<Statement> PROGRAM(){
        List<Statement> statements = new ArrayList<>();
        if(preanalisis.tipo == TipoToken.FUN || preanalisis.tipo == TipoToken.VAR || preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER ||  preanalisis.tipo == TipoToken.LEFT_PAREN || preanalisis.tipo == TipoToken.FOR || preanalisis.tipo == TipoToken.IF || preanalisis.tipo == TipoToken.PRINT ||  preanalisis.tipo == TipoToken.RETURN || preanalisis.tipo == TipoToken.WHILE || preanalisis.tipo == TipoToken.LEFT_BRACE){
            DECLARATION(statements);
        }
        return statements;
    }
    
    private void DECLARATION(List <Statement> statements){
        if(preanalisis.tipo == TipoToken.FUN){
           Statement funDecl = FUN_DECL();
            statements.add(funDecl);
            DECLARATION(statements);
        }
        else if(preanalisis.tipo == TipoToken.VAR){
            Statement varDecl = VAR_DECL();
            statements.add(varDecl);
            DECLARATION(statements);
        }
        else if (preanalisis.tipo == TipoToken.EQUAL || preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE  || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN || preanalisis.tipo == TipoToken.FOR || preanalisis.tipo == TipoToken.IF || preanalisis.tipo == TipoToken.PRINT || preanalisis.tipo == TipoToken.RETURN || preanalisis.tipo == TipoToken.WHILE || preanalisis.tipo == TipoToken.LEFT_BRACE){
            Statement stmt = STATEMENT();
            statements.add(stmt);
            DECLARATION(statements);
        }
    }
    //DECLARACIONES
    private Statement FUN_DECL(){
        if(preanalisis.tipo == TipoToken.FUN){
            match(TipoToken.FUN);
            Statement funDecl = FUNCTION();
            return funDecl;
        }else{
            hayErrores=true;
            System.out.println("Error, se esperaba un 'fun'");
            return null;
        }
    }
    
    private Statement VAR_DECL(){
        Expression initialziaer = null;
        if(preanalisis.tipo == TipoToken.VAR){
            match(TipoToken.VAR);
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            if(preanalisis.tipo == TipoToken.EQUAL){
                initialziaer = VAR_INIT(initialziaer);
            }
            match(TipoToken.SEMICOLON);
            return new StmtVar(name, initialziaer);
        }else{
             hayErrores=true;
            System.out.println("Error, se esperaba un 'var'");
            return null;
        }
    }
    
    private Expression  VAR_INIT(Expression initializer){
        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            initializer = expression();
        }
        return initializer;
    }
    //SENTENCIAS
     private Statement STATEMENT(){
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Statement expr = EXPR_STMT();
            return expr;
        }else if(preanalisis.tipo == TipoToken.FOR){
            Statement forStmt = FOR_STMT();
            return forStmt;
        }else if(preanalisis.tipo == TipoToken.IF){
            Statement if1 = IF_STMT();
            return if1;
        }else if(preanalisis.tipo == TipoToken.PRINT){
            Statement print  = PRINT_STMT();
            return print;
        }else if(preanalisis.tipo == TipoToken.RETURN){
            Statement return1 = RETURN_STMT();
            return return1;
        }else if(preanalisis.tipo == TipoToken.WHILE){
            Statement while1 = WHILE_STMT();
            return while1;
        }else if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            Statement block = BLOCK();
            return block;
        }else{
             hayErrores=true;
             System.out.println("Error");
             return null;
        }
    }
      //expresion set, get, super no se usan 
    
    private Statement EXPR_STMT(){
        Expression expr = expression();
        match(TipoToken.SEMICOLON);
        return new StmtExpression(expr);
        
    } 
    
    private Statement FOR_STMT(){
        if(preanalisis.tipo == TipoToken.FOR){
            match(TipoToken.FOR);
            match(TipoToken.LEFT_PAREN);
            FOR_STMT_1();
            Expression condition = FOR_STMT_2();
            FOR_STMT_3();
            match(TipoToken.RIGHT_PAREN);
            Statement body = STATEMENT();
            return new StmtLoop(condition, body);
        }else{
             hayErrores=true;
            System.out.println("Error, se esperaba un 'for'");
            return null;
        }
    } 
    
    private void FOR_STMT_1(){
        if(preanalisis.tipo == TipoToken.VAR){
            VAR_DECL();
        }else if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPR_STMT();
        }else if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
        }else{
            hayErrores=true;
            System.out.println("Error en el primer elemento del for de la linea ");
        }
    }
    
    private Expression FOR_STMT_2(){
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = expression();
            match(TipoToken.SEMICOLON);
            return new ExprGrouping(expr);
        }else if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
            return new ExprGrouping(null);
        }else{
            hayErrores=true;
            System.out.println("Error en el segundo elemento del for de la linea ");
            return null;
        }
    }
    
    private void FOR_STMT_3(){
       expression();
    }
    
    private Statement IF_STMT(){
        Statement elseBranch = null;
        if(preanalisis.tipo==TipoToken.IF){
            match(TipoToken.IF);
            match(TipoToken.LEFT_PAREN);
            Expression condition = expression();
            match(TipoToken.RIGHT_PAREN);
            Statement thenBranch =STATEMENT();
            if(preanalisis.tipo == TipoToken.ELSE){
                elseBranch= ELSE_STMT(elseBranch);
            }   
            return new StmtIf(condition, thenBranch, elseBranch);
        }else{
            hayErrores=true;
            System.out.println("Error, se esperaba un 'if'");
            return null;
        }
    }
    
    private Statement ELSE_STMT(Statement elseBranch){
        if(preanalisis.tipo == TipoToken.ELSE){
            match(TipoToken.ELSE);
            elseBranch = STATEMENT();
            return elseBranch;
        }
        return elseBranch;
    }
    
    private Statement PRINT_STMT(){
        if(preanalisis.tipo == TipoToken.PRINT){
            match(TipoToken.PRINT);
            Expression expr = expression();
            match(TipoToken.SEMICOLON);
            return new StmtPrint(expr);
        }else{
            hayErrores=true;
            System.out.println("Error, se esperaba un 'print'");
            return null;
        }
    }
    
    private Statement RETURN_STMT(){
        Expression value=null;
        if(preanalisis.tipo == TipoToken.RETURN){
            match(TipoToken.RETURN);
            if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
                value = RETURN_EXP_OPC(value);
            }
            match(TipoToken.SEMICOLON);
            return new StmtReturn(value);
        }else{
            hayErrores=true;
            System.out.println("Error, se esperaba un 'return'");
            return null;
        }
    }
    
    private Expression RETURN_EXP_OPC(Expression value){
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            value = expression();
            return value;
        }
        return value;
    }
    
    private Statement WHILE_STMT(){
        if(preanalisis.tipo == TipoToken.WHILE){
            match(TipoToken.WHILE);
            match(TipoToken.LEFT_PAREN);
            Expression condition = expression();
            match(TipoToken.RIGHT_PAREN);
            Statement body = STATEMENT();
            return new StmtLoop(condition, body);
        }else{
            hayErrores=true;
            System.out.println("Error, se esperaba un 'while'");
            return null;
        }
    }
    
    private Statement BLOCK(){
        List <Statement> statements =new ArrayList<>();
        if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            match(TipoToken.LEFT_BRACE);
            DECLARATION(statements);
            match(TipoToken.RIGHT_BRACE);
            return new StmtBlock(statements);
        }else{
            hayErrores=true;
            System.out.println("Error, se esperaba un 'LEFT_BRACE'");
            return null;
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
        if (hayErrores) return;
        match(TipoToken.EQUAL);
        expression();
    }
    
    private void LOGIC_OR(){
        LOGIC_AND();
        LOGIC_OR_2();
    }
    //exprlogical
    private void LOGIC_OR_2(){
        if (hayErrores) return;
        match(TipoToken.OR);
        LOGIC_AND();
        LOGIC_OR_2();
    }
    
    private void LOGIC_AND(){
        EQUALITY();
        LOGIC_AND_2();
    }
    //exptrlogical
    private void LOGIC_AND_2(){
        if (hayErrores) return;
        match(TipoToken.AND);
        EQUALITY();
        LOGIC_AND_2();
               
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
