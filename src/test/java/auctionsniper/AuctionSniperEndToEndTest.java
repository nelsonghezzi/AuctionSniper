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
    auction.hasReceivedJoinRequestFromSniper();

    auction.announceClosed();
    application.showsSniperHasLostAuction();
  }

  @After
  public void stop() {
    application.stop();
    auction.stop();
  }
}
