/*==================================================================*/
/* Create table for unit tree code attribute */
/*===================================================================*/
DROP TABLE IF EXISTS unittree_unit_code ;
CREATE TABLE unittree_unit_code (
	id_unit INT DEFAULT 0 NOT NULL,
	unit_code VARCHAR(255) DEFAULT '' NOT NULL,
	PRIMARY KEY (id_unit)
);
