package data.datasets;

import faerite.model.RegionSelectionModel;

import java.util.Set;
import data.mapdata.RegionBuilder;

public class ChannelIslandsDataset implements MapDataset {

    @Override
    public String id() {
        return "channel-islands";
    }

    @Override
    public Set<RegionSelectionModel> buildRegions() {
        return Set.of(
            new RegionBuilder("jersey", 0xff000000).build(),
            new RegionBuilder("guernsey", 0xffffffff).build(),
            new RegionBuilder("alderney", 0xffff0000).build(),
            new RegionBuilder("chausey", 0xffffff00).build(),

            new RegionBuilder("sark", 0xff55c449).build(),
            new RegionBuilder("herm", 0xffffffb6).build(),
            new RegionBuilder("jethou", 0xff36982b).build(),

            new RegionBuilder("les-ecrehous", 0xff00ffff).build(),
            new RegionBuilder("les-minquiers", 0xff0000ff).build(),

            new RegionBuilder("les-casquets", 0xff343434).build()
        );
    }
}
