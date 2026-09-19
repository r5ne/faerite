package data.datasets;

import data.regiondata.RegionDataBuilderConfig;
import java.util.Set;

public class BritishIslesRegionDataset implements RegionDataset {

    @Override
    public Set<RegionDataBuilderConfig> getRegionData() {
        return Set.of(
            new RegionDataBuilderConfig("British Isles", "Q38272"),

            new RegionDataBuilderConfig("Great Britain", "Q23666"),
            new RegionDataBuilderConfig("Isle of Wight", "Q9679"),

            new RegionDataBuilderConfig("Ireland", "Q22890"),

            new RegionDataBuilderConfig("Isle of Man", "Q9676", b -> b.area(570)),
            new RegionDataBuilderConfig("isle-of-man-island", "Isle of Man", "Q27508141", b ->
                b.population(84521).area(570)
            ),
            new RegionDataBuilderConfig("Calf of Man", "Q125389"),

            new RegionDataBuilderConfig("Channel Islands", "Q42314"),
            new RegionDataBuilderConfig("Jersey", "Q15706498"),
            new RegionDataBuilderConfig("Guernsey", "Q3311985"),
            new RegionDataBuilderConfig("Alderney", "Q179313"),
            new RegionDataBuilderConfig("Chausey", "Q292600", b -> b.population(30).area(1.825)),
            new RegionDataBuilderConfig("Sark", "Q3405693"),
            new RegionDataBuilderConfig("Herm", "Q202023"),
            new RegionDataBuilderConfig("Jethou", "Q898856"),
            new RegionDataBuilderConfig("les-ecrehous", "Les Écrehous", "Q776075", b -> b.area(0.2)),
            new RegionDataBuilderConfig("Les Minquiers", "Q1435852", b -> b.area(0.1)),
            new RegionDataBuilderConfig("Les Casquets", "Q1048187", b -> b.area(0.072)),

            new RegionDataBuilderConfig("Isles of Scilly", "Q180209"),
            new RegionDataBuilderConfig("Shetland", "Q47134"),
            new RegionDataBuilderConfig("Orkney", "Q100166")
        );
    }
}
