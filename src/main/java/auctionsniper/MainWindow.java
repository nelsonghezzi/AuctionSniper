package auctionsniper;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MainWindow extends JFrame {
  public static final String SNIPER_STATUS_NAME = "sniper status";
  public static final String SNIPERS_TABLE_NAME = "snipers table";

  public static final String STATUS_JOINING = "Joining";
  public static final String STATUS_BIDDING = "Bidding";
  public static final String STATUS_LOST = "Lost";
  public static final String STATUS_WINNING = "Winning";
  public static final String STATUS_WON = "Won";

  private final SnipersTableModel snipers = new SnipersTableModel();

  public MainWindow() {
    super("Auction Sniper");
    this.setName(Main.MAIN_WINDOW_NAME);
    this.fillContentPane(this.makeSnipersTable());
    this.pack();
    this.setLocation(300, 300);
    this.setSize(600, 400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  private void fillContentPane(JTable snipersTable) {
    final Container contentPane = this.getContentPane();
    contentPane.setLayout(new BorderLayout());

    contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
  }

  private JTable makeSnipersTable() {
    final JTable snipersTable = new JTable(this.snipers);
    snipersTable.setName(SNIPERS_TABLE_NAME);
    return snipersTable;
  }

  public void showStatus(String status) {
    this.snipers.setStatusText(status);
  }
}