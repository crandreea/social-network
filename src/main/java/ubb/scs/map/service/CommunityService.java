package ubb.scs.map.service;

import java.util.*;

public class CommunityService {
    private final NetworkService networkService;
    private Map<Long, List<Long>> adjList;

    private final List<Long> longestPathNodes = new ArrayList<>();
    private int longestPathLength;

    public CommunityService(NetworkService networkService) {
        this.networkService = networkService;
        longestPathLength = 0;
    }

    public void refresh(){
        buildAdjacencyList();
    }

    private void buildAdjacencyList() {
        adjList = new HashMap<>();
        networkService.getAllPrietenii().forEach(prietenie -> {
            adjList.computeIfAbsent(prietenie.getIdUser1(), k -> new ArrayList<>()).add(prietenie.getIdUser2());
            adjList.computeIfAbsent(prietenie.getIdUser2(), k -> new ArrayList<>()).add(prietenie.getIdUser1());
        });
    }

    private void DFS(Long nodeId, Set<Long> visited, List<Long> component) {
        refresh();

        visited.add(nodeId);
        component.add(nodeId);
        adjList.getOrDefault(nodeId, Collections.emptyList()).forEach(neighbor -> {
            if (!visited.contains(neighbor)) DFS(neighbor, visited, component);
        });
    }

    public int connectedCommunities() {
        refresh();

        Set<Long> visited = new HashSet<>();
        int count = 0;
        for (Long nodeId : adjList.keySet()) {
            if (!visited.contains(nodeId)) {
                List<Long> component = new ArrayList<>();
                DFS(nodeId, visited, component);
                count++;
            }
        }
        return count;
    }

    private Long[] bfsFarthestNode(Long start, Map<Long, Long> parentMap) {
        refresh();

        Map<Long, Boolean> visited = new HashMap<>();
        Map<Long, Integer> distance = new HashMap<>();
        Queue<Long> queue = new LinkedList<>();

        queue.add(start);
        visited.put(start, true);
        distance.put(start, 0);
        parentMap.put(start, null);

        Long farthestNode = start;

        while (!queue.isEmpty()) {
            Long node = queue.poll();

            for (Long neighbor : adjList.getOrDefault(node, new ArrayList<>())) {
                if (!visited.getOrDefault(neighbor, false)) {
                    visited.put(neighbor, true);
                    queue.add(neighbor);
                    distance.put(neighbor, distance.get(node) + 1);
                    parentMap.put(neighbor, node);

                    if (distance.get(neighbor) > distance.get(farthestNode)) {
                        farthestNode = neighbor;
                    }
                }
            }
        }

        return new Long[]{farthestNode, (long) distance.get(farthestNode)};
    }


    public void findLongestPathInGraph() {
        refresh();

        Set<Long> visitedComponents = new HashSet<>();

        for (Long node : adjList.keySet()) {
            if (!visitedComponents.contains(node)) {
                Map<Long, Long> parentMap1 = new HashMap<>();
                Long[] firstBFS = bfsFarthestNode(node, parentMap1);

                Map<Long, Long> parentMap2 = new HashMap<>();
                Long[] secondBFS = bfsFarthestNode(firstBFS[0], parentMap2);

                if (secondBFS[1] > longestPathLength) {
                    longestPathLength = secondBFS[1].intValue();
                    reconstructPath(firstBFS[0], secondBFS[0], parentMap2);
                }

                markComponentAsVisited(node, visitedComponents);
            }
        }
    }

    private void markComponentAsVisited(Long start, Set<Long> visitedComponents) {
        refresh();

        Queue<Long> queue = new LinkedList<>();
        queue.add(start);
        visitedComponents.add(start);

        while (!queue.isEmpty()) {
            Long node = queue.poll();
            for (Long neighbor : adjList.getOrDefault(node, new ArrayList<>())) {
                if (!visitedComponents.contains(neighbor)) {
                    visitedComponents.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
    }

    private void reconstructPath(Long start, Long end, Map<Long, Long> parentMap) {
        refresh();

        longestPathNodes.clear();
        Long current = end;
        while (current != null) {
            longestPathNodes.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(longestPathNodes);
    }


    public String mostSocialCommunity() {
        refresh();

        findLongestPathInGraph();
        return longestPathLength + " " + longestPathNodes;
    }

}
