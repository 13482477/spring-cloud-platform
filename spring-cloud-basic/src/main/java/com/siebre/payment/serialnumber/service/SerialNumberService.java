package com.siebre.payment.serialnumber.service;

import com.siebre.payment.serialnumber.mapper.SerialNumberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SerialNumberService {
	
	@Autowired
	private SerialNumberMapper serialNumberMapper;
	
	public String nextValue(String serialName) {
		return this.serialNumberMapper.nextValue(serialName);
	}

}
