package com.example.nikejosecaballero.ui.activities.mainActivity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikejosecaballero.MainApplication
import com.example.nikejosecaballero.R
import com.example.nikejosecaballero.di.modules.ViewModelFactory
import com.example.nikejosecaballero.ui.adapters.DefinitionsAdapter
import com.example.nikejosecaballero.utils.DefinitionsSortType
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val  mainActivityVM: MainActivityVM by lazy {
        ViewModelProvider(this, viewModelFactory)[MainActivityVM::class.java]
    }

    private val className = this.javaClass.simpleName
    var definitionsRecyclerView: RecyclerView? = null
    var actionBarEditText: EditText? = null
    var sortSpinner: Spinner? = null
    var currentStatusTextView: TextView? = null

    private var lastSearchResultsFound = false
    private var isASortingAttempt = false

    private val statusLoadingDataMessage = "Loading..."
    private val statusNothingFoundMessage = "Nothing found"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(className, "onCreate")
        setContentView(R.layout.activity_main)
        (application as MainApplication).component.inject(this)
        setupActionBar()
        setupRecyclerView()
        setupSortSpinner()
        setupStatusTextView()
        setDefinitionsObserver()
    }

    override fun onStart() {
        super.onStart()
        setupActionBar()
    }

    private fun setDefinitionsObserver() {
        mainActivityVM.definitions.observe(this, Observer {
            definitionsRecyclerView?.adapter = DefinitionsAdapter(it)
            definitionsRecyclerView?.adapter?.notifyDataSetChanged()
            if(it.isNotEmpty()) {
                lastSearchResultsFound = true
                currentStatusTextView?.visibility = View.INVISIBLE
            }else {
                lastSearchResultsFound = false
                currentStatusTextView?.text = statusNothingFoundMessage
                currentStatusTextView?.visibility = View.VISIBLE
            }
        })
    }

    private fun searchWordDefinitions(word: String) {
        Log.i(className, "searchWordDefinitions, word: $word, isASortingAttempt: $isASortingAttempt")
        currentStatusTextView?.text = statusLoadingDataMessage
        currentStatusTextView?.visibility = View.VISIBLE
        mainActivityVM.requestDefinitions(word)
    }

    private fun setupSortSpinner() {
        sortSpinner = findViewById(R.id.sortSpinner)
        sortSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i(className, "sortSpinner onItemSelected")
                if (lastSearchResultsFound) {
                    val selectedItem = sortSpinner?.getItemAtPosition(position).toString()
                    isASortingAttempt = true
                    mainActivityVM.sortDefinitions(DefinitionsSortType.fromString(selectedItem))
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
        actionBarEditText = findViewById(R.id.actionBarEditText)
        actionBarEditText?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                performSearch()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun setupStatusTextView() {
        currentStatusTextView = findViewById(R.id.currentStatusTextView)
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
        actionBarEditText?.let {
            if (it.text.isNotEmpty()) {
                searchWordDefinitions(it.text.toString())
                it.hint = actionBarEditText?.text
                it.setText("")
                definitionsRecyclerView?.requestFocus()
                this.hideKeyboard()
            }
        }
    }

    private fun resetSortSpinner() {
        // TODO: This should happen when a search is performed and there are results
        sortSpinner?.setSelection(0)
    }
}