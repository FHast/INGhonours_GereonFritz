package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import Services.InvalidParameterException;

public class SQLTransactionService {

	public static void transfer(String senderIBAN, String receiverIBAN, double amount, String usage,
			String receiverName) throws InvalidParameterException {
		try {
			ResultSet sender = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?",
					new Object[] { senderIBAN });
			ResultSet receiver = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?",
					new Object[] { receiverIBAN });

			// All correct

			LocalDateTime currentTime = LocalDateTime.now();
			String datetime = currentTime.toString();

			SQLExecute.execute("UPDATE BankAccounts SET saldo = ? WHERE IBAN = ?",
					new Object[] { sender.getInt(2) - amount, senderIBAN });
			SQLExecute.execute("UPDATE BankAccounts SET saldo = ? WHERE IBAN = ?",
					new Object[] { receiver.getInt(2) + amount, receiverIBAN });
			SQLExecute.execute("INSERT INTO Transactions VALUES(?,?,?,?,?,?,?)",
					new Object[] { null, amount, usage, datetime, receiverName, senderIBAN, receiverIBAN });
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
