package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ABC Bunny", appName)
  }

  @Test
  fun `alphabet repository contains all 26 letters with strokes`() {
    val letters = com.example.data.model.AlphabetRepository.letters
    assertEquals(26, letters.size)
    val first = letters.first()
    assertEquals('A', first.charUpper)
    assertEquals('a', first.charLower)
    assertEquals("Apple", first.word)
    org.junit.Assert.assertTrue(first.upperStrokes.isNotEmpty())
    org.junit.Assert.assertTrue(first.lowerStrokes.isNotEmpty())
  }
}
