import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import packets.FirstConnectPacket;
import packets.Packet;
import packets.PortPacket;
import packets.WinPacket;

import java.io.IOException;

public class ServerRunner implements Runnable {

    Integer tcpPort, udpPort, nrOfPlayers;

    public ServerRunner(PortPair portPair, int nrOfPlayers){
        this.tcpPort = portPair.getTcpPort();
        this.nrOfPlayers = nrOfPlayers;
        this.udpPort = portPair.getUdpPort();
    }
    @Override
    public void run() {
        Server server = new Server();
        Kryo serverKryo = server.getKryo();
        serverKryo.register(Packet.class);
        serverKryo.register(FirstConnectPacket.class);
        serverKryo.register(WinPacket.class);
        serverKryo.register(PortPacket.class);
        try {
            server.bind(tcpPort, udpPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.addListener(new ServerListener(nrOfPlayers, server));

        server.start();

        while (true){
            if(server.getConnections().length < 1){
                server.stop();
                break;
            }else{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
