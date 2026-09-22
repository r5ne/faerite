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
            new RegionDataBuilderConfig("Isle of Wight", "Q9679", b ->
                b
                    .nativeNames(Map.of("Welsh", "Ynys Wyth"))
                    .highestElevation(242)
                    .highestElevationName("St Bonafice Down")
            ),

            new RegionDataBuilderConfig("Ireland", "Q22890"),

            new RegionDataBuilderConfig("Isle of Man", "Q9676", b ->
                b.type(RegionType.ISLAND_GROUP).area(570).addAllNativeNames("Welsh", "Manaw")
            ),
            new RegionDataBuilderConfig("isle-of-man-island", "Isle of Man", "Q27508141", b ->
                b
                    .population(84521)
                    .area(570)
                    .highestElevation(621)
                    .highestElevationName("Snaefell")
                    .nativeNames(Map.of("Manx", "Mannin", "Welsh", "Manaw"))
            ),
            new RegionDataBuilderConfig("Calf of Man", "Q125389", b ->
                b.highestElevation(126).nativeNames(Map.of("Manx", "Yn Cholloo"))
            ),

            new RegionDataBuilderConfig("Channel Islands", "Q42314", b ->
                b.nativeNames(Map.of("French", "îles Anglo-Normandes"))
            ),
            new RegionDataBuilderConfig("Jersey", "Q15706498", b ->
                b
                    .type(RegionType.ISLAND_GROUP)
                    .nativeNames(Map.of("Jèrriais", "Jèrri"))
                    .highestElevation(136)
                    .highestElevationName("Les Platons")
            ),
            new RegionDataBuilderConfig("Guernsey", "Q3311985", b ->
                b
                    .type(RegionType.ISLAND_GROUP)
                    .nativeNames(Map.of("Guernésiais", "Guernési"))
                    .highestElevation(110)
                    .highestElevationName("Hautnez")
            ),
            new RegionDataBuilderConfig("Alderney", "Q179313", b ->
                b
                    .type(RegionType.ISLAND_GROUP)
                    .highestElevation(90)
                    .nativeNames(Map.of("Auregnais", "Aoeur'gny", "French", "Aurigny"))
            ),
            new RegionDataBuilderConfig("Chausey", "Q292600", b ->
                b
                    .population(30)
                    .area(1.825)
                    .nativeNames(Map.of("French", "îles Chausey"))
                    .highestElevation(22)
                    .highestElevationName("Grande-Île (Island)")
            ),
            new RegionDataBuilderConfig("Sark", "Q3405693", b ->
                b.type(RegionType.ISLAND_GROUP).nativeNames(Map.of("French", "Sercq", "Sercquiais", "Sèr"))
            ),
            new RegionDataBuilderConfig("Herm", "Q202023", b ->
                b.type(RegionType.ISLAND_GROUP).nativeNames(Map.of("Guernésiais", "Haerme")).highestElevation(61)
            ),
            new RegionDataBuilderConfig("Jethou", "Q898856", b ->
                b.type(RegionType.ISLAND_GROUP).nativeNames(Map.of("French", "Jéthou")).highestElevation(63)
            ),
            new RegionDataBuilderConfig("les-ecrehous", "Les Écréhous", "Q776075", b ->
                b.type(RegionType.ISLAND_GROUP).area(0.2).nativeNames(Map.of("Jèrriais", "Êcrého")).highestElevation(5)
            ),
            new RegionDataBuilderConfig("Les Minquiers", "Q1435852", b ->
                b
                    .type(RegionType.ISLAND_GROUP)
                    .area(0.1)
                    .nativeNames(Map.of("English", "The Minkies", "Jèrriais", "Les Mîntchièrs"))
            ),
            new RegionDataBuilderConfig("Les Casquets", "Q1048187", b -> b.area(0.072).highestElevation(4)),

            new RegionDataBuilderConfig("Isles of Scilly", "Q180209", b ->
                b
                    .nativeNames(Map.of("Cornish", "Syllan", "Old Norse", "Syllingar"))
                    .highestElevation(51)
                    .highestElevationName("Telegraph")
            ),
            new RegionDataBuilderConfig("Shetland", "Q47134", b ->
                b.keepNativeNames("gd").addAllNativeNames("Norn", "Hjaltland", "Old Norse", "Hjaltland")
            ),
            new RegionDataBuilderConfig("Orkney", "Q100166", b ->
                b.addAllNativeNames("Norn", "Orknøjar", "Old Norse", "Orkneyjar")
            )
        );
    }
}
