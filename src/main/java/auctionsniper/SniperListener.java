package auctionsniper;

import java.util.EventListener;

public interface SniperListener extends EventListener {
  void sniperBidding(SniperState state);
  void sniperLost();
  void sniperWinning();
  void sniperWon();
}