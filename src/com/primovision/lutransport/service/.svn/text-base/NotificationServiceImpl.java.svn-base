package com.primovision.lutransport.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.primovision.lutransport.core.exception.BusinessException;

@Transactional(propagation = Propagation.REQUIRED)
public class NotificationServiceImpl implements NotificationService {

	@Override
	@Transactional(readOnly=false)
	public void sendMessage(String notificationType, String mailId,
			Object[] messageParams) throws BusinessException {
	}
}
