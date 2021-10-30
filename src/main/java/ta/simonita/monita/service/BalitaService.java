package ta.simonita.monita.service;
import ta.simonita.monita.model.PertumbuhanBalitaModel;
import ta.simonita.monita.model.UserModel;
import ta.simonita.monita.model.BalitaModel;
import ta.simonita.monita.model.UserModel;

import java.util.Date;
import java.util.List;

public interface BalitaService {
    List<Integer> diagnosisPerhatian(Integer year, Integer month, List<UserModel> listUser);
    List<BalitaModel> getAllBayiFromSameKelurahan(List<UserModel> listUser);
    List<BalitaModel> getAllBayiFromSameRT(List<BalitaModel> listBalitaKelurahan,Integer rt);
    List<BalitaModel> getAllBayiFromSameRW(List<BalitaModel> listBalitaKelurahan,Integer rw);
    List<BalitaModel> getAllBayiFromSameRTdanRW(List<BalitaModel> listBalitaKelurahan,Integer rt,Integer rw);
    List<String> calculateUmurBalita(List<BalitaModel> listBalita);
    List<List<BalitaModel>> getAllBalitaPerhatian(Integer year, Integer month, List<UserModel> listUser);
    BalitaModel getBalitaById(Long id);
    List<String[]> getStatusPertumbuhan(BalitaModel balita);
    List<Integer> listYearFilter(List<UserModel> listUser);
    String getMonth(Integer month);
}
