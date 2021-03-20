import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import packets.serverKeyPacket;
import packets.PortPacket;

import java.util.ArrayList;
import java.util.HashMap;

public class InitialListener extends Listener {

    ArrayList<Integer> takenPorts;
    Integer nextPort = 7980;
    HashMap<String, PortPair> servers;
    Server server;
    public InitialListener(Server server){
        takenPorts = new ArrayList<>();
        takenPorts.add(7979);
        takenPorts.add(7878);
        servers = new HashMap<>();
        this.server = server;
    }

    public void connected(Connection c){
        System.out.println("Client connected");
    }

    public void received(Connection c, Object p){
        if(p instanceof serverKeyPacket){
            serverKeyPacket packet = (serverKeyPacket) p;
            if(!servers.containsKey(packet.serverKey)){
                PortPacket portPacket = new PortPacket();
                PortPair portPair = new PortPair(nextPort, nextPort+1);
                portPacket.tcpPort = portPair.getTcpPort();
                portPacket.udpPort = portPair.getUdpPort();
                this.nextPort = nextPort+2;
                Thread newServerThread = new Thread(new ServerRunner(portPair, 4));
                newServerThread.start();
                server.sendToTCP(c.getID(), portPacket);
                servers.put(packet.serverKey, portPair);
                System.out.println("New server started");
            }else{
                PortPacket portPacket = new PortPacket();
                portPacket.tcpPort = servers.get(packet.serverKey).getTcpPort();
                portPacket.udpPort = servers.get(packet.serverKey).getUdpPort();
                server.sendToTCP(c.getID(), portPacket);
            }
        }
    }
    public void disconnected(Connection c){
        System.out.println("Client disconnected");
    }
}
