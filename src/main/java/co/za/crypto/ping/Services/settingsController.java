package co.za.crypto.ping.Services;

import co.za.crypto.ping.ValueHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
public class settingsController {

    @RequestMapping(method = RequestMethod.GET)
    public SettingsEntity getSettings(){
        return ValueHolder.getInstance().getSettingsEntity();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void setSettings(@RequestBody SettingsEntity settings){
        ValueHolder.getInstance().setSettingsEntity(settings);
    }

}
