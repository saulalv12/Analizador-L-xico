import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

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
                      //  System.out.println("espacio");
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

                    }
                    else if(c == 'E'){

                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
            }


        }


        return tokens;
    }
}
