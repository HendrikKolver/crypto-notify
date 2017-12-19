package co.za.crypto.ping;

import co.za.crypto.ping.Services.SettingsEntity;

public class ValueHolder {
    private static ValueHolder ourInstance = new ValueHolder();
    private SettingsEntity settingsEntity;

    public static ValueHolder getInstance() {
        return ourInstance;
    }

    private ValueHolder() {
        settingsEntity = new SettingsEntity(1.0, 60000);
    }

    public SettingsEntity getSettingsEntity() {
        return settingsEntity;
    }

    public void setSettingsEntity(SettingsEntity settingsEntity) {
        this.settingsEntity = settingsEntity;
    }
}
