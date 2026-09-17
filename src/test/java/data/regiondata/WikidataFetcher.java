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
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            URI uri = URI.create("https://query.wikidata.org/sparql?query=" + encodedQuery + "&format=json");
            HttpRequest request = HttpRequest.newBuilder().uri(uri).header("User-Agent", "Faerite").GET().build();
            System.out.println("Creating request: " + request + ", URI: " + uri);
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("Failed to fetch data for " + wikidataId);
            return null;
        }
    }
}
