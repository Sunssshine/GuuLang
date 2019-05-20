package tools.tokens

import tools.TokenType

class ValueToken(value: String) : Token(value) {
    override fun getArgs(): List<List<TokenType>>
    {
        return listOf(listOf())
    }

    override fun getType(): TokenType
    {
        return TokenType.VALUE
    }

}