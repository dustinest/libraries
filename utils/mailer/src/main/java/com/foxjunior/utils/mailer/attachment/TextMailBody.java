package com.foxjunior.utils.mailer.attachment;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;

public class TextMailBody extends MailBody {

	public TextMailBody(String body) {
		super(body);
	}

	@Override
	protected MimeBodyPart getBodyPart(String body) throws MessagingException {
		MimeBodyPart rv = new MimeBodyPart();
		rv.setContent(body, "text/plain");
		rv.setHeader("MIME-Version", "1.0");
		rv.setHeader("Content-Type", "text/plain");
		return rv;
	}

}
