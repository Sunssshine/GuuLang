package com.example.demo.view

import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tools.Environment
import tornadofx.*

class MainView : View("Gu_u Lang") {

    var textArea: TextArea by singleAssign()
    var errorsTextArea: TextArea by singleAssign()
    var outputTextArea: TextArea by singleAssign()
    var currStr: Label by singleAssign()


    val isDebugMode = SimpleBooleanProperty()
    val isIgnoreErrors = SimpleBooleanProperty()



    fun updateOutputViews()
    {
        errorsTextArea.text = environment.getErrLog()
        outputTextArea.text = environment.getExecLog()
        currStr.text = environment.getCurrentInstruction()
    }

    lateinit var environment : Environment

    override val root = hbox{
        vbox {
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

                        updateOutputViews()

                    }
                }
                right = hbox{
                    checkbox("Debug", isDebugMode) {isSelected = true}

                    checkbox("Ignore errors", isIgnoreErrors) {isSelected = true}

                    button("Stack trace") {
                        style{
                            textFill = Color.WHITE
                            backgroundColor += Color.PURPLE
                        }

                        action {
                            if(::environment.isInitialized) {
                                environment.getCallTrace()
                                updateOutputViews()
                            }
                            else
                            {
                                alert(Alert.AlertType.WARNING, "You should initialize environment before debug it","(Press Execute after coding is done)")
                            }
                        }
                    }
                    button("Variables") {
                        style{
                            textFill = Color.WHITE
                            backgroundColor += Color.PURPLE
                        }

                        action {

                            if(::environment.isInitialized) {
                                environment.getVariables()
                                updateOutputViews()
                            }
                            else
                            {
                                alert(Alert.AlertType.WARNING, "You should initialize environment before debug it","(Press Execute after coding is done)")
                            }
                        }
                    }
                    button("Step into") {
                        style{
                            textFill = Color.WHITE
                            backgroundColor += Color.PURPLE
                        }

                        action {

                            if(::environment.isInitialized) {
                                environment.executeStepInto()
                                updateOutputViews()
                            }
                            else
                            {
                                alert(Alert.AlertType.WARNING, "You should initialize environment before debug it","(Press Execute after coding is done)")
                            }
                        }
                    }
                    button("Step over") {
                        style{
                            textFill = Color.WHITE
                            backgroundColor += Color.PURPLE
                        }

                        action {
                            if(::environment.isInitialized) {
                                environment.executeStepOver()
                                updateOutputViews()
                            }
                            else
                            {
                                alert(Alert.AlertType.WARNING, "You should initialize environment before debug it","(Press Execute after coding is done)")
                            }
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
                text = "Here will be next instruction"
                style{
                    backgroundColor+=Color.RED
                    padding = box(10.px)
                    fontSize = 20.px
                    fontWeight = FontWeight.BOLD
                }
                vboxConstraints {
                    marginLeft = 20.0
                    marginBottom = 20.0
                }
            }
        }
        vbox {
            label{
                text = "Output"
            }
            outputTextArea = textarea{
                vgrow = Priority.ALWAYS
                isEditable = false
                vboxConstraints {
                    marginTop = 5.0
                    marginRight = 20.0
                    marginBottom = 5.0
                }
            }

            label{
                text = "Errors"
            }
            errorsTextArea = textarea{
                vgrow = Priority.ALWAYS
                isEditable = false
                vboxConstraints {
                    marginRight = 20.0
                    marginTop = 5.0
                    marginBottom = 20.0
                }
            }
        }
    }


}