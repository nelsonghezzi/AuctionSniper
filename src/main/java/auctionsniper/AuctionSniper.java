package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
  private final String itemId;
  private final Auction auction;
  private final SniperListener sniperListener;

  private boolean isWinning = false;

  public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
    this.itemId = itemId;
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
      int bid = price + increment;
      this.auction.bid(bid);
      this.sniperListener.sniperBidding(new SniperState(this.itemId, price, bid));
    }
  }
}
