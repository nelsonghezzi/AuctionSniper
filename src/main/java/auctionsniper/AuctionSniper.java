package auctionsniper;

public class AuctionSniper implements AuctionEventListener {

  private final SniperListener sniperListener;

  public AuctionSniper(SniperListener sniperListener) {
    this.sniperListener = sniperListener;
  }

  @Override
  public void auctionClosed() {
    this.sniperListener.sniperLost();
  }

  @Override
  public void currentPrice(int price, int increment) {

  }
}
