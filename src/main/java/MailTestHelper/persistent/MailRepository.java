package MailTestHelper.persistent;

import MailTestHelper.persistent.MailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kenji on 2016/07/04.
 */
@Repository
public interface MailRepository extends JpaRepository<MailEntity, String> {
}
