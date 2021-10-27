package ta.simonita.monita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ta.simonita.monita.model.UserModel;

import java.util.List;

@Repository
public interface UserDb extends JpaRepository<UserModel, String> {

    List<UserModel> findAllByKelurahan(String kelurahan);
}
