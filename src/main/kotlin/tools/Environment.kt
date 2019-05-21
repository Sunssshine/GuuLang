package tools

import java.util.*

class Environment {

    // TODO PARSE ALL FUNCTIONS ON START

    private var variables = hashMapOf<String, Int>()   // var name -> var value
    private var functions = hashMapOf<String, Int>()   // fun name -> fun str

    private var instructionPointer = 0

    private var callStack = Stack<Int>()

    private val instructions : List<String>

    constructor(sourceCode: String) {
        this.instructions = sourceCode.split('\n').toList()
        parseFuncMap()
        callMain()
        // execute() if not debug
    }

    fun executeNext()
    {
        if(instructionPointer >= instructions.size)
            throw Throwable("Reach end of program")

        while(instructions[instructionPointer].isEmpty()) {
            instructionPointer++
        }
        handlersMatcher(
                Lexer(
                instructions[instructionPointer],
                instructionPointer
                )
        )
        instructionPointer++
    }

    fun executeStepOver()
    {
        val callStackCurrSize = callStack.size
        do {
            try {
                executeNext()
            }
            catch(e : Exception)
            {
                println(e)
                return
            }
        }
        while(callStackCurrSize != callStack.size)
    }

    fun getVariables() : HashMap<String, Int>
    {
        println("Variables list:")
        variables.forEach{
            println("name: ${it.key} -> value: ${it.value}")
        }
        return variables
    }

    fun executeAll()
    {

        while(instructionPointer < instructions.size)
        {
            executeNext()
        }
    }

    fun getCurrentInstruction() : String
    {
        return instructions[instructionPointer] + " (line #${instructionPointer+1})"
    }

    fun parseFuncMap()
    {
        var strCounter = 0
        instructions.forEach {
            if(it.isNotEmpty())
            {
                val lexer = Lexer(it, strCounter)
                if(lexer.getNextToken().value == "sub")
                {
                    subHandler(lexer.getNextToken(), lexer.getStringNum())
                }
            }
            strCounter++
        }
    }

    fun getCallTrace() : String
    {
        for(i in 0 until callStack.size)
        {
            if(callStack[i] == -1)
            {
                println("From nowhere to main")
            }
            else {
                println("-> ${instructions[callStack[i]].trim()} (line #${callStack[i]})")
            }
        }
        return ""
    }

    fun setVariable(identifier : String, value : Int)
    {
        variables[identifier] = value
    }

    fun callMain()
    {
        val mainStrNum = resolveFunction("main")

        if (mainStrNum == null)
        {
            // ERROR FUN NOT FOUND
            println("Can't find entry point for program. Fatal error.")
            return
        }

        instructionPointer = mainStrNum+1
        callStack.push(-1)
    }

    fun callFunction(identifier : String) {
        val funStrNum = resolveFunction(identifier)

        if (funStrNum == null)
        {
            // ERROR FUN NOT FOUND
            "wtf"
            return
        }

        callStack.push(instructionPointer)
        instructionPointer = funStrNum
    }

    fun resolveVariable(identifier : String) : Int?
    {
        return variables[identifier]
    }

    fun tryAddFunction(identifier : String, stringNumber : Int) : Boolean
    {
        if(functions.containsKey(identifier))
            return false

        functions[identifier] = stringNumber
        return true
    }

    fun resolveFunction(identifier : String) : Int?
    {
        return functions[identifier]
    }

    fun returnFromCurrFunction()
    {
        instructionPointer = callStack.pop()
    }

    fun printHandler(argument : Token)
    {
        when(argument.type)
        {
            TokenType.IDENTIFIER->{
                println(
                        this.resolveVariable(argument.value)
                )
            }

            TokenType.VALUE->{println(argument.value)}
            else->{
                println("Expected [IDENTIFIER] or [VALUE] but here is [${argument.type}]")
            }// can't handle error
        }
    }

    fun callHandler(argument : Token)
    {
        when(argument.type)
        {
            TokenType.IDENTIFIER->{
                callFunction(argument.value)
            }//resolve fun identifier
            else->{
                println("Expected [IDENTIFIER] but here is [${argument.type}]")
            }// can't handle error
        }

        println("call handler")
    }

    fun subHandler(identifier : Token, strNumber : Int)
    {
        when(identifier.type)
        {
            TokenType.IDENTIFIER->{
                println(this.tryAddFunction(identifier.value, strNumber))
            }
            else->{
                println("Expected [IDENTIFIER] but here is [${identifier.type}]")
            } // can't handle error
        }

        println("sub handler")
    }

    fun setHandler(identifier : Token, value : Token)
    {
        if((identifier.type == TokenType.IDENTIFIER) &&
                value.type == TokenType.VALUE)
        {
            this.setVariable(identifier.value, value.value.toInt())
        }
        else
        {
            println("Expected [IDENTIFIER, VALUE] but here is [${identifier.type}, ${value.type}]")
        }

        println("set handler")
    }

    fun handlersMatcher(lexer: Lexer)
    {
        val firstToken = lexer.getNextToken()
        if(firstToken.type != TokenType.KEYWORD)
        {
            println("Expected [KEYWORD], but here is ${firstToken.type}")
            return
        }
        else
        {
            when(firstToken.value)
            {
                // TODO EXTRA ARGS HANDLE
                "print" ->  printHandler(lexer.getNextToken())
                "set"   ->  setHandler(lexer.getNextToken(), lexer.getNextToken())
                "call"  ->  callHandler(lexer.getNextToken())
                "sub"   ->  {
                    returnFromCurrFunction()
                    return
                }
                else    ->  {
                    println("Here is keyword without handler!")
                    return
                }
            }
        }

        if(lexer.getTokensCount() > 0)
        {
            println("Extra tokens ignored: ")
            var token = lexer.getNextToken()
            while(token.type != TokenType.EOL)
            {
                println("${token.type} - ${token.value}")
                token = lexer.getNextToken()
            }
        }
    }
}