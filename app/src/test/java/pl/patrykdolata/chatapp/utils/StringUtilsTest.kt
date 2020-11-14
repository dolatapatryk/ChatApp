package pl.patrykdolata.chatapp.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringUtilsTest {

    @Test
    fun isEmptyTest_whenStringIsEmpty_expectTrue() {
        val str = ""
        assertThat(StringUtils.isEmpty(str)).isTrue()
    }

    @Test
    fun isEmptyTest_whenStringIsNull_expectTrue() {
        val str: String? = null
        assertThat(StringUtils.isEmpty(str)).isTrue()
    }

    @Test
    fun isEmptyTest_whenStringIsNotEmpty_expectFalse() {
        val str = "test"
        assertThat(StringUtils.isEmpty(str)).isFalse()
    }

    @Test
    fun isNotEmptyTest_whenStringIsEmpty_expectFalse() {
        val str = ""
        assertThat(StringUtils.isNotEmpty(str)).isFalse()
    }

    @Test
    fun isNotEmptyTest_whenStringIsNull_expectFalse() {
        val str: String? = null
        assertThat(StringUtils.isNotEmpty(str)).isFalse()
    }

    @Test
    fun isNotEmptyTest_whenStringIsNotEmpty_expectTrue() {
        val str = "test"
        assertThat(StringUtils.isNotEmpty(str)).isTrue()
    }
}