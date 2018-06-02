/**
 * CS 120: Bank Account class
 * You must not change this template class.
 * 
 * Last Modified: Oct 1, 2014
 * @author Josh Hursey
 *
 */
public class BankAccount {
    /** Account balance */
    private double accountBalance;
    
    /**
     * Constructor
     * 
     * @param balance Initial balance of the account
     */
    public BankAccount(double balance) {
        accountBalance = balance;
    }
    
    /**
     * Accessor Method: Get the current account balance
     * 
     * @return Current account balance
     */
    public double getBalance() {
        return accountBalance;
    }
    
    /**
     * Mutator/Update Method: Deposit some money in the account
     * 
     * @param amount Amount of money to deposit
     */
    public void depositMoney(double amount) {
        accountBalance += amount;
    }
    
    /**
     * Mutator/Update Method: Withdraw some money from the account
     * 
     * @param amount Amount of money to withdraw
     * @return false if the account is overdrawn, otherwise return true
     */
    public boolean withdrawMoney(double amount) {
        accountBalance -= amount;
        if( accountBalance < 0 ) {
            return false;
        }
        return true;
    }
    
    /**
     * Mutator/Update Method: Apply the interest rate to the account.
     * 
     * @param rate Interest rate in % form
     */
    public void applyInterest(double rate) {
        accountBalance += accountBalance * (rate/100);
    }
}
