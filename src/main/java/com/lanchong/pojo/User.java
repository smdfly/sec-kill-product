package com.lanchong.pojo;

import lombok.*;

import java.util.Date;
/**
 * @program: SeckillProject
 * @description: 用户实体类
 **/
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private int id;
	private String userName;
	private String phone;
	private String password;
	private String salt;
	private String head;
	private int loginCount;
	private Date registerDate;
	private Date lastLoginDate;
}
