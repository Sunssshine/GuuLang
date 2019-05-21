package tools

import java.util.*

class Environment {

    // TODO PARSE ALL FUNCTIONS ON START

    private var variables = hashMapOf<String, Int>()   // var name -> var value
    private var functions = hashMapOf<String, Int>()   // fun name -> fun str
    private var callStack = Stack<Int>()

    private var errorLog = ""
    private var executionLog = ""

    private var instructionPointer = 0

    private val instructions : List<String>

    private var isFinished = false

    private val ignoreErrors: Boolean

    constructor(sourceCode: String, isDebug : Boolean = true, ignoreErrors : Boolean = true) {
        this.instructions = sourceCode.split('\n').toList()

        this.ignoreErrors = ignoreErrors

        try {
            parseFuncMap()
            callMain()
            skipEmptyLines()
        }
        catch(e : Exception)
        {
            println(e.message + " (line #${instructionPointer+1})")
            addLineToErrLog(e.message + " (line #${instructionPointer+1})")
            isFinished = true
            return
        }

        if(!isDebug)
            executeAll()
    }

    fun executeStepInto()
    {
        try{
            executeNext()
        }
        catch (e : Exception)
        {
            println(e.message + " (line #${instructionPointer+1})")
            addLineToErrLog(e.message + " (line #${instructionPointer+1})")
            isFinished = true
            return
        }
    }

    fun addLineToExecutionLog(line : String)
    {
        executionLog += line + '\n'
    }

    fun getExecLog() : String
    {
        return executionLog
    }

    fun getErrLog() : String
    {
        return errorLog
    }

    private fun addLineToErrLog(line : String)
    {
        errorLog += line + '\n'
    }

    private fun tryIncInstructionPointer()
    {
        instructionPointer++
        if(instructionPointer >= instructions.size)
            throw Exception("Reach end of program")
    }

    private fun executeNext()
    {
        if(isFinished)
            throw Exception("Program already finished")

        try {
            handlersMatcher(
                    Lexer(
                            instructions[instructionPointer],
                            instructionPointer
                    )
            )
        }
        catch(e : Exception)
        {
            println(e.message + " (line #${instructionPointer+1})")
            addLineToErrLog(e.message + " (line #${instructionPointer+1})")
            if(!ignoreErrors) {
                println("Program is finished")
                addLineToErrLog("Program is finished")
                isFinished = true
                return
            }
        }

        tryIncInstructionPointer()
        skipEmptyLines()

    }

    private fun skipEmptyLines()
    {
        while(instructions[instructionPointer].isEmpty())
        {
            tryIncInstructionPointer()
        }
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
                println(e.message)
                addLineToErrLog(e.message + " (line #${instructionPointer+1})")
                isFinished = true
                return
            }
        }
        while(callStackCurrSize != callStack.size)
    }

    fun getVariables() : HashMap<String, Int>
    {
        addLineToExecutionLog("Variables list:")
        variables.forEach{
            addLineToExecutionLog("name: ${it.key} -> value: ${it.value}")
        }
        return variables
    }

    fun executeAll()
    {

        while(instructionPointer < instructions.size)
        {
            if(isFinished)
                return
            
            try{
                executeNext()
            }
            catch(e : Exception)
            {
                println(e.message + " (line #${instructionPointer+1})")
                addLineToErrLog(e.message + " (line #${instructionPointer+1})")
                isFinished = true
                return
            }
        }
    }

    fun getCurrentInstruction() : String
    {
        return if(isFinished)
            "Program is finished"
        else
            instructions[instructionPointer].trim() + " (line #${instructionPointer+1})"
    }

    private fun parseFuncMap()
    {
        for(strCounter in 0 until instructions.size) {
            if(instructions[strCounter].isNotEmpty())
            {
                val lexer = Lexer(instructions[strCounter], strCounter)
                if(lexer.getNextToken().value == "sub")
                {
                    subHandler(lexer.getNextToken(), lexer.getStringNum(), this)
                }
            }
        }
    }

    fun getCallTrace() : Stack<Int>
    {
        for(i in 0 until callStack.size)
        {
            if(callStack[i] == -1)
            {
                addLineToExecutionLog("From the Void -> main")
            }
            else {
                addLineToExecutionLog("-> ${instructions[callStack[i]].trim()} (line #${callStack[i]})")
            }
        }
        return callStack
    }

    private fun callMain()
    {
        try {
            val mainStrNum = resolveFunction("main") ?: -1

            instructionPointer = mainStrNum+1
            callStack.push(-1)
        }
        catch(e : Exception)
        {
            throw Exception("Can't find entry point for program. Fatal error.")
        }
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

    private fun returnFromCurrFunction()
    {
        instructionPointer = callStack.pop()
    }

    fun setVariable(identifier : String, value : Int)
    {
        variables[identifier] = value
    }

    fun resolveVariable(identifier : String) : Int?
    {
        if(variables.containsKey(identifier))
            return variables[identifier]
        else
            throw Exception("Can't resolve variable [$identifier]")
    }

    fun resolveFunction(identifier : String) : Int?
    {
        if(functions.containsKey(identifier))
            return functions[identifier]
        else
            throw Exception("Can't resolve function call [$identifier]")
    }

    fun addFunction(identifier : String, stringNumber : Int)
    {
        if(functions.containsKey(identifier))
            throw Exception("Can't add function [$identifier] - it's already exist")
        else
            functions[identifier] = stringNumber
    }


    private fun handlersMatcher(lexer: Lexer)
    {
        val firstToken = lexer.getNextToken()
        if(firstToken.type != TokenType.KEYWORD)
        {
            throw Exception("Expected [KEYWORD], but here is ${firstToken.type}")
        }
        else
        {
            when(firstToken.value)
            {
                // TODO EXTRA ARGS HANDLE
                "print" ->  printHandler(lexer.getNextToken(), this)
                "set"   ->  setHandler(lexer.getNextToken(), lexer.getNextToken(), this)
                "call"  ->  callHandler(lexer.getNextToken(), this)
                "sub"   ->  {
                    returnFromCurrFunction()
                    return
                }
                else    ->  {
                    Exception("Here is [KEYWORD] : [${firstToken.value}] without handler!")
                    return
                }
            }
        }

        if(lexer.getTokensCount() > 0)
        {
            var errorStr = "Extra tokens ignored: "
            var token = lexer.getNextToken()
            while(token.type != TokenType.EOL)
            {
                errorStr +="\n${token.type} - ${token.value}"
                token = lexer.getNextToken()
            }

            throw Exception(errorStr)
        }
    }
}