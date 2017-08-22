package ru.bagrusss.generator

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object Logger {

    @JvmField val logPath = System.getProperty("user.dir") + "/log.txt"
    @JvmField val path: Path = Paths.get(logPath)

    fun prepare() {
        val log = File(logPath)
        log.delete()
        log.createNewFile()
    }

    fun log(string: String) {
        Files.write(path, "$string\n".toByteArray(), StandardOpenOption.APPEND)
    }
}