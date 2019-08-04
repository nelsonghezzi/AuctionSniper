package auctionsniper;

public class ApplicationRunner {
  public final static String SNIPER_ID = "sniper";
  public final static String SNIPER_PASSWORD = "sniper";
  public final static String XMPP_HOSTNAME = "localhost";
  public final static String SNIPER_XMPP_ID = "sniper@localhost/Auction";

  private String itemId;

  private AuctionSniperDriver driver;

  public void startBiddingIn(final FakeAuctionServer auction) {
    this.itemId = auction.getItemId();

    Thread thread = new Thread("Test Application") {
      @Override
      public void run() {
        try {
          Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    thread.setDaemon(true);
    thread.start();

    driver = new AuctionSniperDriver(1000);
    driver.showSniperStatus(this.itemId, 0, 0, MainWindow.STATUS_JOINING);
  }

  public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
    this.driver.showSniperStatus(this.itemId, lastPrice, lastBid, MainWindow.STATUS_BIDDING);
  }

  public void showsSniperHasLostAuction(int lastPrice, int lastBid) {
    this.driver.showSniperStatus(this.itemId, lastPrice, lastBid, MainWindow.STATUS_LOST);
  }

  public void hasShownSniperIsWinning(int winningBid) {
    this.driver.showSniperStatus(this.itemId, winningBid, winningBid, MainWindow.STATUS_WINNING);
  }

  public void showsSniperHasWonAuction(int lastPrice) {
    this.driver.showSniperStatus(this.itemId, lastPrice, lastPrice, MainWindow.STATUS_WON);
  }

  public void stop() {
    if (driver != null) {
      driver.dispose();
    }
  }
}