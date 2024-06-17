
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.Executors;

public class GameA {
    private static final String QUEUE_NAME = "game_queue";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final String SECRET_KEY = "5lg7fd9sdx0Xq/gXAxRCQENRxVoICE/Q5QSGAtPhcFA=";
    private static final int SHIFT_KEY = 3; // Shift key for Caesar Cipher
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        // Trigger SLF4J messages by creating a ConnectionFactory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        // Create ship object
        Ship ship = new Ship("Fighter3000", 50, 10, 5);

        // Print plain text
        System.out.println("Plain text: " + ship.getName() + " " + ship.getSpeed() + " " + ship.getDamage() + " " + ship.getDefense());

        // Encrypt the ship object using Caesar Cipher
        String encryptedShipData = encrypt(ship.getName() + " " + ship.getSpeed() + " " + ship.getDamage() + " " + ship.getDefense());
        System.out.println("Encrypted text: " + encryptedShipData);

        // Send the encrypted ship object as flat file data using RabbitMQ
        sendShipViaRabbitMQ(encryptedShipData);

        // Send the ship object as a serialized JSON message using a Web Service
        sendShipViaWebService(ship);
    }

    private static void sendShipViaRabbitMQ(String encryptedShipData) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, encryptedShipData.getBytes(StandardCharsets.UTF_8));
            System.out.println("[x] Sent '" + encryptedShipData + "'");
        }
    }

    private static void sendShipViaWebService(Ship ship) throws Exception {
        String jsonData = objectMapper.writeValueAsString(ship);
        System.out.println("\nGame object serialized to JSON string: " + jsonData);

        String hmac = calculateHMAC(jsonData);
        System.out.println("HMAC: " + hmac);

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/sendShip", new ShipHandler(jsonData));
        server.createContext("/getHMAC", new HMACHandler(hmac));
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();
    }

    static class ShipHandler implements HttpHandler {
        private final String jsonData;

        ShipHandler(String jsonData) {
            this.jsonData = jsonData;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, jsonData.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonData.getBytes());
            }
        }
    }

    static class HMACHandler implements HttpHandler {
        private final String hmac;

        HMACHandler(String hmac) {
            this.hmac = hmac;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, hmac.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(hmac.getBytes());
            }
        }
    }

    private static String calculateHMAC(String data) {
        try {
            Mac sha256HMAC = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM);
            sha256HMAC.init(secretKey);
            byte[] hashBytes = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String encrypt(String plainText) {
        StringBuilder cipherText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            if (Character.isLetter(c)) {
                c = (char) (c + SHIFT_KEY);
                if ((c > 'z' && Character.isLowerCase(c)) || (c > 'Z' && Character.isUpperCase(c))) {
                    c = (char) (c - 26);
                }
            }
            cipherText.append(c);
        }
        return cipherText.toString();
    }
}

