package com.example.nikejosecaballero.ui.mainActivity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikejosecaballero.MainApplication
import com.example.nikejosecaballero.R
import com.example.nikejosecaballero.di.modules.ViewModelFactory
import com.example.nikejosecaballero.ui.adapters.DefinitionsAdapter
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val  mainActivityVM: MainActivityVM by lazy {
        ViewModelProviders.of(this, viewModelFactory)[MainActivityVM::class.java]
    }

    private val className = this.javaClass.simpleName

    private var definitionsRecyclerView: RecyclerView? = null
    private var actionBarEditText: EditText? = null
    private var actionBarSearchButton: ImageButton? = null
    private var sortSpinner: Spinner? = null
    private var currentStatusTextView: TextView? = null

    var lastSearchResultsFound = false
    var isASpinnerReset = false

    private val loadingDataMessage = "Loading..."
    private val nothingFoundMessage = "Nothing found"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(className, "onCreate")
        setContentView(R.layout.activity_main)
        (application as MainApplication).component.inject(this)
        setupActionBar()
        setupRecyclerView()
        setupSortSpinner()
        setupLoadingTextView()
    }

    override fun onStart() {
        super.onStart()
        setupActionBar()
    }

    private fun searchWordDefinitions(word: String) {
        currentStatusTextView?.text = loadingDataMessage
        currentStatusTextView?.visibility = View.VISIBLE
        mainActivityVM.getDefinitions(word).observe(this, Observer {
            definitionsRecyclerView!!.adapter = DefinitionsAdapter(it)
            if(it.isNotEmpty()) {
                lastSearchResultsFound = true
                currentStatusTextView?.visibility = View.INVISIBLE
            }else {
                lastSearchResultsFound = false
                currentStatusTextView?.text = nothingFoundMessage
            }
            resetSortSpinner()
        })
    }

    private fun setupSortSpinner() {
        sortSpinner = findViewById(R.id.sortSpinner)
        sortSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (lastSearchResultsFound && !isASpinnerReset) {
                    val selectedItem = sortSpinner?.getItemAtPosition(position).toString()
                    mainActivityVM.sortDefinitions(selectedItem, this@MainActivity)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        definitionsRecyclerView = findViewById(R.id.definitionsRecyclerView)
        definitionsRecyclerView!!.layoutManager = LinearLayoutManager(this)
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        supportActionBar?.setCustomView(R.layout.action_bar_search)
        actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        actionBarSearchButton = findViewById(R.id.actionBarSearchButton)
        actionBarEditText = findViewById(R.id.actionBarEditText)
        actionBarSearchButton?.setOnClickListener { performSearch() }
        actionBarEditText?.setOnEditorActionListener { v, actionId, event ->
            performSearch()
            true
        }
        actionBarEditText?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                performSearch()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun setupLoadingTextView() {
        currentStatusTextView = findViewById(R.id.currentStatusTextView)
        currentStatusTextView?.visibility = View.INVISIBLE
    }

    private fun hideKeyboard() {
        if(currentFocus != null) {
            val inputManager: InputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun performSearch() {
        if (actionBarEditText?.text.toString() != "") {
            searchWordDefinitions(actionBarEditText?.text.toString())
            actionBarEditText?.hint = actionBarEditText?.text
            actionBarEditText?.setText("")
            definitionsRecyclerView?.requestFocus()
            this.hideKeyboard()
        }
    }

    private fun resetSortSpinner() {
        isASpinnerReset = true
        sortSpinner?.setSelection(0)
    }
}