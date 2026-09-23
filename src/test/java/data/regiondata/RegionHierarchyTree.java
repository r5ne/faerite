package data.regiondata;

import java.util.*;

public class RegionHierarchyTree {
    private final Map<String, List<String>> regionChildrenMap = new HashMap<>();
    private final Set<String> regions = new HashSet<>();

    public void addRegionRelationship(String parentRegionId, String childRegionId) {
        regionChildrenMap.computeIfAbsent(parentRegionId, _ -> new ArrayList<>()).add(childRegionId);
        regions.add(parentRegionId);
        regions.add(childRegionId);
    }

    public List<String> bottomUpTraversal() {
        List<String> traversalOrder = new ArrayList<>();
        Set<String> visitedRegions = new HashSet<>();

        Set<String> leaves = new HashSet<>(regionChildrenMap.keySet());
        regionChildrenMap.values().forEach(leaves::removeAll);

        for (String leaf : leaves) {
            postOrderDepthFirstSearch(leaf, visitedRegions, traversalOrder);
        }
        return traversalOrder;
    }

    private void postOrderDepthFirstSearch(String root, Set<String> visitedNodes, List<String> nodeOrder) {
        if (!visitedNodes.add(root)) return;

        List<String> children = regionChildrenMap.getOrDefault(root, Collections.emptyList());
        for (String child : children) {
            postOrderDepthFirstSearch(child, visitedNodes, nodeOrder);
        }
        nodeOrder.add(root);
    }

    public List<String> getChildren(String parentId) {
        return regionChildrenMap.getOrDefault(parentId, Collections.emptyList());
    }

    public boolean isLeaf(String nodeId) {
        return !regionChildrenMap.containsKey(nodeId) || regionChildrenMap.get(nodeId).isEmpty();
    }
}
