import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    ArrayList<Client> clients = new ArrayList<>();
    ServerSocket serverSocket;

    public ChatServer() throws IOException{
        // создаём серверный сокет на порту 1234
        this.serverSocket = new ServerSocket(1234);
    }

    // разослать сообщение всем клиентам
    void sendAll(Client clientAuthor, String message) {
        for (Client client : clients) {
            if (client != clientAuthor) {
                client.receive(message);
            }
        }
    }

    public void run() {
        while (true) {
            System.out.println("Waiting...");

            // ждём клиента из сети
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                // создаём клиента на своей стороне
                clients.add(new Client(socket, this));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new ChatServer().run();
    }
}
