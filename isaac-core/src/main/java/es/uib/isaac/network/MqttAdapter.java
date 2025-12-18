package es.uib.isaac.network;

import org.fusesource.mqtt.client.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import static org.fusesource.mqtt.client.QoS.AT_LEAST_ONCE;

public class MqttAdapter {
    private final MQTT mqtt = new MQTT();
    private FutureConnection connection;

    private final Map<String, CopyOnWriteArrayList<MessageCallback>> callbacks = new ConcurrentHashMap<>();
    private final ExecutorService executor;
    private final AtomicBoolean running = new AtomicBoolean(false);

    private final Logger logger = Logger.getLogger(MqttAdapter.class.getName());

    @FunctionalInterface
    public interface MessageCallback {
        void onMessage(String topic, byte[] payload);
    }

    public MqttAdapter() {
        this(Executors.newSingleThreadExecutor());
    }

    public MqttAdapter(ExecutorService executor) {
        this.executor = executor;
    }

    public void connect() {
        try {
            mqtt.setHost("tom.uib.es", 1883);
            mqtt.setClientId("isaac-processing");
        }catch (URISyntaxException e) {
            logger.warning(e.getMessage());
            return;
        }

        connection = mqtt.futureConnection();

        try {
            connection.connect().await();
            running.set(true);
            startReceiveLoop();
        } catch (Exception e) {
            throw new RuntimeException("MQTT connection failed", e);
        }
    }

    public void subscribe(String topic, MessageCallback callback) {
        try {
            connection.subscribe(new Topic[]{ new Topic(topic, AT_LEAST_ONCE) }).await();
            callbacks.computeIfAbsent(topic, t -> new CopyOnWriteArrayList<>()).add(callback);
        } catch (Exception e) {
            throw new RuntimeException("Subscribe failed: " + topic, e);
        }
    }

    public void unsubscribe(String topic, MessageCallback callback) {
        List<MessageCallback> list = callbacks.get(topic);
        if (list == null) return;
        list.remove(callback);

        if (!list.isEmpty()) return;
        callbacks.remove(topic);

        try {
            connection.unsubscribe(new String[]{topic}).await();
        } catch (Exception ignored) {}
    }

    public void publish(String topic, byte[] payload) {
        try {
            connection.publish(topic, payload, AT_LEAST_ONCE, false).await();
        } catch (Exception e) {
            throw new RuntimeException("Publish failed: " + topic, e);
        }
    }

    private void startReceiveLoop() {
        Thread receiver = new Thread(() -> {
            while (running.get()) {
                try {
                    Message message = connection.receive().await();
                    dispatch(message);
                } catch (Exception e) {
                    if (running.get()) {
                        logger.warning("Receive loop failed: " + e.getMessage());
                    }
                }
            }
        }, "mqtt-receiver");

        receiver.setDaemon(true);
        receiver.start();
    }

    private void dispatch(Message message) {
        String topic = message.getTopic();
        byte[] payload = message.getPayload();

        message.ack();

        callbacks.forEach((registeredTopic, list) -> {
            if (topicMatches(registeredTopic, topic)) {
                for (MessageCallback callback : list) {
                    executor.execute(() -> callback.onMessage(topic, payload));
                }
            }
        });
    }

    private boolean topicMatches(String registered, String incoming) {
        if (registered.equals(incoming)) {
            return true;
        }

        if (registered.endsWith("/#")) {
            String prefix = registered.substring(0, registered.length() - 2);
            return incoming.startsWith(prefix);
        }

        return false;
    }

    public void disconnect() {
        running.set(false);
        try {
            connection.disconnect().await();
        } catch (Exception ignored) {}
        executor.shutdown();
    }
}
