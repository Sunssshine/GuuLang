package tools.tokens

import tools.TokenType

class KeywordToken(keyword: String) : Token(keyword) {

    override fun getArgs(): List<List<TokenType>>
    {
        when(getValue())
        {
            "print"-> return listOf(
                    listOf(TokenType.IDENTIFIER),
                    listOf(TokenType.VALUE)
            )

            "call"-> return listOf(
                    listOf(TokenType.IDENTIFIER)
            )

            "sub"-> return listOf(
                    listOf(TokenType.IDENTIFIER)
            )

            "set"-> return listOf(
                    listOf(TokenType.IDENTIFIER, TokenType.IDENTIFIER),
                    listOf(TokenType.IDENTIFIER, TokenType.VALUE)
            )
            else -> error("Can't resolve this keyword: ${getValue()}")
        }
    }

    override fun getType(): TokenType
    {
        return TokenType.KEYWORD
    }
}