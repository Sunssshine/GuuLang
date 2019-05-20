package tools

import tools.tokens.Token

class ExpressionParser {

    constructor(tokens : List<Token>)
    {
        parse(tokens)
    }

    fun parse(tokens : List<Token>)
    {

        tokens.first().getArgs().forEach { args->
            if(tryArgs(tokens, 1, args) && (args.size + 1 == tokens.size))
            {
                println("${tokens.first().getValue()} -> $args OK")
                return
            }
        }

        print("${tokens.first().getValue()}: expected ${tokens.first().getArgs()}," +
                " but got [")
        tokens.subList(1, tokens.size).forEach{
            print(" ${it.getType()}")}
        print(" ]")
        println("")

    }

    fun tryArgs(tokens : List<Token>, index : Int, args: List<TokenType>) : Boolean
    {
        var i = 0
        while(i < args.size)
        {
            if((tokens.size < index+i) || (tokens[index+i].getType() != args[i]))
                return false
            i++
        }
        return true
    }
}