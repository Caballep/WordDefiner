package com

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.nikejosecaballero.R
import com.example.nikejosecaballero.network.UrbanDictionary.Definition
import com.example.nikejosecaballero.repositories.UrbanDictionaryRepo
import com.example.nikejosecaballero.ui.activities.mainActivity.MainActivity
import com.testUtils.CustomMatchers
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import org.hamcrest.Matchers.*
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUITest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    private val urbanDictionaryRepoMock = mockk<UrbanDictionaryRepo>(relaxed = true)
    private val successResponseList: List<Definition> = listOf(
        Definition(
            "Tells Mockito to mock the databaseMock instance",
            100,
            50,
            "James Bond"
        ),
        Definition(
            "Instantiates the class under test using the created mock",
            25,
            70,
            "Some guy"
        ),
        Definition(
            "Asserts that the method call returned true",
            410,
            20,
            "Dragon-born"
        )
    )

    private val currentStatusInitialText = "I can accept failure, everyone fails at something. But I " +
            "can't accept not trying. -Michael Jordan"
    private val actionBarEditTextInitialText = ""
    private val actionBarEditTextInitialHint = "Enter a word"
    private val currentStatusNothingFoundText = "Nothing found"

    @Before
    fun setUp() {
        Thread.sleep(50)
        activityRule.activity.mainActivityVM._setUrbanDictionaryRepo(urbanDictionaryRepoMock)
    }

    @Test
    fun testVerifyViews() {
        onView(withId(R.layout.action_bar_search))
        onView(withId(R.id.actionBarEditText)).check(matches(withText(actionBarEditTextInitialText)))
        onView(withId(R.id.actionBarEditText)).check(matches(withHint(actionBarEditTextInitialHint)))
        onView(withId(R.id.definitionsRecyclerView))
        onView(CustomMatchers.withIdAndText(R.id.currentStatusTextView, currentStatusInitialText))
    }

    @Test
    fun testActionBarEditTextActionPerform() {
        every { runBlocking { urbanDictionaryRepoMock.getDefinitions("uno") } } returns emptyList()
        every { runBlocking { urbanDictionaryRepoMock.getDefinitions("dos") } } returns emptyList()
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.click())
        assertTrue(isKeyboardShown())
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.typeText("uno"))
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        assertFalse(isKeyboardShown())
        onView(CustomMatchers.withIdAndHint(R.id.actionBarEditText, "uno"))
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.click())
        assertTrue(isKeyboardShown())
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.typeText("dos"))
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        assertFalse(isKeyboardShown())
        onView(CustomMatchers.withIdAndHint(R.id.actionBarEditText, "dos"))
    }

    @Test
    fun testActionBarEditTextActionPerform_emptyText() {
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.click())
        assertTrue(isKeyboardShown())
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        // Pressing enter when there is no text in the editText does not dismiss the keyboard
        assertTrue(isKeyboardShown())
        onView(withId(R.id.actionBarEditText)).check(matches(withHint(actionBarEditTextInitialHint)))
        onView(withId(R.id.currentStatusTextView)).check(matches(withText(currentStatusInitialText)))
    }

    @Test
    fun testSearchWord() {
        every { runBlocking { urbanDictionaryRepoMock.getDefinitions("honorable") } } returns successResponseList
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.typeText("honorable"))
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.currentStatusTextView)).check(matches(not(isDisplayed())))
        onView(withRecyclerView(R.id.definitionsRecyclerView)?.isNotEmpty())
        for (listPos in successResponseList.indices) {
            onView(withRecyclerView(R.id.definitionsRecyclerView)
                ?.atPositionOnView(listPos, R.layout.item_definitions_adapter))
            onView(withRecyclerView(R.id.definitionsRecyclerView)?.atPosition(listPos))
                .check(matches(hasDescendant(withText(successResponseList[listPos].author))))
        }
    }

    @Test
    fun testSearchWord_noResult() {
        every { runBlocking { urbanDictionaryRepoMock.getDefinitions("meseeks") } } returns emptyList()
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.typeText("meseeks"))
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.currentStatusTextView)).check(matches(withText(currentStatusNothingFoundText)))
        onView(withRecyclerView(R.id.definitionsRecyclerView)?.isEmpty())
    }

    @Test
    fun testSortResult_highestRated() {
        every { runBlocking { urbanDictionaryRepoMock.getDefinitions("spaceJams") } } returns successResponseList
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.typeText("spaceJams"))
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))

        onView(withRecyclerView(R.id.definitionsRecyclerView)?.isNotEmpty())
        for (listPos in successResponseList.indices) {
            onView(withRecyclerView(R.id.definitionsRecyclerView)
                ?.atPositionOnView(listPos, R.layout.item_definitions_adapter))
            onView(withRecyclerView(R.id.definitionsRecyclerView)?.atPosition(listPos))
                .check(matches(hasDescendant(withText(successResponseList[listPos].author))))
        }

        selectElementInSpinner(R.id.sortSpinner, "Highest rated")

        // Creating a sorted list to verify the data inside the recycler view
        val highestRatedDescending = successResponseList.sortedByDescending { it.thumbsUp }

        onView(withRecyclerView(R.id.definitionsRecyclerView)?.isNotEmpty())
        for (listPos in highestRatedDescending.indices) {
            onView(withRecyclerView(R.id.definitionsRecyclerView)
                ?.atPositionOnView(listPos, R.layout.item_definitions_adapter))
            onView(withRecyclerView(R.id.definitionsRecyclerView)?.atPosition(listPos))
                .check(matches(hasDescendant(withText(highestRatedDescending[listPos].author))))
        }
    }

    @Test
    fun testSortResult_lowestRated() {
        every { runBlocking { urbanDictionaryRepoMock.getDefinitions("cooper") } } returns successResponseList
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.typeText("cooper"))
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))

        onView(withRecyclerView(R.id.definitionsRecyclerView)?.isNotEmpty())
        for (listPos in successResponseList.indices) {
            onView(withRecyclerView(R.id.definitionsRecyclerView)
                ?.atPositionOnView(listPos, R.layout.item_definitions_adapter))
            onView(withRecyclerView(R.id.definitionsRecyclerView)?.atPosition(listPos))
                .check(matches(hasDescendant(withText(successResponseList[listPos].author))))
        }

        selectElementInSpinner(R.id.sortSpinner, "Lowest rated")

        // Creating a sorted list to verify the data inside the recycler view
        val lowestRatedDescending = successResponseList.sortedByDescending { it.thumbsDown }

        onView(withRecyclerView(R.id.definitionsRecyclerView)?.isNotEmpty())
        for (listPos in lowestRatedDescending.indices) {
            onView(withRecyclerView(R.id.definitionsRecyclerView)
                ?.atPositionOnView(listPos, R.layout.item_definitions_adapter))
            onView(withRecyclerView(R.id.definitionsRecyclerView)?.atPosition(listPos))
                .check(matches(hasDescendant(withText(lowestRatedDescending[listPos].author))))
        }
    }

    @Test
    fun testSortResult_nothing() {
        every { runBlocking { urbanDictionaryRepoMock.getDefinitions("alduin") } } returns successResponseList
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.typeText("alduin"))
        onView(withId(R.id.actionBarEditText)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))

        selectElementInSpinner(R.id.sortSpinner, "Lowest rated")

        val lowestRatedDescending = successResponseList.sortedByDescending { it.thumbsDown }

        onView(withRecyclerView(R.id.definitionsRecyclerView)?.isNotEmpty())
        for (listPos in lowestRatedDescending.indices) {
            onView(withRecyclerView(R.id.definitionsRecyclerView)
                ?.atPositionOnView(listPos, R.layout.item_definitions_adapter))
            onView(withRecyclerView(R.id.definitionsRecyclerView)?.atPosition(listPos))
                .check(matches(hasDescendant(withText(lowestRatedDescending[listPos].author))))
        }

        selectElementInSpinner(R.id.sortSpinner, "Nothing")

        onView(withRecyclerView(R.id.definitionsRecyclerView)?.isNotEmpty())
        for (listPos in successResponseList.indices) {
            onView(withRecyclerView(R.id.definitionsRecyclerView)
                ?.atPositionOnView(listPos, R.layout.item_definitions_adapter))
            onView(withRecyclerView(R.id.definitionsRecyclerView)?.atPosition(listPos))
                .check(matches(hasDescendant(withText(successResponseList[listPos].author))))
        }
    }

    @Test
    fun testSortResult_noDataToSort() {
        onView(withId(R.id.actionBarEditText)).check(matches(withHint(actionBarEditTextInitialHint)))
        onView(withId(R.id.currentStatusTextView)).check(matches(withText(currentStatusInitialText)))

        onView(withRecyclerView(R.id.definitionsRecyclerView)!!.isEmpty())
        selectElementInSpinner(R.id.sortSpinner, "Highest rated")

        // Nothing should happen
        onView(withRecyclerView(R.id.definitionsRecyclerView)!!.isEmpty())
        onView(withId(R.id.currentStatusTextView)).check(matches(withText(currentStatusInitialText)))
    }


    private fun selectElementInSpinner(spinnerId: Int, text: String) {
        onView(withId(spinnerId)).perform(ViewActions.click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(text)))
            .perform(ViewActions.click())
        onView(withId(spinnerId))
            .check(matches(withSpinnerText(containsString(text))))
    }

    private fun isKeyboardShown(): Boolean {
        val inputMethodManager = InstrumentationRegistry.getInstrumentation().targetContext
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.isAcceptingText
    }

    private fun withRecyclerView(recyclerViewId: Int): CustomMatchers.RecyclerViewMatcher? {
        return CustomMatchers.RecyclerViewMatcher(recyclerViewId)
    }
}