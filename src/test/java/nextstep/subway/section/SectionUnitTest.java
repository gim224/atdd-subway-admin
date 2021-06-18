package nextstep.subway.section;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

@DisplayName("구간 단위테스트")
public class SectionUnitTest {
	private Station 서울역;
	private Station 광명역;
	private Station 대전역;
	private Station 동대구역;
	private Station 부산역;

	@BeforeEach
	void setUp() {
		서울역 = new Station("서울역");
		광명역 = new Station("광명역");
		대전역 = new Station("대전역");
		동대구역 = new Station("동대구역");
		부산역 = new Station("부산역");
	}

	@Test
	@DisplayName("하나의 역만 같은 경우인지 체크하는 로직 검증")
	void matchedOnlyOneStation() {
		Section 서울대전구간 = new Section(서울역, 대전역, 10);
		Section 서울광명구간 = new Section(서울역, 광명역, 2);
		Section 광명동대구역구간 = new Section(광명역, 동대구역, 2);
		Section 광명대전구간 = new Section(광명역, 대전역, 2);

		assertThat(서울대전구간.matchedOnlyOneStation(서울광명구간)).isTrue();
		assertThat(서울대전구간.matchedOnlyOneStation(광명동대구역구간)).isFalse();
		assertThat(서울대전구간.matchedOnlyOneStation(광명대전구간)).isTrue();
	}

	@Test
	@DisplayName("상행부터 하행까지 정렬기능 검증")
	void stationsFromUpToDown() {
		Section 서울광명구간 = new Section(서울역, 광명역, 2);
		Section 광명대전구간 = new Section(광명역, 대전역, 2);
		Section 대전동대구구간 = new Section(대전역, 동대구역, 2);
		Section 동대구부산구간 = new Section(동대구역, 부산역, 2);

		Sections sections = new Sections(Arrays.asList(광명대전구간, 동대구부산구간, 대전동대구구간, 서울광명구간));
		List<Station> stations = sections.stationsFromUpToDown();

		assertThat(stations).extracting("name").containsExactly("서울역", "광명역", "대전역", "동대구역", "부산역");
	}
}
