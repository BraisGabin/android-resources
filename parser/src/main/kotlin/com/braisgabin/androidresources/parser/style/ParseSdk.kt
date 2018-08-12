package com.braisgabin.androidresources.parser.style

import org.apache.commons.io.FileUtils
import java.io.File

internal fun parseSdk(sdkPath: String, action: (String) -> List<StyleToDomain>): List<Style> {
  val sdkDirectory = File(sdkPath)

  val list = mutableListOf<Style>()
  sdkDirectory.listFiles().forEach { file ->
    if (file.isDirectory) {
      list.addAll(parseSdk(file.absolutePath, action))
    } else if (file.extension == "xml" && FileUtils.readFileToString(file, "UTF-8").contains("<style")) {
      val fileName = file.nameWithoutExtension
      val directory = file.parentFile.name
      list.addAll(action(file.absolutePath).map { it.toDomain(directory, fileName) })
    }
  }

  return list
}
