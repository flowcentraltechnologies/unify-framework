/*
 * Copyright (c) 2018-2025 The Code Department.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.unify.core.notification;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of an e-mail server component using SMTP.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component(ApplicationComponents.APPLICATION_DEFAULTEMAILSERVER)
public class SmtpEmailServer extends AbstractEmailServer implements EmailServer {

	@Override
	public void sendEmail(String configurationCode, Email email) {
		if (!email.isWithError()) {
			try {
				MimeMessage mimeMessage = createMimeMessage(configurationCode, email);
				Transport.send(mimeMessage);
				email.setSent(true);
			} catch (Exception e) {
				email.setError(StringUtils.getPrintableStackTrace(e));
				logError(e);
			}
		}
	}

	@Override
	public void sendEmail(String configurationCode, Email... email) {
		Transport transport = null;
		try {
			Session session = getSession(configurationCode);
			transport = session.getTransport("smtp");
			transport.connect();
			for (Email _email : email) {
				if (!_email.isWithError()) {
					try {
						MimeMessage mimeMessage = createMimeMessage(session, _email);
						transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
						_email.setSent(true);
					} catch (Exception e) {
						_email.setError(StringUtils.getPrintableStackTrace(e));
						logError(e);
					}
				}
			}
		} catch (Exception e) {
			final String error = StringUtils.getPrintableStackTrace(e);
			for (Email _email : email) {
				_email.setError(error);
			}
			logError(e);
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					logError(e);
				}
			}
		}
	}
}
