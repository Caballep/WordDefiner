package com.example.nikejosecaballero.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.inputmethod.CompletionInfo
import android.widget.TextView
import androidx.lifecycle.LifecycleObserver

class DefinitionTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), LifecycleObserver {

    private val className = this.javaClass.simpleName
    private val colorSpan = ForegroundColorSpan(Color.MAGENTA)

    // TODO: Find the right callback so it get decoupled and fix bug where only last pair gets painted and optimize code
    fun paintText() {
        val spannableString = SpannableString(text)
        var pos = 0
        var openBracketPos = 0
        var closeBracketPos = 0
        var openBracketFound = false
        var closeBracketFound = false
        Log.i(className, text.toString())
        Log.i(className, text.toString().length.toString())
        for(character in text.toString()) {
            if(character == '[') {
                openBracketPos = pos
                openBracketFound = true
            }
            if(character == ']') {
                closeBracketPos = pos
                closeBracketFound = true
            }
            if(openBracketFound && closeBracketFound) {
                Log.i(className, "DefinitionTextView, decorating text between $openBracketPos and $closeBracketPos")
                spannableString.setSpan(colorSpan, openBracketPos, closeBracketPos+1, Spanned.SPAN_MARK_MARK)
                openBracketFound = false
                closeBracketFound = false
            }
            pos++
        }
        text = spannableString
    }
}