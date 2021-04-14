CREATE TABLE person (
                          uuid varchar(255) NOT NULL,
                          name varchar(255) DEFAULT NULL,
                          surname varchar(255) DEFAULT NULL,
                          PRIMARY KEY (uuid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `jv_commit` (
                             `commit_pk` bigint(20) NOT NULL AUTO_INCREMENT,
                             `author` varchar(200) DEFAULT NULL,
                             `commit_date` timestamp(3) NOT NULL DEFAULT current_timestamp(3) ON UPDATE current_timestamp(3),
                             `commit_date_instant` varchar(30) DEFAULT NULL,
                             `commit_id` decimal(22,2) DEFAULT NULL,
                             PRIMARY KEY (`commit_pk`),
                             KEY `jv_commit_commit_id_idx` (`commit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `jv_commit_property` (
                                      `property_name` varchar(191) NOT NULL,
                                      `property_value` varchar(600) DEFAULT NULL,
                                      `commit_fk` bigint(20) NOT NULL,
                                      PRIMARY KEY (`commit_fk`,`property_name`),
                                      KEY `jv_commit_property_commit_fk_idx` (`commit_fk`),
                                      KEY `jv_commit_property_property_name_property_value_idx` (`property_name`,`property_value`(191)),
                                      CONSTRAINT `jv_commit_property_commit_fk` FOREIGN KEY (`commit_fk`) REFERENCES `jv_commit` (`commit_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `jv_global_id` (
                                `global_id_pk` bigint(20) NOT NULL AUTO_INCREMENT,
                                `local_id` varchar(191) DEFAULT NULL,
                                `fragment` varchar(200) DEFAULT NULL,
                                `type_name` varchar(200) DEFAULT NULL,
                                `owner_id_fk` bigint(20) DEFAULT NULL,
                                PRIMARY KEY (`global_id_pk`),
                                KEY `jv_global_id_local_id_idx` (`local_id`),
                                KEY `jv_global_id_owner_id_fk_idx` (`owner_id_fk`),
                                CONSTRAINT `jv_global_id_owner_id_fk` FOREIGN KEY (`owner_id_fk`) REFERENCES `jv_global_id` (`global_id_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `jv_snapshot` (
                               `snapshot_pk` bigint(20) NOT NULL AUTO_INCREMENT,
                               `type` varchar(200) DEFAULT NULL,
                               `version` bigint(20) DEFAULT NULL,
                               `state` text DEFAULT NULL,
                               `changed_properties` text DEFAULT NULL,
                               `managed_type` varchar(200) DEFAULT NULL,
                               `global_id_fk` bigint(20) DEFAULT NULL,
                               `commit_fk` bigint(20) DEFAULT NULL,
                               PRIMARY KEY (`snapshot_pk`),
                               KEY `jv_snapshot_global_id_fk_idx` (`global_id_fk`),
                               KEY `jv_snapshot_commit_fk_idx` (`commit_fk`),
                               CONSTRAINT `jv_snapshot_commit_fk` FOREIGN KEY (`commit_fk`) REFERENCES `jv_commit` (`commit_pk`),
                               CONSTRAINT `jv_snapshot_global_id_fk` FOREIGN KEY (`global_id_fk`) REFERENCES `jv_global_id` (`global_id_pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
