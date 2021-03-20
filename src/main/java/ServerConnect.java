import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import packets.FirstConnectPacket;
import packets.Packet;
import packets.PortPacket;
import packets.WinPacket;


import java.io.IOException;

public class ServerConnect {

    static Server server;

    public void start(int nrOfPlayers, int udpPort, int tcpPort) throws IOException {


        server = new Server();

        server.bind(tcpPort, udpPort);

        //register packets that will be sent over the network
        Kryo serverKryo = server.getKryo();
        serverKryo.register(Packet.class);
        serverKryo.register(FirstConnectPacket.class);
        serverKryo.register(WinPacket.class);
        serverKryo.register(PortPacket.class);

        server.start();
        System.out.println("inf112.skeleton.app.Server is up and running");

        server.addListener(new InitialListener(server));



    }

}
