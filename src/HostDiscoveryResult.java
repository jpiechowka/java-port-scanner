public class HostDiscoveryResult
{
    private final String ipAddress;
    private final boolean isOnline;
    private final String hostName;

    HostDiscoveryResult(final String ipAddress, final boolean isOnline, final String hostname)
    {
        this.ipAddress = ipAddress;
        this.isOnline = isOnline;
        this.hostName = hostname;
    }

    public String getIPAddress() { return ipAddress; }
    public boolean isOnline() { return isOnline; }
    public String getHostname() { return hostName; }
}
