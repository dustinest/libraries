package om.foxjunior.utils.mailer

import com.foxjunior.utils.mailer.MailSender
import com.foxjunior.utils.mailer.SmtpMailSender
import spock.lang.Specification


class MailSendingSpec extends Specification {
	def "Empty recipients throws exception"() {
		given:
			SmtpMailSender smtpSender = new SmtpMailSender()
			smtpSender.setHost("test")
			smtpSender.setPort(123)
			smtpSender.setReplyAddress("my.reply@mytest.com")
			smtpSender.setSender("my.sender@mytest.com")
			smtpSender.setUseTls(false)
			MailSender sender = smtpSender.getMailSender("My Test subject")
			sender.addTextMailBody("Lorem ipsum est!")
		when:
			sender.getMessageContainer()
		then:
			Exception exception = thrown(IllegalStateException)
			exception.message == "Recipients are not set!"
	}

	def "Mail body is required"() {
		given:
			SmtpMailSender smtpSender = new SmtpMailSender()
			smtpSender.setHost("test")
			smtpSender.setPort(123)
			smtpSender.setReplyAddress("my.reply@mytest.com", "õäöü")
			smtpSender.setSender("my.sender@mytest.com", "õäöü")
			smtpSender.setUseTls(false)
			MailSender sender = smtpSender.getMailSender("My Test subject")
		when:
			sender.getMessageContainer()
		then:
			Exception exception = thrown(IllegalStateException)
			exception.message == "Mail bodies are not set!"
	}

	def "Mail is OK"() {
		given:
			SmtpMailSender smtpSender = new SmtpMailSender()
			smtpSender.setHost("test")
			smtpSender.setPort(123)
			smtpSender.setSender("my.sender@mytest.com")
			smtpSender.setUseTls(false)
			MailSender sender = smtpSender.getMailSender("My Test subject")
			sender.addHtmlMailBody("Lorem ipsum Est")

			sender.addRecipient("saaja@kuhu.ee")
		when:
			def message = sender.getMessageContainer()
		then:
			message != null
	}
}
