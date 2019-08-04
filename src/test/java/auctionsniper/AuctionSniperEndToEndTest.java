package auctionsniper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuctionSniperEndToEndTest {

  private ApplicationRunner application;

  private FakeAuctionServer auction;

  @Before
  public void setup() {
    this.auction = new FakeAuctionServer("54321");
    this.application = new ApplicationRunner();
  }

  @Test
  public void sniperJoinsBidThenLoses() throws Exception {
    auction.startSellingItem();

    application.startBiddingIn(auction);
    auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

    auction.announceClosed();
    application.showsSniperHasLostAuction(0, 0);
  }

  @Test
  public void sniperMakesAHigherBidButLoses() throws Exception {
    auction.startSellingItem();

    application.startBiddingIn(auction);
    auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

    auction.reportPrice(1000, 98, "other bidder");
    application.hasShownSniperIsBidding(1000, 1098);

    auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

    auction.announceClosed();
    application.showsSniperHasLostAuction(1000, 1098);
  }

  @Test
  public void sniperWinsAnAuctionByBiddingHigher() throws Exception {
    auction.startSellingItem();

    application.startBiddingIn(auction);
    auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

    auction.reportPrice(1000, 98, "other bidder");
    application.hasShownSniperIsBidding(1000, 1098); // last price, last bid

    auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

    auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
    application.hasShownSniperIsWinning(1098); // winning bid

    auction.announceClosed();
    application.showsSniperHasWonAuction(1098); // last price
  }

  @After
  public void stop() {
    application.stop();
    auction.stop();
  }
}
