package tools

fun printHandler(argument : Token)
{
    when(argument.type)
    {
        TokenType.IDENTIFIER->{ }// resolve identifier
        TokenType.VALUE->{println(argument.value)}
        else->{ }// can't handle error
    }
}

fun callHandler(argument : Token)
{
    when(argument.type)
    {
        TokenType.IDENTIFIER->{ }//resolve fun identifier
        else->{ }// can't handle error
    }

    println("call handler")
}

fun subHandler(argument : Token)
{
    when(argument.type)
    {
        TokenType.IDENTIFIER->{ }// add fun identifier
        else->{ } // can't handle error
    }

    println("sub handler")
}

fun setHandler(identifier : Token, value : Token)
{
    if((identifier.type == TokenType.IDENTIFIER) &&
             value.type == TokenType.VALUE)
    {
        // add variable identifier
        // set variable to value
    }
    else
    {
        // can't handle error
    }

    println("set handler")
}

fun handlersMatcher(lexer: Lexer)
{
    val firstToken = lexer.getNextToken()
    if(firstToken.type != TokenType.KEYWORD)
    {
        // expect keyword but got type
    }
    else
    {
        when(firstToken.value)
        {
            // TODO EXTRA ARGS HANDLE
            "print" ->  printHandler(lexer.getNextToken())
            "set"   ->  setHandler(lexer.getNextToken(), lexer.getNextToken())
            "call"  ->  callHandler(lexer.getNextToken())
            "sub"   ->  subHandler(lexer.getNextToken())
            else    ->  "wtf"
        }
    }

    if(lexer.getTokensCount() > 0)
    {
        println("Unexpected tokens: ")
        var token = lexer.getNextToken()
        while(lexer.getNextToken().type != TokenType.EOL)
        {
            println("${token.type} - ${token.value}")
            token = lexer.getNextToken()
        }
    }
}