package com.braisgabin.androidresources.parser.style

import com.nhaarman.mockitokotlin2.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class ParseSdkTest {

  private val loader = javaClass.classLoader

  private val action: (String) -> List<StyleToDomain> = mock {
    on { invoke(any()) } doReturn listOf(object : StyleToDomain {
      override fun toDomain(): Style {
        return (Style("foo"))
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
    assertThat(parseSdk(sdkPath, action), `is`(listOf(
        Style("foo"),
        Style("foo"),
        Style("foo"))))

    verify(action).invoke("${sdkPath}values/styles.xml")
    verify(action).invoke("${sdkPath}values/themes.xml")
    verify(action).invoke("${sdkPath}values-es/styles.xml")
  }
}
