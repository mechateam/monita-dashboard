package ta.simonita.monita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ta.simonita.monita.model.FaskesModel;

public interface FaskesDb extends JpaRepository<FaskesModel, Long> {
    FaskesModel findByUsername(String username);

    FaskesModel findByEmail(String email);

    FaskesModel findByResetPasswordToken(String resetToken);

}
