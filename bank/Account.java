package bank;

class Account {

    // Attributes
    // TODO
    private String name;
    private Integer balance;
    private Integer threshold;
    private Boolean locked;

    // Constructor
    // TODO

    public Account(String name, Integer balance, Integer threshold, Boolean locked) {
        this.name = name;
        this.balance = balance;
        this.threshold = threshold;
        this.locked = locked;
    }


    // Methods
    // TODO
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String toString() {
        // TODO
        String res = "";
        res += this.name + " | ";
        res += this.balance + " | ";
        res += this.threshold + " | ";
        res += this.locked + "\n";
        return res;
    }
}
