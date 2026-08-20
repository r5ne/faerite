package faerite.model;

public record RegionSelectionModel(RegionData regionData, int maskColor, MapModel subMap) implements RegionModel {}
