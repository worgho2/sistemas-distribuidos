/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalho.dois;

import java.security.*;

/**
 *
 * @author otavio
 */
public class Security {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    
    public Security() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("DSA");
            generator.initialize(512, new SecureRandom());
            KeyPair keyPair = generator.generateKeyPair();
            
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            Logger.error("Security exception: %s", e.getMessage());
        }
    }
    
    public byte[] generateSignature(String message) {
        try {
            Signature signature = Signature.getInstance("DSA");
            signature.initSign(this.privateKey);
            signature.update(message.getBytes());
            
            return signature.sign();
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            Logger.error("Security.generateSignature exception: %s", e.getMessage());
            return message.getBytes();
        }
    }
    
    public PublicKey getPublicKey() {
        return this.publicKey;
    }
}
