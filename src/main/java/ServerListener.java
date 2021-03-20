import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import packets.FirstConnectPacket;
import packets.Packet;
import packets.WinPacket;


public class ServerListener extends Listener {

    private final int nrOfPlayers;
    private final Server server;

    public ServerListener(int nrOfPlayers, Server server){
        this.nrOfPlayers = nrOfPlayers;
        this.server = server;
    }

    public void connected(Connection c){
        System.out.println("Client: " + c.getID() + " Just connected");
        FirstConnectPacket initialConnect = new FirstConnectPacket();
        initialConnect.id = c.getID();
        initialConnect.nrOfPlayers = nrOfPlayers;
        server.sendToTCP(c.getID(), initialConnect);

    }
    public void received (Connection c, Object p){
        if(p instanceof Packet){
            Packet packet = (Packet) p;
            System.out.println("Player " + packet.playerThatMovedID + " moved to x = " + packet.x + " y = " + packet.y);
            server.sendToAllExceptTCP(c.getID(), packet);
        }
        if(p instanceof WinPacket){
            WinPacket win = (WinPacket) p;
            System.out.println("Player with ID " + win.ID + " has won!");
            server.sendToAllTCP(win);

        }
    }

    public void disconnected(Connection c){
        System.out.println("Client " + c.getID() + " disconnected");
    }

}
