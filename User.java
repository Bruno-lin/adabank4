public class User {
    String account;
    int balance;
    int transactions;

    public User(String account) {
        this.account = account;
        this.balance = 1000;
        this.transactions = 0;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance += balance;
    }

    public int getTransactions() {
        return transactions;
    }

    public void setTransactions() {
        this.transactions++;
    }

    @Override
    public String toString() {
        return String.format("account:%s balance:%d transactions=%d", account, balance, transactions);
    }
}
