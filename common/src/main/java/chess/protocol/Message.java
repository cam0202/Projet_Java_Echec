package chess.protocol;

public class Message {

    private Header header;
    private Payload payload;

    public Message(Header header, Payload payload) {
        this.header = header;
        this.payload = payload;
    }

    public Header getHeader() {
        return this.header;
    }

    public Payload getPayload() {
        return this.payload;
    }
}
