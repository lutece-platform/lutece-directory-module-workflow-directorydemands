/*==================================================================*/
/* Create table for unit tree code attribute */
/*===================================================================*/
DROP TABLE IF EXISTS unittree_unit_code ;
CREATE TABLE unittree_unit_code (
	id_unit INT DEFAULT 0 NOT NULL,
	unit_code VARCHAR(255) DEFAULT '' NOT NULL,
	PRIMARY KEY (id_unit)
);

/*==================================================================*/
/* Create table for table unittree_unit_assignment   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_directorydemands_record_user_assignment;
CREATE TABLE workflow_directorydemands_record_user_assignment (
  id_record INT DEFAULT 0 NOT NULL,
  id_user int default 0 NOT NULL,
  PRIMARY KEY (id_record)
 );
