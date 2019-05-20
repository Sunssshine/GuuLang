package tools

import tools.tokens.IdentifierToken
import tools.tokens.KeywordToken
import tools.tokens.Token
import tools.tokens.ValueToken

class Lexer {

    val keywords = listOf("print", "sub", "set", "call")
    var identifiers = listOf("a, hello, b, c, d")
    var tokens = ArrayList<Token>()


    constructor(expression : String)
    {
        tokenize(expression)
    }

    fun tokenize(expression : String) : List<Token>
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

                keywords.contains(it) -> tokens.add(KeywordToken(it))

                it.toIntOrNull() == null -> tokens.add(IdentifierToken(it))

                else -> tokens.add(ValueToken(it))
            }

        }
        return tokens

    }


    fun getTokens() : List<Token>
    {
        return tokens
    }
}