package data.regiondata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public final class ApiFetcher {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String WIKIDATA_URL_TEMPLATE = "https://query.wikidata.org/sparql?query=%s&format=json";
    private static final String MAPRESSO_URL_TEMPLATE = "https://climate.mapresso.com/api/koeppen/?lat=%f&lon=%f";

    private ApiFetcher() {}

    public static JsonNode fetchWikidata(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = String.format(WIKIDATA_URL_TEMPLATE, encodedQuery);

            HttpResponse<String> response = getHttpResponse(url);

            if (response.statusCode() == 200) {
                JsonNode bindings = mapper.readTree(response.body()).path("results").path("bindings");
                if (bindings.isArray() && !bindings.isEmpty()) {
                    return bindings.get(0);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch data for " + query);
            return null;
        }
        return null;
    }

    public static JsonNode fetchClimateData(double latitude, double longitude) {
        try {
            String url = String.format(MAPRESSO_URL_TEMPLATE, latitude, longitude);
            HttpResponse<String> response = getHttpResponse(url);

            if (response.statusCode() == 200) {
                JsonNode bindings = mapper.readTree(response.body()).path("data");
                if (bindings.isArray() && !bindings.isEmpty()) {
                    return bindings;
                }
            }
        } catch (Exception e) {
            System.err.println(
                "Failed to fetch data for " + String.format("Latitude: %f, Longitude: %f", latitude, longitude)
            );
            return null;
        }
        return null;
    }

    private static HttpResponse<String> getHttpResponse(String url) throws IOException, InterruptedException {
        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("User-Agent", "Faerite").GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
