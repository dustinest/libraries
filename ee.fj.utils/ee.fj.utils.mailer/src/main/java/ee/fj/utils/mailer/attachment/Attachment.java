package ee.fj.utils.mailer.attachment;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

public interface Attachment {
	MimeBodyPart getBodyPart() throws MessagingException;
}
