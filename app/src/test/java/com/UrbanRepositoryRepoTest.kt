package com

import com.example.nikejosecaballero.network.UrbanDictionary.Definition
import com.example.nikejosecaballero.network.UrbanDictionary.UrbanDictionaryAPI
import com.example.nikejosecaballero.network.UrbanDictionary.UrbanDictionaryResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.io.InputStream

@RunWith(JUnit4::class)
class UrbanRepositoryRepoTest {

    private val urbanDictionaryAPI = mockk<UrbanDictionaryAPI>()
    private val mockWebServer = MockWebServer()

    @Test
    fun testMockWebServer() {

        //val valalala: InputStream = this.javaClass.classLoader!!.getResourceAsStream("../urbanDictionaryGETOKResponse.json")
        /*
        val helloBleprintJson = File(
            javaClass.getResource("../urbanDictionaryGETOKResponse.json")!!.path)
        val content = helloBleprintJson.readText()
         */

        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody("{}")

        mockWebServer.enqueue(mockedResponse)
        mockWebServer.start()
    }

    @Test
    fun testGetDefinitions_200() {
        val okResponse = UrbanDictionaryResponse(
            listOf(
                Definition("I am in great pain, please help me.",
                    1000, 0, "Bird Person"),
                Definition("The worst thing about prison was the... was the Dementors!",
                    20, 10, "Michael Scott"),
                Definition("Some definition here", 5, 5, "Me")
            )
        )
        every { runBlocking { urbanDictionaryAPI.getDefinitions("wubbalubbadubdub") } } returns okResponse
        val result = runBlocking { urbanDictionaryAPI.getDefinitions("wubbalubbadubdub") }
        assertTrue(result.list.size == 3)
        assertTrue(result.list[0].author == "Bird Person")
        assertTrue(result.list[2].thumbsUp == 5)
        verify { runBlocking { urbanDictionaryAPI.getDefinitions("wubbalubbadubdub") } }
    }

    @Test
    fun testGetDefinitions_400() {
        // Bad request
    }

    @Test
    fun testGetDefinitions_401() {
        // Unauthorized
    }

    @Test
    fun testGetDefinitions_500() {
        // Server error
    }

    @Test
    fun cancelJobs() {

    }
}