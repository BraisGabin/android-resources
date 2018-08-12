package com.braisgabin.androidresources.parser.style

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import javax.xml.parsers.SAXParserFactory

internal fun parseFile(path: String): List<StyleToDomain> {
  val saxParser = SAXParserFactory.newInstance().newSAXParser()

  val styles = mutableListOf<StyleMapper>()

  val handler = object : DefaultHandler() {

    private var style: StyleMapper? = null
    private var row: Row? = null

    override fun startElement(uri: String?, localName: String?, qName: String, attributes: Attributes) {
      if (style != null) {
        if (qName != "item") {
          throw RuntimeException("Unknown qName $qName")
        }
        if (attributes.length != 1) {
          throw RuntimeException("Incompatible number of attributes")
        }
        row = Row(attributes.getValue("name"))
      } else if (qName == "style") {
        style = when (attributes.length) {
          1 -> StyleMapper(attributes.getValue("name"))
          2 -> StyleMapper(attributes.getValue("name"), attributes.getValue("parent"))
          else -> throw RuntimeException("Incompatible number of attributes")
        }
      }
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
      if (row != null) {
        row!!.value += ch.sliceArray(start until start + length).joinToString("").trim()
      }
    }

    override fun endElement(uri: String?, localName: String?, qName: String) {
      when (qName) {
        "style" -> {
          styles.add(style!!)
          style = null
        }
        "item" -> {
          style!!.values.add(row!!)
          row = null
        }
      }
    }
  }

  saxParser.parse(path, handler)

  return styles
}

internal interface StyleToDomain {
  fun toDomain(directory: String, fileName: String): Style
}

private data class StyleMapper(
    val name: String,
    val parent: String? = null,
    val values: MutableList<Row> = mutableListOf()
) : StyleToDomain {

  override fun toDomain(directory: String, fileName: String): Style {
    return Style(name, directory, fileName, parent, values.map { it.toDomain() })
  }
}

private data class Row(
    val name: String,
    var value: String = ""
) {

  fun toDomain(): Style.Item {
    return Style.Item(name, value)
  }
}
