public class PortPair {

    private Integer tcpPort, udpPort;
    public PortPair(Integer tcpPort, Integer udpPort){
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
    }

    public Integer getTcpPort(){
        return this.tcpPort;
    }
    public Integer getUdpPort(){
        return this.udpPort;
    }

}
