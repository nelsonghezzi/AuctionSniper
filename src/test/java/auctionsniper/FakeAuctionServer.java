package auctionsniper;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class FakeAuctionServer {
  public static final String ITEM_ID_AS_LOGIN = "auction-item-%s";
  public static final String AUCTION_RESOURCE = "Auction";
  public static final String XMPP_HOSTNAME = "localhost";
  private static final String AUCTION_PASSWORD = "auction";

  private final String itemId;
  private final XMPPConnection connection;
  private Chat currentChat;

  public FakeAuctionServer(String itemId) {
    this.itemId = itemId;
    this.connection = new XMPPConnection(XMPP_HOSTNAME);
  }

  private final SingleMessageListener messageListener = new SingleMessageListener();

  public void startSellingItem() throws XMPPException {
    connection.connect();
    connection.login(String.format(ITEM_ID_AS_LOGIN, this.itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);

    connection.getChatManager().addChatListener(new ChatManagerListener() {
      public void chatCreated(Chat chat, boolean createdLocally) {
        currentChat = chat;
        chat.addMessageListener(messageListener);
      }
    });
  }

  public void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
    this.receivesAMessageMatching(sniperId, equalTo(Main.JOIN_COMMAND_FORMAT));
  }

  public void reportPrice(int price, int increment, String bidder) throws XMPPException {
    this.currentChat.sendMessage(String.format(
        "SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;", price, increment, bidder));
  }

  public void hasReceivedBid(int bid, String sniperId) throws InterruptedException {
    this.receivesAMessageMatching(sniperId, equalTo(String.format(Main.BID_COMMAND_FORMAT, bid)));
  }

  public void announceClosed() throws XMPPException {
    this.currentChat.sendMessage(new Message());
  }

  public String getItemId() {
    return this.itemId;
  }

  public void stop() {
    this.connection.disconnect();
  }

  private void receivesAMessageMatching(String sniperId, Matcher<? super String> messageMatcher)
      throws InterruptedException {
    this.messageListener.receivesAMessage(messageMatcher);
    assertThat(this.currentChat.getParticipant(), equalTo(sniperId));
  }

  public class SingleMessageListener implements MessageListener {

    private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1);

    @Override
    public void processMessage(Chat chat, Message message) {
      messages.add(message);
    }

    public void receivesAMessage(Matcher<? super String> messageMatcher) throws InterruptedException {
      final Message message = messages.poll(5, TimeUnit.SECONDS);
      assertThat("Message", message, is(notNullValue()));
      assertThat(message.getBody(), messageMatcher);
    }
  }
}