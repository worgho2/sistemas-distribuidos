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
    public Security() {}
    
    public Boolean isValidSignature(PublicKey publicKey, byte[] signature, String message) {
        try {
            Signature signatureInstance = Signature.getInstance("DSA");
            signatureInstance.initVerify(publicKey);
            signatureInstance.update(message.getBytes());
            
            if (signatureInstance.verify(signature)) {
                Logger.info("Security signature is valid");
                return true;
            } else {
                return false;
            }
        } catch (SignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            Logger.error("Security.isValidSignature exception: %s", e.getMessage());
            return false;
        }
    }
}
