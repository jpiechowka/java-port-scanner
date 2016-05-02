import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class HostDiscovery
{
    private final String address;
    private final int discoveryTimeout;

    HostDiscovery(final String address, final int discoveryTimeout)
    {
        this.address = address;
        this.discoveryTimeout = discoveryTimeout;
    }

    public Future<HostDiscoveryResult> multithreadedHostDicovery(final ExecutorService exService)
    {
        return exService.submit(() -> {
            try
            {
                String hName = null;
                boolean result = InetAddress.getByName(address).isReachable(discoveryTimeout);
                if (result)
                {
                    hName = InetAddress.getByName(address).getHostName();
                }
                return new HostDiscoveryResult(address, result, hName);
            } catch (IOException e)
            {
                return new HostDiscoveryResult(address, false, null);
            }
        });
    }
}
