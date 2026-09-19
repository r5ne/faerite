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

    private static final String QUERY_TEMPLATE = """
        SELECT\s
          ?area\s
          ?population\s
          ?coordinate\s
          ?highestPoint\s
          ?highestPointLabel\s
          ?elevationQualifier
          (GROUP_CONCAT(CONCAT(LANG(?nativeName), ":", STR(?nativeName)); separator=" | ") AS ?nativeNamesList)
        WHERE {
          OPTIONAL { wd:%1$s wdt:P2046 ?area. }
          OPTIONAL { wd:%1$s wdt:P1082 ?population. }
          OPTIONAL { wd:%1$s wdt:P625 ?coordinate. }
          OPTIONAL { wd:%1$s wdt:P1705 ?nativeName. }\s
          OPTIONAL {\s
            wd:%1$s wdt:P610 ?highestPoint .         \s
            ?highestPoint wdt:P2044 ?elevationQualifier .\s
          }
          SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }
        }
        GROUP BY ?area ?population ?coordinate ?highestPoint ?highestPointLabel ?elevationQualifier
        """;
    private static final String WIKIDATA_URL_TEMPLATE = "https://query.wikidata.org/sparql?query=%s&format=json";

    private WikidataFetcher() {}

    public static JsonNode fetch(String wikidataId) {
        try {
            String query = String.format(QUERY_TEMPLATE, wikidataId);
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
            System.err.println("Failed to fetch data for " + wikidataId);
            return null;
        }
        return null;
    }
}
