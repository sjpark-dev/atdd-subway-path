package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, wooteco.subway.line.dao.LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long sourceId, Long targetId) {
        GraphPath graphPath = graphPath(sourceId, targetId);
        List<Station> shortestPath = graphPath.getVertexList();
        int distance = (int) graphPath.getWeight();
        return new PathResponse(StationResponse.listOf(shortestPath), distance);
    }

    private GraphPath graphPath(Long sourceId, Long targetId) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = graphFromLines();
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(
                stationDao.findById(sourceId),
                stationDao.findById(targetId)
        );
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> graphFromLines() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVerticesToGraph(graph);
        setEdgeWeights(graph);
        return graph;
    }

    private void setEdgeWeights(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Line> lines = lineDao.findAll();
        for (Line line : lines) {
            setEdgeWeightsForLine(graph, line.getSections());
        }
    }

    private void setEdgeWeightsForLine(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance()
            );
        }
    }

    private void addVerticesToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Station> stations = stationDao.findAll();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }
}