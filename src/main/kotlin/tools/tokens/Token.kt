package tools.tokens

import tools.TokenType

abstract class Token {
    private val value: String

    constructor(value: String) {
        this.value = value
    }

    abstract fun getArgs() : List<List<TokenType>>
    abstract fun getType() : TokenType

    fun getValue() : String
    {
        return value
    }
}