package com.example.myapplication.blocks

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.myapplication.R

open class block_print @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val view = LayoutInflater.from(context).inflate(R.layout.block_print, this)
}