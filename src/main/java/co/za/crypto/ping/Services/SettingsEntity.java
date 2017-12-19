package co.za.crypto.ping.Services;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SettingsEntity {
    @Id
    @GeneratedValue
    private Long id;

    private double difference;
    private long refreshTime;

    public SettingsEntity() {
    }

    public SettingsEntity(double difference, long refreshTime) {
        this.difference = difference;
        this.refreshTime = refreshTime;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }
}
