/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabalhoum;

/**
 *
 * @author otavio
 */
public class TCPMessage {
    public enum TCPMessageType {
        NEW_ELECTION,
        ELECTION_REPLY,
        UNKNOWN
    }

    public TCPMessageType type;
    public Integer senderPort;

    public TCPMessage(TCPMessageType type, Integer senderPort) {
        this.type = type;
        this.senderPort = senderPort;
    }

    public TCPMessage(String payload) {
        String[] components = payload.split("@");

        try {
            this.type = TCPMessageType.valueOf(components[0]);
            this.senderPort = Integer.parseInt(components[1].replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
            this.type = TCPMessageType.UNKNOWN;
            this.senderPort = 0;
        }
    }

    public String toPayload() {
        return String.format("%s@%s", type, senderPort);
    }
}