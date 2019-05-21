package com.example.demo.view

import com.example.demo.app.Styles
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import tools.Environment
import tools.ExpressionParser
import tools.Lexer
import tornadofx.*

class MainView : View("Hello TornadoFX") {

    var textArea: TextArea by singleAssign()
    var currStr: Label by singleAssign()


    val isDebugMode = SimpleBooleanProperty()
    val isIgnoreErrors = SimpleBooleanProperty()



    lateinit var environment : Environment

    override val root = vbox {
        minHeight = 200.0
        borderpane{

            vboxConstraints {
                marginLeft = 20.0
                marginRight = 20.0
            }
            left = button("Execute") {
                textFill = Color.BLACK

                style{
                    backgroundColor += Color.LIMEGREEN
                }

                action {
                    println("Start executing")

                    environment = Environment(
                            textArea.text,
                            isDebugMode.value,
                            isIgnoreErrors.value
                    )
                    currStr.text = environment.getCurrentInstruction()

                }
            }
            right = hbox{
                checkbox("Debug", isDebugMode) {}

                checkbox("Ignore errors", isIgnoreErrors) {}

                button("Stack trace") {
                    style{
                        textFill = Color.WHITE
                        backgroundColor += Color.PURPLE
                    }

                    action {
                        environment.getCallTrace()
                    }
                }
                button("Variables") {
                    style{
                        textFill = Color.WHITE
                        backgroundColor += Color.PURPLE
                    }

                    action {
                        environment.getVariables()
                    }
                }
                button("Step into") {
                    style{
                        textFill = Color.WHITE
                        backgroundColor += Color.PURPLE
                    }

                    action {
                        environment.executeStepInto()
                        currStr.text = environment.getCurrentInstruction()
                    }
                }
                button("Step over") {
                    style{
                        textFill = Color.WHITE
                        backgroundColor += Color.PURPLE
                    }

                    action {
                        environment.executeStepOver()
                        currStr.text = environment.getCurrentInstruction()
                    }
                }
            }

        }


        textArea = textarea("Type memo here") {
            vgrow = Priority.ALWAYS
            vboxConstraints {
                marginLeft = 20.0
                marginRight = 20.0
            }



        }

        currStr = label {
            text = "Here will be current str which debugger on"
            style{
                backgroundColor+=Color.RED
            }
            vboxConstraints {
                marginLeft = 20.0
                marginBottom = 20.0
            }
        }
    }


}