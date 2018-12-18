package stenden.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import stenden.spring.data.model.Track;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
