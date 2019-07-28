package test.auctionsniper;

import static auctionsniper.ApplicationRunner.SNIPER_ID;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionEventListener.PriceSource;
import auctionsniper.AuctionMessageTranslator;

public class AuctionMessageTranslatorTest {
  @Rule public final JUnitRuleMockery context = new JUnitRuleMockery();

  private final AuctionEventListener listener = context.mock(AuctionEventListener.class);
  private final AuctionMessageTranslator translator = new AuctionMessageTranslator(SNIPER_ID, listener);

  public static final Chat UNUSED_CHAT = null;

  @Test
  public void notifiesAuctionClosedWhenCloseMessageReceived() {
    context.checking(new Expectations() {{
      oneOf(listener).auctionClosed();
    }});

    Message message = new Message();
    message.setBody("SOLVersion: 1.1; Event: CLOSE;");

    translator.processMessage(UNUSED_CHAT, message);
  }

  @Test
  public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
    context.checking(new Expectations() {{
      exactly(1).of(listener).currentPrice(192, 7, PriceSource.FromOtherBidder);
    }});

    Message message = new Message();
    message.setBody(
      "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");

    translator.processMessage(UNUSED_CHAT, message);
  }

  @Test
  public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
    context.checking(new Expectations() {{
      exactly(1).of(listener).currentPrice(234, 5, PriceSource.FromSniper);
    }});

    Message message = new Message();
    message.setBody(
      "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: "
      + SNIPER_ID + ";");

    translator.processMessage(UNUSED_CHAT, message);
  }
}