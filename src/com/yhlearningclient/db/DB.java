package com.yhlearningclient.db;


/**
 *本地数据库
 */
public interface DB {
	
	public static final int DATABASE_VERSION = 1;
	
	public static final String dbPath = android.os.Environment.getExternalStorageDirectory().getPath() + "/telearningDB";
	public static final String DATABASE_NAME = dbPath + "/" + "t_elearning.db";
	
	/**
	 *本地表结构
	 */
	public interface TABLES{
		
		/**
		 * 信息通知
		 */
		public interface SYSMESSAGE {
			public static final String TABLENAME = "sysmessage";

			public interface FIELDS {
				public static final String ID = "id";
				public static final String _ID = "_id";// 通知的id
				public static final String TIME = "time";
				public static final String NAME = "name";
				public static final String _CONTENT = "_content";
				public static final String ISREAD="isRead";
			}

			public interface SQL {
				public static final String CREATE = "create table if not exists sysmessage(id integer PRIMARY KEY ,_id int,isRead int,time varchar(50),name varchar(100),_content varchar(1000))";
				public static final String DROP = "drop table sysmessage";
				public static final String INSERT = "insert into sysmessage(_id,isRead,time,name,_content,) values(%s,%s,%s,'%s','%s') ";// 插入
				public static final String SELECT = "select *from sysmessage where %s";// 查询
				public static final String UPDATE = " update sysmessage set isRead = %s where _id = %s;";
				public static final String DELETE = " delete FROM sysmessage";
			}
		}
		
		/**
		 *试卷分类
		 */
		public interface TESTPAPER_CATEGORY {
			
			public static final String TABLENAME = "testpaper_catetory";

			public interface FIELDS {
				public static final String ID="id";
				public static final String _ID = "_id";// 试卷分类的id
				public static final String NAME = "name";
			}
			
			public interface SQL{                                                                                       
				public static final String CREATE="create table if not exists testpaper_catetory(id integer PRIMARY KEY AUTOINCREMENT, _id int, name varchar(100))";
				public static final String DROP ="drop table if exists testpaper_catetory";
				public static final String INSERT = "insert into testpaper_catetory (_id, name) values(%s, '%s') ";// 插入
				public static final String DELETE = "delect from testpaper_catetory";// 根据ID删除
				public static final String SELECT = "select * from testpaper_catetory where %s";// 查询
				public static final String SELECT_COUNT = "select count(*) from testpaper_catetory where {0}";
			}
		}
		
		/**
		 *试卷
		 */
		public interface TESTPAPER{
			
			public static final String TABLENAME = "testpaper";

			public interface FIELDS {
				public static final String ID="id";
				public static final String _ID = "_id";// 试卷的id
				public static final String NAME = "name";
				public static final String PARENT_ID = "parent_id";
				public static final String PASS_SCORE = "pass_score";
				public static final String TOTAL_SCORE = "total_score";
				public static final String CLASS_ID="class_id";
				
			}
			
			public interface SQL{                                                                                       
				public static final String CREATE="create table if not exists testpaper(id integer PRIMARY KEY AUTOINCREMENT, _id int, name varchar(100)," +
						" parent_id int, pass_score varchar(10), total_score varchar(10), class_id int)";
				public static final String DROP ="drop table if exists testpaper";
				public static final String INSERT = "insert into testpaper (_id, name, parent_id,pass_score, total_score, class_id) values(%s, '%s', %s, '%s','%s' ,%s) ";// 插入
				public static final String DELETE = "delect from testpaper";// 根据ID删除
				public static final String SELECT = "select * from testpaper where %s";// 查询
				public static final String SELECT_COUNT = "select count(*) from testpaper where {0}";
				
			}
		}
		
		/**
		 *试卷题目
		 */
		public interface TEST_PAPER_QUESTION{
			
			public static final String TABLENAME = "test_paper_question";

			public interface FIELDS {
				public static final String ID="id";
				public static final String _ID = "_id";// 试卷题目的id
				public static final String NAME = "name";
				public static final String TEST_PAPER_ID = "test_paper_id";
				public static final String QUESTION_ID = "question_id";
				public static final String QUESTION_TYPE = "question_type";
				public static final String OPTIONS = "options";
				public static final String ANSWER = "answer";
				public static final String KEN = "ken";
				public static final String DIFFICULTY = "difficulty";
				public static final String SCORE = "score";
				public static final String NOTE = "note";
				
			}
			
			public interface SQL{                                                                                       
				public static final String CREATE="create table if not exists test_paper_question" +
						"(id integer PRIMARY KEY AUTOINCREMENT," +
						" _id int, " +
						" name varchar(100)," +
						" test_paper_id int, " +
						" question_id int, " +
						" question_type int, " +
						" options varchar(100), " +
						" answer varchar(100), " +
						" ken varchar(100), " +
						" difficulty int, " +
						" score float, " +
						" note blob )";
				public static final String DROP ="drop table if exists test_paper_question";
				public static final String INSERT = "insert into test_paper_question " +
						"(_id, name, test_paper_id, question_id, question_type, options, answer, ken, difficulty, score, note) " +
						"values(%s, '%s', %s, %s, %s, '%s', '%s', '%s', %s, %s, %s) ";
				public static final String DELETE = "delect from test_paper_question";// 根据ID删除
				
				public static final String SELECT = "select * from test_paper_question where {0}";
				public static final String SELECT_COUNT = "select count(*) from test_paper_question where {0}";
				
			}
		}
		
		/**
		 *记录试卷成绩
		 */
		public interface USER_TEST_PAPER{
			
			public static final String TABLENAME = "user_test_paper";

			public interface FIELDS {
				public static final String ID="id";
				public static final String _ID = "_id";// 试卷题目的id
				public static final String USER_ID = "user_id";
				public static final String QUESTION_ID = "question_id";
				public static final String TIME = "time";
				public static final String SCORE = "score";
				public static final String OPTIONS = "options";
				public static final String IS_CORRECT = "is_correct";
				public static final String TEST_PAPER_ID = "test_paper_id";
				public static final String NO_SELECT_ANSWER = "no_select_answer";
			}
			
			public interface SQL{                                                                                       
				public static final String CREATE="create table if not exists user_test_paper" +
						"(id integer PRIMARY KEY AUTOINCREMENT," +
						" _id int, " +
						" user_id int, " +
						" question_id int, " +
						" time varchar(50), " +
						" score varchar(50), " +
						" options varchar(10), " +
						" is_correct varchar(10), " +
						" test_paper_id int," +
						" no_select_answer blob)";
				public static final String DROP ="drop table if exists user_test_paper";
				public static final String INSERT = "insert into user_test_paper " +
						"(user_id, question_id, time, score, options, is_correct, test_paper_id, no_select_answer) " +
						"values(%s, %s, '%s', '%s', '%s', '%s', %s, %s) ";
				public static final String DELETE = "delect from user_test_paper";// 根据ID删除
				public static final String SELECT = "select * from user_test_paper where %s";
				public static final String SELECT_COUNT = "select count(*) from user_test_paper where {0}";
				
			}
		}
		
		public interface USERINFO{
			 public static final String TABLENAME = "user_info";
			   public interface FIELDS{
				   public static final String ID="id";
				   public static final String USER_ID = "user_id";
				   public static final String USER_NAME = "user_name";
				   public static final String USER_PASSWORD = "user_password";
				   public static final String CLASS_ID = "class_id";
			   } 
			   public interface SQL{
				   public static final String CREATE ="Create  TABLE if not exists user_info("+
					   	" [id] integer PRIMARY KEY AUTOINCREMENT"+
					   	",[user_id] varchar(20) NOT NULL"+
					   	",[user_name] varchar(50) NOT NULL"+
					   	",[user_password] varchar(50)"+
					   	",[class_id] int"+
					   	");";
				   public static final String DROP = "DROP TABLE IF EXISTS user_info";
				   public static final String SELECT_ALL = "select * from user_info";
				   public static final String SELECT_COUNT = "select count(*) from user_info where {0}";
			   }
		}
		
		public interface GROUP {
			public static final String TABLENAME = "MyGroup";

			public interface FIELDS {
				public static final String GROUPID = "groupId";
				public static final String GROUPNAME = "groupName";

			}

			public interface SQL {
				public static final String CREATE = "create table if not exists MyGroup" +
						" (groupId integer PRIMARY KEY AUTOINCREMENT NOT NULL, groupName varchar(20));";
				public static final String DROP = "DROP TABLE IF EXISTS MyGroup";
				public static final String INSERT = "insert into MyGroup (groupName)   values ('%s');";
				public static final String DELETE_BY_ID = "delete from MyGroup where groupId = %s;";
				public static final String UPDATE = " update MyGroup set groupName = '%s' where groupId = %s;";
				public static final String SELECT = "select * from MyGroup where %s";
				public static final String COUNT = "select count(*) from MyGroup";

			}

		}
		
		public interface STUDENT {
			public static final String TABLENAME = "STUDENT";

			public interface FIELDS {
				public static final String ID = "id";
				public static final String _ID = "_id";// 试卷题目的id
				public static final String NAME = "name";
				public static final String GROUPID = "groupId";
				public static final String ISMARK = "isMark";

			}

			public interface SQL {
				public static final String CREATE = " create table if not exists STUDENT " +
						"(id integer PRIMARY KEY AUTOINCREMENT NOT NULL, _id int, name varchar(20),"
						+ " groupId integer, isMark varchar(20));";
				public static final String DROP = "DROP TABLE IF EXISTS STUDENT";
				public final static String INIT_ISMARK ="update STUDENT set isMark = '%s' where %s";
				public final static String COUNT =  "select count(*) from STUDENT where %s";
				public static final String INSERT = "insert into STUDENT ( _id, name, groupId, isMark )  "
					+"values (%s,'%s',%s,'%s');";
				public static final String DELETE_BY_ID = "delete from STUDENT where  %s;";
			//	public static final String UPDATE = " update STUDENT set image = %s ,name = '%s',groupId =%s ,phone= %s , homePhone = %s, otherPhone = %s,e_mail = '%s',address = '%s',birthday = '%s',comment = '%s',isMark = '%s'   where id = %s;";
				public static final String CHANGE_GROUP="update STUDENT set groupId = %s where %s";
				public static final String SELECT = "select * from STUDENT where %s";

			}

		}
		
	}

}