package auctionsniper;

public class ApplicationRunner {
  public final static String SNIPER_ID = "sniper";
  public final static String SNIPER_PASSWORD = "sniper";
  public final static String XMPP_HOSTNAME = "localhost";

  private AuctionSniperDriver driver;

  public void startBiddingIn(final FakeAuctionServer auction) {
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
    driver.showSniperStatus(MainWindow.STATUS_JOINING);
  }

  public void showsSniperHasLostAuction() {
    this.driver.showSniperStatus(MainWindow.STATUS_LOST);
  }

  public void stop() {
    if (driver != null) {
      driver.dispose();
    }
  }
}