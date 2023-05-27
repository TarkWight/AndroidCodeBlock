package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.blocks.*
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

@SuppressLint("ResourceType")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_animation
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_animation
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_animation
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_animation
        )
    }

    private var clicked = false


    @SuppressLint("WrongViewCast", "ClickableViewAccessibility", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.main.setOnDragListener(dragListener)
        val listEdit: MutableList<EditText> = mutableListOf()
        val listView: MutableList<View> = mutableListOf()

        val barButton = findViewById<FloatingActionButton>(R.id.bar_button)
        val printButton = findViewById<FloatingActionButton>(R.id.print_button)
        val varButton = findViewById<FloatingActionButton>(R.id.var_button)
        val ifButton = findViewById<FloatingActionButton>(R.id.if_button)
        val ifElseButton = findViewById<FloatingActionButton>(R.id.if_else_button)
        val assignButton = findViewById<FloatingActionButton>(R.id.assign_button)
        val whileButton = findViewById<FloatingActionButton>(R.id.while_button)


        //bind actions to buttons
        barButton.setOnClickListener {
            onAddButtonClicked()
        }

        printButton.setOnClickListener {
            onPrintClick(listEdit)
        }

        whileButton.setOnClickListener {
            onWhileClick(listEdit)
        }

        ifButton.setOnClickListener {
            onIfClick(listEdit)
        }

        ifElseButton.setOnClickListener {
            onIfElseClick(listEdit)
        }

        assignButton.setOnClickListener {
            onAssignClick(listEdit)
        }

        varButton.setOnClickListener {
            onVarClick(listEdit)

        }



        binding.runButton.setOnClickListener {
            val layout: LinearLayout = binding.main
            val count = layout.childCount
            var v: View?
            listView.clear() //clear list before filling it to prevent interpreting previous config
            for (i in 0 until count) {
                v = layout.getChildAt(i)
                listView.add(v)
            }
            // switch to console activity and start it
            val consoleIntent = Intent(this, RuntimeConsole::class.java)
            startActivity(consoleIntent)
            val bgInterpreter = Interpreter(listView)
            bgInterpreter.execute()
        }
    }

    private fun onInputClick(listEdit: MutableList<EditText>) {
        val view5 = BlockScan(this)
        view5.id = 5
        binding.main.addView(view5)
        val edit: EditText = view5.findViewById(R.id.editText5)
        listEdit.add(edit)
        view5.setOnClickListener {
            for (i in listEdit) {
                i.isEnabled = true
            }
        }
        view5.setOnLongClickListener { view ->
            for (i in listEdit) {
                i.isEnabled = false
            }
            val clipText = ""
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)
            val dragShadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, dragShadowBuilder, view, 0)
            view.visibility = View.INVISIBLE
            true
        }
    }

    private fun onVarClick(listEdit: MutableList<EditText>) {
        val view1 = BlockInitialization(this)
        view1.id = 1
        binding.main.addView(view1)
        val edit: EditText = view1.findViewById(R.id.editText2)
        listEdit.add(edit)
        view1.setOnClickListener {
            for (i in listEdit) {
                i.isEnabled = true
            }
        }
        view1.setOnLongClickListener { view ->
            for (i in listEdit) {
                i.isEnabled = false
            }
            val clipText = ""
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)
            val dragShadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, dragShadowBuilder, view, 0)
            view.visibility = View.INVISIBLE
            true
        }
    }

    private fun onAssignClick(listEdit: MutableList<EditText>) {
        val view2 = BlockAssigns(this)
        view2.id = 2
        binding.main.addView(view2)
        val edit: EditText = view2.findViewById(R.id.editText2)
        val edit2: EditText = view2.findViewById(R.id.editText1)
        listEdit.add(edit)
        listEdit.add(edit2)
        view2.setOnClickListener {
            for (i in listEdit) {
                i.isEnabled = true
            }
        }
        view2.setOnLongClickListener { view ->
            for (i in listEdit) {
                i.isEnabled = false
            }
            val clipText = ""
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)
            val dragShadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, dragShadowBuilder, view, 0)
            view.visibility = View.INVISIBLE
            true
        }
    }

    private fun onIfElseClick(listEdit: MutableList<EditText>) {
        val view6 = BlockIfElse(this)
        val inflatedView = LayoutInflater.from(this).inflate(R.layout.block_else, view6, false)
        //val inflatedView = View.inflate(this, R.id.block_else, null)
        inflatedView.id = 20
        view6.addView(inflatedView)
        val params =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (view6 as ViewGroup).childCount * 195
            ).apply {
                gravity = Gravity.CENTER
            }
        view6.orientation = LinearLayout.VERTICAL
        view6.layoutParams = params
        binding.main.addView(view6)
        Log.d("count", (view6 as ViewGroup).childCount.toString())
        view6.id = 2131230816
        view6.block.id = 10
        view6.setOnDragListener(dragListener)
        view6.popupMenu3.inflate(R.menu.popup_menu_comparisons)
        view6.popupMenu3.setOnMenuItemClickListener { menu ->
            view6.btn2.text = menu.title
            false
        }
        val edit1: EditText = view6.findViewById(R.id.editText1)
        val edit2: EditText = view6.findViewById(R.id.editText2)
        listEdit.add(edit1)
        listEdit.add(edit2)
        view6.setOnClickListener {
            for (i in listEdit) {
                i.isEnabled = true
            }
        }
        view6.setOnLongClickListener { view ->
            for (i in listEdit) {
                i.isEnabled = false
            }
            val clipText = ""
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)
            val dragShadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, dragShadowBuilder, view, 0)
            view.visibility = View.INVISIBLE
            true
        }
        view6.btn2.setOnClickListener {
            view6.popupMenu3.show()
        }
    }

    private fun onIfClick(listEdit: MutableList<EditText>) {
        val view3 = BlockIf(this)
        binding.main.addView(view3)
        view3.id = 2131230815
        view3.block.id = 10
        view3.setOnDragListener(dragListener)
        view3.popupMenu3.inflate(R.menu.popup_menu_comparisons)
        view3.popupMenu3.setOnMenuItemClickListener { menu ->
            view3.btn2.text = menu.title
            false
        }
        val edit1: EditText = view3.findViewById(R.id.editText1)
        val edit2: EditText = view3.findViewById(R.id.editText2)
        listEdit.add(edit1)
        listEdit.add(edit2)
        view3.setOnClickListener {
            for (i in listEdit) {
                i.isEnabled = true
            }
        }
        view3.setOnLongClickListener { view ->
            for (i in listEdit) {
                i.isEnabled = false
            }
            val clipText = ""
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)
            val dragShadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, dragShadowBuilder, view, 0)
            view.visibility = View.INVISIBLE
            true
        }
        view3.btn2.setOnClickListener {
            view3.popupMenu3.show()
        }
    }

    private fun onWhileClick(listEdit: MutableList<EditText>) {
        val view7 = BlockWhile(this)
        binding.main.addView(view7)
        view7.id = 2131230817
        view7.block3.id = 20
        view7.setOnDragListener(dragListener)
        view7.popupMenu3.inflate(R.menu.popup_menu_comparisons)
        view7.popupMenu3.setOnMenuItemClickListener { menu ->
            view7.btn2.text = menu.title
            false
        }
        val edit1: EditText = view7.findViewById(R.id.editText6)
        val edit2: EditText = view7.findViewById(R.id.editText7)
        listEdit.add(edit1)
        listEdit.add(edit2)
        view7.setOnClickListener {
            for (i in listEdit) {
                i.isEnabled = true
            }
        }
        view7.setOnLongClickListener { view ->
            for (i in listEdit) {
                i.isEnabled = false
            }
            val clipText = ""
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)
            val dragShadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, dragShadowBuilder, view, 0)
            view.visibility = View.INVISIBLE
            true
        }
        view7.btn2.setOnClickListener {
            view7.popupMenu3.show()
        }
    }

    private fun onPrintClick(listEdit: MutableList<EditText>) {
        val view4 = block_print(this)
        view4.id = 4
        binding.main.addView(view4)
        val edit: EditText = view4.findViewById(R.id.editText4)
        listEdit.add(edit)
        view4.setOnClickListener {
            for (i in listEdit) {
                i.isEnabled = true
            }
        }
        view4.setOnLongClickListener { view ->
            for (i in listEdit) {
                i.isEnabled = false
            }
            val clipText = ""
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)
            val dragShadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, dragShadowBuilder, view, 0)
            view.visibility = View.INVISIBLE
            true
        }
    }

    private val dragListener =
        View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> true
                DragEvent.ACTION_DRAG_EXITED -> {
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    view.invalidate()
                    Log.d("position Y: ", event.y.toString())
                    val v = event.localState as View
                    val owner = v.parent as ViewGroup
                    val count = owner.childCount
                    owner.removeView(v)
                    if (count <= 1) {
                        binding.main.removeView(owner)
                    } else {
                        if (owner.id == 2131230815 || owner.id == 2131230816 || owner.id == 2131230817) {
                            val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                (count - 1) * 195
                            ).apply {
                                gravity = Gravity.CENTER
                            }
                            owner.layoutParams = params
                        }
                    }
                    Toast.makeText(this, count.toString(), Toast.LENGTH_SHORT).show()
                    val destination = view as LinearLayout
                    val count2 = destination.childCount
                    Log.d("Id: ", destination.id.toString())
                    Log.d("View Id: ", v.id.toString())
                    if (destination.id == 2131230815 || destination.id == 2131230816 || destination.id == 2131230817) {
                        if (v.id == 2131230815 || v.id == 2131230816 || v.id == 2131230817) {
                            Log.d("count2 ", count2.toString())
                            val params =
                                LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    count2 * 195 + v.height
                                ).apply {
                                    gravity = Gravity.CENTER
                                }
                            destination.orientation = LinearLayout.VERTICAL
                            destination.layoutParams = params
                        } else {
                            val params =
                                LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    count2 * 195 + v.height
                                ).apply {
                                    gravity = Gravity.CENTER
                                }
                            destination.orientation = LinearLayout.VERTICAL
                            destination.layoutParams = params
                        }
                    }
                    if (destination.id == 2131230815 || destination.id == 2131230816 || destination.id == 2131230817) {
                        destination.background =
                            ContextCompat.getDrawable(this, R.drawable.layout_bg_purple)
                    }
                    Log.d("Count: ", count2.toString())
                    for (index in 0 until count2) {
                        Log.d("Iteration: ", index.toString())
                        val nextChild = destination.getChildAt(index)
                        if ((nextChild.top + 97.5 < event.y) && (nextChild.top + 195 > event.y)) {
                            Log.d("Index: ", index.toString())
                            destination.addView(v, index + 1)
                            break
                        } else {
                            if ((nextChild.top + 97.5 > event.y) && (nextChild.top < event.y)) {
                                Log.d("Index: ", index.toString())
                                if ((destination.id == 2131230815 || destination.id == 2131230816 || destination.id == 2131230817) && index == 0) {
                                    destination.addView(v, index + 1)
                                } else {
                                    destination.addView(v, index)
                                }
                                break
                            }
                        }
                        if (index == count2 - 1) {
                            destination.addView(v, index + 1)
                        }
                        Log.d("position of child's: ", nextChild.top.toString())
                    }
                    v.visibility = View.VISIBLE
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.invalidate()
                    if (event.result.toString() == "false") {
                        val v = event.localState as View
                        binding.main.removeView(v)
                    }
                    true
                }
                else -> false
            }


        }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        val barButton = findViewById<FloatingActionButton>(R.id.bar_button)
        val printButton = findViewById<FloatingActionButton>(R.id.print_button)
        val varButton = findViewById<FloatingActionButton>(R.id.var_button)
        val ifButton = findViewById<FloatingActionButton>(R.id.if_button)
        val ifElseButton = findViewById<FloatingActionButton>(R.id.if_else_button)
        val assignButton = findViewById<FloatingActionButton>(R.id.assign_button)
        val whileButton = findViewById<FloatingActionButton>(R.id.while_button)

        if (!clicked) {
            printButton.visibility = View.VISIBLE
            varButton.visibility = View.VISIBLE
            ifButton.visibility = View.VISIBLE
            ifElseButton.visibility = View.VISIBLE
            assignButton.visibility = View.VISIBLE
            whileButton.visibility = View.VISIBLE

        } else {
            printButton.visibility = View.INVISIBLE
            varButton.visibility = View.INVISIBLE
            ifButton.visibility = View.INVISIBLE
            ifElseButton.visibility = View.INVISIBLE
            assignButton.visibility = View.INVISIBLE
            whileButton.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clocked: Boolean) {
        val barButton = findViewById<FloatingActionButton>(R.id.bar_button)
        val printButton = findViewById<FloatingActionButton>(R.id.print_button)
        val varButton = findViewById<FloatingActionButton>(R.id.var_button)
        val ifButton = findViewById<FloatingActionButton>(R.id.if_button)
        val ifElseButton = findViewById<FloatingActionButton>(R.id.if_else_button)
        val assignButton = findViewById<FloatingActionButton>(R.id.assign_button)
        val whileButton = findViewById<FloatingActionButton>(R.id.while_button)
        if (!clicked) {
            printButton.startAnimation(fromBottom)
            varButton.startAnimation(fromBottom)
            ifButton.startAnimation(fromBottom)
            ifElseButton.startAnimation(fromBottom)
            assignButton.startAnimation(fromBottom)
            whileButton.startAnimation(fromBottom)
            barButton.startAnimation(rotateOpen)
        } else {
            printButton.startAnimation(toBottom)
            varButton.startAnimation(toBottom)
            ifButton.startAnimation(toBottom)
            ifElseButton.startAnimation(toBottom)
            assignButton.startAnimation(toBottom)
            whileButton.startAnimation(toBottom)
            barButton.startAnimation(rotateClose)
        }
    }
}