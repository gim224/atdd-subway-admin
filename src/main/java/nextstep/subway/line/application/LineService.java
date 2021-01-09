package nextstep.subway.line.application;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Map<Long, Station> stations = stationRepository
            .findAllByIdIn(request.getUpStationId(), request.getDownStationId())
            .collect(Collectors.toMap(Station::getId, Function.identity()));

        Station upStation = stations.get(request.getUpStationId());
        Station downStation = stations.get(request.getDownStationId());

        Line persistLine = lineRepository.save(request.toLine(upStation, downStation));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
            .stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

	public LineResponse findOne(Long id) {
        return lineRepository.findById(id)
            .map(LineResponse::of)
            .orElseThrow(() -> new NoSuchElementException("주어진 id를 가지는 Line을 찾을 수 없습니다."));
	}

    public void update(Long id, LineRequest updated) {
        lineRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("주어진 id를 가지는 Line을 찾을 수 없습니다."))
            .update(updated.toLine());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}