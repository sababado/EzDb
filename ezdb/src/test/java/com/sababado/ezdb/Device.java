package com.sababado.ezdb;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by robert on 8/31/16.
 */
@TableName("Device")
class Device extends DbRecord {
    public static final String DEVICE_TOKEN = "deviceToken";
    public static final String LAST_NOTIFICATION_FAIL = "lastNotificationFail";
    public static final String KEEP_ALIVE = "keepAlive";

    @Column(ID)
    private long id;
    @Column(DEVICE_TOKEN)
    private String deviceToken;
    @Column(LAST_NOTIFICATION_FAIL)
    private long lastNotificationFail;
    @Column(value = KEEP_ALIVE, ignoreInQueryGenerator = true)
    private long keepAlive;

    public Device(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Device(ResultSet resultSet) throws SQLException {
        this(resultSet, false);
    }

    public Device(ResultSet resultSet, boolean fromJoin) throws SQLException {
        super(resultSet);
        id = resultSet.getLong(fromJoin ? DbHelper.getFkColumnName(ID, "Device") : ID);
        deviceToken = resultSet.getString(DEVICE_TOKEN);
        Timestamp timestamp = resultSet.getTimestamp(LAST_NOTIFICATION_FAIL);
        if (timestamp != null) {
            lastNotificationFail = timestamp.getTime();
        }
        keepAlive = resultSet.getTimestamp(KEEP_ALIVE).getTime();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public long getLastNotificationFail() {
        return lastNotificationFail;
    }

    public void setLastNotificationFail(long lastNotificationFail) {
        this.lastNotificationFail = lastNotificationFail;
    }

    public long getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(long keepAlive) {
        this.keepAlive = keepAlive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        if (id != device.id) return false;
        if (lastNotificationFail != device.lastNotificationFail) return false;
        if (keepAlive != device.keepAlive) return false;
        return deviceToken != null ? deviceToken.equals(device.deviceToken) : device.deviceToken == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (deviceToken != null ? deviceToken.hashCode() : 0);
        result = 31 * result + (int) (lastNotificationFail ^ (lastNotificationFail >>> 32));
        result = 31 * result + (int) (keepAlive ^ (keepAlive >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", deviceToken='" + deviceToken + '\'' +
                ", lastNotificationFail=" + lastNotificationFail +
                ", keepAlive=" + keepAlive +
                "} " + super.toString();
    }
}
