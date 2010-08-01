DELETE FROM TIMECARD
DELETE FROM AppUser
SET IDENTITY_INSERT AppUser ON
INSERT INTO AppUser (ID, UserName, Password, Email, IsActive, Comment, CreationDate)
  VALUES (1, 'bob', 'n/a', 'bob@bob.net', 1, '', getdate())
SET IDENTITY_INSERT AppUser OFF
SET IDENTITY_INSERT TIMECARD ON
INSERT INTO TIMECARD (ID, START_DATE, COMMENTS, SUBMITTER_FK)
  VALUES (1, getdate(), 'This is the first timecard', 1)
INSERT INTO TIMECARD (ID, START_DATE, COMMENTS, SUBMITTER_FK)
  VALUES (2, getdate(), 'This is another timecard', 1)
SET IDENTITY_INSERT TIMECARD OFF
GO