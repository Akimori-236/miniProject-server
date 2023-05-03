CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `givenname` varchar(45) NOT NULL,
  `familyname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(70) NOT NULL,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `store` (
  `store_id` int NOT NULL AUTO_INCREMENT,
  `store_name` varchar(45) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`store_id`),
  KEY `manager_id_idx` (`user_id`),
  CONSTRAINT `manager_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `instruments` (
  `instrument_id` int NOT NULL AUTO_INCREMENT,
  `brand` varchar(45) NOT NULL,
  `model` varchar(45) NOT NULL,
  `serial_number` varchar(45) NOT NULL,
  `store_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`instrument_id`),
  KEY `holder_id_idx` (`user_id`),
  KEY `room_id_idx` (`store_id`),
  CONSTRAINT `store_id` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
