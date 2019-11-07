package ee.fj.utils.mailer;

import javax.mail.Message;
import javax.mail.Transport;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Transport.class})
public class MailSendingTest {
	@Rule
    public final ExpectedException thrown= ExpectedException.none();
	
	@Test(expected=IllegalStateException.class)
	public void testEmptySenderThrowsException() throws Exception {
		SmtpMailSender smtpSender = new SmtpMailSender();
		smtpSender.setHost("test");
		smtpSender.setPort(123);
		smtpSender.setReplyAddress("my.reply@mytest.com");
		smtpSender.setSender("my.sender@mytest.com");
		smtpSender.setUseTls(false);
		MailSender sender = smtpSender.getMailSender("My Test subject");
		PowerMockito.mockStatic(Transport.class);
		PowerMockito.doNothing().when(Transport.class, "send", Mockito.any(Message.class));
		sender.send();
	}

	@Test(expected=IllegalStateException.class)
	public void testAddressWithName() throws Exception {
		SmtpMailSender smtpSender = new SmtpMailSender();
		smtpSender.setHost("test");
		smtpSender.setPort(123);
		smtpSender.setReplyAddress("my.reply@mytest.com", "õäöü");
		smtpSender.setSender("my.sender@mytest.com", "õäöü");
		smtpSender.setUseTls(false);
		MailSender sender = smtpSender.getMailSender("My Test subject");
		PowerMockito.mockStatic(Transport.class);
		PowerMockito.doNothing().when(Transport.class, "send", Mockito.any(Message.class));
		sender.send();
	}

	@Test
	public void testBlankMailSender() throws Exception {
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Recipients are not set!");
		SmtpMailSender smtpSender = new SmtpMailSender();
		smtpSender.setHost("test");
		smtpSender.setPort(123);
		smtpSender.setSender("my.sender@mytest.com");
		smtpSender.setUseTls(false);
		MailSender sender = smtpSender.getMailSender("My Test subject");
		sender.addHtmlMailBody("Lorem ipsum Est");

		PowerMockito.mockStatic(Transport.class);
		Transport transport = Mockito.mock(Transport.class);
		PowerMockito.doNothing().when(Transport.class, "send", Mockito.any(Message.class));
		sender.send();
	}

	@Test
	public void testMailSenderWithRecipients() throws Exception {
		SmtpMailSender smtpSender = new SmtpMailSender();
		smtpSender.setHost("test");
		smtpSender.setPort(123);
		smtpSender.setSender("my.sender@mytest.com");
		smtpSender.setUseTls(false);
		MailSender sender = smtpSender.getMailSender("My Test subject");
		sender.addHtmlMailBody("Lorem ipsum Est");

		sender.addRecipient("saaja@kuhu.ee");


		PowerMockito.mockStatic(Transport.class);
		PowerMockito.doNothing().when(Transport.class, "send", Mockito.any(Message.class));

		sender.send();
	}
}
