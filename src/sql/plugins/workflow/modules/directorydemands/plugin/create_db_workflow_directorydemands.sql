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
