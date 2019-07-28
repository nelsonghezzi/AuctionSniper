package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
  private final Auction auction;
  private final SniperListener sniperListener;

  private boolean isWinning = false;

  public AuctionSniper(Auction auction, SniperListener sniperListener) {
    this.auction = auction;
    this.sniperListener = sniperListener;
  }

  @Override
  public void auctionClosed() {
    if (this.isWinning) {
      this.sniperListener.sniperWon();
    } else {
      this.sniperListener.sniperLost();
    }
  }

  @Override
  public void currentPrice(int price, int increment, PriceSource priceSource) {
    this.isWinning = priceSource == PriceSource.FromSniper;

    if (this.isWinning) {
      this.sniperListener.sniperWinning();
    } else {
      this.auction.bid(price + increment);
      this.sniperListener.sniperBidding();
    }
  }
}
