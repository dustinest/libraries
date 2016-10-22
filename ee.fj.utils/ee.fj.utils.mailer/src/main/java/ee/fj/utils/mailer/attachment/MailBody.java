package ee.fj.utils.mailer.attachment;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

public abstract class MailBody implements Attachment {
	private final MimeBodyPart bodyPart;

	protected MailBody(String body) {
		try {
			this.bodyPart = getBodyPart(body);
		} catch (MessagingException e) {
			throw new IllegalArgumentException("Could not create mail body!", e);
		}
	}

	protected abstract MimeBodyPart getBodyPart(String body) throws MessagingException;

	@Override
	public MimeBodyPart getBodyPart() {
		return bodyPart;
	}
}
