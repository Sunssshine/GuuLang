package tools

class Lexer {

    private val keywords = listOf("print", "sub", "set", "call")
    private var tokens = mutableListOf<Token>()
    private val stringNum : Int


    constructor(expression : String, stringNum : Int)
    {
        tokenize(expression)
        this.stringNum = stringNum
    }

    fun getNextToken() : Token
    {
        if(tokens.size == 0)
        {
            return Token("\n", TokenType.EOL)
        }

        val token = tokens.first()
        tokens.removeAt(0)

        return token
    }

    private fun tokenize(expression : String) : List<Token>
    {

        val splittedArray = expression
                .trim()
                .split("\\s+".toRegex())

        splittedArray.forEach {

            if(it.isEmpty())
            {
                return@forEach
            }

            when {

                keywords.contains(it) -> tokens.add(Token(it, TokenType.KEYWORD))

                it.toIntOrNull() == null -> tokens.add(Token(it, TokenType.IDENTIFIER))

                else -> tokens.add(Token(it, TokenType.VALUE))
            }

        }
        return tokens

    }

    fun getTokensCount() : Int
    {
        return tokens.size
    }

    fun getStringNum() : Int
    {
        return stringNum
    }


}