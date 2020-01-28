package com

import android.os.Build
import androidx.lifecycle.Observer
import com.example.nikejosecaballero.network.UrbanDictionary.Definition
import com.example.nikejosecaballero.repositories.UrbanDictionaryRepo
import com.example.nikejosecaballero.ui.activities.mainActivity.MainActivity
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class MyActivityTest {

    private lateinit var activity: MainActivity
    private lateinit var activityController: ActivityController<MainActivity>

    private val urbanDictionaryRepoMock = mockk<UrbanDictionaryRepo>(relaxed = true)
    private var successResponseList: List<Definition> = listOf(
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

    @Before
    fun setUp() {
        activityController = Robolectric.buildActivity(MainActivity::class.java)
        activity = activityController.get()
        activityController.create().start().resume()
        activity.mainActivityVM._setUrbanDictionaryRepo(urbanDictionaryRepoMock)
    }

    @Test
    fun testActivityCreation() {

    }

    @Test
    fun testRequestDefinitions() {
        every { runBlocking { urbanDictionaryRepoMock.getDefinitions("example") } } returns successResponseList
        activity.mainActivityVM.requestDefinitions("example")
        val recyclerViewSize = activity.definitionsRecyclerView!!.adapter!!.itemCount
    }
}