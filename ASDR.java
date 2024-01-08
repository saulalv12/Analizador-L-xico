package interprete;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ASDR implements Parser {

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private List<Statement> statements; 
    private TablaSimbolos tablaSimbolos = new TablaSimbolos();
    public ASDR(List<Token> tokens) {
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
        statements = new ArrayList<>(); 
    }

    @Override
    public boolean parse() {
        statements = PROGRAM(); 
        
        if (preanalisis.tipo == TipoToken.EOF && !hayErrores) {
            System.out.println("Entrada correcta");
             printTree();
            return true;
        } else {
            System.out.println("Se encontraron errores");
            return false;
        }
    }

    public List<Statement> PROGRAM() {
        statements.clear(); 
        if (preanalisis.tipo == TipoToken.FUN || preanalisis.tipo == TipoToken.VAR || preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN || preanalisis.tipo == TipoToken.FOR || preanalisis.tipo == TipoToken.IF || preanalisis.tipo == TipoToken.PRINT || preanalisis.tipo == TipoToken.RETURN || preanalisis.tipo == TipoToken.WHILE || preanalisis.tipo == TipoToken.LEFT_BRACE) {
            DECLARATION(statements);
        }
        return statements; // Devuelve la lista de declaraciones de la variable de instancia    
    }
    
    public List<Statement> getStatements() {
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
    private Expression expression(){
        ASSIGNMENT();
        return null;
    }
    
    private Expression ASSIGNMENT(){
        Expression expr = LOGIC_OR();
        expr = ASSIGNMENT_OPC(expr);
        return expr;
    }
    //Expresion
    private Expression ASSIGNMENT_OPC(Expression expr){
        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            Token operadorL = previous();
            expr = expression();
            return new ExprAssign(operadorL, expr);
        }
        return expr;
    }
    
    private Expression LOGIC_OR(){
        Expression expr = LOGIC_AND();
        expr = LOGIC_OR_2(expr);
        return expr;
    }
    //exprlogical
    private Expression LOGIC_OR_2(Expression expr){
        if(preanalisis.tipo == TipoToken.AND){
            match(TipoToken.AND);
            Token operadorL= previous();
            Expression expr2= LOGIC_AND();
            ExprLogical expl = new ExprLogical(expr, operadorL, expr2);
            return LOGIC_OR_2(expl);
        }
        return expr;
    }
    
    private Expression LOGIC_AND(){
        Expression expr = EQUALITY();
        expr = LOGIC_AND_2(expr);
        return expr;
    }
    //exptrlogical
    private Expression LOGIC_AND_2(Expression expr){
        if(preanalisis.tipo == TipoToken.AND){
            match(TipoToken.AND);
            Token operadorL= previous();
            Expression expr2= EQUALITY();
            ExprLogical expl = new ExprLogical(expr, operadorL, expr2);
            return LOGIC_AND_2(expl);
        }
        return expr;       
    }
    
    private Expression EQUALITY(){
        Expression expr = COMPARISON();
        expr = EQUALITY_2(expr);
        return expr;
    }
    //equialty es binatry
    private Expression EQUALITY_2(Expression expr){
        switch (preanalisis.tipo) {
            case BANG_EQUAL:
                match(TipoToken.BANG_EQUAL);
                Token operador = previous();
                Expression expr2 = COMPARISON();
                ExprBinary expb = new ExprBinary(expr, operador, expr2);
                return EQUALITY_2(expb);
            case EQUAL_EQUAL:
                match(TipoToken.EQUAL_EQUAL);
                operador = previous();
                expr2 = COMPARISON();
                expb = new ExprBinary(expr, operador, expr2);
                return EQUALITY_2(expb);
        }
        return expr;
    }
    
    private Expression COMPARISON() {
        Expression expr = TERM();
        expr = COMPARISON_2(expr);
        return expr;
    }

    //and or logica
    //binary comparison
    private Expression COMPARISON_2(Expression expr) {
        switch (preanalisis.tipo) {
            case GREATER:
                match(TipoToken.GREATER);
                Token operador = previous();
                Expression expr2 = TERM();
                ExprBinary expb = new ExprBinary(expr, operador, expr2);
                return COMPARISON_2(expb);
            case GREATER_EQUAL:
                match(TipoToken.GREATER_EQUAL);
                operador = previous();
                expr2 = TERM();
                expb = new ExprBinary(expr, operador, expr2);
                return COMPARISON_2(expb);
            case LESS:
                match(TipoToken.LESS);
                operador = previous();
                expr2 = TERM();
                expb = new ExprBinary(expr, operador, expr2);
                COMPARISON_2(expb);
            case LESS_EQUAL:
                match(TipoToken.LESS_EQUAL);
                operador = previous();
                expr2 = TERM();
                expb = new ExprBinary(expr, operador, expr2);
                COMPARISON_2(expb);
        }
        return expr;
    }

    private Expression TERM() {
        Expression expr = FACTOR();
        expr = TERM_2(expr);
        return expr;
    }

    private Expression TERM_2(Expression expr) {
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

    private Expression FACTOR() {
        Expression expr = UNARY();
        expr = FACTOR_2(expr);
        return expr;
    }

    private Expression FACTOR_2(Expression expr) {
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

    private Expression UNARY() {
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

    private Expression CALL() {
        Expression expr = PRIMARY();
        expr = CALL_2(expr);
        return expr;
    }

    private Expression CALL_2(Expression expr) {
        switch (preanalisis.tipo) {
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                List<Expression> lstArguments = ARGUMENTS_OPC();
                match(TipoToken.RIGHT_PAREN);
                ExprCallFunction ecf = new ExprCallFunction(expr, lstArguments);
                return CALL_2(ecf);
        }
        return expr;
    }

    private Expression PRIMARY() {
        switch (preanalisis.tipo) {
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
    private Statement FUNCTION() {
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            Token name=previous();
            match(TipoToken.LEFT_PAREN);
            List <Token> params = PARAMETERS_OPC();       
            match(TipoToken.RIGHT_PAREN);
            Statement body = BLOCK();
            return new StmtFunction(name, params, (StmtBlock) body);
        }else{
            hayErrores=true;
            System.out.println("Error, se esperaba un id");
            return null;
        }
    }

    private void FUNCTIONS() {
        if(preanalisis.tipo == TipoToken.FUN){
            FUN_DECL();
             FUNCTIONS();
         }
    }

    private List <Token> PARAMETERS_OPC() {
        List <Token> params = new ArrayList<>();
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            PARAMETERS(params);
        }
        return params;
    }

    private void PARAMETERS(List <Token> params) {
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            Token paramToken=preanalisis;
            match(TipoToken.IDENTIFIER);
            params.add(paramToken);
            PARAMETERS_2(params);
        }else{
            hayErrores=true;
            System.out.println("Error, se esperaba un id");
        }
    }

    private void PARAMETERS_2(List<Token> parametros) {
        if (preanalisis.tipo == TipoToken.COMMA) {
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            parametros.add(name);
            PARAMETERS_2(parametros);
        } else {
            System.out.println("Error, se esperaba un identificador");
        }
    }

    private List<Expression> ARGUMENTS_OPC(){
        List <Expression> arguments = new ArrayList<>();
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){           
            arguments.add(expression());
            ARGUMENTS(arguments);
        }
        return arguments;
    }

    private void ARGUMENTS(List <Expression> arguments){ 
        while(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            arguments.add(expression());
        }
    }

    private void match(TipoToken tt) {
        if (preanalisis.tipo == tt) {
            i++;
            preanalisis = tokens.get(i);
        } else {
            hayErrores = true;
            System.out.println("Error encontrado");
        }
    }

    private Token previous() {
        return this.tokens.get(i - 1);
    }
    
    public void printTree() {
    for (Statement stmt : statements) {
        stmt.print("");
        }
    }
}
