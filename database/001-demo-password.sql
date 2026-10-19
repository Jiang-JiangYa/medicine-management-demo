-- Execute ONLY on the isolated demo database after restoring schema-only.dmp.
-- Original passwords are not migrated: seed new virtual accounts in an empty demo schema.
ALTER TABLE DEMO_APP.USERS MODIFY PASSWORD VARCHAR(100);
