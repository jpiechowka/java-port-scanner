public class PortScanResult
{
    private final int port;
    private final boolean isOpen;
    private final String description;

    PortScanResult(final int port, final boolean isOpen, final String description)
    {
        this.port = port;
        this.isOpen = isOpen;
        this.description = description;
    }

    public int getPort(){ return port; }
    public boolean getIsOpen() { return isOpen; }
    public String getDescription() { return description; }
}
