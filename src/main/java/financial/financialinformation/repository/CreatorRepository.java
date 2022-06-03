package financial.financialinformation.repository;

import financial.financialinformation.domain.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, Long> {

    Optional<Creator> findByCreatorName(String creatorName);

    Optional<List<Creator>> findByChannelId(Long channelId);

}
