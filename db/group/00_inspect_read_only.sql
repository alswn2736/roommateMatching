SELECT table_name,column_name,data_type,data_length,char_used,nullable
FROM user_tab_columns
WHERE table_name IN ('GROUPS','GROUP_MEMBER','ROLE','RULE','RULEAGREEMENT','SETTLEMENT','SETTLEMENTLIST','LEAVEGROUP','LEAVECONSENT')
ORDER BY table_name,column_id;
SELECT c.table_name,c.constraint_name,c.constraint_type,k.column_name,c.r_constraint_name,c.status
FROM user_constraints c JOIN user_cons_columns k ON c.constraint_name=k.constraint_name
WHERE c.table_name IN ('GROUPS','GROUP_MEMBER','ROLE','RULE','RULEAGREEMENT','SETTLEMENT','SETTLEMENTLIST','LEAVEGROUP','LEAVECONSENT')
ORDER BY c.table_name,c.constraint_name,k.position;
SELECT sequence_name,last_number FROM user_sequences WHERE sequence_name IN ('RM_ROLE_SEQ','RM_RULE_SEQ','RM_SETTLEMENT_ITEM_SEQ','RM_LEAVE_SEQ');
SELECT status,COUNT(*) FROM groups GROUP BY status;
SELECT status,COUNT(*) FROM leavegroup GROUP BY status;
SELECT settled,COUNT(*) FROM settlement GROUP BY settled;
SELECT agreed,COUNT(*) FROM leaveconsent GROUP BY agreed;
