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
    private final String clientName;
    
    public Security(String clientName) {
        this.clientName = clientName;
    }
    
    public Boolean isValidSignature(PublicKey publicKey, byte[] signature) {
        try {
            Signature signatureInstance = Signature.getInstance("DSA");
            signatureInstance.initVerify(publicKey);
            signatureInstance.update(this.clientName.getBytes());
            
            if (signatureInstance.verify(signature)) {
                Logger.info("Security.isValidSignature received a valid signature");
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
