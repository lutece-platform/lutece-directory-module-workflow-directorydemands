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
/* Create table for table workflow_directorydemands_record_user_assignment   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_directorydemands_record_user_assignment;
CREATE TABLE workflow_directorydemands_record_user_assignment (
  id_record INT DEFAULT 0 NOT NULL,
  id_user int default 0 NOT NULL,
  PRIMARY KEY (id_record)
);

/*==================================================================*/
/* Table structure for table workflow_task_directorydemands_information   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_directorydemands_information;
CREATE TABLE workflow_task_directorydemands_information (
  id_history int(11) NOT NULL,
  id_task int(11) NOT NULL,
  information_key VARCHAR(255) NOT NULL,
  information_value VARCHAR(255) NULL
);


/*==================================================================*/
/* Table structure for table workflow_directorydemands_reassignment_resource_history   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_directorydemands_reassignment_resource_history;
CREATE TABLE workflow_directorydemands_reassignment_resource_history (
id_reassignment_resource_history int AUTO_INCREMENT,
id_resource int default '0' NOT NULL,
resource_type varchar(255) default '',
content long varchar,
creation_date timestamp,
PRIMARY KEY (id_reassignment_resource_history)
);
