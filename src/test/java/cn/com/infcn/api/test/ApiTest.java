package cn.com.infcn.api.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.nlpcn.jcoder.domain.User;
import org.nlpcn.jcoder.run.annotation.DefaultExecute;
import org.nlpcn.jcoder.service.TaskService;
import org.nlpcn.jcoder.util.StaticValue;
import org.nutz.dao.Chain;

/**
 * 测试文档搜索
 * 
 * @author Ansj
 *
 */

public class ApiTest {


	/**
	 *
	 * @param i
	 * @return
	 * @throws Exception
	 */
	@DefaultExecute
	public Object test() throws Exception {
		User user = new User() ;
		user.setMail("aaa@aa.com");
		user.setCreateTime(new Date());
		user.setName("aaaa");
		user.setType(0);
		StaticValue.systemDao.save(user);
		return null;
	}

}
