INSERT INTO role (role_id, role_name)
VALUES (1, 'ADMIN'),
       (2, 'USER');
INSERT INTO user_account (avatar, created_at, email, full_name, password, status, updated_at)
VALUES (NULL, '2023-12-12 08:08:54.000000', 'admin', 'ADMIN',
        '$2a$10$nfyjdSZAnJQbNLY5DS3tqufpFWmNKimtrEvxMxn1QOSl/JpXyGYs.', 'ACTIVE', '2023-12-13 09:45:25.776202');
INSERT INTO user_role (user_id, role_id)
VALUES (1, 1);

insert into crawl_config(agent_user, token, email)
VALUES ('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
        'csrftoken=taoxMpbf7FKoFHoZGyNqfdoKcwD0elEJgIKsrel28Pc7cEKWiO5XB9Tc8Lw4r0ge; sessionid=11wlkenrvfpsrris51vl9aqbixlvcvfq; cf_clearance=.UxZwpQCxxLgA3nOPuc8.XCIZ3pXEIYNPBVDEK1r_r8-1711956447-1.0.1.1-MTG7UGOio0vwN9itKBMXt3e8nMGDHKmXBgnQfft.DVHqFqok0rtyy.QQ9imW79wiElSwe8jX0eKt8vHd9HyijQ',
        'hideonbush8405@gmail.com');
insert into email_config(host, port, username, password, status)
values ('smtp.gmail.com', '587', '', '', true);


insert into email_template(name, status, subject, template_code, template_content)
VALUES ('Mẫu email xác thực OTP khi đăng ký', 'active', 'Xác thực tài khoản TOEIC HERO',
        'AUTHENTICATION_AFTER_REGISTER',
        '<!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <meta http-equiv="X-UA-Compatible" content="ie=edge" />
            <title>Toeicute</title>
            <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet" />
        </head>

        <body>
            <div style="font-family: Helvetica,Arial,sans-serif;min-width:700px;overflow:auto;line-height:2">
                <div style="margin:50px auto;width:600px;padding:20px 0">
                    <div style="border-bottom:1px solid #eee">
                        <a href="" style="font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600">TOEIC HERO</a>
                    </div>
                    <p style="font-size:1.1em">Hi, %s</p>
                    <p>Chào mừng bạn đến với TOEIC HERO.<br />Mã OTP kích hoạt tài khoản của bạn là:</p>
                    <h2
                        style="background: #00466a; margin: 0 auto;width: max-content;padding: 0 10px;color: #fff; border-radius: 4px;">
                        %s
                    </h2>
                    <p style="font-size:0.9em;">
                        <br />
                        Nếu bạn không yêu cầu mã này thì có thể người khác đang cố truy cập vào tài khoản của bạn.
                        <br /><b>Không chuyển tiếp hoặc đưa mã này cho bất kỳ ai.</b>
                        <br />
                        <br />
                        Thân mến,
                        <br />
                        Toeicute
                    </p>
                    <hr style="border:none;border-top:1px solid #eee" />
                    <div style="padding:8px 0; color:#aaa; font-size:0.8em; line-height:1; font-weight:300">
                        <p>Email này không thể nhận được thư trả lời.</p>
                    </div>
                </div>
            </div>
        </body>
        </html>');


insert into email_template(name, status, subject, template_code, template_content)
VALUES ('Mẫu email sau khi đăng ký tài khoản bằng FB/GOOGLE', 'active', 'THÔNG TIN TÀI KHOẢN TOEIC HERO', 'LOGIN_SOCIAL',
        '<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>Toeicute</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet" />
</head>

<body>
    <div style="
        font-family: Helvetica, Arial, sans-serif;
        min-width: 700px;
        overflow: auto;
        line-height: 2;
      ">
        <div style="margin: 50px auto; width: 600px; padding: 20px 0">
            <div style="border-bottom: 1px solid #eee">
                <a href="" style="
              font-size: 1.4em;
              color: #00466a;
              text-decoration: none;
              font-weight: 600;
            ">TOEIC HERO</a>
            </div>
            <p style="font-size: 1.1em">Hi, %s</p>
            <p>
                Chào mừng bạn đến với TOEIC HERO.
            </p>
            <ul style="font-size: 0.8em;">
                <li>Bạn có thể sử dụng thông tin này để đăng nhập vào TOEIC HERO hoặc đăng nhập bằng tài khoản %s
                </li>
                <li>Sau khi đăng nhập, bạn có thể thay đổi mật khẩu tùy thích.</li>
                <li>Chúng tôi khuyến khích bạn nên thay đổi mật khẩu để bảo mật tài khoản.</li>
            </ul>
            <p>Tài khoản để đăng nhập vào
                TOEIC HERO của bạn là:</p>
            <table
                style="border-collapse: collapse; width: 600px; border: 1px solid #ddd; font-size: 14px; margin-bottom: 20px;">
                <tr style="border-bottom: 1px solid #ddd;">
                    <td style="padding: 10px;">
                        <b>Tên đăng nhập:</b>
                    </td>
                    <td style="padding: 10px;">
                        <span>%s</span>
                    </td>
                </tr>
                <tr style="border-bottom: 1px solid #ddd;">
                    <td style="padding: 10px;">
                        <b>Họ và tên:</b>
                    </td>
                    <td style="padding: 10px;">
                        <span>%s</span>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 10px;">
                        <b>Mật khẩu:</b>
                    </td>
                    <td style="padding: 10px;">
                        <span>%s</span>
                    </td>
                </tr>
            </table>
            <p>
                <b style="font-size: 1.2em">Link đăng nhập: </b>
                <a href="%s">Toeicute</a>
            </p>

            <p style="font-size: 0.9em">
                <br /><b>Không chuyển tiếp hoặc đưa email này cho bất kỳ ai.</b>
                <br />
                <br />
                Thân mến,
                <br />
                <span class="fst-italic">Toeicute</span>
            </p>
            <hr style="border: none; border-top: 1px solid #eee" />
            <div style="
            padding: 8px 0;
            color: #aaa;
            font-size: 0.8em;
            line-height: 1;
            font-weight: 300;
          ">
                <p>Email này không thể nhận được thư trả lời.</p>
            </div>
        </div>
    </div>
</body>

</html>
        ');



insert into email_template(name, status, subject, template_code, template_content)
VALUES ('Mẫu email cấp lại mật khẩu', 'active', 'Cấp lại mật khẩu TOEIC HERO', 'FORGOT_PASSWORD',
        '
<table style="background-color: #f2f3f8; max-width: 540px; margin: 0 auto;">
    <tbody>
        <tr>
            <td style="height: 80px;">&nbsp;</td>
        </tr>
        <tr>
            <td style="text-align: center;"><a title="logo" href="%s" target="_blank" rel="noopener">
                    <img title="logo"
                        src="https://firebasestorage.googleapis.com/v0/b/toeicute-70460.appspot.com/o/logo_tachnen.png?alt=media&amp;token=b1e89f15-9e51-4010-96bc-004946b3c586"
                        alt="logo" width="60"> </a></td>
        </tr>
        <tr>
            <td style="height: 20px;">&nbsp;</td>
        </tr>
        <tr>
            <td>
                <table
                    style="max-width: 540px; background: #fff; border-radius: 3px; text-align: center; -webkit-box-shadow: 0 6px 18px 0 rgba(0,0,0,.06); -moz-box-shadow: 0 6px 18px 0 rgba(0,0,0,.06); box-shadow: 0 6px 18px 0 rgba(0,0,0,.06);">
                    <tbody>
                        <tr>
                            <td style="height: 40px;">&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="padding: 0 35px;">
                                <h1
                                    style="color: #1e1e2d; font-weight: 500; margin: 0; font-size: 32px; font-family: ''Rubik'',sans-serif;">
                                    Xin ch&agrave;o, bạn vừa gửi y&ecirc;u cầu cấp lại mật khẩu.</h1>
                                <p
                                    style="color: #455056; font-size: 16px; line-height: 24px; margin: 0; text-decoration: solid;">
                                    Nếu bạn kh&ocirc;ng thực hiện y&ecirc;u cầu n&agrave;y, vui
                                    l&ograve;ng bỏ qua email n&agrave;y. <br>Để cấp lại mật khẩu, vui
                                    l&ograve;ng nhấn v&agrave;o n&uacute;t b&ecirc;n dưới.</p>
                                <a style="background: #20e277; text-decoration: none !important; font-weight: 500; margin-top: 35px; color: #fff; text-transform: uppercase; font-size: 14px; padding: 10px 24px; display: inline-block; border-radius: 50px;"
                                    title="Reset Password" href="%s" target="_blank" rel="noopener">Cấp
                                    lại mật khẩu</a>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 40px;">&nbsp;</td>
                        </tr>
                    </tbody>
                </table>
            </td>
        </tr>
    </tbody>
</table>
        ');

insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Home', 'mdi mdi-home menu-icon', 1, false, '/admin', '', 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('User Management', 'mdi mdi-account-circle menu-icon', 2, true, null, null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Topic Exam Management', 'mdi mdi-database menu-icon', 3, true, null, null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Exam Management', 'mdi mdi-book-open menu-icon', 4, true, null, null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Score Management', 'mdi mdi-checkbox-marked-circle menu-icon', 5, false, '/admin/score', null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Transcript', 'mdi mdi-tooltip-text menu-icon', 6, true, null, null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Crawl', 'mdi mdi-database-plus menu-icon', 7, true, null, null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Blog', 'mdi mdi mdi-clipboard-text menu-icon', 8, true, null, null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Pricing management', 'mdi mdi-currency-usd menu-icon', 9, false, '/admin/pricing', null, 'MEMBER');

insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('System Monitoring', 'mdi mdi mdi-android menu-icon', 10, false, 'http://localhost:8080/api/applications', null,
        'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Firebase Config', 'mdi mdi-cloud-upload menu-icon', 11, true, null, null, 'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Email Config', 'mdi mdi-email menu-icon', 12, true, null, null, 'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Slider Config', 'mdi mdi-equal-box menu-icon', 13, true, null, null, 'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Rev AI Config', 'mdi mdi-at menu-icon', 14, true, null, null, 'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Kommunicate Config', 'mdi mdi-earth menu-icon', 15, true, null, null, 'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Tiny Editor Config', 'mdi mdi-widgets menu-icon', 16, true, null, null, 'SYSTEM');


insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Member Management', '/admin/users-management', null, 2);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Member Activity', '/admin/users-activity', null, 2);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Topic', '/admin/topic/list', null, 3);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Topic', '/admin/topic/add', null, 3);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Exam', '/admin/exam/list', null, 4);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Exam', '/admin/exam/add', null, 4);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Get Transcript', '/admin/transcript/get', null, 6);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Transcript History', '/admin/transcript/history', null, 6);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Crawl Data', '/admin/crawl/get', null, 7);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Config Crawl', '/admin/crawl/config', null, 7);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Crawl History', '/admin/crawl/list', null, 7);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Blog', '/admin/blog/get', null, 8);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Blog', '/admin/blog/add', null, 8);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Firebase Config', '/admin/firebase/list', null, 11);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Config', '/admin/firebase/update', null, 11);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('History Upload', '/admin/firebase/history', null, 11);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Email Config', '/admin/email/account/list', null, 12);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Email Config', '/admin/email/account/update', null, 12);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Template Email', '/admin/email/template-email/list', null, 12);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Template Email', '/admin/email/template-email/update', null, 12);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Slider', '/admin/slider/list', null, 13);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Slider', '/admin/slider/update', null, 13);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List RevAi Account', '/admin/rev-ai/account/list', null, 14);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add RevAi Account', '/admin/rev-ai/account/update', null, 14);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List RevAi Config', '/admin/rev-ai/config/list', null, 14);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add RevAi Config', '/admin/rev-ai/config/update', null, 14);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Kommunicate Account', '/admin/kommunicate/account/list', null, 15);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Kommunicate Account', '/admin/kommunicate/account/update', null, 15);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Kommunicate Config', '/admin/kommunicate/bot/list', null, 15);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Kommunicate Config', '/admin/kommunicate/bot/update', null, 15);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Tiny Account', '/admin/tiny/account/list', null, 16);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Tiny Account', '/admin/tiny/account/update', null, 16);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Tiny Config', '/admin/tiny/config/list', null, 16);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Tiny Config', '/admin/tiny/config/update', null, 16);


insert into revai_config(access_token, status)
    value ('02Nr61oQry1wxj-hsCiaMUmAAdfM_VWYGTcFXiNxB0RwmozYdeLpBRde7O_wmZ63pE18gsxYor7OOm8tLbunHjuKma3e0', true);


INSERT INTO chatai(type, url, prompt, model_name, status)
VALUES ('LLAMAS', 'https://llamastudio.dev/api/clwa4ji0e0001jv08e7otvmm4', '', '', true);
INSERT INTO chatai (location, model_name, project_id, prompt, status, token, type, url)
VALUES ('', 'gpt-3.5-turbo', '',
        'Rule for answering this question: As an English tutor specializing in TOEIC exam preparation and English language learning, the chatbot is dedicated to addressing inquiries pertinent to these subjects. Should the user inquire about unrelated topics, the chatbot will courteously guide them back to the main theme by posing relevant questions. This ensures clarity in discussion and adherence to the chatbot\'s designated purpose of assisting users with TOEIC and English language learning.
Start a conversation like "Hello!" and encourage them like "No worries!" when you feel they do not understand. Question: %s', true, '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', 'GPT', 'https://api.openai.com/v1/chat/completions');

insert into chatai(type, token, status, prompt, url) values ('GEMINI-CURL', 'AIzaSyBRL8Lz9vaftejrIqHAaHOZhuobfJFHgis', true, 'Rule for answering this question: As an English tutor specializing in TOEIC exam preparation and English language learning,
        the chatbot is dedicated to addressing inquiries pertinent to these subjects. Should the user inquire about unrelated topics,
        the chatbot will courteously guide them back to the main theme by posing relevant questions. This ensures clarity in discussion and adherence to the chatbot\'s designated purpose of assisting users with TOEIC and English language learning.
Start a conversation like "Hello!" and encourage them like "No worries!" when you feel they do not understand. Question: %s',
        'https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=%s');

insert into system_config(config_key, value, description, updated_at)
VALUES ('GOOGLE_TRANSLATE_URL',
        'https://translate.google.com/translate_a/single?client=at&dt=t&dt=ld&dt=qca&dt=rm&dt=bd&dj=1&hl=%25s&ie=UTF-8&oe=UTF-8&inputm=2&otf=2&iid=1dd3b944-fa62-4b55-b330-74909a99969e&',
        '', CURDATE());
insert into system_config(config_key, value, description, updated_at)
VALUES ('TWILIO_SID', '', '', CURRENT_DATE());
insert into system_config(config_key, value, description, updated_at)
VALUES ('TWILIO_TOKEN', '', '', CURRENT_DATE());
insert into system_config(config_key, description, updated_at, value) VALUES ('authorizedRedirectUris', '', CURRENT_DATE(), 'http://localhost:4200/oauth2/redirect');
insert into system_config (config_key, description, updated_at, value)
values ('VERTEX','Description for vertex ai', CURRENT_DATE, '');
insert into system_config (config_key, description, updated_at, value)
values ('PALM2','Description for PALM2', CURRENT_DATE, 'AIzaSyBRL8Lz9vaftejrIqHAaHOZhuobfJFHgis');
insert into system_config (config_key, description, updated_at, value)
values ('GPT','Description for GPT', CURRENT_DATE, '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');

insert into kommunicate_bot(api_key, app_id, status, script)
VALUES ('21e1deaca7ccde427eab393663be5b77a', '', true, '');

insert into system_config(config_key, description, updated_at, value)
VALUES ('URL_FRONTEND', 'Domain of frontend', current_date, 'http://localhost:4200');

insert into system_config(config_key, description, updated_at, value)
VALUES ('REDIS_HOST', 'Host of redis', current_date, 'toeicute-redis-toeicute.f.aivencloud.com');
insert into system_config(config_key, description, updated_at, value)
VALUES ('REDIS_PORT', 'Port of redis', current_date, '23605');
insert into system_config(config_key, description, updated_at, value)
VALUES ('REDIS_PASSWORD', 'Password of redis', current_date, 'AVNS_x8WigoRV7Q8k-6X2rOB');

INSERT INTO system_config (config_key, description, updated_at, value)
VALUES ('GPT_4', null, '2024-06-09 13:22:30.000000', 'sk-proj-hCtKKpRFdz2fkp05TmaET3BlbkFJijf1eYearfBzThtVMM3r');

update left_menu
set path = '/admin/users/manager'
where left_menu_id = 1;
update left_menu
set path = '/admin/users/activity'
where left_menu_id = 2;
update menu_group
set path = '/admin/home'
where menu_group_id = 1;


INSERT INTO firebase_config (id, bucket_name, file_json, project_id, status, token_key) VALUES (2, '', 'toeicute-70460', true, 'toeicute');
INSERT INTO firebase_config (id, bucket_name, file_json, project_id, status, token_key) VALUES (3, '', false, 'dsadsadsa');
INSERT INTO system_config (system_config_id, config_key, description, updated_at, value) VALUES (14, 'VN_PAY_RETURN_URL', null, '2024-06-09 13:22:30.000000', 'http://localhost:4200/thank-you');

INSERT INTO system_config (config_key, description, updated_at, value) VALUES ('PAYPAL_CLIENT_ID', null, '2024-06-09 13:22:30.000000', 'AfUIEEg0iDSc1w33ZqAwjYj5kCPlJ1GKC3yxmf9DjjI9SZScVEMb8xcBGTEKjvCofXFkMHlnc3gB3Wxj');
INSERT INTO system_config (config_key, description, updated_at, value) VALUES ('PAYPAL_CLIENT_SECRET', null, '2024-06-09 13:22:30.000000', 'EFll9RFSqEdqW_bPBfWlCAnXtkCyaQJiV3INTBCCNpRqgMsEqe6pG48TQ6lXR2KfO70Cn9fhhd10ejKs');
INSERT INTO system_config (config_key, description, updated_at, value) VALUES ('STRIPE_API_KEY', null, '2024-06-09 13:22:30.000000', 'sk_test_51P9ezb01sXpkr4EaHPTB9Irk8J9D7ZJAee8s7MIdAF8deWb4cA9X99Jy3JqGT4qkggOCiizP3J53J2s8yEpGJqP500sluzPaaP');
INSERT INTO system_config (config_key, description, updated_at, value) VALUES ('STRIPE_RETURN_URL', null, '2024-06-09 13:22:30.000000', 'http://localhost:4200/thank-you/stripe');

UPDATE menu_group t1
JOIN (
    SELECT menu_group_id
    FROM menu_group
    WHERE display_name = 'Exam Management'
) t2
ON t1.menu_group_id > t2.menu_group_id
SET t1.priority = t1.priority + 1
where true;

insert into menu_group(display_name, icon, priority, have_child, path, roles, type)
VALUES ('Course Management', 'mdi mdi-widgets menu-icon', 5, true, null, null, 'MEMBER');

insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Course', '/admin/course-mana/list', null, 17);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Course', '/admin/course-mana/add', null, 17);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('List Category Course', '/admin/course-mana/list-category', null, 17);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Add Category Course', '/admin/course-mana/add-category', null, 17);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Management Course Register', '/admin/course-mana/course-register', null, 17);
insert into left_menu(display_name, path, roles, menu_group_id)
VALUES ('Management QR Code', '/admin/course-mana/qr', null, 17);
