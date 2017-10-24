package ru.bagrusss.generator

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object Logger {

    @JvmField val logPath = System.getProperty("user.dir") + File.separator
    private lateinit var path: Path

    private lateinit var currentFile: String

    fun prepare(fileName: String) {
        currentFile = "$logPath$fileName"
        path = Paths.get(currentFile)
        val log = File(currentFile)
        log.delete()
        log.createNewFile()
    }

    fun log(string: String) {
        Files.write(path, "$string\n".toByteArray(), StandardOpenOption.APPEND)
    }
}