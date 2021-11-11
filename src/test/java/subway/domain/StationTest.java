package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StationTest {

    @Autowired
    private StationRepository stations;

    @Test
    void save() {
        final Station station = new Station("잠실역");
        final Station actual = stations.save(station);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo("잠실역")
        );
    }

    @Test
    void findByName() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findByName("잠실역");
        assertAll(
            () -> assertThat(station2.getId()).isEqualTo(station1.getId()),
            () -> assertThat(station2.getName()).isEqualTo(station1.getName()),
            () -> assertThat(station1).isEqualTo(station1),
            () -> assertThat(station1).isSameAs(station1)
        );
    }

    // ID 기반으로 조회 시 1차 캐시 효과를 볼 수 있다; SELECT 쿼리 x
    @Test
    void findById() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findById(station1.getId()).get();
        assertAll(
            () -> assertThat(station2.getId()).isEqualTo(station1.getId()),
            () -> assertThat(station2.getName()).isEqualTo(station1.getName()),
            () -> assertThat(station1).isEqualTo(station1),
            () -> assertThat(station1).isSameAs(station1)
        );
    }

    @Test
    void update() {
        final Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        final Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }
}
