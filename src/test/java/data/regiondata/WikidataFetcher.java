package data.regiondata;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public final class WikidataFetcher {
    private WikidataFetcher() {}

    private static final HttpClient client = HttpClient.newHttpClient();

    public static HttpResponse<String> fetch(String wikidataId) {
        String query =
                "SELECT ?area ?elevation ?population ?coordinate ?typeLabel ?countryLabel ?nativeName WHERE {" +
                        "OPTIONAL { wd:" + wikidataId + " wdt:P2046 ?area . }" +
                        "OPTIONAL { wd:" + wikidataId + " wdt:P1082 ?population . }" +
                        //"OPTIONAL { wd:" + wikidataId + " wdt:P1705 ?nativeName . }" +
                        "} LIMIT 1";
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            URI uri = URI.create("https://query.wikidata.org/sparql?query=" + encodedQuery + "&format=json");
            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(uri)
                                             .header("User-Agent", "Faerite")
                                             .GET()
                                             .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e) {
            System.err.println("Failed to fetch data for " + wikidataId);
            return null;
        }
    }
}
