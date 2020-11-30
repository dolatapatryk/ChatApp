package pl.patrykdolata.chatapp.crypto

import android.content.Context
import android.util.Base64
import android.util.Base64InputStream
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.security.AlgorithmParameters
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

data class EncryptedText(val text: ByteArray, val params: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedText

        if (!text.contentEquals(other.text)) return false
        if (!params.contentEquals(other.params)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text.contentHashCode()
        result = 31 * result + params.contentHashCode()
        return result
    }
}

data class EncryptedMessage(
    val senderId: String,
    val receiverId: String,
    val text: String,
    val params: String,
    val timestamp: Long
)

@Singleton
class MessageCrypto @Inject constructor(@ApplicationContext private val context: Context) {

    fun encryptMessage(text: String, userId: String, friendId: String): EncryptedText {
        val cipher = getCipher(userId, friendId, CryptoType.ENCRYPT)
        val cipherText = cipher.doFinal(text.toByteArray())
        val params = cipher.parameters.encoded

        return EncryptedText(cipherText, params)
    }

    fun decryptMessage(text: EncryptedText, userId: String, friendId: String): String {
        val aesParams: AlgorithmParameters = AlgorithmParameters.getInstance("AES")
        aesParams.init(text.params)
        val cipher = getCipher(userId, friendId, CryptoType.DECRYPT, aesParams)
        return String(cipher.doFinal(text.text))
    }


    private fun getCipher(
        userId: String,
        friendId: String,
        type: CryptoType,
        params: AlgorithmParameters? = null
    ): Cipher {
        val secret = getSecret(userId, friendId)
        val aesKey = SecretKeySpec(secret, 0, 16, "AES")
        val mode = if (type == CryptoType.ENCRYPT) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE

        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        if (params != null) {
            cipher.init(mode, aesKey, params)
        } else {
            cipher.init(mode, aesKey)
        }
        return cipher
    }

    private fun getSecret(userId: String, friendId: String): ByteArray {
        val shared =
            context.getSharedPreferences("pl.patrykdolata.chatapp.prefs.keys", Context.MODE_PRIVATE)
        val bytes: ByteArray =
            shared.getString("${userId}/${friendId}/secret", "{}")!!.toByteArray()
        val byteArray = ByteArrayInputStream(bytes)
        val b64InputStream = Base64InputStream(byteArray, Base64.DEFAULT)
        val objIn = ObjectInputStream(b64InputStream)
        return objIn.readObject() as ByteArray
    }
}

enum class CryptoType {
    ENCRYPT,
    DECRYPT
}