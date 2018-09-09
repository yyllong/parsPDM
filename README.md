# parsPDM

Java parses PDM file code, realizes single file and batch read PDM file table, field and other information, and batch insert Oracle is a database

-- Create table
create table TEST_PDM
(
  SYSTEMNAME  VARCHAR2(1000),
  SYSTEMCNAME VARCHAR2(1000),
  TABLENAME   VARCHAR2(1000),
  TABLECNAME  VARCHAR2(1000),
  COLUMNAME   VARCHAR2(1000),
  COLUMCNAME  VARCHAR2(1000),
  REMARK      VARCHAR2(1000),
  DATETYPE    VARCHAR2(1000)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );
