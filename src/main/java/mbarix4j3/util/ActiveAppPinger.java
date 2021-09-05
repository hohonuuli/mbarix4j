package mbarix4j3.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Tools for checking if an ActiveAppBeacon is running.
 *
 * Created by brian on 6/17/14.
 */
public class ActiveAppPinger {

    public static boolean pingAll(Collection<Integer> ports, String expectedMsg) {
        boolean beaconExists = false;
        for (Integer p : ports) {
            beaconExists = ping(p, expectedMsg);
            if (beaconExists) {
                break;
            }
        }
        return beaconExists;
    }

    public static boolean ping(int port, final String expectedMsg) {
        boolean beaconExists = false;
        try {
            Socket socket = new Socket("localhost", port);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final OutputStream output = socket.getOutputStream();
            output.write("ping\n".getBytes());
            String response = reader.readLine();
            beaconExists = response.equals(expectedMsg);
            socket.close();
        }
        catch (Exception e) {
            Logger log = Logger.getLogger(ActiveAppPinger.class.getName());
            log.fine("Failed to connect to port " + port + ". Reason " + e.getMessage());
        }
        return beaconExists;

    }
}
