package com

import androidx.lifecycle.MutableLiveData
import com.example.nikejosecaballero.network.UrbanDictionary.Definition
import com.example.nikejosecaballero.repositories.UrbanDictionaryRepo
import com.example.nikejosecaballero.ui.activities.mainActivity.MainActivityVM
import com.testUtils.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.Description
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestWatcher
import kotlin.coroutines.ContinuationInterceptor

@RunWith(JUnit4::class)
class MainActivityVMTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val urbanRepositoryRepoMock = mockk<UrbanDictionaryRepo>(relaxed = true)
    private lateinit var mainActivityVM: MainActivityVM
    private lateinit var successResponseList: List<Definition>

    @Before
    fun setUp() {
        mainActivityVM = MainActivityVM(urbanRepositoryRepoMock)
        successResponseList = listOf(
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
    }

    @Test
    fun testRequestDefinitions() {
        every { runBlocking { urbanRepositoryRepoMock.getDefinitions("moc~key") } } returns successResponseList
        mainActivityVM.requestDefinitions("moc~key")
        assertTrue(mainActivityVM.definitions.value?.isNotEmpty()!!)
        assertTrue(mainActivityVM.definitions.value!![1].author == "Some guy")
        every { runBlocking { urbanRepositoryRepoMock.getDefinitions("10A20B30C") } } returns emptyList()
        assertTrue(mainActivityVM.definitions.value?.isEmpty()!!)
        verify(exactly = 1){ runBlocking { urbanRepositoryRepoMock.getDefinitions("moc~key") } }
        verify(exactly = 1){ runBlocking { urbanRepositoryRepoMock.getDefinitions("10A20B30C") } }
    }

    @Test
    fun testSortDefinitions_ascending() {

    }

    @Test
    fun testSortDefinitions_descending() {

    }

    @Test
    fun testSortDefinitions_noOrder() {

    }

    @Test
    fun testTimeOutFromRepository() {
        // Not implemented
    }
}