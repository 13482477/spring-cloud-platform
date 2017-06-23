package com.siebre.uaa.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siebre.basic.query.PageInfo;
import com.siebre.basic.web.WebResult;
import com.siebre.uaa.role.entity.Role;
import com.siebre.uaa.role.service.RoleService;
import com.siebre.uaa.user.entity.User;
import com.siebre.uaa.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	/**
	 * 用户信息根据pageInfo分页展示
	 * 
	 * @param pageInfo
	 *            包含查询信息username,nickname,createDate,active
	 * @return 返回角色的列表 包含分页page信息
	 */
	@RequestMapping(value = "/api/v1/users", method = { RequestMethod.GET })
	public WebResult<List<User>> userList(@RequestParam int page, @RequestParam int limit, @RequestParam String sortField, @RequestParam String order) {
		PageInfo pageInfo = new PageInfo(limit, page, sortField, order);
		List<User> users = userService.listUser(pageInfo);
		return WebResult.<List<User>>builder().returnCode("200").data(users).pageInfo(pageInfo).build();
	}

	/**
	 * 根据id查询用户详细信息
	 * 
	 * @param id
	 *            用户id
	 * @return 用户详细信息
	 */
	@RequestMapping(value = "/api/v1/user/{id}", method = { RequestMethod.GET })
	public WebResult<User> user(@PathVariable Long id) {
		User data = userService.loadUserByID(id);
		return WebResult.<User>builder().returnCode("200").data(data).build();
	}

	/**
	 * 创建用户
	 * 
	 * @param user
	 *            用户信息
	 * @return 创建成功|失败
	 */
	@RequestMapping(value = "/api/v1/user", method = { RequestMethod.POST })
	public WebResult<User> createUser(@RequestBody User user) {
		this.userService.createUser(user);
		return WebResult.<User>builder().returnCode("200").data(user).build();
	}

	/**
	 * 更新用户
	 * 
	 * @param user
	 *            用户信息
	 * @return 更新成功|失败
	 */
	@RequestMapping(value = "/api/v1/user", method = RequestMethod.PUT)
	public WebResult<User> updateUser(@RequestBody User user) {
		userService.updateUser(user);
		return WebResult.<User>builder().returnCode("200").data(user).build();
	}

	/**
	 * 用户角色信息
	 * 
	 * @param id
	 *            用户id
	 * @return 用户所属的角色列表
	 */
	@RequestMapping(value = "/api/v1/roles/user/{userId}", method = RequestMethod.GET)
	public WebResult<List<Role>> userRole(@PathVariable Long userId) {
		List<Role> roles = this.roleService.getUserRoles(userId);
		return WebResult.<List<Role>>builder().returnCode("200").data(roles).build();
	}

	/**
	 * 更新用户角色信息
	 * 
	 * @param id
	 *            用户id
	 * @param rolesID
	 *            需要添加的角色id列表
	 * @return 更新成功|失败
	 */
	@RequestMapping(value = "/api/v1/user/{userId}/role", method = RequestMethod.POST)
	public WebResult<Object> updateUserRole(@PathVariable Long userId, @RequestBody List<Long>roleIds) {
		userService.updateUserRoles(userId, roleIds);
		return WebResult.<Object>builder().returnCode("200").build();
	}

	/**
	 * 用户名校验
	 * 
	 * @param username
	 * @return 用户名是否重复
	 */
	@RequestMapping(value = "/api/v1/user/validation/{username}", method = RequestMethod.GET)
	public WebResult<Object> validateRepeatAddOrUpdate(@PathVariable String username) {
		boolean result = this.userService.validateRepeatUsername(username);
		String returnCode = result ? "200" : "500";
		String returnMessage = result ? "成功" : "用户已存在";
		return WebResult.<Object>builder().returnCode(returnCode).returnMessage(returnMessage).build();
	}
	
	/**
	 * Load user detail info by user name;
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/api/v1/user/loadUserByUserName/{username}", method = RequestMethod.GET)
	public WebResult<User> loadUserByUserName(@PathVariable String username) {
		User user = this.userService.loadUserByUsername(username);
		return WebResult.<User>builder().returnCode("200").data(user).build();
	}

}
