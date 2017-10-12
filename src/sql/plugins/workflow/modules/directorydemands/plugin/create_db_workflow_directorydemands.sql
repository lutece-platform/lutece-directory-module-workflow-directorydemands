/*==================================================================*/
/* Table structure for table workflow_directorydemands_record_assignment   */
/*==================================================================*/
DROP TABLE IF EXISTS directory_record_unit_assignment;
CREATE TABLE directory_record_unit_assignment (
  id int(11) NOT NULL DEFAULT '0',
  id_record int(11) NOT NULL DEFAULT '0',
  id_assignor_unit int(11) NOT NULL DEFAULT '0',
  id_assigned_unit int(11) NOT NULL DEFAULT '0',
  assignment_type VARCHAR(50) NOT NULL DEFAULT '',
  assignment_date TIMESTAMP  ,
  is_active SMALLINT NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
 ) ;

/*==================================================================*/
/* Indexes creation for module workflow_directorydemands */
/*===================================================================*/
 CREATE INDEX index_directory_record_unit_assignment_id_record ON directory_record_unit_assignment (id_record);
 CREATE INDEX index_directory_record_unit_assignment_id_assigned_unit ON directory_record_unit_assignment (id_assigned_unit,assignment_date);
 CREATE INDEX index_directory_record_unit_assignment_id_assignor_unit ON directory_record_unit_assignment (id_assignor_unit,assignment_date);

