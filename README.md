# Java Port Scanner
Port Scanner created using Java with GUI in JavaFX

#Download
You can download latest release from [Releases Page] (https://github.com/jpiechowka/java-port-scanner/releases)

#Usage 
####Make sure you have latest JRE installed####

###Host Discovery

This tab is used for discovering hosts on LAN

You can specify desired timeout and number of threads. It uses **.isReachable()** method to detect online devices. Sometimes it will fail to detect online hosts. Try increasing timeout and running again to solve this problem. Take note that increasing threads will produce more network load. Some firewalls may block your connection.

###Port Scan
This tab is used for port scanning

**Target IP** is the IP address to scan. Make sure to enter it properly

**Starting Port** is the first port that will be scanned. Must be in range: 1 - 65534

**Ending Port** is the last port that will be scanned. Must be greater than starting port and must be in range: 2 - 65535

You can specify desired timeout and number of threads. Similar to host discovery, more threads mean more network load. Some firewalls may block your connection.
