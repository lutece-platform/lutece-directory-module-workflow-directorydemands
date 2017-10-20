/*==================================================================*/
/* Table structure for table directory_record_unit_assignment   */
/*==================================================================*/
DROP TABLE IF EXISTS directory_record_unit_assignment;
CREATE TABLE directory_record_unit_assignment (
  id int(11) NOT NULL DEFAULT '0',
  id_record int(11) NOT NULL DEFAULT '0',
  id_assignor_unit int(11) NOT NULL DEFAULT '0',
  id_assigned_unit int(11) NOT NULL DEFAULT '0',
  assignment_type VARCHAR(50) NOT NULL DEFAULT '',
  assignment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  is_active SMALLINT NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
 ) ;
 
/*==================================================================*/
/* Table structure for table workflow_task_directorydemands_information   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_directorydemands_information;
CREATE TABLE workflow_task_directorydemands_information (
  id_history int(11) NOT NULL DEFAULT '0',
  id_task int(11) NOT NULL DEFAULT '0',
  information_key VARCHAR(255) NOT NULL,
  information_value VARCHAR(255) NULL
 ) ;
 
/*==================================================================*/
/* Table structure for table workflow_task_directorydemands_assign_to_unit_cf   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_directorydemands_assign_to_unit_cf;
CREATE TABLE workflow_task_directorydemands_assign_to_unit_cf (
  id_task int(11) NOT NULL DEFAULT '0',
  assignment_type VARCHAR(50) NULL,
  unit_selections VARCHAR(1000) NULL,
  PRIMARY KEY (id_task)
 ) ;

/*==================================================================*/
/* Indexes creation for module workflow_directorydemands */
/*===================================================================*/
 CREATE INDEX index_directory_record_unit_assignment_id_record ON directory_record_unit_assignment (id_record);
 CREATE INDEX index_directory_record_unit_assignment_id_assigned_unit ON directory_record_unit_assignment (id_assigned_unit,assignment_date);
 CREATE INDEX index_directory_record_unit_assignment_id_assignor_unit ON directory_record_unit_assignment (id_assignor_unit,assignment_date);

/*==================================================================*/
/* Create table for unit tree code attribute */
/*===================================================================*/
CREATE TABLE unittree_unit_code (
	id_unit INT DEFAULT 0 NOT NULL,
	unit_code VARCHAR(255) DEFAULT '' NOT NULL,
	PRIMARY KEY (id_unit)
);
