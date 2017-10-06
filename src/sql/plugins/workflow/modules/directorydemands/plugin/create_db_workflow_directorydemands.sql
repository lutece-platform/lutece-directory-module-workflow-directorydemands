/*==================================================================*/
/* Table structure for table workflow_directorydemands_record_assignment   */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_directorydemands_record_assignment;
CREATE TABLE workflow_directorydemands_record_assignment
(
    id INT DEFAULT 0 NOT NULL,
    id_record INT DEFAULT 0 NOT NULL,
    id_assignee_unit INT DEFAULT NULL,
    id_assigner_unit INT DEFAULT NULL,
    id_assignee_user INT DEFAULT NULL,
    id_assigner_user INT DEFAULT NULL,
    assignment_type varchar(50) NOT NULL DEFAULT "",
    assignment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
