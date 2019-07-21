package auctionsniper;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class MainWindow extends JFrame {
  public static final String SNIPER_STATUS_NAME = "sniper status";

  public static final String STATUS_JOINING = "Joining";
  public static final String STATUS_BIDDING = "Bidding";
  public static final String STATUS_LOST = "Lost";

  private final JLabel sniperStatus = createLabel(STATUS_JOINING);

  public MainWindow() {
    super("Auction Sniper");
    this.setName(Main.MAIN_WINDOW_NAME);
    this.add(sniperStatus);
    this.pack();
    this.setLocation(300, 300);
    this.setSize(600, 400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  private static JLabel createLabel(String initialText) {
    JLabel result = new JLabel(initialText);
    result.setName(SNIPER_STATUS_NAME);
    result.setBorder(new LineBorder(Color.BLACK));
    return result;
  }

  public void showStatus(String status) {
    this.sniperStatus.setText(status);
  }
}