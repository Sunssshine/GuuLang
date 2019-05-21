package tools

fun printHandler(argument : Token, env : Environment)
{
    when(argument.type)
    {
        TokenType.IDENTIFIER->{
            println(
                    env.resolveVariable(argument.value)
            )
            env.addLineToExecutionLog(env.resolveVariable(argument.value).toString())
        }

        TokenType.VALUE->{
            println(argument.value)
            env.addLineToExecutionLog(argument.value)
        }
        else->{
            throw Exception("Expected [IDENTIFIER] or [VALUE] but here is [${argument.type}]")
        }
    }
}

fun callHandler(argument : Token, env : Environment)
{
    when(argument.type)
    {
        TokenType.IDENTIFIER->{
            env.callFunction(argument.value)
        }
        else->{
            throw Exception("Expected [IDENTIFIER] but here is [${argument.type}]")
        }
    }
}

fun subHandler(identifier : Token, strNumber : Int, env : Environment)
{
    when(identifier.type)
    {
        TokenType.IDENTIFIER->{
            env.addFunction(identifier.value, strNumber)
        }
        else->{
            throw Exception("Expected [IDENTIFIER] but here is [${identifier.type}]")
        }
    }
}

fun setHandler(identifier : Token, value : Token, env : Environment)
{
    if((identifier.type == TokenType.IDENTIFIER) &&
            value.type == TokenType.VALUE)
    {
        env.setVariable(identifier.value, value.value.toInt())
    }
    else
    {
        throw Exception("Expected [IDENTIFIER, VALUE] but here is [${identifier.type}, ${value.type}]")
    }
}