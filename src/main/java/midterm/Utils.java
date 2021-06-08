package midterm;

import midterm.models.Transaction;
import midterm.models.TransactionPartners;
import midterm.models.accounts.Account;
import midterm.models.accounts.CreditCard;
import midterm.models.enums.Alignment;
import midterm.models.users.User;
import midterm.repository.TransactionPartnersRepository;
import midterm.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Utils {

    //Funktion to transact Money
    public static void transactMoney(Account Sender, Account Receiver, TransactionRepository transactionRepository, TransactionPartnersRepository transactionPartnersRepository, BigDecimal amount) throws Exception {
        //FIXME Prüfung ob Fraud vorliegt (prüfung, friere jeweiligen account ein, führe die transaktion nicht aus und werfe exception)
        //Check if sender has enough money on account
            Sender.changeBalance(amount.multiply(new BigDecimal("-1")));
            Receiver.changeBalance(amount);
            Transaction newTransaction = new Transaction(LocalDateTime.now(), amount);
            TransactionPartners newTransactionPartner1 = new TransactionPartners(Sender, newTransaction, Alignment.SENDER);
            TransactionPartners newTransactionPartner2 = new TransactionPartners(Receiver, newTransaction, Alignment.RECEIVER);
            transactionRepository.save(newTransaction);
            transactionPartnersRepository.save(newTransactionPartner1);
            transactionPartnersRepository.save(newTransactionPartner2);
    }
}
