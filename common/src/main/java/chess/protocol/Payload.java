package chess.protocol;

import java.nio.charset.StandardCharsets;

public class Payload {

    private StringBuffer data;

    Payload() {

    }

    public StringBuffer getData() {
        return this.data;
    }

    public void setData(StringBuffer data) {
        this.data = data;
    }

    public static Payload decode(byte[] data) {
        StringBuffer buffer = new StringBuffer(new String(data, StandardCharsets.UTF_8));
        
        Payload payload = new Payload();
        payload.setData(buffer);
        return payload;
    }

    public static byte[] encode(Payload data) {
        byte[] bytes = data.toString().getBytes(StandardCharsets.UTF_8);
        return bytes;
    }
}
