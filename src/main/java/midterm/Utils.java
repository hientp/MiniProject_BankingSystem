package midterm;

import midterm.models.Transaction;
import midterm.models.TransactionPartners;
import midterm.models.accounts.Account;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Alignment;
import midterm.models.enums.Status;
import midterm.models.users.User;
import midterm.repository.AccountRepository;
import midterm.repository.TransactionPartnersRepository;
import midterm.repository.TransactionRepository;
import org.apache.tomcat.jni.Local;
import org.apache.tomcat.jni.Time;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Utils {

    //Function to transact money with fraud detection activated and with given timestamp
    public static void transactMoneySecured(LocalDateTime TimeStamp, AccountRepository accountRepository, Account Sender, Account Receiver, TransactionRepository transactionRepository, TransactionPartnersRepository transactionPartnersRepository, BigDecimal amount) throws Exception {
        //Check if status is active
        Status status = Status.ACTIVE;
        if(Sender instanceof CheckingAccount){status=((CheckingAccount) Sender).getStatus();}
        if(Sender instanceof SavingsAccount){status=((SavingsAccount) Sender).getStatus();}
        if(status==Status.ACTIVE) {
            Boolean isCreditCard=Sender instanceof CreditCard;
            //Fraud detection for checking and savings accounts
            if (!isCreditCard) {
                Boolean isFraud = fraudDetection(TimeStamp, accountRepository, Sender, transactionRepository, amount);
                if (isFraud) {
                    if(Sender instanceof CheckingAccount){((CheckingAccount) Sender).setStatus(Status.FROZEN);}
                    if(Sender instanceof SavingsAccount){((SavingsAccount) Sender).setStatus(Status.FROZEN);}
                    throw new Exception("Suspicious behaviour was detected. Account was frozen. Please contact your banking advisor.");
                }
            }
            //Money from credit card can be transacted without fraud detection
            transactMoney(TimeStamp,accountRepository,Sender,Receiver,transactionRepository,transactionPartnersRepository,amount);
            } else {
            throw new Exception("Account is frozen. Please contact your banking advisor.");
        }
    }

    //Function to transact money with fraud detection and no given timestamp
    public static void transactMoneySecured(AccountRepository accountRepository, Account Sender, Account Receiver, TransactionRepository transactionRepository, TransactionPartnersRepository transactionPartnersRepository, BigDecimal amount) throws Exception {
        transactMoneySecured(LocalDateTime.now(), accountRepository,Sender,Receiver,transactionRepository,transactionPartnersRepository,amount);
    }

    //Function to transact Money with given timestamp
    public static void transactMoney(LocalDateTime TimeStamp, AccountRepository accountRepository, Account Sender, Account Receiver, TransactionRepository transactionRepository, TransactionPartnersRepository transactionPartnersRepository, BigDecimal amount) throws Exception {
            //Check if sender has enough money on account
            Sender.changeBalance(amount.multiply(new BigDecimal("-1")));
            accountRepository.save(Sender);
            Receiver.changeBalance(amount);
            accountRepository.save(Receiver);
            Transaction newTransaction = new Transaction(TimeStamp, amount);
            TransactionPartners newTransactionPartner1 = new TransactionPartners(Sender, newTransaction, Alignment.SENDER);
            TransactionPartners newTransactionPartner2 = new TransactionPartners(Receiver, newTransaction, Alignment.RECEIVER);
            transactionRepository.save(newTransaction);
            transactionPartnersRepository.save(newTransactionPartner1);
            transactionPartnersRepository.save(newTransactionPartner2);
    }

    //Function to transact Money without given timestamp
    public static void transactMoney(AccountRepository accountRepository, Account Sender, Account Receiver, TransactionRepository transactionRepository, TransactionPartnersRepository transactionPartnersRepository, BigDecimal amount) throws Exception {
        transactMoney(LocalDateTime.now(),accountRepository,Sender,Receiver,transactionRepository,transactionPartnersRepository,amount);
    }

    //Functions for fraud detection with given TimeStamp
    public static Boolean fraudDetection(LocalDateTime TimeStamp,AccountRepository accountRepository,Account Sender, TransactionRepository transactionRepository, BigDecimal amount) throws Exception{
        if(fraudDetectionMaxDailyAmount(TimeStamp,accountRepository,Sender, transactionRepository,amount)){
            return Boolean.TRUE;
        }
        if(fraudDetectionSameSecond(TimeStamp,Sender,transactionRepository)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Function for detecting if amounts of the day would exceed 150% of max value
    public static Boolean fraudDetectionMaxDailyAmount(LocalDateTime TimeStamp,AccountRepository accountRepository,Account Sender,TransactionRepository transactionRepository, BigDecimal amount) throws Exception{
        Integer userId= accountRepository.getPrimaryOwnerIdById(Sender.getId());
        List<Object[]> objList = transactionRepository.getMaxTransactionAmountPerDay(userId);
        List<Object[]> objList2 = transactionRepository.getSumTransactionAmountForThisDay(userId, TimeStamp.toLocalDate());
        if(objList.get(0)==null){
            return Boolean.FALSE;
        } else {
            BigDecimal maxValue = (BigDecimal) objList.get(0)[0];
            BigDecimal cumAmount;
            if(objList2.isEmpty()) {
                cumAmount=new BigDecimal("0");
            } else {
                cumAmount= (BigDecimal) objList2.get(0)[1];
            }
            if(cumAmount.compareTo(maxValue.multiply(new BigDecimal("1.5")))>0){
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
    }

    public static Boolean fraudDetectionSameSecond(LocalDateTime TimeStamp, Account Sender,TransactionRepository transactionRepository){
        List<Object[]> objList = transactionRepository.getLastTimeStampOfTransactions(Sender.getId());
        if(objList.get(0)==null){
            return Boolean.FALSE;
        } else {
            Object dateTimeObject=objList.get(0)[0];
            LocalDateTime dateTime= LocalDateTime.parse(dateTimeObject.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            if(TimeStamp.withNano(0)==dateTime.withNano(0)) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
    }

}
