/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalhoum;

/**
 *
 * @author otavio
 */
public class MulticastMessage {
    public enum MulticastMessageType {
        NEW_COORDINATOR,
        COORDINATOR_HELLO,
        UNKNOWN
    }

    public MulticastMessageType type;
    public Integer senderUnicastPort;

    public MulticastMessage(MulticastMessageType type, Integer senderUnicastPort) {
        this.type = type;
        this.senderUnicastPort = senderUnicastPort;
    }

    public MulticastMessage(String payload) {
        String[] components = payload.split("@");

        try {
            this.type = MulticastMessageType.valueOf(components[0]);
            this.senderUnicastPort = Integer.parseInt(components[1].replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            this.type = MulticastMessageType.UNKNOWN;
            this.senderUnicastPort = 0;
        }
    }

    public String toPayload() {
        return String.format("%s@%s", type, senderUnicastPort);
    }
}