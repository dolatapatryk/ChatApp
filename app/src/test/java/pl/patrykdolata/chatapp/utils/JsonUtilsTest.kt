package pl.patrykdolata.chatapp.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import pl.patrykdolata.chatapp.models.FcmData
import pl.patrykdolata.chatapp.models.Friend
import pl.patrykdolata.chatapp.models.User

class JsonUtilsTest {

    @Test
    fun toJsonTest_whenArgIsString_expectTheSameString() {
        val obj = "testString"
        assertThat(JsonUtils.toJson(obj)).isEqualTo(obj)
    }

    @Test
    fun toJsonTest_whenArgIsNumber_expectStringRepresentationOfNumber() {
        val int = 2
        assertThat(JsonUtils.toJson(int)).isEqualTo("2")
        val long = 2L
        assertThat(JsonUtils.toJson(long)).isEqualTo("2")
        val float = 2.46F
        assertThat(JsonUtils.toJson(float)).isEqualTo("2.46")
        val double = 2.465
        assertThat(JsonUtils.toJson(double)).isEqualTo("2.465")
    }

    @Test
    fun toJsonTest_whenArgIsBoolean_expectStringRepresentationOfBoolean() {
        val truthy = true
        val falsy = false
        assertThat(JsonUtils.toJson(truthy)).isEqualTo("true")
        assertThat(JsonUtils.toJson(falsy)).isEqualTo("false")
    }

    @Test
    fun toJsonTest_whenArgIsObject_expectStringRepresentationOfObject() {
        val data = User("1", "test", "test@test.pl")

        val result = JsonUtils.toJson(data)
        assertThat(result).isEqualTo(
            "{\"id\":\"1\",\"username\":\"test\",\"email\":\"test@test.pl\"}"
        )
    }

    @Test
    fun fromJsonTest_whenArgIsString_expectString() {
        val obj = "test"
        assertThat(JsonUtils.fromJson(obj, String::class.java)).isEqualTo(obj)
    }

    @Test
    fun fromJsonTest_whenArgIsStringNumber_expectNumber() {
        val int = "2"
        assertThat(JsonUtils.fromJson(int, Int::class.java)).isEqualTo(2)
        val long = "2"
        assertThat(JsonUtils.fromJson(long, Long::class.java)).isEqualTo(2L)
        val float = "2.46"
        assertThat(JsonUtils.fromJson(float, Float::class.java)).isEqualTo(2.46F)
        val double = "2.465"
        assertThat(JsonUtils.fromJson(double, Double::class.java)).isEqualTo(2.465)
    }

    @Test
    fun fromJsonTest_whenArgIsStringBoolean_expectBoolean() {
        val truthy = "true"
        val falsy = "false"
        assertThat(JsonUtils.fromJson(truthy, Boolean::class.java)).isTrue()
        assertThat(JsonUtils.fromJson(falsy, Boolean::class.java)).isFalse()
    }

    @Test
    fun fromJsonTest_whenArgIsStringObject_expectObject() {
        val data = "{\"id\":\"1\",\"username\":\"test\",\"email\":\"test@test.pl\"}"

        val result = JsonUtils.fromJson(data, User::class.java)
        assertThat(result is User)
        assertThat(result?.id).isEqualTo("1")
        assertThat(result?.username).isEqualTo("test")
        assertThat(result?.email).isEqualTo("test@test.pl")
    }

    @Test
    fun fromJsonTest_whenArgIsMalformedJson_expectNull() {
        val data = "{,\"id\":\"1\",\"username\":\"test\",\"email\":\"test@test.pl\"}"

        val result = JsonUtils.fromJson(data, User::class.java)
        assertThat(result is User)
        assertThat(result).isNull()
    }

    @Test
    fun fromJsonArray_whenArgIsFriendArrayJson_expectFriendList() {
        val data = "[{\"id\":\"1\",\"username\":\"michalek\"}," +
                "{\"id\":\"2\",\"username\":\"patol2\"}]"
        val result: List<Friend> = JsonUtils.fromJsonArray(data)
        assertThat(result is ArrayList<Friend>)
        assertThat(result.size).isEqualTo(2)
        assertThat(result[1].username).isEqualTo("patol2")
    }

    @Test
    fun mapToModel_whenArgIsDataMap_expectFcmDataModel() {
        val data = mapOf(
            "type" to "message",
            "fromUserId" to "123",
            "fromUsername" to "user",
            "toUserId" to "456",
            "text" to "message text",
            "timestamp" to "12308219213"
        )
        val cloudMessage = JsonUtils.mapToModel(data, FcmData::class.java)
        assertThat(cloudMessage).isNotNull()
        assertThat(cloudMessage!!.type).isEqualTo("message")
        assertThat(cloudMessage.fromUserId).isEqualTo("123")
        assertThat(cloudMessage.fromUsername).isEqualTo("user")
        assertThat(cloudMessage.toUserId).isEqualTo("456")
        assertThat(cloudMessage.text).isEqualTo("message text")
        assertThat(cloudMessage.timestamp).isEqualTo(12308219213L)
    }
}