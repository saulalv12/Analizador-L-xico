package interprete;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    int linea = 1;
    private static final Map<String, TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and",    TipoToken.AND);
        palabrasReservadas.put("else",   TipoToken.ELSE);
        palabrasReservadas.put("false",  TipoToken.FALSE);
        palabrasReservadas.put("for",    TipoToken.FOR);
        palabrasReservadas.put("fun",    TipoToken.FUN);
        palabrasReservadas.put("if",     TipoToken.IF);
        palabrasReservadas.put("null",   TipoToken.NULL);
        palabrasReservadas.put("or",     TipoToken.OR);
        palabrasReservadas.put("print",  TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true",   TipoToken.TRUE);
        palabrasReservadas.put("var",    TipoToken.VAR);
        palabrasReservadas.put("while",  TipoToken.WHILE);
    }
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    public Scanner(String source){
        this.source = source + " ";
    }
    public List<Token> scan() throws Exception {
        int estado = 0;
        String lexema = "";
        char c;
        for(int i=0; i<source.length(); i++){
            c = source.charAt(i);
           switch (estado){
                case 0:
                   if (c=='+'){
                        tokens.add(new Token(TipoToken.PLUS, "+"));
                        estado = 0;
                    }
                    else if(c=='-'){
                        tokens.add(new Token(TipoToken.MINUS, "-"));
                        estado = 0;
                    }
                    else if(c=='*'){
                        tokens.add(new Token(TipoToken.STAR, "*"));
                        estado = 0;
                    }
                    else if(c=='{'){
                        tokens.add(new Token(TipoToken.LEFT_BRACE, "{"));
                        estado = 0;
                    }
                    else if(c=='}'){
                        tokens.add(new Token(TipoToken.RIGHT_BRACE, "}"));
                        estado = 0;
                    }
                    else if(c=='('){
                        tokens.add(new Token(TipoToken.LEFT_PAREN, "("));
                        estado = 0;
                    }
                    else if(c==')'){
                        tokens.add(new Token(TipoToken.RIGHT_PAREN, ")"));
                        estado = 0;
                    }
                    else if(c==','){
                        tokens.add(new Token(TipoToken.COMMA, ","));
                        estado = 0;
                    }
                    else if(c=='.'){
                        tokens.add(new Token(TipoToken.DOT, "."));
                        estado = 0;
                    }
                    else if(c==';'){
                        tokens.add(new Token(TipoToken.SEMICOLON, ";"));
                        estado = 0;
                    }
                    else if(c=='>'){
                        estado = 1;
                        lexema+=c;
                    }
                    else if(c=='<'){
                        estado=4;
                        lexema+=c;
                    }
                    else if (c=='=') {
                        estado = 7;
                        lexema += c;
                    }
                    else if(c=='!'){
                        estado = 10;
                        lexema += c;
                    }
                    else if(Character.isLetter(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    else if(c=='"'){
                        estado = 24;
                        lexema += c;
                    }
                    else if (c== '/') {
                        estado = 26;
                    }
                    else if(Character.isWhitespace(c)){
                      //System.out.println("espacio");
                      //Se agrega este if para evitar errores de reconocimiento de casos posteriores
                    }
                    else{
                        throw new Exception("Caracter desconocido " + "'"+  c +"' "+ "en la linea " + linea + ".");
                     }
                    break;
                case 1:
                    if (c=='=') {
                        lexema+=c;
                        tokens.add(new Token(TipoToken.GREATER_EQUAL,lexema));
                        lexema = "";
                    }
                    else{
                        i--;
                        tokens.add(new Token(TipoToken.GREATER,lexema));
                        lexema = "";
                    }
                    estado = 0;
                    break;
                case 4:
                    if (c=='=') {
                        lexema+=c;
                        tokens.add(new Token(TipoToken.LESS_EQUAL,lexema));
                        lexema = "";
                    }
                    else{
                        i--;
                        tokens.add(new Token(TipoToken.LESS,lexema));
                        lexema = "";
                    }
                    estado = 0;
                    break;
                case 7:
                    if (c=='=') {
                        lexema+=c;
                        tokens.add(new Token(TipoToken.EQUAL_EQUAL,lexema));
                        lexema = "";
                    }
                    else{
                        i--;
                        tokens.add(new Token(TipoToken.EQUAL,lexema));
                        lexema = "";
                    }
                    estado = 0;
                    break;
                case 10:
                    if (c=='=') {
                        lexema+=c;
                        tokens.add(new Token(TipoToken.BANG_EQUAL,lexema));
                        lexema = "";
                    }
                    else{
                        i--;
                        tokens.add(new Token(TipoToken.BANG,lexema));
                        lexema = "";
                    }
                    estado = 0;
                    break;
                case 13:
                    if(Character.isLetterOrDigit(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else{
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }
                        else{
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 15:
                    if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    else if(c == '.'){
                        estado=16;
                        lexema+=c;
                    }
                    else if(c == 'E'){
                        estado=18;
                        lexema+=c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 16:
                    if (Character.isDigit(c)) {
                        estado=17;
                        lexema+=c;
                    }
                    break;
                case 17:
                    if(Character.isDigit(c)){
                        estado = 17;
                        lexema += c;
                    }
                    else if (c=='E') {
                        estado=18;
                        lexema+=c;
                    }else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 18:
                    if (c == '+' || c == '-' ) {
                        estado = 19;
                        lexema+=c;
                    }else{
                        estado=20;
                        lexema+=c;
                    }
                    break;
              case 19:
                    if (Character.isDigit(c)) {
                        estado = 20;                        
                        lexema+=c;
                    }
                    break;   
              case 20:
                    if (Character.isDigit(c)) {
                        estado = 20;
                        lexema+=c;
                    }else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;  
              case 24:
                    if (c == '"') {
                        lexema+=c;
                        tokens.add(new Token(TipoToken.STRING, lexema, lexema));
                        lexema = "";
                        estado = 0;
                    } 
                    else if (c == '\n') {
                        throw new Exception("Cadena no puede contener un salto de línea en la línea " + linea + ".");
                    }
                    else if (i == source.length() - 1) {
                        throw new Exception("Cadena no cerrada en la línea " + linea + ".");
                    } 
                    else {
                        lexema += c;
                    }
                    break;
              case 26:
                    if(c=='*'){
                        estado = 27; 
                    }
                    else if(c=='/'){
                        estado=30;  
                    }
                    else{
                        i--;  
                        tokens.add(new Token(TipoToken.SLASH,"/"));
                        estado=0;
                        lexema="";  
                    }
                    break;  
              case 27:
                    if(c=='*'){
                        estado=28; 
                    }
                    else {
                        estado=27;   
                    }
                    break;
              case 28:
                    if(c=='/'){
                        estado=0;    
                    }
                    else if(c=='*'){
                        estado=28;   
                    }
                    else{
                        estado=27;   
                    }
                    break;
                case 30:
                    if(c=='\n'){
                        estado=0;
                        lexema="";
                    }
                    else{
                        estado=30;
                    }
                    break;
            }
        }
        return tokens;
    }
}
