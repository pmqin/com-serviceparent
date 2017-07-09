package com.pmqin.service;

import org.springframework.stereotype.Service;

@Service("studentService")
public class StudentServiceImpl {
	
//	 public int addStudent(Student stu) {
//	        String sql = "insert into student values (null,?,?)";
//	        return baseDao.execute(sql, new Object[]{stu.getName(),stu.getStatus()});
//	    }
//
//	    public void updateStudentStatus() {
//	        String sql = "update student s set s.status=? where s.id=?";
//	        System.out.println((new Date())+"执行了定时任务");
//	        //baseDao.execute(sql, new Object[]{stu.getStatus(),stu.getId()});
//	    }
//	 @Resource
//	    public void setBaseDao(IBaseDao baseDao) {
//	        this.baseDao = baseDao;
//	    }
}
