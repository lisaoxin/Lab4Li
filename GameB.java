import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GameB {
    private static final String QUEUE_NAME = "game_queue";
    private static final String WEB_SERVICE_URL = "http://localhost:8000";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final String SECRET_KEY = "5lg7fd9sdx0Xq/gXAxRCQENRxVoICE/Q5QSGAtPhcFA=";
    private static final int SHIFT_KEY = 3;

    // Variable to store encrypted message received from RabbitMQ
    private static String receivedEncryptedMessage;

    public static void main(String[] args) throws Exception {
        // Receive the encrypted game object as a flat file data from RabbitMQ
        receiveGameViaRabbitMQ();

        // Receive the game object as a serialized JSON message from a Web Service
        receiveGameViaWebService();
    }

    private static void receiveGameViaRabbitMQ() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String encryptedData = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[x] Received encrypted message from RabbitMQ: " + encryptedData);

            // Store the received encrypted message
            receivedEncryptedMessage = encryptedData;

            String decryptedData = decrypt(encryptedData);
            System.out.println("\nDecrypted message: " + decryptedData);

            Ship ship = convertFlatFileToShip(decryptedData);
            System.out.println("\nDeserialized ship object: " + ship);
            processShipData(ship);
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }

    private static void receiveGameViaWebService() throws Exception {
        URL url = new URL(WEB_SERVICE_URL + "/sendShip");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        int responseCode = connection.getResponseCode();
        System.out.println("\nResponse Code from Web Service: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String jsonData = in.readLine();
            in.close();

            System.out.println("Received JSON data: " + jsonData);

            Ship ship = convertJSONToShip(jsonData);
            System.out.println("\nDeserialized ship object: " + ship);
            processShipData(ship);

            // Print the received encrypted message from RabbitMQ
            if (receivedEncryptedMessage != null) {
                System.out.println("\nReceived encrypted message from RabbitMQ: " + receivedEncryptedMessage);
            } else {
                System.out.println("\nNo encrypted message received from RabbitMQ.");
            }

            // Wait for 2 seconds before sending the HMAC request
            Thread.sleep(2000);

            URL hmacUrl = new URL(WEB_SERVICE_URL + "/getHMAC");
            HttpURLConnection hmacConnection = (HttpURLConnection) hmacUrl.openConnection();
            hmacConnection.setRequestMethod("POST");
            hmacConnection.setDoOutput(true);
            OutputStream os = hmacConnection.getOutputStream();
            os.write(jsonData.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            int hmacResponseCode = hmacConnection.getResponseCode();
            System.out.println("\nHMAC Response Code: " + hmacResponseCode);
            if (hmacResponseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader hmacIn = new BufferedReader(new InputStreamReader(hmacConnection.getInputStream()));
                String receivedHmac = hmacIn.readLine();
                hmacIn.close();

                String calculatedHmac = calculateHMAC(jsonData);
                System.out.println("Received HMAC: " + receivedHmac);
                System.out.println("Calculated HMAC: " + calculatedHmac);

                if (receivedHmac.equals(calculatedHmac)) {
                    System.out.println("HMAC Verified.");
                } else {
                    System.out.println("HMAC Verification Failed.");
                }
            } else {
                System.out.println("Failed to receive HMAC: HTTP error code " + hmacResponseCode);
            }
        } else {
            System.out.println("Failed to receive game via Web Service: HTTP error code " + responseCode);
        }
    }

    private static void processShipData(Ship ship) {
        System.out.println("\nProcessing ship data:");
        System.out.println("Ship name: " + ship.getName());
        System.out.println("Speed: " + ship.getSpeed());
        System.out.println("Damage: " + ship.getDamage());
        System.out.println("Defense: " + ship.getDefense());
    }

    private static Ship convertFlatFileToShip(String flatFileData) {
        String[] parts = flatFileData.split(" ");
        String name = parts[0];
        int speed = Integer.parseInt(parts[1]);
        int damage = Integer.parseInt(parts[2]);
        int defense = Integer.parseInt(parts[3]);
        return new Ship(name, speed, damage, defense);
    }

    private static Ship convertJSONToShip(String jsonData) throws IOException {
        return objectMapper.readValue(jsonData, Ship.class);
    }

    private static String calculateHMAC(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(GameB.SECRET_KEY.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM);
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String decrypt(String cipherText) {
        StringBuilder plainText = new StringBuilder();
        for (char c : cipherText.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                int decryptedChar = c - GameB.SHIFT_KEY;
                if (decryptedChar < base) {
                    decryptedChar += 26;
                }
                plainText.append((char) decryptedChar);
            } else {
                plainText.append(c);
            }
        }
        return plainText.toString();
    }

}










