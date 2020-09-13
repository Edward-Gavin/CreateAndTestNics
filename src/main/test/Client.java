/**
 * @Author: Edward Gavin
 * @Create: 2020-09-13 18:27
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        long total = 0;
        try {
            Socket socket = new Socket("119.3.162.133", 80);

            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(sout);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("input 'GETSPEED' to start the test! ");
            String line = keyboard.readLine();

            if (line.equals("GETSPEED")) {
                System.out.println("200 OK\n");
                out.writeUTF(line);
                out.flush();
                long start = System.currentTimeMillis();

                byte[] bytes = new byte[100*1024]; //100K
                for(int i=1;true ;i++) {
                    if(socket.isConnected()){
                        int read = sin.read(bytes);
                        if (read < 0) break;
                        total += read;
                        if (i % 100000 == 0) {
                            long cost = System.currentTimeMillis() - start;
                            System.out.printf("Speed: %,d kb/s%n", (8*total)/(cost/1000));
                        }
                    }
                }
                long cost = System.currentTimeMillis() - start;
                System.out.printf("Speed: %,d B/s%n", (8*total)/(cost/1000));
                System.out.println("Test finished！");
            }else {
                System.out.println("Input error!");
                out.writeUTF(line);
                out.flush();
            }
            socket.close();
        }
        catch (IOException x) {
            x.printStackTrace();
        }
    }
}
