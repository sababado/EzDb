package com.sababado.ezdb.models;

import com.sababado.ezdb.Column;
import com.sababado.ezdb.DbRecord;
import com.sababado.ezdb.Fk;
import com.sababado.ezdb.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by robert on 9/1/16.
 */
@TableName(value = "PubDevices", joinTable = "Device,Pub")
public class PubDevices extends DbRecord {
    public static final String PUB_DEVICES_ID = "PubDevices." + ID;
    public static final String DEVICE_ID = "deviceId";
    public static final String PUB_ID = "pubId";

    @Column(ID)
    private long id;
    @Fk
    private Device device;
    @Fk
    private Pub pub;

    public PubDevices() {

    }

    public PubDevices(ResultSet resultSet) throws SQLException {
        id = resultSet.getLong(ID);
        device = new Device(resultSet, true);
        pub = new Pub(resultSet, true);
    }

    public static String getInsertQuery() {
        return getInsertQuery(PubDevices.class);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Pub getPub() {
        return pub;
    }

    public void setPub(Pub pub) {
        this.pub = pub;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PubDevices that = (PubDevices) o;

        if (id != that.id) return false;
        if (device != null ? !device.equals(that.device) : that.device != null) return false;
        return pub != null ? pub.equals(that.pub) : that.pub == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (device != null ? device.hashCode() : 0);
        result = 31 * result + (pub != null ? pub.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PubDevices{" +
                "id=" + id +
                ", device=" + device +
                ", pub=" + pub +
                "} " + super.toString();
    }
}
