package com.braisgabin.androidresources.parser.style

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class ParseFileTest {

  private val loader = javaClass.classLoader

  @Test
  internal fun `parse file`() {
    assertThat(parseFile(loader.getResource("style/styles.xml").path).map { it.toDomain("", "") }, `is`(listOf(
        Style("style1", "", "", null, listOf(Style.Item("android:background", "@android:drawable/title_bar"))),
        Style("style2", "", "", "foo", listOf(Style.Item("android:background", "@android:drawable/title_bar")))
    )))
  }
}
