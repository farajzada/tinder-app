package az.edu.turing.tinderapp.domain.repository;

import az.edu.turing.tinderapp.domain.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            Long sender1, Long receiver1, Long sender2, Long receiver2);
}
