package org.webrtc.kite.janustutorial.steps;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.cosmosoftware.kite.entities.Timeouts;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.steps.TestStep;
import io.cosmosoftware.kite.interfaces.Runner;
import org.webrtc.kite.janustutorial.pages.MainPage;
import org.webrtc.kite.janustutorial.pages.StreamPage;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cosmosoftware.kite.util.TestUtils.executeJsScript;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class CollectStatsStep extends TestStep {

    private final StreamPage streamPage;


    public CollectStatsStep(Runner runner) {
        super(runner);
        this.streamPage = new StreamPage(runner);
    }


    @Override
    public String stepDescription() {
        return "Collect Stats ";
    }

    @Override
    protected void step() throws KiteTestException {

        long lastVideoBytesReceived = 0;
        long lastVideoTimestamp = 0;
        long lastAudioBytesReceived = 0;
        long lastAudioTimestamp = 0;
        String clientId = getClientID();
        String clientIdWithoudSpace;
        if (Character.isDigit(clientId.charAt(clientId.length() - 3))) {
            clientIdWithoudSpace = clientId.substring(clientId.length() - 3).replaceAll("\\s", "");
        } else {
            clientIdWithoudSpace = clientId.substring(clientId.length() - 2).replaceAll("\\s", "");
        }
        int clientNumber = Integer.parseInt(clientIdWithoudSpace);
        int clientRampUpNumber = clientNumber / 10;
        int totalRampups = 15;
        int extraRounds = ((totalRampups - 1) - clientRampUpNumber) * 30;
        for (int i = 1; i <= 150 + extraRounds; i++) {

        String script2 =
                "const getStatsValues = () =>" +
                        " window.pc.getStats()" +
                        "    .then(stats => {" +
                                "let filteredStats = [];" +
                                "stats.forEach(report => { " +
                                    "if (report.type === \"inbound-rtp\" && report.kind === \"audio\") {" +
                                        "filteredStats.push(report);" +
                                     "}" +
                                     "if (report.type === \"inbound-rtp\" && report.kind === \"video\") {" +
                                        "filteredStats.push(report);" +
                                      "}" +
                                     "if (report.type === \"remote-outbound-rtp\" && report.kind === \"audio\") {" +
                                            "filteredStats.push(report);" +
                                      "}" +
                                     "if (report.type === \"remote-outbound-rtp\" && report.kind === \"video\") {" +
                                            "filteredStats.push(report);" +
                                      "}" +
                                "});" +
                                "return filteredStats;" +
                            "});" +
                        "const stashStats = async () => {" +
                            "window.KITEStats_" + i + " = await getStatsValues();" +
                            "return 0;" +
                        "};" +
                        "stashStats();";

            executeJsScript(webDriver, script2);
            waitAround(Timeouts.ONE_SECOND_INTERVAL);
            List<Map<String, Object>> stats = (List<Map<String, Object>>) executeJsScript(webDriver, "return window.KITEStats_" + i + ";");
            if (stats == null) {
                logger.info("stats are null");
                stats = new ArrayList<>();
            }
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            // For Latency
            long senderTimestamp = 0;
            long receiverTimestamp = 0;
            for (Map<String, Object> map : stats) {
                JsonObjectBuilder innerJsonObjectBuilder = Json.createObjectBuilder();
                // compute bitrates
                if (map.get("type").equals("inbound-rtp")) {
                    // compute bitrate for Video
                    if (map.get("kind").equals("video")) {
                        long currentVideoBytesReceived = (long) map.get("bytesReceived");
                        long currentVideoTimestamp = (long) map.get("timestamp");
                        // For latency
                        receiverTimestamp = currentVideoTimestamp;
                        int kbitPerSecond = 0;
                        if (i > 1) {
                            int diffBytesReceived = (int) (currentVideoBytesReceived - lastVideoBytesReceived);
                            int diffTimestamp = (int) (currentVideoTimestamp - lastVideoTimestamp);
                            // Mal 8 wegen byte nach bit
                            kbitPerSecond = (diffBytesReceived * 8) / diffTimestamp;
                        }

                        innerJsonObjectBuilder.add("video-bitrate-in-kbit-per-second", kbitPerSecond);

                        lastVideoBytesReceived = currentVideoBytesReceived;
                        lastVideoTimestamp = currentVideoTimestamp;
                    }
                    // Compute Bitrate for Audio
                    if (map.get("kind").equals("audio")) {
                        long currentAudioBytesReceived = (long) map.get("bytesReceived");
                        long currentAudioTimestamp = (long) map.get("timestamp");
                        long kbitPerSecond = 0;
                        if (i > 1) {
                            long diffBytesReceived = currentAudioBytesReceived - lastAudioBytesReceived;
                            long diffTimestamp = currentAudioTimestamp - lastAudioTimestamp;
                            kbitPerSecond = (diffBytesReceived * 8) / diffTimestamp;
                        }
                        innerJsonObjectBuilder.add("audio-bitrate-in-kbit-per-second", kbitPerSecond);
                        lastAudioBytesReceived = currentAudioBytesReceived;
                        lastAudioTimestamp = currentAudioTimestamp;
                    }
                }
                if (map.get("type").equals("remote-outbound-rtp")) {
                    // compute bitrate for Video
                    if (map.get("kind").equals("video")) {
                        senderTimestamp = (long) map.get("timestamp");
                    }
                }

                for (String key : map.keySet()) {
                    String value = "";
                    Object objectValue = map.get(key);
                    if (objectValue == null) {
                        continue;
                    } else if (objectValue instanceof Long) {
                        value = Long.toString((Long) objectValue);
                    } else if (objectValue instanceof Integer) {
                        value = Integer.toString((Integer) objectValue);
                    } else if (objectValue instanceof Double) {
                        value = Double.toString((Double) objectValue);
                    } else if (objectValue instanceof Float) {
                        value = Float.toString((Float) objectValue);
                    } else {
                        value = (String) objectValue;
                    }
                    innerJsonObjectBuilder.add(key, value);
                }
                jsonObjectBuilder.add(map.get("type") + "/" + map.get("kind"), innerJsonObjectBuilder.build());
            }
            long latencyInMs = receiverTimestamp - senderTimestamp;
            jsonObjectBuilder.add("latencyInMs", latencyInMs);
            JsonObject jsonObject = jsonObjectBuilder.build();
            reporter.jsonAttachment(
                    this.report, "RTCStats_" + i,
                    jsonObject);
            if (i % 20 == 0) {
                logger.info("stat_" + i + " successfully collected");
            }
            waitAround(2000);
        }
    }
}
