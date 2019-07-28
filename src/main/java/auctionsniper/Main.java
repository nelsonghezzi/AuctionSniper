package auctionsniper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public final class Main {
  private static final int ARG_HOSTNAME = 0;
  private static final int ARG_USERNAME = 1;
  private static final int ARG_PASSWORD = 2;
  private static final int ARG_ITEM_ID = 3;

  public static final String AUCTION_RESOURCE = "Auction";
  public static final String ITEM_ID_AS_LOGIN = "auction-item-%s";
  public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

  public static final String MAIN_WINDOW_NAME = "Auction Sniper";

  private MainWindow ui;

  private Main() throws Exception {
    startUserInterface();
  }

  @SuppressWarnings("unused")
  private Chat notToBeGCd;

  public static void main(String... args) throws Exception {
    Main main = new Main();
    main.joinAuction(connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]), args[ARG_ITEM_ID]);
  }

  private void startUserInterface() throws InvocationTargetException, InterruptedException {
    SwingUtilities.invokeAndWait(new Runnable() {
      public void run() {
        ui = new MainWindow();
      }
    });
  }

  private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
    this.disconnectWhenUICloses(connection);

    final Chat chat =
      connection.getChatManager().createChat(auctionId(itemId, connection), null);

    this.notToBeGCd = chat;

    Auction auction = new XMPPAuction(chat);

    chat.addMessageListener(
      new AuctionMessageTranslator(
        connection.getUser(),
        new AuctionSniper(
          auction,
          new SniperStateDisplayer())));

    auction.join();
  }

  private void disconnectWhenUICloses(final XMPPConnection connection) {
    ui.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        connection.disconnect();
      }
    });
  }

  private static String auctionId(String itemId, XMPPConnection connection) {
    return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
  }

  private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
    XMPPConnection connection = new XMPPConnection(hostname);
    connection.connect();
    connection.login(username, password, AUCTION_RESOURCE);

    return connection;
  }

  public class SniperStateDisplayer implements SniperListener {

    @Override
    public void sniperBidding() {
      this.showStatus(MainWindow.STATUS_BIDDING);
    }

    @Override
    public void sniperLost() {
      this.showStatus(MainWindow.STATUS_LOST);
    }

    @Override
    public void sniperWinning() {
      this.showStatus(MainWindow.STATUS_WINNING);
    }

    @Override
    public void sniperWon() {
      this.showStatus(MainWindow.STATUS_WON);
    }

    private void showStatus(final String status) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          ui.showStatus(status);
        }
      });
    }
  }
}
