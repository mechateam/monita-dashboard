package ta.simonita.monita.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ta.simonita.monita.model.FaskesModel;
import ta.simonita.monita.model.UserModel;
import ta.simonita.monita.repository.FaskesDb;

import javax.transaction.Transactional;

@Service
@Transactional
public class FaskesServiceImpl implements FaskesService {

    @Autowired
    FaskesDb faskesDb;

    @Override
    public void addFaskes(FaskesModel faskes){
        String pass = encrypt(faskes.getPassword());
        faskes.setPassword(pass);
        faskesDb.save(faskes);
    }

    public String encrypt(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

//    @Override
//    public String updateResetPasswordToken(String token, String email) {
//        UserModel user = userDb.findByEmail(email);
//        if (user != null) {
//            user.setResetPasswordToken(token);
//            userDb.save(user);
//            return "token_success";
//        } else {
//            return null;
//        }
//    }
}
