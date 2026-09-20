package data.regiondata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public final class WikidataFetcher {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String WIKIDATA_URL_TEMPLATE = "https://query.wikidata.org/sparql?query=%s&format=json";

    private WikidataFetcher() {}

    public static JsonNode fetch(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = String.format(WIKIDATA_URL_TEMPLATE, encodedQuery);
            URI uri = URI.create(url);

            HttpRequest request = HttpRequest.newBuilder().uri(uri).header("User-Agent", "Faerite").GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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
}
