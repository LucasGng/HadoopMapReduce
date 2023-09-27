package advanced.customwritable;


import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class CommodityTrade implements WritableComparable<CommodityTrade> {

    private String commodity;
    private long trade;

    public CommodityTrade() {
        this.commodity = null;
        this.trade = 0;
    }

    public CommodityTrade(String commodity, long value) {
        this.commodity = commodity;
        this.trade = value;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public long getTrade() {
        return trade;
    }

    public void setTrade(long trade) {
        this.trade = trade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommodityTrade that = (CommodityTrade) o;
        return Objects.equals(commodity, that.commodity) && Objects.equals(trade, that.trade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commodity, trade);
    }

    @Override
    public String toString() {
        return commodity + "\t" + trade;
    }

    @Override
    public int compareTo(CommodityTrade o) {
        if (hashCode() < o.hashCode()) {
            return -1;
        } else if (hashCode() > o.hashCode()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(commodity);
        dataOutput.writeLong(trade);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        commodity = dataInput.readUTF();
        trade = dataInput.readLong();
    }
}
