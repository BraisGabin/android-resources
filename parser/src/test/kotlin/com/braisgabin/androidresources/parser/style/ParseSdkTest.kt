package com.braisgabin.androidresources.parser.style

import com.nhaarman.mockitokotlin2.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class ParseSdkTest {

  private val loader = javaClass.classLoader

  private val action: (String) -> List<StyleToDomain> = mock {
    on { invoke(any()) } doReturn listOf(object : StyleToDomain {
      override fun toDomain(directory: String, fileName: String): Style {
        return (Style("foo", directory, fileName))
      }
    })
  }

  @AfterEach
  internal fun tearDown() {
    verifyNoMoreInteractions(action)
  }

  @Test
  internal fun `parse directory`() {
    val sdkPath = loader.getResource("sdk/").path
    assertThat(parseSdk(sdkPath, action), containsInAnyOrder(
        Style("foo", "values", "styles"),
        Style("foo", "values", "themes"),
        Style("foo", "values-es", "styles")))

    verify(action).invoke("${sdkPath}values/styles.xml")
    verify(action).invoke("${sdkPath}values/themes.xml")
    verify(action).invoke("${sdkPath}values-es/styles.xml")
  }
}
