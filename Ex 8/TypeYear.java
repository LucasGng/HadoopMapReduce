package advanced.customwritable;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class TypeYear implements WritableComparable<TypeYear> {

    private String unit;
    private String year;

    public TypeYear() {
        this.unit = null;
        this.year = null;
    }

    public TypeYear(String commodity, String unit) {
        this.unit = commodity;
        this.year = unit;
    }

    public String getCommodity() {
        return unit;
    }

    public void setCommodity(String commodity) {
        this.unit = commodity;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeYear that = (TypeYear) o;
        return Objects.equals(unit, that.unit) && Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit, year);
    }

    @Override
    public String toString() {
        return unit + "\t" + year;
    }

    @Override
    public int compareTo(TypeYear o) {
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
        dataOutput.writeUTF(unit);
        dataOutput.writeUTF(year);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        unit = dataInput.readUTF();
        year = dataInput.readUTF();
    }
}
