package ta.simonita.monita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ta.simonita.monita.model.FaskesModel;

import java.util.List;

@Repository
public interface FaskesDb extends JpaRepository<FaskesModel, Long> {
    FaskesModel findByUsername(String username);

    FaskesModel findByEmail(String email);

    FaskesModel findByResetPasswordToken(String resetToken);



}
