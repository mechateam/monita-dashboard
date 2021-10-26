package ta.simonita.monita.service;

import ta.simonita.monita.model.BalitaModel;
import ta.simonita.monita.model.UserModel;

import java.util.List;

public interface BalitaService {

    List<BalitaModel> getAllBayiFromSameKelurahan(List<UserModel> listUser);
    List<BalitaModel> getAllBayiFromSameRT(List<BalitaModel> listBalitaKelurahan,Integer rt);
    List<BalitaModel> getAllBayiFromSameRW(List<BalitaModel> listBalitaKelurahan,Integer rw);
    List<BalitaModel> getAllBayiFromSameRTdanRW(List<BalitaModel> listBalitaKelurahan,Integer rt,Integer rw);

    List<String> calculateUmurBalita(List<BalitaModel> listBalita);
}
