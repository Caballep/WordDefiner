package com.example.nikejosecaballero.ui.views

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class ShimmerTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr), LifecycleObserver {

    private val className = this.javaClass.simpleName

    private val textLength = text.length

    private var spannableString = SpannableString(text)
    private val colorSpan = ForegroundColorSpan(Color.DKGRAY)
    private val colorSpanShadow = ForegroundColorSpan(Color.GRAY)
    private var charLightedPos = 0

    private lateinit var animationHandler: Handler
    private val animationRunnable = getAnimationRunnable()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun startAnimationHandler() {
        animationHandler = Handler()
        animateText()
        Log.i(className, "Animation started")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun stopAnimationHandler() {
        animationHandler.removeCallbacks(animationRunnable)
        Log.i(className, "Animation stopped")
    }

    private fun animateText() {
        animationHandler.postDelayed(animationRunnable,120)
    }

    private fun getAnimationRunnable(): Runnable {
        return Runnable {
            spannableString = SpannableString(text)
            if (charLightedPos+1 <= textLength) {
                spannableString.setSpan(colorSpan, charLightedPos, charLightedPos+1, Spanned.SPAN_INTERMEDIATE)
            }
            if (charLightedPos-1 >= 0) {
                spannableString.setSpan(colorSpanShadow, charLightedPos-1, charLightedPos, Spanned.SPAN_INTERMEDIATE)
            }
            this.text = spannableString
            charLightedPos++
            if (charLightedPos == textLength+1) {
                charLightedPos = 0
            }
            animateText()
        }
    }

}