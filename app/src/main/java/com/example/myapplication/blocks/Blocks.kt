package com.example.myapplication.blocks

interface Blocks {
    fun createBlock() {

    }
}

class VariableBlock(): Blocks {
    private var variables = mutableSetOf<String>()
    fun create() {

    }
    fun set(variable: String) {
        variables.add(variable)
    }
    fun get(): MutableSet<String> {
        return variables
    }
}

class AssignBlock(): Blocks {
    private var ValuesOfBariables = mutableMapOf<String, Int>()
    fun get(variables: MutableSet<String>, values: MutableSet<Int>) {

    }
}

class ArithmeticBlock(): Blocks {

}