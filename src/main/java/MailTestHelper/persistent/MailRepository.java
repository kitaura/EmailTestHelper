package MailTestHelper.persistent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kenji on 2016/07/04.
 */
@Repository
public interface MailRepository extends JpaRepository<MailEntity, String> {
    Page<MailEntity> findAll(Pageable pageabl);
    Page<MailEntity> findBySearchKeyContains(String searchKey, Pageable pageabl);
}
