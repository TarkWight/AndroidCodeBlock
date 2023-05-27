package com.example.myapplication.blocks

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import com.example.myapplication.R

open class BlockIf @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val view = LayoutInflater.from(context).inflate(R.layout.block_if, this, true)
    val block: View = view.findViewById(R.id.block_if_parent)
    val btn2: TextView = view.findViewById(R.id.select_comp)
    val popupMenu3: PopupMenu = PopupMenu(context, btn2)
}