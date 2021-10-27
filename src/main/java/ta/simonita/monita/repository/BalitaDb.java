package ta.simonita.monita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ta.simonita.monita.model.BalitaModel;

public interface BalitaDb extends JpaRepository<BalitaModel, Long> {
}
