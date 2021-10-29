package ta.simonita.monita.service;

import ta.simonita.monita.model.FaskesModel;

public interface FaskesService {

    void addFaskes(FaskesModel faskes);

    String updateResetPasswordToken(String token, String email);

    FaskesModel getFaskesByResetToken(String token);
    FaskesModel getFaskesByUsername(String username);

    void updatePassword(FaskesModel faskes,String newPassword);
    void changeFaskes(FaskesModel faskes);
}
