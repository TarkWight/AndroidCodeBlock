package com.example.myapplication

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.jraska.console.Console
import android.os.AsyncTask
import java.lang.Exception
import java.util.*

class Interpreter(private val userCode: MutableList<View> = mutableListOf()) :
    AsyncTask<Any, Void, Void>() {
    companion object {
        const val INITIALIZATION_BLOCK = 1
        const val ASSIGN_BLOCK = 2
        const val OUTPUT_BLOCK = 4
        const val INPUT_BLOCK = 5
        const val ARRAY_BLOCK = 6
        const val IF_BLOCK = 2131230815
        const val IF_ELSE_BLOCK = 2131230816
        const val WHILE_BLOCK = 2131230817
    }

    val var_name_regex = "[a-zA-Z]+[a-zA-Z_]*"

    private var variables = mutableSetOf<String>()
    private var valuesOfVariables = mutableMapOf<String, Double>()
    private var listArray = mutableMapOf<String, Array<Double>>()
    private var checkout: Boolean = true

    fun start_program(listView: MutableList<View> = mutableListOf()) {
        listView.forEach { view ->
            implement(view)
        }
    }

    //method for ASYNCHRONOUS code execution to provide real-time console interaction
    override fun doInBackground(vararg params: Any?): Void? {
        start_program(userCode)
        //debug()
        return null
    }

    @SuppressLint("CutPasteId")
    private fun implement(view: View) {
        when (view.id) {
            INITIALIZATION_BLOCK -> {
                val edit: EditText = view.findViewById(R.id.editText2)
                val string: String = edit.text.toString().filter { !it.isWhitespace() }
//                Console.writeLine("String $string")
                val list_variables = Regex(var_name_regex).findAll(string)
                list_variables.forEach { variable ->
                    if (listArray[variable.value] == null) {
//                        Console.writeLine("variable: ${variable.value}")
                        valuesOfVariables[variable.value] = 0.0
                    }
                }
            }

            ASSIGN_BLOCK -> {
                val edit: EditText = view.findViewById(R.id.editText1)
                val edit2: EditText = view.findViewById(R.id.editText2)
                val variable = edit.text.toString().filter { !it.isWhitespace() }
                val value = edit2.text.toString().filter { !it.isWhitespace() }

                /*val matchResultVariable = Regex("""[a-z]\[[^#]*\]""").find(variable)
                val list_variables = Regex("""[a-z]\[[^#]*\]""").findAll(value)
                list_variables.forEach { variables ->
                    val name = Regex("""[a-z]""").find(variable)!!.value
                    variable.replace(name, "")
                    value = Regex("""[^#]*""").find(variable)!!.value.filter { !it.isWhitespace() }
                    val result = calc(value)
                    listArray[variable]?.set(result!!.toInt(), calc(value)!!)
                }*/

                var result: Double?
                try {
                    result = calc(value)
                } catch (e: Exception) {
                    Console.writeLine("Incorrect data or unknown variable in Assign block")
                    result = null
                }
                if (result != null) {
                    valuesOfVariables[variable] = result.toDouble()
                }
            }

            IF_BLOCK -> {
                runThroughIf(view as ViewGroup)
            }

            IF_ELSE_BLOCK -> {
                runThroughIfElse(view as ViewGroup)
            }

            WHILE_BLOCK -> {
                runThroughWhile(view as ViewGroup)
            }

            OUTPUT_BLOCK -> {
                val edit: EditText = view.findViewById(R.id.editText4)
                val string: String = edit.text.toString().filter { !it.isWhitespace() }
                val list_variables = Regex(var_name_regex).findAll(string)
                list_variables.forEach { variable ->
                    Console.writeLine("${variable.value} - ${valuesOfVariables[variable.value]}")
                }
            }

            INPUT_BLOCK -> {
                val edit: EditText = view.findViewById(R.id.editText5)
                val string: String = edit.text.toString().filter { !it.isWhitespace() }
                val list_variables = Regex(var_name_regex).findAll(string)

                list_variables.forEach { variable ->
                    Console.writeLine("${variable.value} - ${valuesOfVariables[variable.value]}")
                }
            }


            ARRAY_BLOCK -> {
                val edit: EditText = view.findViewById(R.id.editText1)//название массива
                val edit2: EditText = view.findViewById(R.id.editText2)//длина
                val variable = edit.text.toString().filter { !it.isWhitespace() }
                val value = edit2.text.toString().filter { !it.isWhitespace() }

                val matchResultVariable = Regex("""[a-z]""").find(variable)
                val matchResultValue = Regex("""([1-9]\d+|[1-9])+""").find(value)

                if(matchResultVariable != null && matchResultValue != null) {
                    if (valuesOfVariables[variable] == null) {
                        listArray[variable] = Array<Double>(value.toInt()) { 0.0 }
                    }
                } else {
                    checkout = false
                }
            }
        }
    }

    private fun runFor(view: ViewGroup, iter: Int) {
        for (index in iter until view.childCount) {//1 - обычный
            val nextChild = view.getChildAt(index)
            implement(nextChild)
        }
    }

    @SuppressLint("ResourceType")
    private fun runThroughIfElse(view: ViewGroup) {
        val blockOfCondition: View = view.getChildAt(0)
        val edit: EditText = blockOfCondition.findViewById(R.id.editText1)
        val edit2: EditText = blockOfCondition.findViewById(R.id.editText2)
        val condition: TextView = blockOfCondition.findViewById(R.id.select_comp)
        edit.text.toString().filter { !it.isWhitespace() }
        edit2.text.toString().filter { !it.isWhitespace() }
        when (condition.text) {
            "==" -> {
                if (calc(edit.text.toString()) == calc(edit2.text.toString())) {
                    runFor(view, 1)
                } else {
                    val blockElse: View = view.findViewById(20)
                    val it = view.indexOfChild(blockElse) + 1
                    runFor(view, it)
                }
            }
            "!=" -> {
                if (calc(edit.text.toString()) != calc(edit2.text.toString())) {
                    runFor(view, 1)
                } else {
                    val blockElse: View = view.findViewById(20)
                    val it = view.indexOfChild(blockElse) + 1
                    runFor(view, it)
                }
            }
            ">" -> {
                if (calc(edit.text.toString())!! > calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                } else {
                    val blockElse: View = view.findViewById(20)
                    val it = view.indexOfChild(blockElse) + 1
                    runFor(view, it)
                }
            }
            "<" -> {
                if (calc(edit.text.toString())!! < calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                } else {
                    val blockElse: View = view.findViewById(20)
                    val it = view.indexOfChild(blockElse) + 1
                    runFor(view, it)
                }
            }
            ">=" -> {
                if (calc(edit.text.toString())!! >= calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                } else {
                    val blockElse: View = view.findViewById(20)
                    val it = view.indexOfChild(blockElse) + 1
                    runFor(view, it)
                }
            }
            "<=" -> {
                if (calc(edit.text.toString())!! <= calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                } else {
                    val blockElse: View = view.findViewById(20)
                    val it = view.indexOfChild(blockElse) + 1
                    runFor(view, it)
                }
            }
        }
    }

    private fun runThroughWhile(view: ViewGroup) {
        val blockOfCondition: View = view.getChildAt(0)
        val edit: EditText = blockOfCondition.findViewById(R.id.editText6)
        val edit2: EditText = blockOfCondition.findViewById(R.id.editText7)
        val condition: TextView = blockOfCondition.findViewById(R.id.select_comp)
        edit.text.toString().filter { !it.isWhitespace() }
        edit2.text.toString().filter { !it.isWhitespace() }
        when (condition.text) {
            "==" -> {
                while (calc(edit.text.toString()) == calc(edit2.text.toString())) {
                    runFor(view, 1)
                }
            }
            "!=" -> {
                while (calc(edit.text.toString()) != calc(edit2.text.toString())) {
                    runFor(view, 1)
                }
            }
            ">" -> {
                while (calc(edit.text.toString())!! > calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                }
            }
            "<" -> {
                while (calc(edit.text.toString())!! < calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                }
            }
            ">=" -> {
                while (calc(edit.text.toString())!! >= calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                }
            }
            "<=" -> {
                while (calc(edit.text.toString())!! <= calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                }
            }
        }
    }


    private fun runThroughIf(view: ViewGroup) {
        val blockOfCondition: View = view.getChildAt(0)
        val edit: EditText = blockOfCondition.findViewById(R.id.editText1)
        val edit2: EditText = blockOfCondition.findViewById(R.id.editText2)
        val condition: TextView = blockOfCondition.findViewById(R.id.select_comp)
        edit.text.toString().filter { !it.isWhitespace() }
        edit2.text.toString().filter { !it.isWhitespace() }
        when (condition.text) {
            "==" -> {
                if (calc(edit.text.toString()) == calc(edit2.text.toString())) {
                    runFor(view, 1)
                }
            }
            "!=" -> {
                if (calc(edit.text.toString()) != calc(edit2.text.toString())) {
                    runFor(view, 1)
                }
            }
            ">" -> {
                if (calc(edit.text.toString())!! > calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                }
            }
            "<" -> {
                if (calc(edit.text.toString())!! < calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                }
            }
            ">=" -> {
                if (calc(edit.text.toString())!! >= calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                }
            }
            "<=" -> {
                if (calc(edit.text.toString())!! <= calc(edit2.text.toString())!!) {
                    runFor(view, 1)
                }
            }
        }
    }

    private fun delim(c: Char): Boolean {
        return c == ' '
    }

    private fun is_op(c: Char): Boolean {
        return (c == '+') || (c == '-') || (c == '*') || (c == '/') || (c == '%')
    }

    private fun priority(op: Char): Int {
        return if (op == '+' || op == '-') 1 else if (op == '*' || op == '/' || op == '%') 2 else -1
    }

    private fun process_op(st: ArrayDeque<Double>, op: Char) {
        val r = st.last()
        st.removeLast()
        var l: Double = 0.0
        if (!st.isEmpty()) {
            l = st.last()
            st.removeLast()
        }
        when (op) {
            '+' -> st.addLast(l + r)
            '-' -> st.addLast(l - r)
            '*' -> st.addLast(l * r)
            '/' -> st.addLast(l / r)
            '%' -> st.addLast(l % r)
        }
    }

    fun calc(s: String): Double? {
        var st = ArrayDeque<Double>()
        var op = ArrayDeque<Char>()
        var i = 0
        while (i < s.length) {
            if (!delim(s[i])) {
                if (s[i] == '(') {
                    op.addLast('(')
                } else if (s[i] == ')') {
                    while (op.last() != '(') {
                        process_op(st, op.last())
                        op.removeLast()
                    }
                    op.removeLast()
                } else if (is_op(s[i])) {
                    var curop: Char = s[i]
                    if (i > 0 && s[i - 1] == '-') {
                        curop = '+'
                        op.removeLast()
                    }
                    while (!op.isEmpty() && priority(op.last()) >= priority(curop)) {
                        process_op(st, op.last())
                        op.removeLast()
                    }
                    op.addLast(curop)
                } else {
                    var operand: String = ""
                    while (i < s.length && (s[i].isLetterOrDigit() || s[i] == '.')) {
                        operand += s[i++]
                    }
                    --i
                    if (operand[0].isDigit())
                        st.addLast(operand.toDouble())
                    else {
                        st.addLast(valuesOfVariables[operand]!!)
                    }
                }
            }
            i++
        }
        while (!op.isEmpty()) {
            process_op(st, op.last())
            op.removeLast()
        }
        return st.last()
    }
}