package ru.bagrusss.generator

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object Logger {

    @JvmField val currentPath = System.getProperty("user.dir") + File.separator
    private lateinit var path: Path

    fun prepare(fileName: String) {
        val currentFile = "$currentPath$fileName"
        path = Paths.get(currentFile)
        File(currentFile).apply {
            delete()
            createNewFile()
        }
    }

    fun log(string: String) {
        Files.write(path, "$string\n".toByteArray(), StandardOpenOption.APPEND)
    }
}