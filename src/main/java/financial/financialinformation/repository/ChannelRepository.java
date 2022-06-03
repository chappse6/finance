package financial.financialinformation.repository;

import financial.financialinformation.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Optional<Channel> findByChannelName(String channelName);

}
