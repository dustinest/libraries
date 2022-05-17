package com.foxjunior.utils.mailer.attachment;

import java.io.File;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;

public class FileAttachment implements Attachment {
	private final File file;
	private final String name;
	private final String contentId;

	public FileAttachment(File filePath, String name, String contentId) {
		this.file = filePath.getAbsoluteFile();
		this.name = name == null ? filePath.getAbsoluteFile().getName() : name;
		this.contentId = contentId;
	}

	public FileAttachment(File filePath, String name) {
		this(filePath, name, null);
	}

	public FileAttachment(File filePath) {
		this(filePath, null);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FileAttachment)) {
			return super.equals(obj);
		}
		return this.file.equals(((FileAttachment)obj).file);
	}

	@Override
	public MimeBodyPart getBodyPart() throws MessagingException {
		MimeBodyPart rv = new MimeBodyPart();
		DataSource fileDataSource = new FileDataSource(file);
		rv.setDataHandler(new DataHandler(fileDataSource));
		rv.setFileName(name);
		if (contentId != null) {
			rv.setHeader("Content-ID", "<" + contentId + ">");
		}
		return rv;
	}

}
