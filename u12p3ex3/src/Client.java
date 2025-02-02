import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{
    Socket socket;
    Scanner in;
    PrintStream out;
    ChatServer server;

    public Client(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        // запускаем поток
        new Thread(this).start();
    }

    // принимает строку/сообщение от другого пользователя
    // когда Client принимает, он должен переслать сообщение NetClient
    void receive(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // создаём удобные средства ввода и вывода
            this.in = new Scanner(is);
            this.out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Welcome to chat!");
            out.println("Enter your name please:");
            String clientName = in.nextLine();
            server.sendAll(null, "-- " + clientName + " joined the chat. --");
            String input = in.nextLine();
            while (!input.equals("bye")) {
                server.sendAll(this, clientName + ": " + input);
                input = in.nextLine();
            }
            server.sendAll(null, "-- " + clientName + " leaved the chat. --");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
