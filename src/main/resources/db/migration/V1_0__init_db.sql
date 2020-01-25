CREATE TABLE Address (
  id int(11) NOT NULL AUTO_INCREMENT,
  city varchar(255) DEFAULT NULL,
  created datetime DEFAULT NULL,
  line1 varchar(255) DEFAULT NULL,
  line2 varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  postCode varchar(255) DEFAULT NULL,
  updated datetime DEFAULT NULL,
  version int(11) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Customer (
  id int(11) NOT NULL AUTO_INCREMENT,
  created datetime DEFAULT NULL,
  emailAddress varchar(255) DEFAULT NULL,
  firstName varchar(255) DEFAULT NULL,
  lastName varchar(255) DEFAULT NULL,
  mobile varchar(255) DEFAULT NULL,
  systemUserId bigint(20) DEFAULT NULL,
  telephone varchar(255) DEFAULT NULL,
  updated datetime DEFAULT NULL,
  userName varchar(255) DEFAULT NULL,
  version int(11) DEFAULT NULL,
  address_id int(11) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKfok4ytcqy7lovuiilldbebpd9 (address_id),
  CONSTRAINT FKfok4ytcqy7lovuiilldbebpd9 FOREIGN KEY (address_id) REFERENCES Address (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE Customer_Device (
  Customer_id int(11) NOT NULL,
  devices_id int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Device (
  id int(11) NOT NULL AUTO_INCREMENT,
  created datetime DEFAULT NULL,
  event varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  updated datetime DEFAULT NULL,
  version int(11) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
