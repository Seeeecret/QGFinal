package pojo.po;

public class Printer {
    Integer printerId;
    String name;
    String status;
    Integer ownerId;

    public Integer getPrinterId() {
        return printerId;
    }

    public void setPrinterId(Integer printerId) {
        this.printerId = printerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Printer() {
    }
    public Printer(Integer printerId, String name, String status, Integer ownerId) {
        this.printerId = printerId;
        this.name = name;
        this.status = status;
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Printer{" +
                "printerId=" + printerId +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", ownerId=" + ownerId +
                '}';
    }
}
