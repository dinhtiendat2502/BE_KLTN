INSERT INTO role(role_name) VALUES ('SUPERADMIN');
INSERT INTO role(role_name) VALUES ('ADMIN');
INSERT INTO role(role_name) VALUES ('USER');


INSERT INTO user_account(email, full_name, password)
VALUES('superadmin@toeicute.com','super admin toeicute', '1234qwer');
INSERT INTO user_account(email, full_name, password)
VALUES('admin@toeicute.com','admin toeicute', '1234qwer');


-- INSERT FOR SUPER ADMIN --------------------------------
INSERT INTO user_account_roles(user_account_user_id, roles_role_id) VALUES ('1', '1');
INSERT INTO user_account_roles(user_account_user_id, roles_role_id) VALUES ('1', '2');

-- INSERT FOR ADMIN -- JUST ROLE ADMIN --------------------------------
INSERT INTO user_account_roles(user_account_user_id, roles_role_id) VALUES ('2', '2');