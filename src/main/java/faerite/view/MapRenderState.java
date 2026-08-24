package faerite.view;

import java.awt.image.BufferedImage;

public record MapRenderState(BufferedImage mapImage, BufferedImage hoveredBorder, BufferedImage SelectedBorder) {}
