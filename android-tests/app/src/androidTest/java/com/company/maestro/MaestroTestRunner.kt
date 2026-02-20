package com.company.maestro

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.squareup.maestro.Maestro
import com.squareup.maestro.android.AndroidDriver
import com.squareup.maestro.android.MaestroTest
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class MaestroTestRunner {

    private val assetsFolder = "androidTest/assets"

    /**
     * Helper to load all YAML test files from assets folder
     */
    private fun getTestFiles(): List<File> {
        val context = InstrumentationRegistry.getInstrumentation().context
        val assetDir = File(context.filesDir.parentFile, assetsFolder)
        if (!assetDir.exists()) {
            throw RuntimeException("Assets folder not found at: $assetDir")
        }

        return assetDir.listFiles { file -> file.extension == "yaml" || file.extension == "yml" }?.toList()
            ?: emptyList()
    }

    @Test
    fun runAllMaestroTests() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val driver = AndroidDriver(context)
        val maestro = Maestro(driver)

        val testFiles = getTestFiles()
        if (testFiles.isEmpty()) {
            throw RuntimeException("No Maestro YAML test files found in assets folder")
        }

        for (file in testFiles) {
            println("Running Maestro test: ${file.name}")
            val test = MaestroTest(file.inputStream())
            maestro.run(test)
        }
    }
}
