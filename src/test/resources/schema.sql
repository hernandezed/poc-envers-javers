CREATE TABLE person (
                          uuid varchar(255) NOT NULL,
                          name varchar(255) DEFAULT NULL,
                          surname varchar(255) DEFAULT NULL,
                          PRIMARY KEY (uuid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE revinfo (
                           rev bigint(20) NOT NULL AUTO_INCREMENT,
                           revtstmp bigint(20) DEFAULT NULL,
                           PRIMARY KEY (rev)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE audit_log_person (
                                    uuid varchar(255) NOT NULL,
                                    rev bigint(20) NOT NULL,
                                    revtype tinyint(4) DEFAULT NULL,
                                    name varchar(255) DEFAULT NULL,
                                    surname varchar(255) DEFAULT NULL,
                                    PRIMARY KEY (uuid,rev),
                                    KEY FK5ttow0dayp04270fwjcw79dqn (rev),
                                    CONSTRAINT FK5ttow0dayp04270fwjcw79dqn FOREIGN KEY (rev) REFERENCES revinfo (rev)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


