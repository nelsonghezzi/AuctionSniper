package auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuction implements Auction {
  public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
  public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

  private final Chat chat;

  public XMPPAuction(Chat chat) {
    this.chat = chat;
  }

  @Override
  public void join() {
    this.sendMessage(JOIN_COMMAND_FORMAT);
  }

  @Override
  public void bid(int amount) {
    this.sendMessage(String.format(BID_COMMAND_FORMAT, amount));
  }

  private void sendMessage(final String message) {
    try {
      this.chat.sendMessage(message);
    } catch (XMPPException e) {
      e.printStackTrace();
    }
  }
}