package data.regiondata;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public final class WikidataFetcher {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static HttpResponse<String> fetch(String wikidataId) {
        String query = """
        SELECT\s
          ?area\s
          ?population\s
          ?coordinate\s
          ?highestPoint\s
          ?highestPointLabel\s
          ?elevationQualifier
          # This merges all native names into a single string separated by " | "
          (GROUP_CONCAT(CONCAT(LANG(?nativeName), ":", STR(?nativeName)); separator=" | ") AS ?nativeNamesList)
        WHERE {

          # 1. Base Properties
          OPTIONAL { wd:Q38272 wdt:P2046 ?area. }
          OPTIONAL { wd:Q38272 wdt:P1082 ?population. }
          OPTIONAL { wd:Q38272 wdt:P625 ?coordinate. }
          OPTIONAL { wd:Q38272 wdt:P1705 ?nativeName. }\s

          # 2. High Point Properties
          OPTIONAL {\s
            wd:Q38272 wdt:P610 ?highestPoint .         \s
            ?highestPoint wdt:P2044 ?elevationQualifier .\s
          }

          # 3. Label Service
          SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }
        }
        # Group by every single non-aggregated column to compress the duplicates
        GROUP BY ?area ?population ?coordinate ?highestPoint ?highestPointLabel ?elevationQualifier
        """;
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
