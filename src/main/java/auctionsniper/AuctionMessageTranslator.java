package auctionsniper;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class AuctionMessageTranslator implements MessageListener {

  private final AuctionEventListener listener;

  public AuctionMessageTranslator(AuctionEventListener listener) {
    this.listener = listener;
  }

  public void processMessage(Chat chat, Message message) {
    AuctionEvent event = AuctionEvent.from(message.getBody());

    String eventType = event.type();

    if ("CLOSE".equals(eventType)) {
      this.listener.auctionClosed();
    } else if ("PRICE".equals(eventType)) {
      this.listener.currentPrice(event.currentPrice(), event.increment());
    }
  }

  private static class AuctionEvent {
    private final Map<String, String> fields = new HashMap<String, String>();

    public String type() {
      return this.get("Event");
    }

    public int currentPrice() {
      return this.getInt("CurrentPrice");
    }

    public int increment() {
      return this.getInt("Increment");
    }

    private int getInt(String fieldName) {
      return Integer.parseInt(this.get(fieldName));
    }

    private String get(String fieldName) {
      return this.fields.get(fieldName);
    }

    private void addField(String field) {
      String[] pair = field.split(":");
      this.fields.put(pair[0].trim(), pair[1].trim());
    }

    static AuctionEvent from(String messageBody) {
      AuctionEvent event = new AuctionEvent();

      for (String field : fieldsIn(messageBody)) {
        event.addField(field);
      }

      return event;
    }

    static String[] fieldsIn(String messageBody) {
      return messageBody.split(";");
    }
  }
}
