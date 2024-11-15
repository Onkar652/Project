package gettersetter;

import java.io.Serializable;

public class RideRequest implements Serializable {
    private double fromlatitude;
    private double fromlongitude;

    private double tolatitude;
    private double tolongitude;
    private int seatNumber;
    private String date;

    boolean isaccepted;
    String cabproviderid;
    String reqestername;

    String reqid;
    String rid;
boolean iscompleted;
boolean ispaid;
    private String time;
    private boolean ac;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isIspaid() {
        return ispaid;
    }

    public void setIspaid(boolean ispaid) {
        this.ispaid = ispaid;
    }

    public boolean isIscompleted() {
        return iscompleted;
    }

    public void setIscompleted(boolean iscompleted) {
        this.iscompleted = iscompleted;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public boolean isIsaccepted() {
        return isaccepted;
    }

    public void setIsaccepted(boolean isaccepted) {
        this.isaccepted = isaccepted;
    }

    public String getCabproviderid() {
        return cabproviderid;
    }

    public void setCabproviderid(String cabproviderid) {
        this.cabproviderid = cabproviderid;
    }

    public String getReqestername() {
        return reqestername;
    }

    public void setReqestername(String reqestername) {
        this.reqestername = reqestername;
    }

    public double getFromlatitude() {
        return fromlatitude;
    }

    public void setFromlatitude(double fromlatitude) {
        this.fromlatitude = fromlatitude;
    }

    public double getFromlongitude() {
        return fromlongitude;
    }

    public void setFromlongitude(double fromlongitude) {
        this.fromlongitude = fromlongitude;
    }

    public double getTolatitude() {
        return tolatitude;
    }

    public void setTolatitude(double tolatitude) {
        this.tolatitude = tolatitude;
    }

    public double getTolongitude() {
        return tolongitude;
    }

    public void setTolongitude(double tolongitude) {
        this.tolongitude = tolongitude;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }


}
