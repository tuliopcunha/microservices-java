package academy.devdojo.youtube.core.repository;

import academy.devdojo.youtube.core.model.ApplicationUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long> {

    public ApplicationUser findByUsername(String username);
}
