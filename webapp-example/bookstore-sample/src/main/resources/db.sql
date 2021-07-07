DROP DATABASE IF EXISTS bookstrore;

CREATE DATABASE IF NOT EXISTS bookstore CHARACTER SET UTF8MB4;

USE bookstore;

DROP VIEW IF EXISTS UserBookStatistic;

DROP TABLE IF EXISTS UserBookVisit;

DROP TABLE IF EXISTS OrderItem;

DROP TABLE IF EXISTS UserOrder;

DROP TABLE IF EXISTS Address;

DROP TABLE IF EXISTS Book;

DROP TABLE IF EXISTS Publisher;

DROP TABLE IF EXISTS BookType;

DROP TABLE IF EXISTS User;

DROP TABLE IF EXISTS Degree;

DROP TABLE IF EXISTS Career;

CREATE TABLE IF NOT EXISTS Career (
  cid BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  cname VARCHAR(32) NOT NULL
) ENGINE = InnoDB CHARSET = UTF8MB4;

CREATE TABLE IF NOT EXISTS Degree (
  did BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  dname VARCHAR(32) NOT NULL
) ENGINE = InnoDB CHARSET = UTF8MB4;

CREATE TABLE IF NOT EXISTS User (
  uid BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  uname VARCHAR(32) NOT NULL,
  password VARCHAR(26) NOT NULL,
  sex TINYINT NOT NULL CHECK (sex IN (0, 1, 2)),
  birth DATETIME NOT NULL,
  phone VARCHAR(20) UNIQUE NOT NULL,
  email VARCHAR(48) UNIQUE DEFAULT NULL,
  career_id BIGINT(16) DEFAULT NULL REFERENCES Career (cid) ON DELETE cascade,
  degree_id BIGINT(16) DEFAULT NULL REFERENCES Degree (did) ON DELETE cascade,
  admin_tag TINYINT NOT NULL CHECK (admin_tag IN (0, 1)) DEFAULT 0
) ENGINE = InnoDB CHARSET = UTF8MB4;

CREATE TABLE IF NOT EXISTS BookType (
  tid BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  tname VARCHAR(32) NOT NULL
) ENGINE = InnoDB CHARSET = UTF8MB4;

CREATE TABLE IF NOT EXISTS Publisher (
  pid BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  pname VARCHAR(32) NOT NULL
) ENGINE = InnoDB CHARSET = UTF8MB4;

CREATE TABLE IF NOT EXISTS Book (
  bid BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  bname VARCHAR(32) NOT NULL,
  isbn VARCHAR(32) NOT NULL,
  price DECIMAL(12, 2) NOT NULL,
  stock BIGINT NOT NULL,
  pic  VARCHAR(128) DEFAULT "/img/default.jpg",
  type_id BIGINT(16) NOT NULL REFERENCES BookType (tid) ON DELETE cascade,
  publisher_id BIGINT(16) NOT NULL REFERENCES Publisher (pid) ON DELETE cascade
) ENGINE = InnoDB CHARSET = UTF8MB4;

CREATE TABLE IF NOT EXISTS Address(
  aid  BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  uid BIGINT(16) NOT NULL REFERENCES User (uid) ON DELETE cascade,
  location VARCHAR(64) NOT NULL
) ENGINE = InnoDB CHARSET = UTF8MB4;

CREATE TABLE IF NOT EXISTS UserOrder (
  uoid BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  uid BIGINT(16) NOT NULL REFERENCES User (uid) ON DELETE cascade,
  paid TINYINT NOT NULL CHECK (paid IN (0, 1)) DEFAULT 0,
  address_id BIGINT(16) NOT NULL REFERENCES Address (aid) ON DELETE cascade
) ENGINE = InnoDB CHARSET = UTF8MB4;

CREATE TABLE IF NOT EXISTS OrderItem (
  oiid BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  uoid BIGINT(16) NOT NULL REFERENCES UserOrder (uoid) ON DELETE cascade,
  bid BIGINT(16) NOT NULL REFERENCES Book (bid) ON DELETE cascade,
  num BIGINT NOT NULL CHECK (num >= 1)
) ENGINE = InnoDB CHARSET = UTF8MB4;

CREATE TABLE IF NOT EXISTS UserBookVisit (
  vid BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  uid BIGINT(16) REFERENCES User (uid) ON DELETE cascade,
  bid BIGINT(16) REFERENCES Book (bid) ON DELETE cascade,
  vtime DATETIME NOT NULL
) ENGINE = InnoDB CHARSET = UTF8MB4;

CREATE VIEW UserBookStatistic AS
  SELECT tmp1.uid,tmp1.bid,IFNULL(SUM(IFNULL(vnum,0))*0.01+SUM(IFNULL(onum,0)),0) AS rate
  FROM (SELECT usr.uid AS uid,ubv.bid AS bid,COUNT(ubv.vid) AS vnum
      FROM User usr LEFT JOIN UserBookVisit ubv ON usr.uid=ubv.uid
      GROUP BY usr.uid,ubv.bid) AS tmp1
      LEFT JOIN
     (SELECT usr.uid AS uid,odi.bid AS bid,SUM(num) AS onum
      FROM User usr LEFT JOIN UserOrder uso NATURAL JOIN OrderItem odi ON usr.uid=uso.uid
      GROUP BY usr.uid,odi.bid) AS tmp2
      ON tmp1.uid=tmp2.uid AND tmp1.bid=tmp2.bid
  GROUP BY tmp1.uid,tmp1.bid
  HAVING IFNULL(SUM(IFNULL(vnum,0))*0.01+SUM(IFNULL(onum,0)),0) != 0
  ORDER BY tmp1.uid,tmp1.bid;

INSERT INTO Career(cname) VALUES("教育");
INSERT INTO Career(cname) VALUES("行政");
INSERT INTO Career(cname) VALUES("金融");
INSERT INTO Career(cname) VALUES("商业");
INSERT INTO Career(cname) VALUES("传媒");
INSERT INTO Career(cname) VALUES("医疗");
INSERT INTO Career(cname) VALUES("法律");
INSERT INTO Career(cname) VALUES("计算机");
INSERT INTO Career(cname) VALUES("制造业");
INSERT INTO Career(cname) VALUES("其他");

INSERT INTO Degree(dname) VALUES("小学");
INSERT INTO Degree(dname) VALUES("初中");
INSERT INTO Degree(dname) VALUES("高中");
INSERT INTO Degree(dname) VALUES("专科");
INSERT INTO Degree(dname) VALUES("本科");
INSERT INTO Degree(dname) VALUES("硕士");
INSERT INTO Degree(dname) VALUES("博士");

INSERT INTO User(uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag) 
VALUES("Ryan","000000",0,"1998-11-14","13004599675","ryan@gmail.com",8,6,1);

INSERT INTO User(uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag) 
VALUES("程锋靖","111222",0,"1992-06-05","13954866754","cfj@yahoo.com",4,6,0);

INSERT INTO User(uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag) 
VALUES("林晗允","333444",1,"1999-03-12","17785534521","yuner@126.com",3,5,0);

INSERT INTO User(uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag) 
VALUES("王渝奇","555666",0,"1988-11-04","15814500257","wyq88@163.com",9,4,0);

INSERT INTO User(uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag) 
VALUES("沈俞晴","777888",1,"1985-07-25","138155687333","7333shen@qq.com",1,7,0);

INSERT INTO User(uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag) 
VALUES("于昭哲","999000",0,"1992-08-12","130075285869","zhaozhe@139.com",5,5,0);

INSERT INTO Address(uid,location) VALUES(1,"苏州市吴中区木渎镇金枫南路1388号");
INSERT INTO Address(uid,location) VALUES(2,"南京市高淳区古柏镇128号1508室");
INSERT INTO Address(uid,location) VALUES(3,"上海市浦东新区祖冲之路1500号1803室");
INSERT INTO Address(uid,location) VALUES(4,"北京市海淀区北四环西路9号银谷大厦1708室");
INSERT INTO Address(uid,location) VALUES(5,"深圳市福田区景田商报路商报大厦5楼102室");
INSERT INTO Address(uid,location) VALUES(6,"成都市金牛区金周路589号A楼3206室");

INSERT INTO BookType(tname) VALUES("经典名著");
INSERT INTO BookType(tname) VALUES("文学散文");
INSERT INTO BookType(tname) VALUES("金融管理");
INSERT INTO BookType(tname) VALUES("艺术设计");
INSERT INTO BookType(tname) VALUES("自然科学");
INSERT INTO BookType(tname) VALUES("计算机科学");
INSERT INTO BookType(tname) VALUES("少儿读物");
INSERT INTO BookType(tname) VALUES("工业技术");
INSERT INTO BookType(tname) VALUES("科普读物");

INSERT INTO Publisher(pname) VALUES("机械工业出版社");
INSERT INTO Publisher(pname) VALUES("清华大学出版社");
INSERT INTO Publisher(pname) VALUES("人民邮电出版社");
INSERT INTO Publisher(pname) VALUES("电子工业出版社");
INSERT INTO Publisher(pname) VALUES("人民文学出版社");
INSERT INTO Publisher(pname) VALUES("北京大学出版社");
INSERT INTO Publisher(pname) VALUES("复旦大学出版社");

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("西游记","7305386-9",25.9,800,"/img/6746539.jpg",1,5);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("红楼梦","7766584-1",23.9,300,"/img/5888883.jpg",1,2);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("三国演义","6059241-3",39.9,600,"/img/6704541.jpg",1,3);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("水浒传","2950751-3",19.9,300,"/img/7446316.jpg",1,5);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("瓦尔登湖","8649754-4",27.0,600,"/img/8238276.jpg",2,2);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("汉字王国","2884212-1",49.0,500,"/img/8013807.jpg",2,1);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("人间失格","8160923-5",32.0,300,"/img/8160923.jpg",2,5);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("经济学思维","7340541-7",69.9,100,"/img/7340541.jpg",3,2);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("影响力","7403102-4",32.9,200,"/img/7403102.jpg",3,3);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("金字塔原理","8062278-3",32.9,200,"/img/8062278.jpg",3,5);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("美学境界","7728408-4",28.9,900,"/img/7728408.jpg",4,5);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("艺术精神","7993244-3",28.9,700,"/img/7993244.jpg",4,2);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("美术的故事","7293478-5",28.9,400,"/img/7293478.jpg",4,3);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("艺术哲学","7572801-9",69.9,500,"/img/7572801.jpg",4,5);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("时间简史","7719601-9",89.9,900,"/img/7719601.jpg",5,4);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("生态学基础","8514673-3",29.9,200,"/img/8514673.jpg",5,1);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("50个数学知识","8378229-1",19.9,400,"/img/8378229.jpg",5,3);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("费曼物理学原理","8331343-1",49.9,600,"/img/8331343.jpg",5,4);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("机器学习","5908563-1",89.9,800,"/img/5908563.jpg",6,1);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("Java编程思想","8224032-2",69.9,400,"/img/8224032.jpg",6,2);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("MySQL经典实例","8080060-5",49.9,300,"/img/8080060.jpg",6,3);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("Hadoop权威指南","6715938-4",59.9,400,"/img/6715938.jpg",6,4);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("Python数据分析","7740213-6",23.9,800,"/img/7740213.jpg",6,5);

INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id)
VALUES("Linux内核解析","8079787-6",49.9,300,"/img/8079787.jpg",6,4);

INSERT INTO UserOrder(uid,paid,address_id) VALUES(1,1,1);
INSERT INTO UserOrder(uid,paid,address_id) VALUES(2,1,2);
INSERT INTO UserOrder(uid,paid,address_id) VALUES(3,1,3);
INSERT INTO UserOrder(uid,paid,address_id) VALUES(4,1,4);
INSERT INTO UserOrder(uid,paid,address_id) VALUES(5,1,5);
INSERT INTO UserOrder(uid,paid,address_id) VALUES(6,1,6);

INSERT INTO OrderItem(uoid,bid,num) VALUES(1,1,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,2,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,3,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,4,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,5,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,6,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,7,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,8,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,9,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,10,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,11,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,12,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,13,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,14,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,15,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,16,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,17,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,18,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,19,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,20,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,21,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,22,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,23,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(1,24,10);

INSERT INTO OrderItem(uoid,bid,num) VALUES(2,1,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,2,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,3,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,4,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,5,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,6,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,7,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,8,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,9,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,10,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,11,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,12,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,13,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,14,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,15,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,16,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,17,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,18,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,19,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,20,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,21,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,22,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,23,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(2,24,1);

INSERT INTO OrderItem(uoid,bid,num) VALUES(3,1,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,2,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,3,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,4,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,5,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,6,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,7,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,8,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,9,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,10,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,11,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,12,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,13,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,14,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,15,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,16,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,17,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,18,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,19,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,20,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,21,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,22,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,23,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(3,24,10);

INSERT INTO OrderItem(uoid,bid,num) VALUES(4,1,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,2,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,3,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,4,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,5,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,6,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,7,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,8,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,9,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,10,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,11,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,12,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,13,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,14,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,15,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,16,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,17,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,18,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,19,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,20,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,21,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,22,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,23,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(4,24,1);

INSERT INTO OrderItem(uoid,bid,num) VALUES(5,1,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,2,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,3,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,4,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,5,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,6,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,7,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,8,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,9,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,10,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,11,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,12,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,13,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,14,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,15,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,16,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,17,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,18,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,19,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,20,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,21,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,22,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,23,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(5,24,1);

INSERT INTO OrderItem(uoid,bid,num) VALUES(6,1,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,2,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,3,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,4,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,5,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,6,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,7,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,8,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,9,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,10,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,11,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,12,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,13,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,14,10);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,15,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,16,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,17,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,18,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,19,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,20,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,21,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,22,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,23,1);
INSERT INTO OrderItem(uoid,bid,num) VALUES(6,24,1);

INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,1,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,2,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,3,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,4,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,5,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,6,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,7,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,8,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,9,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,10,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,11,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,12,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,13,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,14,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,15,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,16,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,17,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,18,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,19,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,20,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,21,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,22,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,23,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(1,24,"2021-01-01");

INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,1,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,2,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,3,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,4,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,5,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,6,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,7,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,8,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,9,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,10,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,11,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,12,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,13,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,14,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,15,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,16,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,17,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,18,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,19,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,20,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,21,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,22,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,23,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(2,24,"2021-01-01");

INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,1,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,2,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,3,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,4,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,5,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,6,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,7,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,8,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,9,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,10,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,11,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,12,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,13,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,14,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,15,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,16,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,17,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,18,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,19,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,20,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,21,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,22,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,23,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(3,24,"2021-01-01");


INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,1,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,2,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,3,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,4,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,5,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,6,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,7,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,8,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,9,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,10,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,11,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,12,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,13,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,14,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,15,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,16,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,17,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,18,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,19,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,20,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,21,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,22,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,23,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(4,24,"2021-01-01");

INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,1,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,2,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,3,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,4,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,5,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,6,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,7,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,8,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,9,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,10,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,11,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,12,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,13,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,14,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,15,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,16,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,17,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,18,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,19,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,20,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,21,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,22,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,23,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(5,24,"2021-01-01");

INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,1,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,2,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,3,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,4,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,5,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,6,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,7,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,8,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,9,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,10,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,11,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,12,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,13,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,14,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,15,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,16,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,17,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,18,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,19,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,20,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,21,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,22,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,23,"2021-01-01");
INSERT INTO UserBookVisit(uid,bid,vtime) VALUES(6,24,"2021-01-01");