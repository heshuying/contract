package com.haier.hailian.contract;

import com.haier.hailian.contract.service.GrabService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PayApplicationTests {
	@Autowired
	private GrabService grabService;

	@Test
	public void doJob(){
		grabService.refreshContractStatusJob();
	}

}
