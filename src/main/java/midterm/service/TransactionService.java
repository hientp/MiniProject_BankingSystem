package midterm.service;

import midterm.Utils;
import midterm.controller.dto.*;
import midterm.models.Transaction;
import midterm.models.TransactionPartners;
import midterm.models.accounts.Account;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Alignment;
import midterm.models.enums.Status;
import midterm.models.users.FirstPartyUser;
import midterm.repository.AccountRepository;
import midterm.repository.TransactionPartnersRepository;
import midterm.repository.TransactionRepository;
import midterm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionPartnersRepository transactionPartnersRepository;
    @Autowired
    UserRepository userRepository;

    public Transaction transferMoney(TransactionDTO transactionDTO) {
        try {
            BigDecimal amount = transactionDTO.getAmount();
            Integer sender_id = transactionDTO.getSenderAccountId();
            Integer receiver_id = transactionDTO.getReceiverAccountId();
            Account sender = findAccountById(sender_id,accountRepository);
            Account receiver = findAccountById(receiver_id,accountRepository);
            return transactMoneySecured(accountRepository,sender,receiver,transactionRepository,transactionPartnersRepository,amount);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test");
        }
    }

    //Function to find account by id
    public Account findAccountById(Integer id,AccountRepository accountRepository) throws Exception {
        if(accountRepository.findCheckingAccountbyId(id) != null){return accountRepository.findCheckingAccountbyId(id);}
        if(accountRepository.findSavingsAccountbyId(id) != null){return accountRepository.findSavingsAccountbyId(id);}
        if(accountRepository.findCreditCardbyId(id) != null){return accountRepository.findCreditCardbyId(id);}
        else { throw new Exception("Id not found!");}
    }

    //Function to transact money with fraud detection activated and with given timestamp
    public Transaction transactMoneySecured(LocalDateTime TimeStamp, AccountRepository accountRepository, Account Sender, Account Receiver, TransactionRepository transactionRepository, TransactionPartnersRepository transactionPartnersRepository, BigDecimal amount) throws Exception {
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
                    accountRepository.save(Sender);
                    throw new Exception("Suspicious behaviour was detected. Account was frozen. Please contact your banking advisor.");
                }
            }
            //Money from credit card can be transacted without fraud detection
            return transactMoney(TimeStamp,accountRepository,Sender,Receiver,transactionRepository,transactionPartnersRepository,amount);
        } else {
            throw new Exception("Account is frozen. Please contact your banking advisor.");
        }
    }

    //Function to transact money with fraud detection and no given timestamp
    public Transaction transactMoneySecured(AccountRepository accountRepository, Account Sender, Account Receiver, TransactionRepository transactionRepository, TransactionPartnersRepository transactionPartnersRepository, BigDecimal amount) throws Exception {
        return transactMoneySecured(LocalDateTime.now(), accountRepository,Sender,Receiver,transactionRepository,transactionPartnersRepository,amount);
    }

    //Function to transact Money with given timestamp
    public Transaction transactMoney(LocalDateTime TimeStamp, AccountRepository accountRepository, Account Sender, Account Receiver, TransactionRepository transactionRepository, TransactionPartnersRepository transactionPartnersRepository, BigDecimal amount) throws Exception {
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
        return newTransaction;
    }

    //Function to transact Money without given timestamp
    public Transaction transactMoney(AccountRepository accountRepository, Account Sender, Account Receiver, TransactionRepository transactionRepository, TransactionPartnersRepository transactionPartnersRepository, BigDecimal amount) throws Exception {
        return transactMoney(LocalDateTime.now(),accountRepository,Sender,Receiver,transactionRepository,transactionPartnersRepository,amount);
    }

    //Functions for fraud detection with given TimeStamp
    public Boolean fraudDetection(LocalDateTime TimeStamp,AccountRepository accountRepository,Account Sender, TransactionRepository transactionRepository, BigDecimal amount) throws Exception{
        if(fraudDetectionMaxDailyAmount(TimeStamp,accountRepository,Sender, transactionRepository,amount)){
            return Boolean.TRUE;
        }
        if(fraudDetectionSameSecond(TimeStamp,Sender,transactionRepository)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Function for detecting if amounts of the day would exceed 150% of max value
    public Boolean fraudDetectionMaxDailyAmount(LocalDateTime TimeStamp,AccountRepository accountRepository,Account Sender,TransactionRepository transactionRepository, BigDecimal amount) throws Exception{
        Integer userId= accountRepository.getPrimaryOwnerIdById(Sender.getId());
        List<Object[]> objList = transactionRepository.getMaxTransactionAmountPerDay(userId, TimeStamp.toLocalDate());
        List<Object[]> objList2 = transactionRepository.getSumTransactionAmountForThisDay(userId, TimeStamp.toLocalDate());
        if(objList.get(0)==null){
            return Boolean.FALSE;
        } else {
            BigDecimal maxValue = (BigDecimal) objList.get(0)[0];
            BigDecimal cumAmount;
            if(objList2.isEmpty()) {
                cumAmount=new BigDecimal("0");
            } else {
                cumAmount= ((BigDecimal) objList2.get(0)[1]).add(amount);
            }
            if(cumAmount.compareTo(maxValue.multiply(new BigDecimal("1.5")))>0){
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
    }

    public Boolean fraudDetectionSameSecond(LocalDateTime TimeStamp, Account Sender, TransactionRepository transactionRepository){
        List<Object[]> objList = transactionRepository.getLastTimeStampOfTransactions(Sender.getId());
        if(objList.get(0)==null){
            return Boolean.FALSE;
        } else {
            Object dateTimeObject=objList.get(0)[0];
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    // date / time
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    // nanoseconds, with minimum 1 and maximum 9 digits and a decimal point
                    .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
                    // create formatter
                    .toFormatter();
            LocalDateTime dateTime= LocalDateTime.parse(dateTimeObject.toString(), formatter);
            if(TimeStamp.withNano(0).equals(dateTime.withNano(0))) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
    }

}