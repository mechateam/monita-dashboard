package ta.simonita.monita.service;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "listkelurahan")
public class ServerProperties {

    private List<String> kelurahan;

    public List<String> getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(List<String> kelurahan) {
        this.kelurahan = kelurahan;
    }
}
