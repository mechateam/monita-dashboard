package ta.simonita.monita.service;

import ta.simonita.monita.model.BalitaModel;

import java.util.Date;
import java.util.List;

public interface BalitaService {
    List<BalitaModel> getAllBalita();
    List<String> diagnosisPerhatian(Date date);
}
