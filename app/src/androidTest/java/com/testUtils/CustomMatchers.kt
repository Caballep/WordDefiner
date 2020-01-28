package com.testUtils

import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


class CustomMatchers {

    companion object {

        private const val matcherDescriptionMessage = "Error: "

        fun withIdAndText(id: Int, text: String): Matcher<View?>? {
            val idMatcher = withId(id)
            val textMatcher = withText(text)

            return object : BoundedMatcher<View?, TextView>(TextView::class.java) {

                override fun describeTo(description: Description) {
                    description.appendText(matcherDescriptionMessage)

                    textMatcher.describeTo(description)
                    idMatcher.describeTo(description)
                }

                override fun matchesSafely(textView: TextView): Boolean {
                    return textMatcher.matches(textView.text.toString()) &&
                            idMatcher.matches(textView.id)
                }
            }
        }

        fun withIdAndHint(id: Int, text: String): Matcher<View?>? {
            val idMatcher = withId(id)
            val textMatcher = withHint(text)

            return object : BoundedMatcher<View?, TextView>(TextView::class.java) {

                override fun describeTo(description: Description) {
                    description.appendText(matcherDescriptionMessage)

                    textMatcher.describeTo(description)
                    idMatcher.describeTo(description)
                }

                override fun matchesSafely(textView: TextView): Boolean {
                    return textMatcher.matches(textView.hint.toString()) &&
                            idMatcher.matches(textView.id)
                }
            }
        }

        fun withResDrawable(@DrawableRes id: Int) = object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("ImageView with drawable same as drawable with id $id")
            }

            override fun matchesSafely(view: View): Boolean {
                val context = view.context
                val expectedBitmap = context.getDrawable(id)!!.toBitmap()
                return view is ImageView && view.drawable.toBitmap().sameAs(expectedBitmap)
            }
        }
    }

    class RecyclerViewMatcher(private val recyclerViewId: Int) {
        fun isEmpty(): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description?) {
                    // TODO: Describe this error
                }

                override fun matchesSafely(view: View?): Boolean {
                    val recyclerView = view?.rootView?.findViewById<View>(recyclerViewId) as RecyclerView
                    if(recyclerView.size < 1) {
                        return true
                    }
                    return false
                }
            }
        }

        fun isNotEmpty(): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description?) {
                    // TODO: Describe this error
                }

                override fun matchesSafely(view: View?): Boolean {
                    val recyclerView = view?.rootView?.findViewById<View>(recyclerViewId) as RecyclerView
                    if(recyclerView.size >= 1) {
                        return true
                    }
                    return false
                }
            }
        }

        fun atPosition(position: Int): Matcher<View> {
            return atPositionOnView(position, -1)
        }

        fun atPositionOnView(
            position: Int,
            targetViewId: Int
        ): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                var resources: Resources? = null
                var childView: View? = null
                override fun describeTo(description: Description) {
                    var idDescription = Integer.toString(recyclerViewId)
                    if (resources != null) {
                        idDescription = try {
                            resources!!.getResourceName(recyclerViewId)
                        } catch (var4: NotFoundException) {
                            String.format(
                                "%s (resource name not found)",
                                *arrayOf<Any>(Integer.valueOf(recyclerViewId))
                            )
                        }
                    }
                    description.appendText("with id: $idDescription")
                }

                public override fun matchesSafely(view: View): Boolean {
                    resources = view.resources
                    if (childView == null) {
                        val recyclerView =
                            view.rootView.findViewById<View>(recyclerViewId) as RecyclerView
                        childView = if (recyclerView != null && recyclerView.id == recyclerViewId) {
                            recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                        } else {
                            return false
                        }
                    }
                    return if (targetViewId == -1) {
                        view === childView
                    } else {
                        val targetView =
                            childView!!.findViewById<View>(targetViewId)
                        view === targetView
                    }
                }
            }
        }
    }
}