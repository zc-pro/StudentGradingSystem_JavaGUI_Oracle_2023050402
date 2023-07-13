/*级联删除*/
/*
drop table teacher cascade constraints;
drop table student cascade constraints;
drop table course cascade constraints;
drop table grades cascade constraints;
drop table grade_order cascade constraints;
*/
-- 学生信息表
create table student
(
  sno varchar2(11) primary key, --学号
  sname varchar2(50) not null unique, --姓名
  ssex char(3) check(ssex in('男','女')), --性别
  sage smallint not null, --年龄
  pwd varchar2(20) not null, --访问密码
  sclass varchar2(20) not null, --班级
  faculty varchar2(50), -- 院系
  syear varchar2(10), --年级
  senter varchar2(4) --入学年份
);

-- 教师信息表
create table teacher
(
  tno varchar2(7) primary key, --教师编号
  tname varchar2(50) not null unique, --教师名字
  tsex char(3) check(tsex in('男','女')), --教师性别
  tage smallint not null, --年龄
  tposition varchar2(20) not null, --教师职位
  tfaculty varchar2(50), -- 院系
  pwd varchar2(20) not null --访问密码
);

--课程信息
create table course
(
  cno varchar2(4), --课程编号
  --cpno varchar2(10), --先修课程编号
  cname varchar2(40) not null , --课程名
  ccredit number(2,1) not null, --课程学分
  tno varchar2(7) not null, --任课老师编号
  cscore smallint not null, --课程分制(0表示百分制，1表示五分制（优、良、中、差、不及格）)
  tname varchar2(50) not null, --任课老师
  primary key(cno,tno),
  foreign key (tno) references teacher(tno) 
);

--学生成绩表
create table grades
(
  sno varchar2(11) not null, --学生学号
  cno varchar2(4) not null, --课程编号
  grade varchar2(6) not null, --课程成绩
  cscore smallint not null, --课程分制(0表示百分制，1表示五分制（优、良、中、及格、不及格）)
  tno varchar2(7) not null, --教师编号
  primary key (sno,cno), 
  foreign key (sno) references student(sno),
  foreign key (cno,tno) references course(cno,tno),
  foreign key (tno) references teacher(tno)
);

-- 五分制排序表 用于对教师界面对“优、良、中、及格、不及格”五个等级的成绩进行排序
create table grade_order
(
  gradeFive varchar2(6) not null,
  orderNum int not null,
  primary key(gradeFive)
);
insert into grade_order(gradeFive,ordernum) values('优',5);
insert into grade_order(gradeFive,ordernum) values('良',4);
insert into grade_order(gradeFive,ordernum) values('中',3);
insert into grade_order(gradeFive,ordernum) values('及格',2);
insert into grade_order(gradeFive,ordernum) values('不及格',1);

--drop table grade_order cascade CONSTRAINTS;
-- 插入数据

-- 学生数据
insert into student(sname,ssex,sno, sage, sclass, faculty, pwd, syear, senter) values('张三','男','32106300040',20,'软件212','计算机科学与网络工程学院','123456','大一','2021');
insert into student(sname,ssex,sno, sage, sclass, faculty, pwd, syear, senter) values('李四','女','32106300041',19,'软件213','计算机科学与网络工程学院','123456','大一','2021');
insert into student(sname,ssex,sno, sage, sclass, faculty, pwd, syear, senter) values('王五','女','32106300042',18,'软件212','计算机科学与网络工程学院','123456','大一','2021');
insert into student(sname,ssex,sno, sage, sclass, faculty, pwd, syear, senter) values('刘六','男','32106300043',19,'软件212','计算机科学与网络工程学院','123456','大一','2021');
insert into student(sname,ssex,sno, sage, sclass, faculty, pwd, syear, senter) values('陈七','男','32106300044',19,'软件211','计算机科学与网络工程学院','123456','大一','2021');

insert into student(sname,ssex,sno, sage, sclass, faculty, pwd, syear, senter) values('林八','女','32106300045',20,'软件212','计算机科学与网络工程学院','123456','大一','2021');
insert into student(sname,ssex,sno, sage, sclass, faculty, pwd, syear, senter) values('吴九','男','32106300046',20,'软件213','计算机科学与网络工程学院','123456','大一','2021');
insert into student(sname,ssex,sno, sage, sclass, faculty, pwd, syear, senter) values('黄十','男','32106300047',21,'软件211','计算机科学与网络工程学院','123456','大一','2021');



-- 教师数据
insert into teacher(tno, tname, tsex, tage, tposition, pwd, tfaculty) values('t200001','肖铁','男',42,'教授','123456','计算机科学与网络工程学院');
insert into teacher(tno, tname, tsex, tage, tposition, pwd, tfaculty) values('t200002','王羽','女',34,'副教授','123456','数学与信息科学学院');
insert into teacher(tno, tname, tsex, tage, tposition, pwd, tfaculty) values('t200003','吴辉','男',55,'教授','123456','计算机科学与网络工程学院');
insert into teacher(tno, tname, tsex, tage, tposition, pwd, tfaculty) values('t200004','刘昱','女',38,'副教授','123456','计算机科学与网络工程学院');
insert into teacher(tno, tname, tsex, tage, tposition, pwd, tfaculty) values('t200005','李越明','男',60,'副教授','123456','计算机科学与网络工程学院');
insert into teacher(tno, tname, tsex, tage, tposition, pwd, tfaculty) values('t200006','林博','男',54,'教授','123456','计算机科学与网络工程学院');
insert into teacher(tno, tname, tsex, tage, tposition, pwd, tfaculty) values('t200007','黄敬','男',40,'教授','123456','计算机科学与网络工程学院');


--课程数据
insert into course(cno,cname,ccredit,tno,tname,cscore) values('6','数据结构',2,'t200001','肖铁',0);
insert into course(cno,cname,ccredit,tno,tname,cscore) values('2','高等数学',4,'t200002','王羽',0);
insert into course(cno,cname,ccredit,tno,tname,cscore) values('7','汇编语言',4,'t200001','肖铁',0);
insert into course(cno,cname,ccredit,tno,tname,cscore) values('5','软件工程',2,'t200003','吴辉',0);
insert into course(cno,cname,ccredit,tno,tname,cscore) values('1','java',3,'t200004','刘昱',0);
insert into course(cno,cname,ccredit,tno,tname,cscore) values('3','数据库',3,'t200005','李越明',0);
insert into course(cno,cname,ccredit,tno,tname,cscore) values('4','操作系统',3,'t200006','林博',0);
insert into course(cno,cname,ccredit,tno,tname,cscore) values('5','软件工程',2,'t200005','李越明',0);
insert into course(cno,cname,ccredit,tno,tname,cscore) values('8','java实验',3,'t200004','刘昱',1);


--学生成绩数据
insert into grades values('32106300040','6','50',0,'t200001');
insert into grades values('32106300041','2','40',0,'t200002');
insert into grades values('32106300042','5','60',0,'t200003');
insert into grades values('32106300043','1','80',0,'t200004');
insert into grades values('32106300044','3','90',0,'t200005');

insert into grades values('32106300045','1','85',0,'t200004');
insert into grades values('32106300046','3','65',0,'t200005');
insert into grades values('32106300047','3','75',0,'t200005');

insert into grades values('32106300040','5','75',0,'t200005');
insert into grades values('32106300041','5','85',0,'t200005');
insert into grades values('32106300043','5','99',0,'t200005');

insert into grades values('32106300040','8','中',1,'t200004');
insert into grades values('32106300041','8','优',1,'t200004');
insert into grades values('32106300043','8','良',1,'t200004');


-- 创建视图

-- 学生端查看成绩视图
create or replace view stugradeinfo as select student.sno sno, student.sname sname, course.cno cno , course.cname cname,course.ccredit ccredit,grades.grade grade, course.tno tno,course.tname tname 
from student, course, grades
where student.sno = grades.sno and grades.tno = course.tno and course.cno = grades.cno; 
-- 学生端基本信息视图
create view stubasicinfo as select student.sno sno, student.sname sname, student.ssex ssex, student.sclass sclass, student.sage sage, student.faculty faculty from student;


-- 课程信息视图

create or replace view courseinfo as
select c.cno, c.cname, c.tno, t.tname, c.ccredit, count(s.sno) as select_count, c.cscore 
from course c 
join teacher t on c.tno = t.tno
left join grades g on c.cno = g.cno and c.tno = g.tno
left join student s on g.sno = s.sno
group by c.cno, c.cname, c.tno, c.ccredit, t.tname, c.cscore;


-- 教师端查看成绩视图
create or replace view tgradeinfo as
select c.cno cno , c.cname cname, c.cscore cscore, s.sno sno, s.sname sname, s.sclass sclass, g.grade grade, --课程编号、课程名称、课程分制、学生学号、学生姓名、学生班级、学生成绩
       rank() over(partition by c.cno order by g.grade desc) rank, --学生成绩排名
       max(case when c.cscore = 0 then g.grade end) over(partition by c.cno, c.tno) highest,   --该课程的最高分
       min(case when c.cscore = 0 then g.grade end) over(partition by c.cno, c.tno) lowest,    --该课程的最低分
       round(avg(case when c.cscore = 0 then g.grade end) over(partition by c.cno, c.tno),1) average,   --该课程的平均分,如果出现小数就四舍五入保留一位
       t.tno tno, t.tname tname,                         -- 教师编号和教师姓名
       count(distinct s.sno) over(partition by c.cno, c.tno) num        -- 添加选修人数
from grades g join course c on g.cno = c.cno and g.tno = c.tno 
             join student s on g.sno = s.sno
             join teacher t on c.tno = t.tno;  -- 连接teacher表
-- 教师端基本信息视图
create or replace view tbasicinfo as select teacher.tno tno, teacher.tname tname, teacher.tsex tsex, teacher.tposition tposition from teacher;


-- 管理端学生管理视图
create or replace view smanage as select student.sno sno, student.sname sname, student.ssex ssex, student.sclass sclass, student.sage sage, student.faculty faculty, student.syear syear, student.senter senter from student;
-- 管理端教师管理视图
create or replace view tmanage as select teacher.tno tno, teacher.tname tname, teacher.tage tage, teacher.tsex tsex, teacher.tposition tposition, teacher.tfaculty from teacher;
-- 管理端课程管理视图
create or replace view cmanage as select course.cno cno, course.cname cname, course.tno tno, course.tname tname, course.ccredit ccredit, course.cscore cscore from course ;
-- 管理端账号管理视图
create or replace view accountmanage as select sno ano, pwd pwd from student union select tno ano, pwd pwd from teacher;


-- 查询表的数据
select * from student;
select * from teacher;
select * from course;
select * from grades;
select * from grade_order;

-- 查询视图的苏剧
select * from stugradeinfo;
select * from stubasicinfo;
select * from tgradeinfo;
select * from tbasicinfo;
select * from courseinfo;

select * from smanage;
select * from tmanage;
select * from cmanage;
select * from accountmanage;

/*
drop view stugradeinfo cascade constraints;
drop view stubasicinfo cascade constraints;
drop view tgradeinfo cascade constraints;
drop view tbasicinfo cascade constraints;
drop view courseinfo cascade constraints;
drop view smanage cascade constraints;
drop view tmanage cascade constraints;
drop view cmanage cascade constraints;
drop view accountmanage cascade constraints;
*/


select * 
from tgradeinfo
left join grade_order
on tgradeinfo.grade = grade_order.gradefive
where tno = 't200004' 
order by tgradeinfo.cno desc, case when grade_order.gradefive is NULL then cast(tgradeinfo.grade as int) else grade_order.ordernum end desc;

SELECT * FROM tgradeinfo 
LEFT JOIN grade_order  
ON tgradeinfo.grade = grade_order.gradefive  
WHERE tno = 't200004' AND cname = 'java实验'
ORDER BY tgradeinfo.cno DESC,  
         CASE WHEN grade_order.gradefive IS NULL THEN  
              CASE WHEN REGEXP_LIKE(tgradeinfo.grade, '^[0-9]+$')   
                   THEN cast(tgradeinfo.grade as int)  
                   ELSE NULL  
              END    
         ELSE grade_order.ordernum  
         END DESC;

commit;
