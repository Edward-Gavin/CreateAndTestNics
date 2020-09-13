/**
 * @Author: Edward Gavin
 * @Create: 2020-09-13 18:51
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args){
        try { ServerSocket server = new ServerSocket(80);
            System.out.println("Wait for client...");
            Socket socket = server.accept();
            System.out.println("Connect to client...\n");

            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            String line = in.readUTF();
            System.out.println("Command: " + line);

            if (line.equals("GETSPEED")){
                System.out.println("Start to test...");
                out.flush();
                byte[] bytes = new byte[100*1024]; // 100K
                int i = 0;
                long start = System.currentTimeMillis();
                while (i < 5000000) {
                    sout.write(bytes);
                    i++;
                    long cost = (System.currentTimeMillis() - start);
                    //30 segundos
                    if(cost >= 30000){
                        socket.close();
                        System.out.println("Test finished!!\n");
                    }
                    if(i == 5000000){
                        socket.close();
                        System.out.println("Test finished!!\n");
                    }
                }
            }else{
                System.out.println("Error Command !!\n");
            }
        } catch(IOException x) {
            x.printStackTrace();
        }
    }
}
