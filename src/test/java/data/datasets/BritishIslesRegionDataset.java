package data.datasets;

import data.regiondata.RegionDataBuilderConfig;
import faerite.model.RegionType;
import java.util.Map;
import java.util.Set;

public class BritishIslesRegionDataset implements RegionDataset {

    @Override
    public Set<RegionDataBuilderConfig> getRegionData() {
        return Set.of(
            new RegionDataBuilderConfig("British Isles", "Q38272", b -> b.keepNativeNames("cy", "ga", "gd", "sco")),

            new RegionDataBuilderConfig("Great Britain", "Q23666"),
            new RegionDataBuilderConfig("Isle of Wight", "Q9679"),

            new RegionDataBuilderConfig("Ireland", "Q22890"),

            new RegionDataBuilderConfig("Isle of Man", "Q9676", b -> b.type(RegionType.ISLAND_GROUP).area(570)),
            new RegionDataBuilderConfig("isle-of-man-island", "Isle of Man", "Q27508141", b ->
                b.population(84521).area(570)
            ),
            new RegionDataBuilderConfig("Calf of Man", "Q125389", b ->
                b.highestElevation(128).nativeNames(Map.of("Manx", "Yn Cholloo"))
            ),

            new RegionDataBuilderConfig("Channel Islands", "Q42314", b ->
                b.nativeNames(Map.of("French", "îles Anglo-Normandes"))
            ),
            new RegionDataBuilderConfig("Jersey", "Q15706498", b -> b.type(RegionType.ISLAND_GROUP)),
            new RegionDataBuilderConfig("Guernsey", "Q3311985", b -> b.type(RegionType.ISLAND_GROUP)),
            new RegionDataBuilderConfig("Alderney", "Q179313", b ->
                b
                    .type(RegionType.ISLAND_GROUP)
                    .highestElevation(90)
                    .nativeNames(Map.of("Auregnais", "Aoeur'gny", "French", "Aurigny"))
            ),
            new RegionDataBuilderConfig("Chausey", "Q292600", b ->
                b.population(30).area(1.825).nativeNames(Map.of("French", "îles Chausey")).highestElevation(24)
            ),
            new RegionDataBuilderConfig("Sark", "Q3405693", b -> b.type(RegionType.ISLAND_GROUP)),
            new RegionDataBuilderConfig("Herm", "Q202023", b -> b.type(RegionType.ISLAND_GROUP)),
            new RegionDataBuilderConfig("Jethou", "Q898856", b -> b.type(RegionType.ISLAND_GROUP)),
            new RegionDataBuilderConfig("les-ecrehous", "Les Écrehous", "Q776075", b ->
                b.type(RegionType.ISLAND_GROUP).area(0.2)
            ),
            new RegionDataBuilderConfig("Les Minquiers", "Q1435852", b -> b.type(RegionType.ISLAND_GROUP).area(0.1)),
            new RegionDataBuilderConfig("Les Casquets", "Q1048187", b -> b.area(0.072)),

            new RegionDataBuilderConfig("Isles of Scilly", "Q180209"),
            new RegionDataBuilderConfig("Shetland", "Q47134"),
            new RegionDataBuilderConfig("Orkney", "Q100166")
        );
    }
}
