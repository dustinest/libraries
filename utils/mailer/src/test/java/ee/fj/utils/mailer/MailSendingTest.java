package ee.fj.utils.mailer;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import javax.mail.Message;

public class MailSendingTest {
	@Rule
    public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void testEmptyRecipientsThrowsException() {
		SmtpMailSender smtpSender = new SmtpMailSender();
		smtpSender.setHost("test");
		smtpSender.setPort(123);
		smtpSender.setReplyAddress("my.reply@mytest.com");
		smtpSender.setSender("my.sender@mytest.com");
		smtpSender.setUseTls(false);
		MailSender sender = smtpSender.getMailSender("My Test subject");
		sender.addTextMailBody("Lorem ipsum est!");
		Exception exception = Assertions.assertThrows(IllegalStateException.class, sender::getMessageContainer);
		Assertions.assertEquals("Recipients are not set!", exception.getMessage());
	}

	@Test
	public void testAddressWithName() {
		SmtpMailSender smtpSender = new SmtpMailSender();
		smtpSender.setHost("test");
		smtpSender.setPort(123);
		smtpSender.setReplyAddress("my.reply@mytest.com", "õäöü");
		smtpSender.setSender("my.sender@mytest.com", "õäöü");
		smtpSender.setUseTls(false);
		MailSender sender = smtpSender.getMailSender("My Test subject");
		Exception exception = Assertions.assertThrows(IllegalStateException.class, sender::getMessageContainer);
		Assertions.assertEquals("Mail bodies are not set!", exception.getMessage());
	}

	@Test
	void testMailSenderWithRecipients() throws Exception {
		SmtpMailSender smtpSender = new SmtpMailSender();
		smtpSender.setHost("test");
		smtpSender.setPort(123);
		smtpSender.setSender("my.sender@mytest.com");
		smtpSender.setUseTls(false);
		MailSender sender = smtpSender.getMailSender("My Test subject");
		sender.addHtmlMailBody("Lorem ipsum Est");

		sender.addRecipient("saaja@kuhu.ee");
		Message message = sender.getMessageContainer();
		Assertions.assertNotNull(message);
	}

}
