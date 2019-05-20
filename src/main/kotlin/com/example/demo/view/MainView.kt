package com.example.demo.view

import com.example.demo.app.Styles
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import tools.ExpressionParser
import tools.Lexer
import tornadofx.*

class MainView : View("Hello TornadoFX") {

    var textArea: TextArea by singleAssign()

    override val root = vbox {
        label(title) {
            addClass(Styles.heading)
        }
        button("Press Me") {
            textFill = Color.RED
            action {
                println("Button pressed!")
                println(textArea.text)
                textArea.text.split('\n').forEach {
                    if(it.isNotEmpty())
                    {
                        ExpressionParser(Lexer(it).getTokens())
                    }
                }
                println("Expression parse done")
            }
        }

        textArea = textarea("Type memo here") {
            selectAll()
        }
    }
}