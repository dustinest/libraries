package com.foxjunior.utils.mailer.attachment;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;

public interface Attachment {
	MimeBodyPart getBodyPart() throws MessagingException;
}
