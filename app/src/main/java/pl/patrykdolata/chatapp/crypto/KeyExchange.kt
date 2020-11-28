package pl.patrykdolata.chatapp.crypto

import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.interfaces.DHPublicKey
import javax.crypto.spec.DHParameterSpec

object KeyExchange {


//    fun generateKeyPair(): KeyPair {
//        val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("DH")
//        keyPairGenerator.initialize(2048)
//
//        return keyPairGenerator.generateKeyPair()
//    }
//
//    fun generatePublicKeyFromSenderKey(encodedSenderPublicKey: ByteArray): ByteArray {
//        val keyFactory: KeyFactory = KeyFactory.getInstance("DH")
//        val x509EncodedKeySpec = X509EncodedKeySpec(encodedSenderPublicKey)
//        val senderPublicKey: PublicKey = keyFactory.generatePublic(x509EncodedKeySpec)
//        val dhParamFromSenderPublicKey: DHParameterSpec = (senderPublicKey as DHPublicKey).params
//
//    }
}