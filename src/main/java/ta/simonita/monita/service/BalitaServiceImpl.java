package ta.simonita.monita.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ta.simonita.monita.model.BalitaModel;
import ta.simonita.monita.repository.BalitaDb;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BalitaServiceImpl implements BalitaService {
    @Autowired
    BalitaDb balitaDb;

    @Override
    public List<BalitaModel> getAllBalita() {
        return balitaDb.findAll();
    }

    @Override
    public List<String> diagnosisPerhatian(Date date) {
        List<String> list = new ArrayList<String>();
        int count1; int count2; int count3; int count4;
        for(BalitaModel balita : this.getAllBalita()) {
            for(int i=0; i<balita.getListPertumbuhan().size(); i++) {


            }
        }
        return list;
    }
}
