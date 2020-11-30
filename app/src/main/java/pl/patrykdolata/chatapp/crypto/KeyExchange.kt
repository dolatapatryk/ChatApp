package pl.patrykdolata.chatapp.crypto

import android.content.Context
import android.util.Base64
import android.util.Base64InputStream
import android.util.Base64OutputStream
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.KeyAgreement
import javax.crypto.interfaces.DHPublicKey
import javax.crypto.spec.DHParameterSpec
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.experimental.and

@Singleton
class KeyExchange @Inject constructor(@ApplicationContext private val context: Context) {

    fun generateKeyPair(userId: String, friendId: String, spec: DHParameterSpec? = null): KeyPair {
        val keyPairGenerator: KeyPairGenerator =
            KeyPairGenerator.getInstance("DH")
        if (spec != null) {
            keyPairGenerator.initialize(spec)
        } else {
            keyPairGenerator.initialize(2048)
        }

        val keyPair = keyPairGenerator.generateKeyPair()

        saveObject(keyPair, userId, friendId, "key")

        return keyPair
    }

    fun doKeyExchangeFromSenderKey(
        encodedSenderPublicKey: ByteArray,
        userId: String,
        friendId: String
    ) {
        val senderPublicKey = getSenderPublicKey(encodedSenderPublicKey)
        val dhParamFromSenderPublicKey: DHParameterSpec = (senderPublicKey as DHPublicKey).params

        val keyPair = generateKeyPair(userId, friendId, dhParamFromSenderPublicKey)
        doAgreement(keyPair, senderPublicKey, userId, friendId)
    }

    fun doAgreement(
        keyPair: KeyPair,
        senderPublicKey: PublicKey,
        userId: String,
        friendId: String
    ) {
        val keyAgreement: KeyAgreement = KeyAgreement.getInstance("DH")
        keyAgreement.init(keyPair.private)

        keyAgreement.doPhase(senderPublicKey, true)
        val secret = keyAgreement.generateSecret()
        saveObject(secret, userId, friendId, "secret")
    }

    fun getSenderPublicKey(encodedSenderPublicKey: ByteArray): PublicKey {
        val keyFactory: KeyFactory = KeyFactory.getInstance("DH")
        val x509EncodedKeySpec = X509EncodedKeySpec(encodedSenderPublicKey)
        return keyFactory.generatePublic(x509EncodedKeySpec)
    }

    fun getKeyPair(userId: String, friendId: String): KeyPair {
        return getObject(userId, friendId, "key", KeyPair::class.java)
    }

    fun getSecret(userId: String, friendId: String): ByteArray {
        return getObject(userId, friendId, "secret", ByteArray::class.java)
    }

    private fun <T> getObject(userId: String, friendId: String, type: String, klazz: Class<T>): T {
        val shared =
            context.getSharedPreferences("pl.patrykdolata.chatapp.prefs.keys", Context.MODE_PRIVATE)
        val bytes: ByteArray =
            shared.getString("${userId}/${friendId}/${type}", "{}")!!.toByteArray()
        val byteArray = ByteArrayInputStream(bytes)
        val b64InputStream = Base64InputStream(byteArray, Base64.DEFAULT)
        val objIn = ObjectInputStream(b64InputStream)
        return objIn.readObject() as T
    }

    private fun saveObject(obj: Any, userId: String, friendId: String, type: String) {
        val shared =
            context.getSharedPreferences("pl.patrykdolata.chatapp.prefs.keys", Context.MODE_PRIVATE)
        val editor = shared.edit()

        val arrayOutputStream = ByteArrayOutputStream()
        val objectOutput = ObjectOutputStream(arrayOutputStream)
        objectOutput.writeObject(obj)
        val data: ByteArray = arrayOutputStream.toByteArray()
        objectOutput.close()
        arrayOutputStream.close()

        val out = ByteArrayOutputStream()
        val b64 = Base64OutputStream(out, Base64.DEFAULT)
        b64.write(data)
        b64.close()
        out.close()

        editor.putString("${userId}/${friendId}/${type}", String(out.toByteArray()))
        editor.apply()
    }

    private fun byte2hex(b: Byte, buf: StringBuffer) {
        val hexChars: CharArray = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F'
        )
        val high = (b and 0x0f).toInt() shr 4
        val low = (b and 0x0f).toInt()
        buf.append(hexChars[high])
        buf.append(hexChars[low])
    }

    private fun toHexString(block: ByteArray): String {
        val buf = StringBuffer()
        for (i in block.indices) {
            byte2hex(block[i], buf)
            if (i < block.size - 1) {
                buf.append(":")
            }
        }
        return buf.toString()
    }
}