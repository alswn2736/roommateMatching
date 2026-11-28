-- 팀 담당자가 DDL/ID 정책 확인 후 신규 시퀀스가 필요한 경우에만 실행.
-- 기존 행을 변경하거나 삭제하지 않는다. DDL은 Oracle에서 자동 commit된다.
-- 실행 중에는 해당 테이블의 신규 등록을 잠시 멈춘다.
SET SERVEROUTPUT ON
DECLARE
  PROCEDURE ensure_sequence(seq_name VARCHAR2, table_name VARCHAR2, id_column VARCHAR2) IS
    existing_count NUMBER;
    first_id NUMBER;
  BEGIN
    SELECT COUNT(*) INTO existing_count FROM user_sequences WHERE sequence_name=seq_name;
    IF existing_count=0 THEN
      EXECUTE IMMEDIATE 'SELECT NVL(MAX(' || id_column || '),0)+1 FROM ' || table_name INTO first_id;
      IF first_id > 2147483647 THEN RAISE_APPLICATION_ERROR(-20001,'Java int ID 범위 초과'); END IF;
      EXECUTE IMMEDIATE 'CREATE SEQUENCE ' || seq_name || ' START WITH ' || TO_CHAR(first_id,'FM9999999999') || ' INCREMENT BY 1 MAXVALUE 2147483647 NOCYCLE NOCACHE';
      DBMS_OUTPUT.PUT_LINE(seq_name || ' created');
    ELSE
      DBMS_OUTPUT.PUT_LINE(seq_name || ' exists: 현재 데이터보다 충분히 큰 값인지 별도 확인');
    END IF;
  END;
BEGIN
  ensure_sequence('RM_ROLE_SEQ','ROLE','ROLEID');
  ensure_sequence('RM_RULE_SEQ','RULE','RULEID');
  ensure_sequence('RM_LEAVE_SEQ','LEAVEGROUP','LEAVEID');
END;
/
