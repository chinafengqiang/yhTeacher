package com.yhlearningclient.dao;

import java.util.List;

import com.yhlearningclient.model.Manager;
import com.yhlearningclient.model.SysMessage;
import com.yhlearningclient.model.User;
import com.yhlearningclient.model.UserInfo;


/**
 * 用户登录接口
 * @author Administrator
 *
 */
public interface IUser {

	/**
	 * 用户登录方法
	 * @param name
	 * @param password
	 * @return 返回用户对象
	 * @throws Exception 
	 */
	public UserInfo login(String serverIP, String name, String password) throws Exception;
	
	/**
	 * 用户登录方法
	 * @param name
	 * @param password
	 * @return 返回用户对象
	 * @throws Exception 
	 */
	public boolean loginBool(String serverIP, String name, String password) throws Exception;
	
	/**
	 * 获得课程表集合
	 * @param classId
	 * @return
	 */
	public List<SysMessage> getNewSysMessageList(String serverIP, int last_message_id,  Long classId);
	
	/**
	 * 本地获得通知消息
	 * @return
	 */
	public List<SysMessage> getAllSysMessageFromDB();
	
	/**
	 * 获得通知信息
	 * @param serverIP服务地址
	 * @return
	 */
	public List<SysMessage> getAllSysMessageList(String serverIP,  Long classId);
	
	/**
	 * 获得最后值
	 * @return
	 */
	public int getLastSysMessageId();
	
	
	/**
	 * 获得课程表集合
	 * @param classId
	 * @return
	 */
	public List<SysMessage> getRefreshSysMessageList(String serverIP,  Long classId);
	
	/**
	 * 更新
	 * @param sysMessage
	 */
	public void updataSysMessage(SysMessage sysMessage);
	
	/**
	 * 用户登录方法
	 * @param name
	 * @param password
	 * @return 返回用户对象
	 * @throws Exception 
	 */
	public Manager loginManager(String serverIP, String name, String password) throws Exception;
	
	/**
	 * 本用户
	 * @return
	 */
	public List<User> getAllUserFromLocal();
	
	/**
	 * 用户插入
	 * @param user
	 */
	public void insert(User user);
	
	/**
	 * 获得记录数
	 * @param condtion
	 * @return
	 */
	public int getUserCount(String condtion);

}
