package eson.co2p.se;

/**
 * Created by Isidor on 2014-10-18.
 */
public abstract class RecMessage {
    private int op;
    PDU PDUData;

    public RecMessage(byte[] rawData){
        PDUData = new PDU(rawData, rawData.length);
        setOp(PDUData.getByte(0));
    }

    public void setOp(int op){
        this.op = op;
    }

    public int getOp(){
        return op;
    }

    public byte[] getBytes(){
        return PDUData.getBytes();
    }
}
